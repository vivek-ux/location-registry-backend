package com.example.locationregistry;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitRepository extends JpaRepository<Visit, Long> {
    List<Visit> findByLocationId(Long locationId);
}
