package com.luxstay.hotelreservationsystem.model;

import java.time.LocalDate;


public class GuestReview extends Review {

    public GuestReview() {
        super();
    }

    public GuestReview(String guestName, String roomType, String reviewText, int rating, String guestEmail) {
        super();
        setGuestName(guestName);
        setRoomType(roomType);
        setReviewText(reviewText);
        setRating(rating);
        setGuestEmail(guestEmail);
    }

    public GuestReview(String id, String guestName, String guestEmail, String roomType, int rating,
                       String reviewText, LocalDate date, String status, int helpfulCount) {
        super(id, guestName, guestEmail, roomType, rating, reviewText, date, status, helpfulCount);
    }
}


