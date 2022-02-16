package com.patika.paycore.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "score")
public class Score extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String identityNumber;

    private Long score;
}
