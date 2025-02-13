package com.crowdfunding.capital_connection.model.mapper;

import com.crowdfunding.capital_connection.controller.dto.AccountRequest;
import com.crowdfunding.capital_connection.controller.dto.EntrepreneurshipRequest;
import com.crowdfunding.capital_connection.repository.entity.AccountEntity;
import com.crowdfunding.capital_connection.repository.entity.EntrepreneurshipEntity;
import com.crowdfunding.capital_connection.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
@Component
public class EntrepreneurshipMapper {

    @Autowired
    private AccountService accountService;

    public EntrepreneurshipEntity toEntity(EntrepreneurshipRequest entrepreneurshipRequest) {
        EntrepreneurshipEntity entrepreneurshipEntity = new EntrepreneurshipEntity();
        entrepreneurshipEntity.setId(entrepreneurshipRequest.getId());
        entrepreneurshipEntity.setName(entrepreneurshipRequest.getName());
        entrepreneurshipEntity.setImages(entrepreneurshipRequest.getImages());
        entrepreneurshipEntity.setDescription(entrepreneurshipRequest.getDescription());
        entrepreneurshipEntity.setVideos(entrepreneurshipRequest.getVideos());
        entrepreneurshipEntity.setGoal(entrepreneurshipRequest.getGoal());
        entrepreneurshipEntity.setCategory(entrepreneurshipRequest.getCategory());
        entrepreneurshipEntity.setCollected(entrepreneurshipRequest.getCollected());
        entrepreneurshipEntity.setIsActivated(entrepreneurshipRequest.getIsActivated());

        // AccountService se inyecta automáticamente
        AccountEntity accountEntity = accountService.getAccountById(entrepreneurshipRequest.getId_account());
        entrepreneurshipEntity.setAccount(accountEntity);

        return entrepreneurshipEntity;
    }

    public EntrepreneurshipRequest toDto(EntrepreneurshipEntity entrepreneurshipEntity) {
        EntrepreneurshipRequest dto = new EntrepreneurshipRequest();
        dto.setId(entrepreneurshipEntity.getId());
        dto.setName(entrepreneurshipEntity.getName());
        dto.setIsActivated(entrepreneurshipEntity.getIsActivated());
        dto.setGoal(entrepreneurshipEntity.getGoal());
        dto.setCategory(entrepreneurshipEntity.getCategory());
        dto.setCollected(entrepreneurshipEntity.getCollected());
        dto.setDescription(entrepreneurshipEntity.getDescription());

        // Convertir PersistentBag a ArrayList
        dto.setImages(new ArrayList<>(entrepreneurshipEntity.getImages()));
        dto.setVideos(new ArrayList<>(entrepreneurshipEntity.getVideos()));

        dto.setId_account(entrepreneurshipEntity.getAccount().getId());
        return dto;
    }
}