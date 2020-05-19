package com.example.restservice;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.apache.commons.lang.time.DateUtils;

import com.example.restservice.controllers.WeatherController;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.assertEquals;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@SpringBootTest
@AutoConfigureMockMvc
public class WeatherControllerTests {

    @Autowired
    private MockMvc mockMvc;
    private static final WeatherController wc = new WeatherController();

    @Test
    public void noParamShouldReturnCurrentTimeAndPrague() throws Exception {
        Date d = new Date();
        String date = new SimpleDateFormat("dd.MM.yyyy").format(d);
        String time = new SimpleDateFormat("HH:mm").format(d);
        this.mockMvc.perform(get("/weather")).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.date").value(date))
                .andExpect(jsonPath("$.time").value(time))
                .andExpect(jsonPath("$.location").value("Prague"));
    }

    @Test
    public void paramShouldReturnItself() throws Exception {
        this.mockMvc.perform(get("/weather?location=New york")).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.location").value("New york"));
    }

    @Test
    public void paramsShouldReturnThemselves() throws Exception {
        this.mockMvc.perform(get("/weather?location=Tokyo&date=1.1.1960")).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.location").value("Tokyo"))
                .andExpect(jsonPath("$.date").value("01.01.1960"));
    }

    @Test
    public void paramsShouldReturnThemselvesScramble() throws Exception {
        this.mockMvc.perform(get("/weather?time=00:00&location=Paris&date=31.12.2000"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.location").value("Paris"))
                .andExpect(jsonPath("$.date").value("31.12.2000"))
                .andExpect(jsonPath("$.time").value("00:00"));
    }

    @Test
    public void wrongParametersShouldReturnDefaultValues() throws Exception {
        Date d = new Date();
        String date = new SimpleDateFormat("dd.MM.yyyy").format(d);
        String time = new SimpleDateFormat("HH:mm").format(d);
        this.mockMvc.perform(get("/weather?time=pp&date=Tokyo"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.location").value("Prague"))
                .andExpect(jsonPath("$.time").value(time))
                .andExpect(jsonPath("$.date").value(date));
    }


    @Test
    public void wrongPutParametersShouldReturnDefaultValues() throws Exception {
        Date d = new Date();
        String time = new SimpleDateFormat("HH:mm").format(d);
        this.mockMvc.perform(get("/weather?time:12&date=21.3.2000"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.location").value("Prague"))
                .andExpect(jsonPath("$.time").value(time))
                .andExpect(jsonPath("$.date").value("21.03.2000"));
    }

    @Test
    public void getDateFromDayAndTimeShouldReturnNewDate() throws Exception {
        final Date expected = new SimpleDateFormat("d.M.yyyy;HH:mm").parse("20.2.2020;15:20");
        final Date value = getDateFromDayAndTime("20.2.2020", "15:20");
        assertEquals(expected, value);
    }
    @Test
    public void wrongFormatShouldReturnCurrentTimeMinutes() throws Exception {
        Date expected = DateUtils.round(new Date(), Calendar.MINUTE);
        Date value = DateUtils.round(getDate("202.2020", new SimpleDateFormat("d.M.yyyy")), Calendar.MINUTE);
        assertEquals(expected, value);
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
    private Date getDateFromDayAndTime(String day, String time)
    {
        return getDate(day + ";" + time, new SimpleDateFormat("d.M.yyyy;HH:mm"));
    }
}
