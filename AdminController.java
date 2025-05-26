package com.luxstay.hotelreservationsystem.Controller;

import com.luxstay.hotelreservationsystem.model.Review;
import com.luxstay.hotelreservationsystem.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class AdminController {

    private final ReviewService reviewService;

    @Autowired
    public AdminController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/admin")
    public String viewAdminReviews(Model model) {
        model.addAttribute("reviews", reviewService.getAllReviews());
        return "adminReviews";
    }

    @PostMapping("/admin/update")
    public String updateReview(@ModelAttribute Review review, RedirectAttributes redirectAttributes) {
        Optional<Review> updated = reviewService.updateReview(review.getId(), review);

        if (updated.isPresent()) {
            redirectAttributes.addFlashAttribute("message", "Review updated successfully!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Review not found for update.");
        }
        return "redirect:/admin";
    }

    @PostMapping("/admin/delete")
    public String deleteReview(@RequestParam String id, RedirectAttributes redirectAttributes) {
        boolean deleted = reviewService.deleteReview(id);
        if (deleted) {
            redirectAttributes.addFlashAttribute("message", "Review deleted successfully!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Review not found for deletion.");
        }
        return "redirect:/admin";
    }

    @PostMapping("/admin/status")
    public String updateReviewStatus(@RequestParam String id, @RequestParam String status, RedirectAttributes redirectAttributes) {
        Optional<Review> updated = reviewService.updateReviewStatus(id, status);
        if (updated.isPresent()) {
            redirectAttributes.addFlashAttribute("message", "Review status updated to " + status + "!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Review not found for status update.");
        }
        return "redirect:/admin";
    }
}



