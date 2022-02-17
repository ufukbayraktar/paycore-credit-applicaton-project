package com.patika.paycore.service.impl;

import com.patika.paycore.entity.Application;
import com.patika.paycore.entity.User;
import com.patika.paycore.enums.ApplicationStatus;
import com.patika.paycore.model.request.ApplicationRequest;
import com.patika.paycore.model.response.ApplicationResponse;
import com.patika.paycore.repository.ApplicationRepository;
import com.patika.paycore.repository.UserRepository;
import com.patika.paycore.service.ApplicationService;
import com.patika.paycore.service.ScoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplicationServiceImpl implements ApplicationService {

    private static final BigDecimal creditMultiplier = new BigDecimal(4);

    private final ScoreService scoreService;
    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;


    @Override
    @Transactional
    public ApplicationResponse createApplication(ApplicationRequest request) {
        Long score = scoreService.getScore(request.getIdentityNumber());
        ApplicationResponse response = new ApplicationResponse();
        if (score < 500) {
            response.setStatus(ApplicationStatus.REJECTED);
            response.setCreditLimit(new BigDecimal(0));
        } else if (score >= 500 && score < 1000 && BigDecimal.valueOf(5000).compareTo(request.getSalary()) > 0) {
            response.setStatus(ApplicationStatus.CONFIRMED);
            response.setCreditLimit(new BigDecimal(10000));
        } else if (score >= 500 && score < 1000 && BigDecimal.valueOf(5000).compareTo(request.getSalary()) < 0) {
            response.setStatus(ApplicationStatus.CONFIRMED);
            response.setCreditLimit(new BigDecimal(20000));
        } else if (score >= 1000) {
            BigDecimal creditLimit = request.getSalary().multiply(creditMultiplier);
            response.setStatus(ApplicationStatus.CONFIRMED);
            response.setCreditLimit(creditLimit);
        }
        Optional<User> user = userRepository.findByIdentityNumber(request.getIdentityNumber());
        User userPresent;
        if (user.isPresent()) {
            userPresent = user.get();
            userPresent.setSalary(request.getSalary());
            userPresent.setPhoneNumber(request.getPhoneNumber());
        } else {
            userPresent = User.builder()
                    .identityNumber(request.getIdentityNumber())
                    .name(request.getName())
                    .surname(request.getSurname())
                    .phoneNumber(request.getPhoneNumber())
                    .salary(request.getSalary())
                    .build();
        }

        Application application = Application.builder()
                .user(userPresent)
                .applicationStatus(response.getStatus())
                .creditLimit(response.getCreditLimit())
                .build();
        applicationRepository.save(application);
        log.info("Application saved successfully: {}", application);

        return response;
    }

    @Override
    public List<ApplicationResponse> getStatus(String identityNumber) {
        List<Application> applicationList = applicationRepository.findByUserIdentityNumber(identityNumber);
        if (!applicationList.isEmpty()) {
            List<ApplicationResponse> applicationResponseList = new ArrayList<>();
            applicationList.forEach(application -> {
                ApplicationResponse applicationResponse = ApplicationResponse.builder()
                        .status(application.getApplicationStatus())
                        .creditLimit(application.getCreditLimit())
                        .build();
                applicationResponseList.add(applicationResponse);
            });

            log.info("Applications found: {}", applicationList);
            return applicationResponseList;

        } else {
            return Collections.emptyList();
        }
    }
}
