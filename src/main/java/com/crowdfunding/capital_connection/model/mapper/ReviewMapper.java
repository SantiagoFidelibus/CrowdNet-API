package com.crowdfunding.capital_connection.model.mapper;

import com.crowdfunding.capital_connection.controller.dto.EntrepreneurshipRequest;
import com.crowdfunding.capital_connection.controller.dto.ReviewRequest;
import com.crowdfunding.capital_connection.repository.entity.AccountEntity;
import com.crowdfunding.capital_connection.repository.entity.EntrepreneurshipEntity;
import com.crowdfunding.capital_connection.repository.entity.ReviewEntity;
import com.crowdfunding.capital_connection.service.AccountService;
import com.crowdfunding.capital_connection.service.EntrepreneurshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

        @Autowired
        private EntrepreneurshipService entrepreneurshipService;

        @Autowired
        private AccountService accountService;

        @Autowired
        private EntrepreneurshipMapper entrepreneurshipMapper;

        public ReviewEntity toEntity(ReviewRequest dto){
            ReviewEntity reviewEntity = new ReviewEntity();
            reviewEntity.setId(dto.getId());
            reviewEntity.setStars(dto.getStars());
            reviewEntity.setReviewText(dto.getReviewText());
            reviewEntity.setIsActivated(dto.getIsActivated());

            EntrepreneurshipRequest entrepreneurshipRequest = entrepreneurshipService.getEntrepreneurshipById(dto.getId_entrepreneurship());
            EntrepreneurshipEntity entrepreneurshipEntity = entrepreneurshipMapper.toEntity(entrepreneurshipRequest);
            reviewEntity.setEntrepreneurship(entrepreneurshipEntity);

            AccountEntity accountEntity = accountService.getAccountById(dto.getId_user());
            reviewEntity.setAccount(accountEntity);

            return reviewEntity;
        }

        public ReviewRequest toDto(ReviewEntity entity) {
            ReviewRequest reviewRequest = new ReviewRequest();
            reviewRequest.setId(entity.getId());
            reviewRequest.setStars(entity.getStars());
            reviewRequest.setReviewText(entity.getReviewText());
            reviewRequest.setIsActivated(entity.getIsActivated());
            reviewRequest.setUsername(entity.getAccount().getUsername());
            reviewRequest.setId_entrepreneurship(entity.getEntrepreneurship().getId());
            reviewRequest.setId_user(entity.getAccount().getId());

            return reviewRequest;
        }
    }


