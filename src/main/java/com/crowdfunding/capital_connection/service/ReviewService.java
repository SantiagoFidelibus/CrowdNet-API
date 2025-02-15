package com.crowdfunding.capital_connection.service;

import com.crowdfunding.capital_connection.controller.dto.ReviewRequest;
import com.crowdfunding.capital_connection.model.mapper.ReviewMapper;
import com.crowdfunding.capital_connection.repository.AccountRepository;
import com.crowdfunding.capital_connection.repository.EntrepreneurshipRepository;
import com.crowdfunding.capital_connection.repository.ReviewRepository;
import com.crowdfunding.capital_connection.repository.entity.ReviewEntity;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private EntrepreneurshipRepository entrepreneurshipRepository;

    @Autowired
    private ReviewMapper reviewMapper;

    /**
     * Crea una nueva reseña.
     */
    @Transactional
    public ReviewRequest createReview(ReviewRequest reviewRequest) {
        if (reviewRequest.getId_user() == null || reviewRequest.getId_entrepreneurship() == null) {
            throw new IllegalArgumentException("User ID and Entrepreneurship ID must not be null");
        }

        ReviewEntity reviewEntity = reviewMapper.toEntity(reviewRequest);

        ReviewEntity savedEntity = reviewRepository.save(reviewEntity);

        return reviewMapper.toDto(savedEntity);
    }

    /**
     * Obtiene todas las reseñas de un emprendimiento específico.
     */
    @Transactional
    public List<ReviewRequest> getReviewsByEntrepreneurshipId(Long entrepreneurshipId) {
        entrepreneurshipRepository.findById(entrepreneurshipId)
                .orElseThrow(() -> new RuntimeException("Entrepreneurship not found"));

        return reviewRepository.findByEntrepreneurshipId(entrepreneurshipId).stream()
                .map(reviewMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene todas las reseñas realizadas por un usuario específico.
     */
    @Transactional
    public List<ReviewRequest> getReviewsByUserId(Long userId) {
        accountRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        return reviewRepository.findAll().stream()
                .filter(review -> review.getAccount().getId().equals(userId))
                .map(reviewMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Actualiza una reseña existente.
     */
    @Transactional
    public ReviewRequest updateReview(Long id, ReviewRequest reviewRequest) {
        ReviewEntity reviewEntity = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        reviewEntity.setStars(reviewRequest.getStars());
        reviewEntity.setReviewText(reviewRequest.getReviewText());
        reviewEntity.setIsActivated(reviewRequest.getIsActivated());

        ReviewEntity updatedEntity = reviewRepository.save(reviewEntity);
        return reviewMapper.toDto(updatedEntity);
    }

    /**
     * Elimina una reseña por su ID.
     */
    @Transactional
    public void deleteReview(Long id) {
        ReviewEntity reviewEntity = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        reviewRepository.delete(reviewEntity);
    }

    /**
     * Desactiva una reseña sin eliminarla de la base de datos.
     */
    @Transactional
    public ReviewRequest deactivateReview(Long id) {
        ReviewEntity reviewEntity = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        reviewEntity.deactivate();
        ReviewEntity updatedEntity = reviewRepository.save(reviewEntity);
        return reviewMapper.toDto(updatedEntity);
    }


    /**
     * Partially updates an existing review.
     * Updates only the fields that are provided in the reviewRequest.
     */
    @Transactional
    public ReviewRequest updateReviewPartially(Long id, ReviewRequest reviewRequest) {
        ReviewEntity reviewEntity = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        if (reviewRequest.getStars() != 0) {
            reviewEntity.setStars(reviewRequest.getStars());
        }
        if (reviewRequest.getReviewText() != null) {
            reviewEntity.setReviewText(reviewRequest.getReviewText());
        }

        ReviewEntity updatedEntity = reviewRepository.save(reviewEntity);

        return reviewMapper.toDto(updatedEntity);
    }
    public boolean findReviewByEntrepreneurshipAndAccount(Long entrepreneurshipId, Long accountId) {
        return reviewRepository.findByEntrepreneurshipIdAndAccountId(entrepreneurshipId, accountId);

    }
}
