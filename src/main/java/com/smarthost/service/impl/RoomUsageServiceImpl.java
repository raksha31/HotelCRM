/**
 *
 */
package com.smarthost.service.impl;

import com.smarthost.config.PotentialCustomers;
import com.smarthost.dto.request.HotelInputDTO;
import com.smarthost.dto.response.HotelResponseDTO;
import com.smarthost.exception.custom.InvalidInputException;
import com.smarthost.exception.custom.NoAvailableRoomException;
import com.smarthost.service.RoomUsageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import java.util.PriorityQueue;

/**
 * @author Raksha
 */
@Slf4j
@Service
@RequestScope
public class RoomUsageServiceImpl implements RoomUsageService {
    @Autowired
    PotentialCustomers potentialCustomers;

    Double premiumRates = 0D, economyRates = 0D;
    Integer usedPremiumRooms = 0, usedEconomyRooms = 0;


    @Override
    public HotelResponseDTO getHotelRoomUsage(HotelInputDTO hotelInputDTO) {

        PriorityQueue<Double> premiumQueue = potentialCustomers.getPremiumQueue();
        PriorityQueue<Double> economyQueue = potentialCustomers.getEconomyQueue();

        Integer availablePremiumRooms = hotelInputDTO.getAvailablePremiumRooms();
        Integer availableEconomyRooms = hotelInputDTO.getAvailableEconomyRooms();

        validateHotelInputDTO(hotelInputDTO);
        availablePremiumRooms = fillPremiumRooms(availablePremiumRooms, premiumQueue);
        availableEconomyRooms = upgradeEconomyRooms(availablePremiumRooms, availableEconomyRooms, economyQueue);
        fillEconomyRooms(availableEconomyRooms, economyQueue);
        return new HotelResponseDTO(usedPremiumRooms, usedEconomyRooms, premiumRates, economyRates);
    }

    private void validateHotelInputDTO(HotelInputDTO hotelInputDTO) {
        if (hotelInputDTO.getAvailablePremiumRooms() < 0 || hotelInputDTO.getAvailableEconomyRooms() < 0) {
            throw new InvalidInputException();
        }
        if (hotelInputDTO.getAvailablePremiumRooms() == 0 && hotelInputDTO.getAvailableEconomyRooms() == 0) {
            throw new NoAvailableRoomException();
        }
    }

    private void fillEconomyRooms(Integer availableEconomyRooms, PriorityQueue<Double> economyQueue) {
        while (availableEconomyRooms > 0 && economyQueue.size() > 0) {
            economyRates += economyQueue.poll();
            availableEconomyRooms--;
            usedEconomyRooms++;
        }
        economyQueue.clear();
    }

    private int upgradeEconomyRooms(Integer availablePremiumRooms, Integer availableEconomyRooms, PriorityQueue<Double> economyQueue) {
        int upgradableEconomyRooms = (economyQueue.size() > availableEconomyRooms) ? (economyQueue.size() - availableEconomyRooms) : 0;
        log.debug("Economy rules available for update are  {}", upgradableEconomyRooms);
        while (availablePremiumRooms > 0 && upgradableEconomyRooms > 0 && economyQueue.size() > 0) {
            premiumRates += economyQueue.poll();
            availablePremiumRooms--;
            usedPremiumRooms++;
            upgradableEconomyRooms--;
        }
        log.debug("Economy Rooms updated {} and are still available {}", (usedPremiumRooms - availablePremiumRooms), economyQueue.size());
        return availableEconomyRooms;
    }

    private int fillPremiumRooms(Integer availablePremiumRooms, PriorityQueue<Double> premiumQueue) {
        while (availablePremiumRooms > 0 && premiumQueue.size() > 0) {
            premiumRates += premiumQueue.poll();
            availablePremiumRooms--;
            usedPremiumRooms++;
        }
        log.debug("Premium Rooms filled {} and are still available {}", usedPremiumRooms, availablePremiumRooms);
        premiumQueue.clear();
        return availablePremiumRooms;
    }
}