package com.example.locationregistry;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationVisitRepository
        extends JpaRepository<LocationVisit, Long> {
}
