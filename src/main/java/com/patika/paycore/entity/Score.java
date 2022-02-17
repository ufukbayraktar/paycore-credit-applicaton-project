package com.patika.paycore.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "score")
public class Score extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "score_seq_gen")
    @SequenceGenerator(name = "score_seq_gen", sequenceName = "score_seq", allocationSize = 1)
    private Long id;

    private String identityNumber;

    private Long score;
}
