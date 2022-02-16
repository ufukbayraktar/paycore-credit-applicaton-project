package com.patika.paycore.service.impl;

import com.patika.paycore.entity.Score;
import com.patika.paycore.repository.ScoreRepository;
import com.patika.paycore.service.ScoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScoreServiceImpl implements ScoreService {
    private final ScoreRepository scoreRepository;

    @Override
    public Long getScore(String identityNumber) {
        Optional<Score> score = scoreRepository.findByIdentityNumber(identityNumber);
        if (score.isPresent()) {
            Score scorePresent = score.get();
            return scorePresent.getScore();
        } else {
            return -1L;
        }
    }
}
