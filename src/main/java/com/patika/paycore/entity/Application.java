package com.patika.paycore.entity;

import com.patika.paycore.enums.ApplicationStatus;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "application")
public class Application extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY,cascade=CascadeType.ALL)
    @ToString.Exclude
    private User user;

    private BigDecimal creditLimit;

    private ApplicationStatus applicationStatus;


}
