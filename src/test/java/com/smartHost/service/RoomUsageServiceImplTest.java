package com.smartHost.service;

import com.jayway.restassured.RestAssured;
import com.smarthost.Application;
import com.smarthost.config.PotentialCustomers;
import com.smarthost.dto.request.HotelInputDTO;
import com.smarthost.dto.response.HotelResponseDTO;
import com.smarthost.exception.custom.InvalidInputException;
import com.smarthost.exception.custom.NoAvailableRoomException;
import com.smarthost.service.impl.RoomUsageServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.PriorityQueue;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@Slf4j
@ContextConfiguration(classes = Application.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)

public class RoomUsageServiceImplTest {
    int port = 8080;

    @InjectMocks
    RoomUsageServiceImpl roomUsageServiceImpl;

    @Mock
    PotentialCustomers potentialCustomers;

    private final String roomUsageURL = "/smarthost/roomusage";

    @Before
    public void setup() throws IOException {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
        PriorityQueue<Double> economyQueue = new PriorityQueue<>(Collections.reverseOrder());
        PriorityQueue<Double> premiumQueue = new PriorityQueue<>(Collections.reverseOrder());
        economyQueue.addAll(Arrays.asList(22D, 45D, 99D, 23D));
        premiumQueue.addAll(Arrays.asList(100D, 209D, 101D, 115D, 374D, 155D));
        when(potentialCustomers.getPremiumQueue()).thenReturn(premiumQueue);
        when(potentialCustomers.getEconomyQueue()).thenReturn(economyQueue);
    }

    @Test(expected = InvalidInputException.class)
    public void negativeRoomsInputTest() {
        HotelInputDTO hotelInputDTO = new HotelInputDTO(-15, 3);
        roomUsageServiceImpl.getHotelRoomUsage(hotelInputDTO);
    }

    @Test(expected = NoAvailableRoomException.class)
    public void zeroPremium_zeroEconomy_RoomsAvailable() {
        HotelInputDTO hotelInputDTO = new HotelInputDTO(0, 0);
        roomUsageServiceImpl.getHotelRoomUsage(hotelInputDTO);
    }

    @Test
    public void equalPremium_eqaulEconomy_toCustomers() {
        HotelInputDTO hotelInputDTO = new HotelInputDTO(6, 4);
        HotelResponseDTO responseDTO = roomUsageServiceImpl.getHotelRoomUsage(hotelInputDTO);
        assertCorrectOutput(responseDTO, 6, 4, 1054D, 189D);
    }

    @Test
    public void morePremium_lessEconomy_thanCustomers() {
        HotelInputDTO hotelInputDTO = new HotelInputDTO(7, 3);
        HotelResponseDTO responseDTO = roomUsageServiceImpl.getHotelRoomUsage(hotelInputDTO);
        assertCorrectOutput(responseDTO, 7, 3, 1153D, 90D);
    }

    @Test
    public void morePremium_noEconomy_thanCustomers() {
        HotelInputDTO hotelInputDTO = new HotelInputDTO(13, 0);
        HotelResponseDTO responseDTO = roomUsageServiceImpl.getHotelRoomUsage(hotelInputDTO);
        assertCorrectOutput(responseDTO, 10, 0, 1243D, 0D);
    }

    @Test
    public void noPremium_lessEconomy_thanCustomers() {
        HotelInputDTO hotelInputDTO = new HotelInputDTO(0, 2);
        HotelResponseDTO responseDTO = roomUsageServiceImpl.getHotelRoomUsage(hotelInputDTO);
        assertCorrectOutput(responseDTO, 0, 2, 0D, 144D);
    }

    @Test
    public void noPremium_economyEquals_toCustomers() {
        HotelInputDTO hotelInputDTO = new HotelInputDTO(0, 4);
        HotelResponseDTO responseDTO = roomUsageServiceImpl.getHotelRoomUsage(hotelInputDTO);
        assertCorrectOutput(responseDTO, 0, 4, 0D, 189D);
    }

    @Test
    public void noPremium_moreEconomy_thanCustomers() {
        HotelInputDTO hotelInputDTO = new HotelInputDTO(0, 8);
        HotelResponseDTO responseDTO = roomUsageServiceImpl.getHotelRoomUsage(hotelInputDTO);
        assertCorrectOutput(responseDTO, 0, 4, 0D, 189D);
    }

    @Test
    public void morePremium_moreEconomy_thanCustomers() {
        HotelInputDTO hotelInputDTO = new HotelInputDTO(80, 90);
        HotelResponseDTO responseDTO = roomUsageServiceImpl.getHotelRoomUsage(hotelInputDTO);
        assertCorrectOutput(responseDTO, 6, 4, 1054D, 189D);
    }

    private void assertCorrectOutput(HotelResponseDTO responseDTO, Integer usedPreiumRooms, Integer usedEconomyRooms, Double businessFromPremium, Double businessFromEconomy) {
        assertThat(responseDTO.getUsedPremiumRooms(), is(usedPreiumRooms));
        assertThat(responseDTO.getUsedEconomyRooms(), is(usedEconomyRooms));
        assertThat(responseDTO.getBusinessFromPremium(), is(businessFromPremium));
        assertThat(responseDTO.getBusinessFromEconomy(), is(businessFromEconomy));
    }

}