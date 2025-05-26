package com.luxstay.hotelreservationsystem.service;

import com.luxstay.hotelreservationsystem.model.Review;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Service
public class ReviewService {
    private static final ReentrantLock fileLock = new ReentrantLock();
    private final List<Review> reviews = new ArrayList<>();
    private final ObjectMapper objectMapper;
    @Value("${reviews.file.path:reviews.json}")
    private String reviewsFilePath;
    private Path filePath;

    public ReviewService() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @PostConstruct
    public void init() {
        this.filePath = Paths.get(reviewsFilePath).toAbsolutePath();
        loadReviewsFromFile();
    }

    private void loadReviewsFromFile() {
        fileLock.lock();
        try {
            if (Files.exists(filePath)) {
                try {
                    String content = Files.readString(filePath);
                    if (!content.trim().isEmpty()) {
                        List<Review> loadedReviews = objectMapper.readValue(
                                content,
                                objectMapper.getTypeFactory().constructCollectionType(List.class, Review.class)
                        );
                        reviews.clear();
                        reviews.addAll(loadedReviews);
                        System.out.println("Successfully loaded " + reviews.size() + " reviews from " + filePath);
                    } else {
                        System.out.println("Reviews file is empty at " + filePath + ". Initializing with empty list.");
                        reviews.clear();
                    }
                } catch (IOException e) {
                    System.err.println("Error loading reviews from file: " + e.getMessage());
                    reviews.clear();
                }
            } else {
                System.out.println("No existing reviews file found at " + filePath + ". New file will be created.");
                reviews.clear();
            }
        } finally {
            fileLock.unlock();
        }
    }

    private void saveReviewsToFile() {
        fileLock.lock();
        try {
            try {
                Files.createDirectories(filePath.getParent());
                String content = objectMapper.writeValueAsString(reviews);
                Files.writeString(filePath, content);
                System.out.println("Successfully saved " + reviews.size() + " reviews to " + filePath);
            } catch (IOException e) {
                System.err.println("Error saving reviews to file: " + e.getMessage());
                throw new RuntimeException("Failed to persist reviews", e);
            }
        } finally {
            fileLock.unlock();
        }
    }

    public List<Review> getAllReviews() {
        return Collections.unmodifiableList(reviews);
    }

    public List<Review> getReviewsByStatus(String status) {
        return reviews.stream()
                .filter(review -> review.getStatus().equalsIgnoreCase(status))
                .collect(Collectors.toList());
    }

    public Optional<Review> getReviewById(String id) {
        return reviews.stream()
                .filter(review -> review.getId().equals(id))
                .findFirst();
    }

    public Review addReview(Review review) {
        Objects.requireNonNull(review, "Review cannot be null");

        if (review.getGuestName() == null || review.getGuestName().trim().isEmpty()) {
            throw new IllegalArgumentException("Guest name is required");
        }
        if (review.getGuestEmail() == null || !review.getGuestEmail().contains("@")) { // Basic email validation
            throw new IllegalArgumentException("Valid guest email is required");
        }
        if (review.getRating() < 1 || review.getRating() > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        if (review.getReviewText() == null || review.getReviewText().trim().isEmpty()) {
            throw new IllegalArgumentException("Review text is required");
        }
        if (review.getRoomType() == null || review.getRoomType().trim().isEmpty()) {
            throw new IllegalArgumentException("Room type is required");
        }

        review.setId(UUID.randomUUID().toString());
        review.setDate(LocalDate.now());
        review.setStatus("PENDING");
        review.setHelpfulCount(0);

        reviews.add(review);
        saveReviewsToFile();
        return review;
    }

    public Optional<Review> updateReview(String id, Review updatedReview) {
        Objects.requireNonNull(updatedReview, "Updated review cannot be null");
        Objects.requireNonNull(id, "Review ID cannot be null");

        Optional<Review> existingReviewOpt = getReviewById(id);
        if (existingReviewOpt.isPresent()) {
            Review existingReview = existingReviewOpt.get();

            if (updatedReview.getGuestName() != null && !updatedReview.getGuestName().trim().isEmpty()) {
                existingReview.setGuestName(updatedReview.getGuestName());
            }
            if (updatedReview.getGuestEmail() != null && !updatedReview.getGuestEmail().trim().isEmpty()) {
                existingReview.setGuestEmail(updatedReview.getGuestEmail());
            }
            if (updatedReview.getRoomType() != null && !updatedReview.getRoomType().trim().isEmpty()) {
                existingReview.setRoomType(updatedReview.getRoomType());
            }
            if (updatedReview.getRating() >= 1 && updatedReview.getRating() <= 5) {
                existingReview.setRating(updatedReview.getRating());
            }
            if (updatedReview.getReviewText() != null && !updatedReview.getReviewText().trim().isEmpty()) {
                existingReview.setReviewText(updatedReview.getReviewText());
            }
            if (updatedReview.getStatus() != null && !updatedReview.getStatus().trim().isEmpty()) {
                existingReview.setStatus(updatedReview.getStatus());
            }

            saveReviewsToFile();
            return Optional.of(existingReview);
        }
        return Optional.empty();
    }

    public boolean deleteReview(String id) {
        Objects.requireNonNull(id, "Review ID cannot be null");

        boolean removed = reviews.removeIf(review -> review.getId().equals(id));
        if (removed) {
            saveReviewsToFile();
        }
        return removed;
    }

    public Optional<Review> updateReviewStatus(String id, String newStatus) {
        Objects.requireNonNull(id, "Review ID cannot be null");
        Objects.requireNonNull(newStatus, "Status cannot be null");

        if (!List.of("PENDING", "APPROVED", "REJECTED").contains(newStatus.toUpperCase())) {
            throw new IllegalArgumentException("Invalid status value: " + newStatus + ". Must be PENDING, APPROVED, or REJECTED.");
        }

        Optional<Review> reviewOpt = getReviewById(id);
        if (reviewOpt.isPresent()) {
            Review review = reviewOpt.get();
            review.setStatus(newStatus);
            saveReviewsToFile();
            return Optional.of(review);
        }
        return Optional.empty();
    }

    public Optional<Review> incrementHelpfulCount(String id) {
        Optional<Review> reviewOpt = getReviewById(id);
        if (reviewOpt.isPresent()) {
            Review review = reviewOpt.get();
            review.setHelpfulCount(review.getHelpfulCount() + 1);
            saveReviewsToFile();
            return Optional.of(review);
        }
        return Optional.empty();
    }
}
