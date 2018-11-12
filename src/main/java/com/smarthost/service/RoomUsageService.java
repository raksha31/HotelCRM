/**
 *
 */
package com.smarthost.service;


import com.smarthost.dto.request.HotelInputDTO;
import com.smarthost.dto.response.HotelResponseDTO;

/**
 * @author Raksha
 */

public interface RoomUsageService {
    public HotelResponseDTO getHotelRoomUsage(HotelInputDTO hotelInputDTO);
}