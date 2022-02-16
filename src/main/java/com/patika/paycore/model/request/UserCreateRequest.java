package com.patika.paycore.model.request;

import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Builder
public class UserCreateRequest {

    @NotNull(message = "Identity number can not be empty.")
    @Length(min = 11, max = 11,message = "Identity number must have 11 characters.")
    private String identityNumber;

    @NotEmpty(message = "Name can not be empty.")
    private String name;

    @NotEmpty(message = "Surname can not be empty.")
    private String surname;

    @NotEmpty(message = "Surname can not be empty.")
    private String phoneNumber;

    @NotNull(message = "Salary can not be empty.")
    @Min(value = 0, message = "Salary must be bigger than 0 ")
    private BigDecimal salary;

}
