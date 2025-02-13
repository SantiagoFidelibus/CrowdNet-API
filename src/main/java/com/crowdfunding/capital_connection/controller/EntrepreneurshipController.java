package com.crowdfunding.capital_connection.controller;

import com.crowdfunding.capital_connection.controller.dto.EntrepreneurshipRequest;
import com.crowdfunding.capital_connection.service.EntrepreneurshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/entrepreneurships")
public class EntrepreneurshipController {

    @Autowired
    private EntrepreneurshipService entrepreneurshipService;

    /**
     * Create a new Entrepreneurship
     */
    @PostMapping
    public ResponseEntity<EntrepreneurshipRequest> createEntrepreneurship(@RequestBody EntrepreneurshipRequest entrepreneurshipRequest) {
        EntrepreneurshipRequest createdEntrepreneurship = entrepreneurshipService.createEntrepreneurship(entrepreneurshipRequest);
        return new ResponseEntity<>(createdEntrepreneurship, HttpStatus.CREATED);
    }

    /**
     * Get an Entrepreneurship by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<EntrepreneurshipRequest> getEntrepreneurshipById(@PathVariable Long id) {
        EntrepreneurshipRequest entrepreneurship = entrepreneurshipService.getEntrepreneurshipById(id);
        return new ResponseEntity<>(entrepreneurship, HttpStatus.OK);
    }

    /**
     * Get all Entrepreneurships by Account ID
     */
    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<EntrepreneurshipRequest>> getEntrepreneurshipsByAccountId(@PathVariable Long accountId) {
        List<EntrepreneurshipRequest> entrepreneurships = entrepreneurshipService.getEntrepreneurshipByAccountId(accountId);
        return new ResponseEntity<>(entrepreneurships, HttpStatus.OK);
    }

    /**
     * Update an Entrepreneurship (Full update)
     */
    @PutMapping("/{id}")
    public ResponseEntity<EntrepreneurshipRequest> updateEntrepreneurship(@PathVariable Long id, @RequestBody EntrepreneurshipRequest entrepreneurshipRequest) {
        EntrepreneurshipRequest updatedEntrepreneurship = entrepreneurshipService.updateEntrepreneurship(id, entrepreneurshipRequest);
        return new ResponseEntity<>(updatedEntrepreneurship, HttpStatus.OK);
    }

    /**
     * Partially Update an Entrepreneurship (PATCH)
     */
    @PatchMapping("/{id}")
    public ResponseEntity<EntrepreneurshipRequest> partiallyUpdateEntrepreneurship(@PathVariable Long id, @RequestBody EntrepreneurshipRequest entrepreneurshipRequest) {
        EntrepreneurshipRequest updatedEntrepreneurship = entrepreneurshipService.partiallyUpdateEntrepreneurship(id, entrepreneurshipRequest);
        return new ResponseEntity<>(updatedEntrepreneurship, HttpStatus.OK);
    }

    /**
     * Deactivate an Entrepreneurship (DELETE)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivateEntrepreneurship(@PathVariable Long id) {
        entrepreneurshipService.deactivateEntrepreneurship(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Get all Active Entrepreneurships with Pagination and Sorting
     */
    @GetMapping("/active")
    public ResponseEntity<Page<EntrepreneurshipRequest>> getActiveEntrepreneurships(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        Sort sort = sortDirection.equals("asc") ? Sort.by(Sort.Order.asc(sortBy)) : Sort.by(Sort.Order.desc(sortBy));
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<EntrepreneurshipRequest> activeEntrepreneurships = entrepreneurshipService.getActiveEntrepreneurships(pageable);
        return new ResponseEntity<>(activeEntrepreneurships, HttpStatus.OK);
    }

    /**
     * Obtiene emprendimientos activos filtrados opcionalmente por categoría y/o goal.
     * Ejemplo de URL:
     * /entrepreneurships/filter?category=salud&goal=10000&page=0&size=10&sortBy=name&sortDirection=asc
     */
    @GetMapping("/filter")
    public ResponseEntity<Page<EntrepreneurshipRequest>> filterEntrepreneurships(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) BigDecimal goal,
            @RequestParam(defaultValue = "none") String goalCondition, // Nuevo parámetro
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "goal") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        Sort sort = Sort.by(sortBy);

        if (goal != null) {
            if ("asc".equalsIgnoreCase(goalCondition)) {
                sort = Sort.by(Sort.Order.asc("goal")); // Metas mayores primero (orden ascendente)
            } else if ("desc".equalsIgnoreCase(goalCondition)) {
                sort = Sort.by(Sort.Order.desc("goal")); // Metas menores primero (orden descendente)
            } else {
                sort = Sort.unsorted(); // Mostrar solo el exacto sin ordenar
            }
        } else {
            sort = sortDirection.equalsIgnoreCase("asc") ? sort.ascending() : sort.descending();
        }

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<EntrepreneurshipRequest> entrepreneurshipPage =
                entrepreneurshipService.getEntrepreneurshipsByFilter(category, goal, goalCondition, pageable);

        return new ResponseEntity<>(entrepreneurshipPage, HttpStatus.OK);
    }


}
