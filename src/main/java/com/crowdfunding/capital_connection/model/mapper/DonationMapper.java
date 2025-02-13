package com.crowdfunding.capital_connection.model.mapper;

import com.crowdfunding.capital_connection.controller.dto.DonationRequest;
import com.crowdfunding.capital_connection.controller.dto.EntrepreneurshipRequest;
import com.crowdfunding.capital_connection.repository.entity.AccountEntity;
import com.crowdfunding.capital_connection.repository.entity.DonationEntity;
import com.crowdfunding.capital_connection.repository.entity.EntrepreneurshipEntity;
import com.crowdfunding.capital_connection.service.AccountService;
import com.crowdfunding.capital_connection.service.EntrepreneurshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DonationMapper {


    private final EntrepreneurshipService entrepreneurshipService;
    private final AccountService accountService;
    private final EntrepreneurshipMapper entrepreneurshipMapper; // Inyectar EntrepreneurshipMapper

    // Constructor para inyección de dependencias
    @Autowired
    public DonationMapper(EntrepreneurshipService entrepreneurshipService, AccountService accountService, EntrepreneurshipMapper entrepreneurshipMapper) {
        this.entrepreneurshipService = entrepreneurshipService;
        this.accountService = accountService;
        this.entrepreneurshipMapper = entrepreneurshipMapper;
    }

    public DonationEntity toEntity(DonationRequest donationRequest) {
        DonationEntity donationEntity = new DonationEntity();
        donationEntity.setId(donationRequest.getId());
        donationEntity.setAmount(donationRequest.getAmount());
        donationEntity.setDate(donationRequest.getDate());
        donationEntity.setIsActivated(true);
        donationEntity.setStatus(donationRequest.getStatus());

        // Obtener los datos relacionados con Entrepreneurship
        EntrepreneurshipRequest entrepreneurshipRequest = entrepreneurshipService.getEntrepreneurshipById(donationRequest.getId_entrepreneurship());
        EntrepreneurshipEntity entrepreneurshipEntity = entrepreneurshipMapper.toEntity(entrepreneurshipRequest); // Usar el mapper inyectado
        donationEntity.setEntrepreneurship(entrepreneurshipEntity);

        // Obtener los datos relacionados con Account
        AccountEntity accountEntity = accountService.getAccountById(donationRequest.getId_user());
        donationEntity.setAccount(accountEntity);

        return donationEntity;
    }

    public DonationRequest toDto(DonationEntity donationEntity) {
        DonationRequest dto = new DonationRequest();
        dto.setId(donationEntity.getId());
        dto.setDate(donationEntity.getDate());
        dto.setAmount(donationEntity.getAmount());
        dto.setIsActivated(donationEntity.getIsActivated());
        dto.setId_user(donationEntity.getAccount().getId());
        dto.setId_entrepreneurship(donationEntity.getEntrepreneurship().getId());
        dto.setStatus(donationEntity.getStatus());
        return dto;
    }
}