package com.smarthost.controller;

import com.smarthost.dto.request.HotelInputDTO;
import com.smarthost.dto.response.HotelResponseDTO;
import com.smarthost.service.RoomUsageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/smarthost")
public class RoomUsageController {
    @Autowired
    RoomUsageService roomUsageService;

    @RequestMapping(value = "/roomusage",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public HotelResponseDTO roomUsage(@RequestBody HotelInputDTO hotelInputDTO) {
        return roomUsageService.getHotelRoomUsage(hotelInputDTO);
    }
}