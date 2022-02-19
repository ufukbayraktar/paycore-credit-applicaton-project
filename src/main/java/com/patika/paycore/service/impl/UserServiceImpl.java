package com.patika.paycore.service.impl;

import com.patika.paycore.entity.User;
import com.patika.paycore.exception.ApiErrorType;
import com.patika.paycore.exception.UserNotFoundException;
import com.patika.paycore.model.request.UserCreateRequest;
import com.patika.paycore.model.request.UserUpdateRequest;
import com.patika.paycore.model.response.UserResponse;
import com.patika.paycore.repository.UserRepository;
import com.patika.paycore.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserResponse createUser(UserCreateRequest request) {
        User user = User.builder()
                .identityNumber(request.getIdentityNumber())
                .name(request.getName())
                .surname(request.getSurname())
                .phoneNumber(request.getPhoneNumber())
                .salary(request.getSalary())
                .build();
        userRepository.save(user);
        log.info("User saved: {}", user);
        return UserResponse.builder()
                .identityNumber(user.getIdentityNumber())
                .name(user.getName())
                .surname(user.getSurname())
                .phoneNumber(user.getPhoneNumber())
                .salary(user.getSalary())
                .build();
    }

    @Override
    @Transactional
    public UserResponse updateUser(UserUpdateRequest request) {
        Optional<User> user = userRepository.findByIdentityNumber(request.getIdentityNumber());
        if (user.isPresent()) {
            User userPresent = user.get();
            userPresent.setName(request.getName());
            userPresent.setSurname(request.getSurname());
            userPresent.setPhoneNumber(request.getPhoneNumber());
            userPresent.setSalary(request.getSalary());
            userRepository.save(userPresent);
            log.info("User updated: {}", userPresent);
            return UserResponse.builder()
                    .identityNumber(userPresent.getIdentityNumber())
                    .name(userPresent.getName())
                    .surname(userPresent.getSurname())
                    .phoneNumber(userPresent.getPhoneNumber())
                    .salary(userPresent.getSalary())
                    .build();
        } else {
            throw new UserNotFoundException(ApiErrorType.USER_NOT_FOUND_ERROR.getErrorCode(),
                    ApiErrorType.USER_NOT_FOUND_ERROR.getErrorMessage(),
                    ApiErrorType.INTERNAL_SERVER_ERROR.getHttpStatus());
        }
    }


    @Override
    @Transactional
    public void deleteUser(String identityNumber) {
        Optional<User> user = userRepository.findByIdentityNumber(identityNumber);
        if (user.isPresent()) {
            User userPresent = user.get();
            userRepository.delete(userPresent);
            log.info("User deleted: {}", userPresent);
        } else {
            log.info("User not found for given identity number: {}", identityNumber);
        }
    }

    @Override
    public UserResponse getUser(String identityNumber) {
        Optional<User> user = userRepository.findByIdentityNumber(identityNumber);
        if (user.isPresent()) {
            User userPresent = user.get();
            log.info("User found: {} ", userPresent);
            return UserResponse.builder()
                    .identityNumber(userPresent.getIdentityNumber())
                    .name(userPresent.getName())
                    .surname(userPresent.getSurname())
                    .phoneNumber(userPresent.getPhoneNumber())
                    .salary(userPresent.getSalary())
                    .build();
        } else {
            throw new UserNotFoundException(ApiErrorType.USER_NOT_FOUND_ERROR.getErrorCode(),
                    ApiErrorType.USER_NOT_FOUND_ERROR.getErrorMessage(),
                    ApiErrorType.INTERNAL_SERVER_ERROR.getHttpStatus());
        }
    }

}
