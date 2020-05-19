package com.example.restservice.controllers;

import com.example.restservice.Weather;
import com.example.restservice.Wind;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
        return getWeather(location, date, time, Option.random);
    }

    private Weather getWeather(String location, String date, String time, Option option)
    {
        Weather ret;
        Date d;
        switch(option)
        {
            case api:
                d = getDateFromDayAndTime(date, time);
                ret = getWeatherFromApi(location, d);
                break;
            case dtb:
                d = getDateFromDayAndTime(date, time);
                ret = getWeatherFromDtb(location, d);
                break;
            case random:
            default:
                ret = createWeather(location, date, time);
        }
        return ret;
    }


    /**
     * creates random weather by given seed
     * the seed is changed by the method arguments
     * temperature's range <-10 ; 20> (integers)
     * speed's range <0 ; 20> (floats)
     * @param location
     * @param date
     * @param time
     * @return Weather
     */
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

    /**
     * parse day;time to Date
     * if cannot Date is set to current time
     * @param day
     * @param time
     * @return Date
     */
    private Date getDateFromDayAndTime(String day, String time)
    {
        return getDate(day + ";" + time, new SimpleDateFormat("d.M.yyyy;HH:mm"));
    }

    //only for test purposes
    public Date testGetDateFromDayAndTime(String day, String time)
    {
        return getDateFromDayAndTime(day, time);
    }

    /**
     * parse String date by SimpleDateFormat
     * if fails, then set current time
     * @param date
     * @param sdf
     * @return Date
     */
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

    //only for test purposes
    public Date testGetDate(String date, SimpleDateFormat sdf)
    {
        return getDate(date, sdf);
    }

    private enum Option {
        random,api,dtb;
    }
}
