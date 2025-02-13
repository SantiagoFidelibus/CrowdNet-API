package com.crowdfunding.capital_connection.service;

import com.crowdfunding.capital_connection.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressService {


    private AddressRepository addressRepository;

    @Autowired
    public AddressService(AddressRepository addressRepository){

    }
}
