package com.smarthost.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Raksha
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelResponseDTO {
    private Integer usedPremiumRooms;
    private Integer usedEconomyRooms;
    private Double businessFromPremium;
    private Double businessFromEconomy;
}