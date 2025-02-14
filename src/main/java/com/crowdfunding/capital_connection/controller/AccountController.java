package com.crowdfunding.capital_connection.controller;

import com.crowdfunding.capital_connection.controller.dto.AccountDataRequest;
import com.crowdfunding.capital_connection.controller.dto.AccountRequest;
import com.crowdfunding.capital_connection.controller.dto.AddressRequest;
import com.crowdfunding.capital_connection.exception.DuplicateFieldException;
import com.crowdfunding.capital_connection.model.mapper.AccountMapper;
import com.crowdfunding.capital_connection.model.mapper.AddressMapper;
import com.crowdfunding.capital_connection.repository.entity.AccountEntity;
import com.crowdfunding.capital_connection.repository.entity.AddressEntity;
import com.crowdfunding.capital_connection.repository.entity.EntrepreneurshipEntity;
import com.crowdfunding.capital_connection.service.AccountService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;
    private final AccountMapper accountMapper;
    private final AddressMapper addressMapper;

    @Autowired
    public AccountController(AccountService accountService, AccountMapper accountMapper, AddressMapper addressMapper) {
        this.accountService = accountService;
        this.accountMapper = accountMapper;
        this.addressMapper = addressMapper;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<AccountEntity> getAccount(@PathVariable Long userId) {
        AccountEntity account = accountService.getAccountById(userId);
        return ResponseEntity.ok(account);
    }
    @Operation(summary = "Add a favorite entrepreneurship for a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Favorite added successfully"),
            @ApiResponse(responseCode = "404", description = "User or Entrepreneurship not found"),
            @ApiResponse(responseCode = "400", description = "Invalid data or request")
    })
    @PostMapping("/{userId}/favorites/{entrepreneurshipId}")
    public ResponseEntity<String> addFavorite(
            @PathVariable Long userId,
            @PathVariable Long entrepreneurshipId) {
        try {
            accountService.addFavorite(userId, entrepreneurshipId);
            return ResponseEntity.ok("Favorite added successfully");
        } catch (RuntimeException e) {
            // Manejar el error y devolver el código de error adecuado
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User or Entrepreneurship not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request data");
        }
    }

    @Operation(summary = "Remove a favorite entrepreneurship for a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Favorite removed successfully"),
            @ApiResponse(responseCode = "404", description = "User or Entrepreneurship not found"),
            @ApiResponse(responseCode = "400", description = "Invalid data or request")
    })
    @DeleteMapping("/{userId}/favorites/{entrepreneurshipId}")
    public ResponseEntity<String> removeFavorite(
            @PathVariable Long userId,
            @PathVariable Long entrepreneurshipId) {
        try {
            accountService.removeFavorite(userId, entrepreneurshipId);
            return ResponseEntity.ok("Favorite removed successfully");
        } catch (RuntimeException e) {
            // Manejar el error y devolver el código de error adecuado
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User or Entrepreneurship not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request data");
        }
    }
    @Operation(summary = "Get all favorite entrepreneurships of a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of favorites retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{userId}/favorites")
    public ResponseEntity<List<Long>> getFavorites(@PathVariable Long userId) {
        List<Long> favoriteIds = accountService.getFavoriteIds(userId);
        return ResponseEntity.ok(favoriteIds);
    }

    @PostMapping
    public ResponseEntity<AccountRequest> createAccount(@RequestBody AccountRequest accountRequest, HttpServletRequest request) {
        try {
            // Si no hay password, intentamos obtener el providerId de la cookie
            if (accountRequest.getPassword() == null || accountRequest.getPassword().isEmpty()) {
                String providerId = accountRequest.getProviderId(); // Recupera el provider_id de la cookie
                System.out.println(providerId);
                if (providerId != null) {
                    accountRequest.setProviderId(providerId); // Asigna el provider_id al account
                    accountRequest.setPassword(null); // Deja el password como null (no se necesita para provider)
                } else {
                    // Si no se encuentra un providerId, podría considerarse un error
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
                }
            } else {
                // Si hay password, setea providerId a null (no debe haber providerId si hay contraseña)
                accountRequest.setProviderId(null);
            }

            // Mapear el DTO a la entidad y guardar
            AccountEntity accountEntity = accountMapper.toEntity(accountRequest);
            AccountEntity createdAccount = accountService.createAccount(accountEntity);

            // Devolvemos el AccountDto con la información necesaria para el login
            return ResponseEntity.ok(accountMapper.toDto(createdAccount));

        } catch (DuplicateFieldException ex) {
            // Manejo de excepción si los campos duplicados son detectados (por ejemplo, email ya existe)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception ex) {
            // Manejo general de excepciones
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Método para obtener el providerId de la cookie
    private String getProviderIdFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("provider_id".equals(cookie.getName())) {
                    return cookie.getValue();  // Devolver el valor del cookie provider_id
                }
            }
        }
        return null;  // Si no se encuentra, devolver null
    }

    @PutMapping("/{accountId}")
    public ResponseEntity<AccountRequest> updateAccount(
            @PathVariable Long accountId,
            @RequestBody AccountRequest accountRequest) {

        AccountEntity accountEntity = accountMapper.toEntity(accountRequest);
        accountEntity.setId(accountId);

        AccountEntity updatedAccount = accountService.updateAccount(accountEntity);

        AccountRequest updatedAccountRequest = accountMapper.toDto(updatedAccount);
        return ResponseEntity.ok(updatedAccountRequest);
    }

    @PutMapping("/{accountId}/address")
    public ResponseEntity<AddressRequest> updateAddress(
            @PathVariable Long accountId,
            @RequestBody AddressRequest addressRequest) {

        AddressEntity addressEntity = addressMapper.toEntity(addressRequest);

        AccountEntity accountEntity = accountService.getAccountById(accountId);

        addressEntity.setAccount(accountEntity);

        AddressEntity updatedAddress = accountService.updateAddress(addressEntity);

        AddressRequest updatedAddressRequest = addressMapper.toDto(updatedAddress);
        return ResponseEntity.ok(updatedAddressRequest);
    }

    @PatchMapping("/{accountId}")
    @Operation(summary = "Update Account partially")
    public ResponseEntity<AccountRequest> patchAccount(
            @PathVariable Long accountId,
            @RequestBody AccountRequest accountRequest) {

        AccountEntity updatedAccount = accountService.updateAccountPartial(accountId, accountRequest);

        if (updatedAccount == null) {
            return ResponseEntity.notFound().build();
        }

        AccountRequest updatedAccountRequest = accountMapper.toDto(updatedAccount);
        return ResponseEntity.ok(updatedAccountRequest);
    }

    @PatchMapping("/{accountId}/address")
    @Operation(summary = "Update Address partially for an Account")
    public ResponseEntity<AddressRequest> patchAddress(
            @PathVariable Long accountId,
            @RequestBody AddressRequest addressRequest) {

        AddressEntity updatedAddress = accountService.updateAddressPartial(accountId, addressRequest);

        if (updatedAddress == null) {
            return ResponseEntity.notFound().build();
        }

        AddressRequest updatedAddressRequest = addressMapper.toDto(updatedAddress);
        return ResponseEntity.ok(updatedAddressRequest);
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<?> deactivateAccount(
            @PathVariable Long accountId) {

        accountService.deactivateAccountAndAddress(accountId);

        return ResponseEntity.ok("Account and associated address deactivated successfully");
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<AccountDataRequest> getByUsername(@PathVariable String username) {
        AccountDataRequest accountData = accountService.getAccountData(username);
        return ResponseEntity.ok(accountData);
    }

    @GetMapping("/exists/username/{username}")
    public boolean checkIfUsernameExists(@PathVariable String username) {
        return accountService.existsByUsername(username);
    }

    @GetMapping("/exists/email/{email}")
    public boolean checkIfEmailExists(@PathVariable String email) {
        return accountService.existsByEmail(email);
    }
}
