package com.patika.paycore.entity;

import com.patika.paycore.entity.validation.IdentityNumber;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq_gen")
    @SequenceGenerator(name = "user_seq_gen", sequenceName = "user_seq", allocationSize = 1)
    private Long id;

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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Application> applications = new ArrayList<>();

}
