package com.patika.paycore.entity;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "users")
public class User  extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq_gen")
    @SequenceGenerator(name = "user_seq_gen", sequenceName = "user_seq", allocationSize = 1)
    private Long id;

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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Application> applications = new ArrayList<>();

}
