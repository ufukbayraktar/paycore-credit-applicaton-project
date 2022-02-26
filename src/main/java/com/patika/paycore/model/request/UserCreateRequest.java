package com.patika.paycore.model.request;

import com.patika.paycore.entity.validation.IdentityNumber;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

@Getter
@Builder
@ToString
public class UserCreateRequest {

    @IdentityNumber
    private String identityNumber;

    @NotEmpty(message = "Name can not be empty.")
    private String name;

    @NotEmpty(message = "Surname can not be empty.")
    private String surname;

    @NotEmpty(message = "Surname can not be empty.")
    @Length(min = 10, max = 10, message = "Number must be 10 characters")
    @Pattern(regexp = "^(0|[1-9][0-9]*)$")
    private String phoneNumber;

    @NotNull(message = "Salary can not be empty.")
    @Min(value = 0, message = "Salary must be bigger than 0 ")
    private BigDecimal salary;

}
