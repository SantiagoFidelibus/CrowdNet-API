package com.crowdfunding.capital_connection.service;

import com.crowdfunding.capital_connection.controller.dto.EntrepreneurshipRequest;
import com.crowdfunding.capital_connection.model.mapper.EntrepreneurshipMapper;
import com.crowdfunding.capital_connection.repository.EntrepreneurshipRepository;
import com.crowdfunding.capital_connection.repository.entity.EntrepreneurshipEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EntrepreneurshipService {

    @Autowired
    private EntrepreneurshipRepository entrepreneurshipRepository;

    @Autowired
    private EntrepreneurshipMapper entrepreneurshipMapper;

    /**
     * Create a new entrepreneurship.
     */
    @Transactional
    public EntrepreneurshipRequest createEntrepreneurship(EntrepreneurshipRequest entrepreneurshipRequest) {
        EntrepreneurshipEntity entrepreneurshipEntity = entrepreneurshipMapper.toEntity(entrepreneurshipRequest);
        EntrepreneurshipEntity savedEntity = entrepreneurshipRepository.save(entrepreneurshipEntity);
        return entrepreneurshipMapper.toDto(savedEntity);
    }

    /**
     * Get entrepreneurship by ID.
     */
    @Transactional
    public EntrepreneurshipRequest getEntrepreneurshipById(Long id) {
        EntrepreneurshipEntity entrepreneurshipEntity = entrepreneurshipRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entrepreneurship not found"));
        return entrepreneurshipMapper.toDto(entrepreneurshipEntity);
    }

    /**
     * Get entrepreneurship by account ID.
     */
    @Transactional
    public List<EntrepreneurshipRequest> getEntrepreneurshipByAccountId(Long accountId) {
        List<EntrepreneurshipEntity> entrepreneurshipEntities = entrepreneurshipRepository.findByAccountId(accountId);
        return entrepreneurshipEntities.stream()
                .map(entrepreneurshipMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Update entrepreneurship (PUT).
     */
    @Transactional
    public EntrepreneurshipRequest updateEntrepreneurship(Long id, EntrepreneurshipRequest entrepreneurshipRequest) {
        EntrepreneurshipEntity entrepreneurshipEntity = entrepreneurshipRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entrepreneurship not found"));

        entrepreneurshipEntity.setName(entrepreneurshipRequest.getName());
        entrepreneurshipEntity.setImages(entrepreneurshipRequest.getImages());
        entrepreneurshipEntity.setDescription(entrepreneurshipRequest.getDescription());
        entrepreneurshipEntity.setVideos(entrepreneurshipRequest.getVideos());
        entrepreneurshipEntity.setGoal(entrepreneurshipRequest.getGoal());
        entrepreneurshipEntity.setCategory(entrepreneurshipRequest.getCategory());
        entrepreneurshipEntity.setCollected(entrepreneurshipRequest.getCollected());
        entrepreneurshipEntity.setIsActivated(entrepreneurshipRequest.getIsActivated());

        EntrepreneurshipEntity updatedEntity = entrepreneurshipRepository.save(entrepreneurshipEntity);
        return entrepreneurshipMapper.toDto(updatedEntity);
    }

    /**
     * Partially update entrepreneurship (PATCH).
     */
    @Transactional
    public EntrepreneurshipRequest partiallyUpdateEntrepreneurship(Long id, EntrepreneurshipRequest entrepreneurshipRequest) {
        EntrepreneurshipEntity entrepreneurshipEntity = entrepreneurshipRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entrepreneurship not found"));

        if (entrepreneurshipRequest.getName() != null) {
            entrepreneurshipEntity.setName(entrepreneurshipRequest.getName());
        }
        if (entrepreneurshipRequest.getImages() != null) {
            entrepreneurshipEntity.setImages(entrepreneurshipRequest.getImages());
        }
        if (entrepreneurshipRequest.getDescription() != null) {
            entrepreneurshipEntity.setDescription(entrepreneurshipRequest.getDescription());
        }
        if (entrepreneurshipRequest.getVideos() != null) {
            entrepreneurshipEntity.setVideos(entrepreneurshipRequest.getVideos());
        }
        if (entrepreneurshipRequest.getGoal() != null) {
            entrepreneurshipEntity.setGoal(entrepreneurshipRequest.getGoal());
        }
        if (entrepreneurshipRequest.getCategory() != null) {
            entrepreneurshipEntity.setCategory(entrepreneurshipRequest.getCategory());
        }
        if (entrepreneurshipRequest.getCollected() != null) {
            entrepreneurshipEntity.setCollected(entrepreneurshipRequest.getCollected());
        }
        if (entrepreneurshipRequest.getIsActivated() != null) {
            entrepreneurshipEntity.setIsActivated(entrepreneurshipRequest.getIsActivated());
        }

        EntrepreneurshipEntity updatedEntity = entrepreneurshipRepository.save(entrepreneurshipEntity);
        return entrepreneurshipMapper.toDto(updatedEntity);
    }

    /**
     * Deactivate entrepreneurship (soft delete).
     */
    @Transactional
    public EntrepreneurshipRequest deactivateEntrepreneurship(Long id) {
        EntrepreneurshipEntity entrepreneurshipEntity = entrepreneurshipRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entrepreneurship not found"));

        entrepreneurshipEntity.setIsActivated(false);
        EntrepreneurshipEntity updatedEntity = entrepreneurshipRepository.save(entrepreneurshipEntity);
        return entrepreneurshipMapper.toDto(updatedEntity);
    }

    /**
     * Get all active entrepreneurships with sorting and pagination.
     */
    @Transactional
    public Page<EntrepreneurshipRequest> getActiveEntrepreneurships(Pageable pageable) {
        Page<EntrepreneurshipEntity> entrepreneurshipEntities = entrepreneurshipRepository.findByIsActivatedTrue(pageable);
        return entrepreneurshipEntities.map(entrepreneurshipMapper::toDto);
    }

    /**
     * Obtiene emprendimientos activos filtrados opcionalmente por categoría y/o goal.
     * Si category o goal son null, se ignora ese filtro.
     */
    @Transactional
    public Page<EntrepreneurshipRequest> getEntrepreneurshipsByFilter(String category, BigDecimal goal, String goalCondition, Pageable pageable) {
        Page<EntrepreneurshipEntity> entities;

        if (goal != null) {
            if ("asc".equalsIgnoreCase(goalCondition)) {
                entities = entrepreneurshipRepository.findByCategoryAndGoalGreaterThanEqualAndActive(category, goal, pageable);
            } else if ("desc".equalsIgnoreCase(goalCondition)) {
                entities = entrepreneurshipRepository.findByCategoryAndGoalLessThanEqualAndActive(category, goal, pageable);
            } else {
                entities = entrepreneurshipRepository.findByCategoryAndGoalExactAndActive(category, goal, pageable);
            }
        } else {
            entities = entrepreneurshipRepository.findByCategoryAndActive(category, pageable);
        }

        return entities.map(entrepreneurshipMapper::toDto);
    }


}