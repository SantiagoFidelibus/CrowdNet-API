package com.crowdfunding.capital_connection.service;

import com.crowdfunding.capital_connection.model.mapper.DonationMapper;
import com.crowdfunding.capital_connection.repository.AccountRepository;
import com.crowdfunding.capital_connection.repository.DonationRepository;
import com.crowdfunding.capital_connection.repository.EntrepreneurshipRepository;
import com.crowdfunding.capital_connection.repository.entity.DonationEntity;
import com.crowdfunding.capital_connection.controller.dto.DonationRequest;
import com.crowdfunding.capital_connection.repository.entity.EntrepreneurshipEntity;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DonationService {

    @Autowired
    private DonationRepository donationRepository;

    @Autowired
    private AccountRepository accountRepository;


    @Autowired
    private EntrepreneurshipRepository entrepreneurshipRepository;

    @Autowired
    private DonationMapper donationMapper;

    /**
     * Crea una nueva donación a partir de un objeto DonationRequest.
     */
    @Transactional
    public DonationRequest createDonation(DonationRequest donationRequest) {
        accountRepository.findById(donationRequest.getId_user())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        entrepreneurshipRepository.findById(donationRequest.getId_entrepreneurship())
                .orElseThrow(() -> new RuntimeException("Entrepreneurship not found"));

        DonationEntity donationEntity = donationMapper.toEntity(donationRequest);
        DonationEntity savedEntity = donationRepository.save(donationEntity);


        return donationMapper.toDto(savedEntity);
    }

    @Transactional
    public DonationRequest updateStatus(Long donationId, String status) {
        try {
            DonationEntity donationEntity = donationRepository.findById(donationId)
                    .orElseThrow(() -> new RuntimeException("Donation not found"));

            // Actualizar el estado de la donación
            donationEntity.setStatus(status);

            if ("approved".equalsIgnoreCase(status)) {
            donationEntity.setIsActivated(true);
                // Obtener el emprendimiento asociado a la donación
                EntrepreneurshipEntity entrepreneurshipEntity = donationEntity.getEntrepreneurship();

                if (entrepreneurshipEntity == null) {
                    throw new RuntimeException("Entrepreneurship not found for donation ID: " + donationId);
                }

                // Sumar el monto de la donación a collected
                entrepreneurshipEntity.addCollectedAmount(donationEntity.getAmount());

                // Guardar cambios en el emprendimiento primero
                entrepreneurshipRepository.save(entrepreneurshipEntity);

            }else if ("rejected".equalsIgnoreCase(status)) {
                donationEntity.setIsActivated(true);
            }

            // Guardar la donación solo si el emprendimiento se guardó correctamente
            donationRepository.save(donationEntity);

            return donationMapper.toDto(donationEntity);

        } catch (Exception e) {
            // Registrar el error para facilitar la depuración
            throw new RuntimeException("Failed to update donation status: " + e.getMessage());
        }
    }

    /**
     * Obtiene todas las donaciones realizadas por un usuario específico.
     */
    @Transactional
    public List<DonationRequest> getDonationsByAccountId(Long accountId) {
        accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        return donationRepository.findByAccountId(accountId).stream()
                .map(donationMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<DonationRequest> getDonationsByOwnerId(Long ownerId) {
        accountRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        return donationRepository.findReceivedDonationsByOwner(ownerId).stream()
                .map(donationMapper::toDto)
                .collect(Collectors.toList());
    }

}
