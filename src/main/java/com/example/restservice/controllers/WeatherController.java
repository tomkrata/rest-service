package com.example.restservice.controllers;

import com.example.restservice.Weather;
import com.example.restservice.Wind;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

@RestController
public class WeatherController
{
    @GetMapping("/weather")
    public Weather weather(@RequestParam(value="location", defaultValue = "Prague")  String location,
                           @RequestParam(value="date", defaultValue = "today") String date,
                           @RequestParam(value="time", defaultValue = "now") String time) {
        return createWeather(location, date, time);
    }

    private Weather createWeather(String location, String date, String time)
    {
        Date day = getDate(date, new SimpleDateFormat("d.M.yyyy"));
        Date t = getDate(time, new SimpleDateFormat("HH:mm"));
        long seed = 0;
        for (char c : location.toCharArray()) {
            seed += c;
        }
        seed += day.getTime() + t.getTime();
        Random rnd = new Random();
        rnd.setSeed(seed);
        int temperature = rnd.nextInt(31) - 10; // -10 - 20
        float speed = rnd.nextFloat() * rnd.nextInt(20);
        Wind.Direction dir = Wind.Direction.randomDir(rnd);
        Weather.Cloudiness cloudiness = Weather.Cloudiness.randomCloud(rnd);
        return new Weather(location, day, t, temperature, cloudiness, new Wind(dir, speed));
    }

    private Weather getWeatherFromApi(String location, Date date)
    {
        throw new java.lang.UnsupportedOperationException();
    }
    private Weather getWeatherFromDtb(String location, Date date)
    {
        throw new java.lang.UnsupportedOperationException();
    }

    private Date getDate(String date,SimpleDateFormat sdf)
    {
        Date d;
        try {
            d = sdf.parse(date);
        } catch (ParseException e) {
            d = new Date();
        }
        return d;
    }
}
