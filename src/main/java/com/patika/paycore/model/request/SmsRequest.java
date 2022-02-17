package com.patika.paycore.model.request;

import com.patika.paycore.enums.ApplicationStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Builder
@ToString
public class SmsRequest {
    private String phoneNumber;
    private BigDecimal creditLimit;
    private ApplicationStatus applicationStatus;
}
