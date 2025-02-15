package com.crowdfunding.capital_connection.controller;

import com.crowdfunding.capital_connection.controller.dto.ReviewRequest;
import com.crowdfunding.capital_connection.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/entrepreneurships/{entrepreneurshipId}/reviews")
@Tag(name = "Reviews", description = "API for managing reviews of entrepreneurship projects")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    /**
     * Create a new review for a specific entrepreneurship.
     *
     * @param entrepreneurshipId The entrepreneurship ID (from the route)
     * @param reviewRequest      The request object containing review details
     * @return The created review
     */
    @Operation(summary = "Create a new review", description = "Creates a new review for a specific entrepreneurship.")
    @PostMapping
    public ResponseEntity<ReviewRequest> createReview(
            @PathVariable Long entrepreneurshipId,
            @RequestBody ReviewRequest reviewRequest) {

        reviewRequest.setId_entrepreneurship(entrepreneurshipId);

        ReviewRequest createdReview = reviewService.createReview(reviewRequest);

        return ResponseEntity.ok(createdReview);
    }

    /**
     * Get all reviews for a specific entrepreneurship.
     */
    @Operation(summary = "Get all reviews", description = "Returns all reviews for a specific entrepreneurship.")
    @GetMapping
    public ResponseEntity<List<ReviewRequest>> getReviewsByEntrepreneurshipId(@PathVariable Long entrepreneurshipId) {
        List<ReviewRequest> reviews = reviewService.getReviewsByEntrepreneurshipId(entrepreneurshipId);

        return ResponseEntity.ok(reviews);
    }

    /**
     * Update an existing review.
     */
    @Operation(summary = "Update a review", description = "Updates the details of an existing review.")
    @PutMapping("/{id}")
    public ResponseEntity<ReviewRequest> updateReview(
            @PathVariable Long id,
            @RequestBody ReviewRequest reviewRequest) {

        ReviewRequest updatedReview = reviewService.updateReview(id, reviewRequest);

        return ResponseEntity.ok(updatedReview);
    }

    /**
     * Partially update a review (PATCH).
     */
    @Operation(summary = "Partially update a review", description = "Partially updates the details of an existing review.")
    @PatchMapping("/{id}")
    public ResponseEntity<ReviewRequest> updateReviewPartially(
            @PathVariable Long entrepreneurshipId,  // entrepreneurshipId comes from the URL path
            @PathVariable Long id,                   // review ID also comes from the URL path
            @RequestBody ReviewRequest reviewRequest) {

        reviewRequest.setId_entrepreneurship(entrepreneurshipId);  // Ensure entrepreneurshipId is set in request

        ReviewRequest updatedReview = reviewService.updateReviewPartially(id, reviewRequest);

        return ResponseEntity.ok(updatedReview);
    }

    /**
     * Deactivate a review without removing it from the database.
     */
    @Operation(summary = "Deactivate a review", description = "Deactivates a review without removing it from the database.")
    @DeleteMapping("/{id}")
    public ResponseEntity<ReviewRequest> deactivateReview(@PathVariable Long id) {
        ReviewRequest deactivatedReview = reviewService.deactivateReview(id);

        return ResponseEntity.ok(deactivatedReview);
    }

    @Operation(summary = "Search a review for user", description = "Deactivates a review without removing it from the database.")
    @GetMapping("/a/{accountId}")
    public ResponseEntity<Boolean> getReviewForAccount(   @PathVariable Long entrepreneurshipId,
                                                                @PathVariable Long accountId) {
        boolean exists =  reviewService.findReviewByEntrepreneurshipAndAccount(entrepreneurshipId, accountId);
        return ResponseEntity.ok(exists);
    }
}
