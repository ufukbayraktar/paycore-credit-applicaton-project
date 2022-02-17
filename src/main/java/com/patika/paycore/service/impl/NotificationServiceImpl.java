package com.patika.paycore.service.impl;

import com.patika.paycore.model.request.SmsRequest;
import com.patika.paycore.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    @Override
    public void sendCreditStatusSms(SmsRequest request) {

        log.info("Sending sms to number : {} ",request.getPhoneNumber());
        log.info("Your credit application status is {} and your credit limit is {} ",request.getApplicationStatus(),request.getCreditLimit());

    }
}
