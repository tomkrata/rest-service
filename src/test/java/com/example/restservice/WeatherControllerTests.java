/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *	  https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.restservice;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.restservice.controllers.WeatherController;
import org.junit.jupiter.api.Test;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
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
        final Date value = wc.getDateFromDayAndTime("20.2.2020", "15:20");
        assertEquals(expected, value);
    }
}
