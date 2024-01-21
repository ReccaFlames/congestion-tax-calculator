package com.reccaflames.calculator.repository;

import com.reccaflames.calculator.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CityRepository extends JpaRepository<City, UUID> {
    Optional<City> findCityByCodeAndCountry(String code, String country);
}
