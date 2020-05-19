package com.example.restservice;

import java.text.SimpleDateFormat;
import java.util.*;

public class Weather {
    private final String location;
    private final Date date;
    private final Date time;
    private final int temperature;
    private final Cloudiness cloudiness;
    private final Wind wind;

    public Weather(String location, Date date, Date time, int temperature, Cloudiness cloudiness, Wind wind) {
        this.location = location;
        this.date = date;
        this.time = time;
        this.temperature = temperature;
        this.cloudiness = cloudiness;
        this.wind = wind;
    }

    public int getTemperature() {
        return temperature;
    }

    public String getLocation() {
        return location;
    }

    public String getDate() {
        return new SimpleDateFormat("dd.MM.yyyy").format(date);
    }

    public String getTime() {
        return new SimpleDateFormat("HH:mm").format(time);
    }

    public Cloudiness getCloudiness() {
        return cloudiness;
    }

    public String getWind() {
        return wind.toString();
    }

    public enum Cloudiness {
        clear,
        cloudy,
        sunny;

        public static Cloudiness randomCloud(Random rnd)  {
            return values()[rnd.nextInt(values().length)];
        }
    }
}
