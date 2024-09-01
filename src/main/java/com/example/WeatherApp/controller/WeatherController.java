package com.example.WeatherApp.controller;


import com.example.WeatherApp.data.WeatherData;
import com.example.WeatherApp.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {
    @Autowired
    private WeatherService weatherService;

    @GetMapping("/{city}")
    public WeatherData getWeatherData(@PathVariable String city) {
        return weatherService.getWeatherData(city);
    }

    @GetMapping("/{city}")
    public String getWeatherData(@PathVariable String city, Model model) {
        WeatherData weatherData = weatherService.getWeatherData(city);
        model.addAttribute("weatherData", weatherData);
        return "index";
    }
}