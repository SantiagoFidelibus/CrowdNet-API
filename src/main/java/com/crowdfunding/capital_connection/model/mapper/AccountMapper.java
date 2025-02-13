package com.crowdfunding.capital_connection.model.mapper;

import com.crowdfunding.capital_connection.controller.dto.AccountDataRequest;
import com.crowdfunding.capital_connection.controller.dto.AccountRequest;
import com.crowdfunding.capital_connection.controller.dto.AddressRequest;
import com.crowdfunding.capital_connection.repository.entity.AccountEntity;
import com.crowdfunding.capital_connection.repository.entity.AddressEntity;
import com.crowdfunding.capital_connection.repository.entity.EntrepreneurshipEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.stream.Collectors;

@Component
public class AccountMapper {


    public AccountEntity toEntity(AccountRequest dto) {
        AccountEntity account = new AccountEntity();
        account.setId(dto.getId());
        account.setUsername(dto.getUsername());
        if(dto.getPassword() != null) {
            account.setPassword(dto.getPassword());
            account.setProviderId(null);
        }else{
            account.setPassword(null);
            account.setProviderId(dto.getProviderId());
        }
        account.setEmail(dto.getEmail());
        account.setName(dto.getName());
        account.setSurname(dto.getSurname());
        account.setDateOfBirth(LocalDate.parse(dto.getDateOfBirth()));
        account.setYearsOfExperience(dto.getYearsOfExperience());
        account.setIndustry(dto.getIndustry());
        account.setWallet(dto.getWallet());
        account.setIsActivated(dto.getIsActivated());
        AddressMapper addressMapper = new AddressMapper();
        AddressEntity address = addressMapper.toEntity(dto.getAddress());
        account.setAddress(address);
        account.setProviderId(dto.getProviderId());

        return account;
    }

    public AccountRequest toDto(AccountEntity entity) {
        AccountRequest dto = new AccountRequest();
        dto.setId(entity.getId());
        dto.setUsername(entity.getUsername());
        if(entity.getPassword() != null) {
            dto.setPassword(entity.getPassword());
            dto.setProviderId(null);
        }else{
            dto.setPassword(null);
            dto.setProviderId(entity.getProviderId());
        }
        dto.setEmail(entity.getEmail());
        dto.setName(entity.getName());
        dto.setSurname(entity.getSurname());
        dto.setDateOfBirth(String.valueOf(entity.getDateOfBirth()));
        dto.setYearsOfExperience(entity.getYearsOfExperience());
        dto.setIndustry(entity.getIndustry());
        dto.setWallet(entity.getWallet());
        dto.setIsActivated(entity.getIsActivated());
        AddressMapper addressMapper = new AddressMapper();
        AddressRequest address = addressMapper.toDto(entity.getAddress());
        dto.setAddress(address);
        dto.setFavoriteEntrepreneurshipIds(entity.getFavoriteEntrepreneurships()
                .stream()
                .map(EntrepreneurshipEntity::getId)
                .collect(Collectors.toList()));
        return dto;
    }


    public AccountDataRequest toRequest(AccountEntity entity){
        AccountDataRequest dto = new AccountDataRequest();
        dto.setId(entity.getId());
        dto.setUsername(entity.getUsername());
        dto.setEmail(entity.getEmail());
        dto.setPassword(entity.getPassword());
        return dto;
    }
}