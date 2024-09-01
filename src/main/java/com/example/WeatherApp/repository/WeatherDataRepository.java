package com.example.WeatherApp.repository;

import com.example.WeatherApp.data.WeatherData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
@EnableJpaRepositories

public interface WeatherDataRepository extends JpaRepository<WeatherData, Long> {
    List<WeatherData> findByCityAndTimestampAfter(String cityName, LocalDateTime timestamp);
}
