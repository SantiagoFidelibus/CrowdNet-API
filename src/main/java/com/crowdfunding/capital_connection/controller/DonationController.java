package com.crowdfunding.capital_connection.controller;

import com.crowdfunding.capital_connection.controller.dto.DonationRequest;
import com.crowdfunding.capital_connection.controller.dto.StatusUpdateRequest;
import com.crowdfunding.capital_connection.repository.entity.DonationEntity;
import com.crowdfunding.capital_connection.service.DonationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/accounts/{accountId}/donations")
public class DonationController {

    @Autowired
    private DonationService donationService;
    /**
     * Endpoint para crear una nueva donación.
     *
     * @param donationRequest  Información de la donación.
     * @return La donación creada como respuesta.
     */
    @PostMapping
    public ResponseEntity<DonationRequest> createDonation(
            @RequestBody DonationRequest donationRequest) {
        DonationRequest createdDonation = donationService.createDonation(donationRequest);
        return ResponseEntity.ok(createdDonation);
    }

    /**
     * Endpoint para obtener todas las donaciones de un usuario.
     *
     * @param accountId ID del usuario.
     * @return Lista de donaciones realizadas por el usuario.
     */
    @GetMapping
    public ResponseEntity<List<DonationRequest>> getDonationsByAccountId(@PathVariable Long accountId) {
        List<DonationRequest> donations = donationService.getDonationsByAccountId(accountId);
        return ResponseEntity.ok(donations);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DonationRequest> updateDonationStatus(
            @PathVariable Long id,
            @RequestBody StatusUpdateRequest statusUpdate) {

        String status = statusUpdate.getStatus(); // Ahora extraemos bien el valor
        System.out.println(" AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA juanjoputoooooooooooooo cordes codeaaaaa\n AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA \n id "+ id+ "status "+ statusUpdate);
        if (status == null || status.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        DonationRequest updatedDonation = donationService.updateStatus(id, status);

        if (updatedDonation != null) {
            return ResponseEntity.ok(updatedDonation);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}

