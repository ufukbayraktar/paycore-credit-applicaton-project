package com.patika.paycore.repository;

import com.patika.paycore.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application,Long> {
    List<Application> findByUserIdentityNumber(String identityNumber);
}
