package com.crowdfunding.capital_connection.model.mapper;


import com.crowdfunding.capital_connection.controller.dto.AddressRequest;
import com.crowdfunding.capital_connection.repository.entity.AddressEntity;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    public AddressEntity toEntity(AddressRequest addressRequest) {
        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setStreet(addressRequest.getStreet());
        addressEntity.setNumber(addressRequest.getNumber());
        addressEntity.setLocality(addressRequest.getLocality());
        addressEntity.setProvince(addressRequest.getProvince());
        addressEntity.setType(addressRequest.getType());
        addressEntity.setIsActivated(addressRequest.getIsActivated());
        return addressEntity;
    }

    public AddressRequest toDto(AddressEntity addressEntity) {
        AddressRequest addressRequest = new AddressRequest();
        addressRequest.setStreet(addressEntity.getStreet());
        addressRequest.setNumber(addressEntity.getNumber());
        addressRequest.setLocality(addressEntity.getLocality());
        addressRequest.setProvince(addressEntity.getProvince());
        addressRequest.setType(addressEntity.getType());
        addressRequest.setIsActivated(addressEntity.getIsActivated());
        return addressRequest;
    }
}
