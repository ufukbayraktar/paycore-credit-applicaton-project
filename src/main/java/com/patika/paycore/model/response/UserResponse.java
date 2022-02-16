package com.patika.paycore.model.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Builder
@Setter
@Getter
public class UserResponse {
    private String identityNumber;
    private String name;
    private String surname;
    private String phoneNumber;
    private BigDecimal salary;
}
