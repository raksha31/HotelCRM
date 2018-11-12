package com.smarthost.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelInputDTO {
    private Integer availablePremiumRooms;
    private Integer availableEconomyRooms;
}