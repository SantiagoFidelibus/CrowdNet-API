package com.crowdfunding.capital_connection.controller.dto;
import com.crowdfunding.capital_connection.repository.entity.EntrepreneurshipEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Schema(description = "Payload for Account information")
@Getter
@Setter
public class AccountRequest {

    @Schema(description = "ID of the user", example = "1")
    private Long id;

    @Schema(description = "Username of the user", example = "johntheinvestor20")
    private String username;

    @Schema(description = "Password of at least 8 characters, lowercase, uppercase, number and special character", example = "12345Aa!")
    private String password;

    @Schema(description = "Id of google", example = "12345Aa!")
    private String providerId;

    @Schema(description = "Email of the user", example = "example@gmail.com")
    private String email;

    @Schema(description = "Name of the user", example = "John")
    private String name;

    @Schema(description = "Surname of the user", example = "Doe")
    private String surname;

    @Schema(description = "Date of birth in YYYY-MM-DD format", example = "2003-12-16")
    private String dateOfBirth;

    @Schema(description = "How many years of experience the user has in the entrepreneurial and investing world", example = "2")
    private Integer yearsOfExperience;

    @Schema(description = "Industry closest to the user's taste", example = "Technology")
    private String industry;

    @Schema(description = "How much money does the user have in their account (only if they have a business in USD)", example = "100000")
    private BigDecimal wallet;

    @Schema(description = "ID of the address of the user", example = "1")
    private List<Long> favoriteEntrepreneurshipIds;

    @Schema(description = "if this data is activated", example = "true")
    private Boolean isActivated;

    @Schema(description = "Address object", example = "1")
    private AddressRequest address;

    // Este método se ejecuta al momento de la validación del DTO
    @AssertTrue(message = "Debe proporcionarse al menos un valor: password o providerId")
    public boolean isPasswordOrProviderIdProvided() {
        return (password != null && !password.trim().isEmpty()) ||
                (providerId != null && !providerId.trim().isEmpty());
    }

}
