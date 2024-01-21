package com.reccaflames.calculator.repository;

import com.reccaflames.calculator.entity.TaxRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TaxRateRepository extends JpaRepository<TaxRate, UUID> {
    List<TaxRate> findAllByCityId(UUID cityId);
}
