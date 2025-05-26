package com.luxstay.hotelreservationsystem.model;

import java.time.LocalDate;

public class AdminReview extends Review {

    public AdminReview() {
        super();
    }

    public AdminReview(String guestName, String roomType, String reviewText, int rating, String guestEmail) {
        super();
        setGuestName(guestName);
        setRoomType(roomType);
        setReviewText(reviewText);
        setRating(rating);
        setGuestEmail(guestEmail);
    }

    public AdminReview(String id, String guestName, String roomType, String reviewText, int rating, String guestEmail, LocalDate date, String status, int helpfulCount) {
        super(id, guestName, roomType, reviewText, rating, guestEmail, date, status, helpfulCount); // Corrected to use reviewText and guestEmail
    }

    @Override
    public String display() {
        return "[ADMIN VIEW] ID: " + getId() +
                " | Guest: " + getGuestName() +
                " | Email: " + getGuestEmail() +
                " | Room: " + getRoomType() +
                " | Rating: " + getRating() +
                " | Date: " + getDate() +
                " | Status: " + getStatus() +
                " | Helpful: " + getHelpfulCount() +
                "\nComment: " + getReviewText() + "\n---";
    }
}