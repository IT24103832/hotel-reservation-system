package com.luxstay.hotelreservationsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.luxstay.hotelreservationsystem")
public class HotelReservationSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(HotelReservationSystemApplication.class, args);
    }
}
