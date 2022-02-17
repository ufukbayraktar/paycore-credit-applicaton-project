package com.patika.paycore.model.response;

import com.patika.paycore.enums.ApplicationStatus;
import lombok.*;

import java.math.BigDecimal;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ApplicationResponse {
    private ApplicationStatus status;
    private BigDecimal creditLimit;
}
