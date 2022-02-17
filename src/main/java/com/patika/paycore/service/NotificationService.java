package com.patika.paycore.service;

import com.patika.paycore.model.request.SmsRequest;

public interface NotificationService {
    void sendCreditStatusSms(SmsRequest request);
}
