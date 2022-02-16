package com.patika.paycore.repository;

import com.patika.paycore.entity.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ScoreRepository  extends JpaRepository<Score,Long> {
    Optional<Score> findByIdentityNumber(String identityNumber);
}
