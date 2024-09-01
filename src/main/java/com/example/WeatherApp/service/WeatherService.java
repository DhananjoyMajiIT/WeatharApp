package com.example.WeatherApp.service;

import com.example.WeatherApp.data.WeatherData;
import com.example.WeatherApp.repository.WeatherDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class WeatherService {
    @Value("${openweather.api.key}")
    private String apiKey;

    @Value("${openweather.api.url}")
    private String apiUrl;

    @Autowired
    private WeatherDataRepository weatherRepository;

    private final WebClient webClient = WebClient.create();

    public WeatherData getWeatherData(String cityName) {
        LocalDateTime threshold = LocalDateTime.now().minusMinutes(30);
        List<WeatherData> recentData = weatherRepository.findByCityAndTimestampAfter(cityName,threshold);
        if (!recentData.isEmpty()) {
            return recentData.get(0);
        } else {
            System.out.println("Using API Key: " + apiKey);
            String baseurl = apiUrl + "?q=" + cityName + "&appid=" + apiKey + "&units=metric";
            Mono<Map> responseMono = webClient
                    .get()
                    .uri(baseurl)
                    .retrieve()
                    .bodyToMono(Map.class);

            Map<String, Object> response = responseMono.block();
            if (response != null) {
                Map<String, Object> main = (Map<String, Object>) response.get("main");
                double temperature = (double) main.get("temp");
                double humidity = (int) main.get("humidity");
                WeatherData weatherData = new WeatherData();
                weatherData.setCityName(cityName);
                weatherData.setTemperature(temperature);
                weatherData.setHumidity(humidity);
                weatherData.setTimestamp(LocalDateTime.now());
                weatherRepository.save(weatherData);
                return weatherData;
            } else {
                throw new RuntimeException("Unable to fetch weather data for city: " + cityName);
            }
        }
    }
}
