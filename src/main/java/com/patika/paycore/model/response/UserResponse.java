package com.patika.paycore.model.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Builder
@Setter
@Getter
@ToString
public class UserResponse {
    private String identityNumber;
    private String name;
    private String surname;
    private String phoneNumber;
    private BigDecimal salary;
}
