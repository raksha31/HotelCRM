/**
 *
 */
package com.smarthost.config;

import com.smarthost.exception.custom.FileEmptyException;

import lombok.Data;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;


@Component
@Data
@RequestScope
public class PotentialCustomers {
    private PriorityQueue<Double> premiumQueue = new PriorityQueue<>(Collections.reverseOrder());
    private PriorityQueue<Double> economyQueue = new PriorityQueue<>(Collections.reverseOrder());

    public PotentialCustomers() throws IOException {
        readCustomersFromFile();
    }

    public void readCustomersFromFile() throws IOException {

        String urlString = "https://gist.githubusercontent.com/fjahr/b164a446db285e393d8e4b36d17f4143/raw/75108c09a72a001a985d27b968a0ac5a867e830b/smarthost_hotel_guests.json";
        // create the url
        URL url = new URL(urlString);
        File f = new File("smarthost_hotel_guests.json");
        FileUtils.copyURLToFile(url, f);

        try (FileReader fileReader = new FileReader(f); BufferedReader reader = new BufferedReader(fileReader)) {
            String line;
            ArrayList<String> prices = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                prices.add(line.replaceAll(",", "").trim());
            }

            if (prices.isEmpty()) {
                throw new FileEmptyException();
            }

            for (int i = 1; i < prices.size() - 1; i++) {

                double currentPrice = Double.parseDouble(prices.get(i));

                if (currentPrice >= 100D) {
                    premiumQueue.add(currentPrice);
                } else {
                    economyQueue.add(currentPrice);
                }
            }

        } catch (IOException e) {
            throw new IOException();
        }
    }
}