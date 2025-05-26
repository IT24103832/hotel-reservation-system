package com.luxstay.hotelreservationsystem.model;

import java.time.LocalDate;

public class Review {

    private String id;
    private String guestName;
    private String guestEmail;
    private String roomType;
    private int rating;
    private String reviewText;
    private LocalDate date;
    private String status;
    private int helpfulCount;

    public Review() {
    }

    public Review(String id, String guestName, String guestEmail, String roomType, int rating,
                  String reviewText, LocalDate date, String status, int helpfulCount) {
        this.id = id;
        this.guestName = guestName;
        this.guestEmail = guestEmail;
        this.roomType = roomType;
        this.rating = rating;
        this.reviewText = reviewText;
        this.date = date;
        this.status = status;
        this.helpfulCount = helpfulCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public String getGuestEmail() {
        return guestEmail;
    }

    public void setGuestEmail(String guestEmail) {
        this.guestEmail = guestEmail;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getHelpfulCount() {
        return helpfulCount;
    }

    public void setHelpfulCount(int helpfulCount) {
        this.helpfulCount = helpfulCount;
    }

    public String display() {
        return "Review{" +
                "id='" + id + '\'' +
                ", guestName='" + guestName + '\'' +
                ", guestEmail='" + guestEmail + '\'' +
                ", roomType='" + roomType + '\'' +
                ", rating=" + rating +
                ", reviewText='" + reviewText + '\'' +
                ", date=" + date +
                ", status='" + status + '\'' +
                ", helpfulCount=" + helpfulCount +
                '}';
    }

    @Override
    public String toString() {
        return display();
    }
}