package com.smartHost.controller;

import com.jayway.restassured.http.ContentType;
import com.smarthost.Application;
import com.smarthost.config.PotentialCustomers;
import com.smarthost.controller.RoomUsageController;
import com.smarthost.dto.request.HotelInputDTO;
import com.smarthost.service.RoomUsageService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static com.jayway.restassured.RestAssured.given;


@RunWith(SpringRunner.class)
@Slf4j
@ContextConfiguration(classes = Application.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RoomUsageControllerTest {

    @InjectMocks
    RoomUsageController roomUsageController;

    @Mock
    RoomUsageService roomUsageService;

    @Mock
    PotentialCustomers potentialCustomers;

    private final String roomUsageURL = "/smarthost/roomusage";

    @Test
    public void inputNegativeRooms() {
        HotelInputDTO hotelInputDTO = new HotelInputDTO(-15, 3);
        roomUsageServiceCall(hotelInputDTO, HttpStatus.NOT_ACCEPTABLE.value());
    }

    @Test
    public void inputZeroRooms() {
        HotelInputDTO hotelInputDTO = new HotelInputDTO(0, 0);
        roomUsageServiceCall(hotelInputDTO, HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void inputPositiveRooms() {
        HotelInputDTO hotelInputDTO = new HotelInputDTO(5, 3);
        roomUsageServiceCall(hotelInputDTO, HttpStatus.OK.value());
    }

    private void roomUsageServiceCall(HotelInputDTO hotelInputDTO, Integer statusCode) {
        //@formatter:off
        given()
                .body(hotelInputDTO).
        when()
                .contentType(ContentType.JSON)
                .post(roomUsageURL).prettyPeek().
        then()
                .statusCode(statusCode);
         //@formatter:on
    }
}
