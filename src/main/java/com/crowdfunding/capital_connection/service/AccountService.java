package com.crowdfunding.capital_connection.service;

import com.crowdfunding.capital_connection.controller.dto.AccountDataRequest;
import com.crowdfunding.capital_connection.controller.dto.AccountRequest;
import com.crowdfunding.capital_connection.controller.dto.AddressRequest;
import com.crowdfunding.capital_connection.exception.AccountNotFoundException;
import com.crowdfunding.capital_connection.exception.DuplicateFieldException;
import com.crowdfunding.capital_connection.model.mapper.AccountMapper;
import com.crowdfunding.capital_connection.repository.AccountRepository;
import com.crowdfunding.capital_connection.repository.AddressRepository;
import com.crowdfunding.capital_connection.repository.EntrepreneurshipRepository;
import com.crowdfunding.capital_connection.repository.entity.AccountEntity;
import com.crowdfunding.capital_connection.repository.entity.AddressEntity;
import com.crowdfunding.capital_connection.repository.entity.EntrepreneurshipEntity;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private EntrepreneurshipRepository entrepreneurshipRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Transactional
    public void addFavorite(Long userId, Long entrepreneurshipId) {
        AccountEntity account = accountRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        EntrepreneurshipEntity entrepreneurship = entrepreneurshipRepository.findById(entrepreneurshipId)
                .orElseThrow(() -> new RuntimeException("Entrepreneurship not found"));

        // Añadir el emprendimiento a los favoritos del usuario
        account.getFavoriteEntrepreneurships().add(entrepreneurship);

        // Añadir el usuario a la lista de personas que han marcado este emprendimiento como favorito
        entrepreneurship.getUsersWhoFavorited().add(account);

        // No es necesario llamar a accountRepository.save(account) si ya estamos dentro de una transacción
    }

    @Transactional
    public void removeFavorite(Long userId, Long entrepreneurshipId) {
        AccountEntity account = accountRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        EntrepreneurshipEntity entrepreneurship = entrepreneurshipRepository.findById(entrepreneurshipId)
                .orElseThrow(() -> new RuntimeException("Entrepreneurship not found"));

        // Eliminar el emprendimiento de los favoritos del usuario
        account.getFavoriteEntrepreneurships().remove(entrepreneurship);

        // Eliminar el usuario de la lista de personas que han marcado este emprendimiento como favorito
        entrepreneurship.getUsersWhoFavorited().remove(account);

        // No es necesario llamar a accountRepository.save(account) si ya estamos dentro de una transacción
    }

    @Transactional
    public List<Long> getFavoriteIds(Long userId) {
        AccountEntity account = accountRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        // Mapea las entidades de emprendimiento a sus IDs
        return account.getFavoriteEntrepreneurships()
                .stream()
                .map(EntrepreneurshipEntity::getId)
                .collect(Collectors.toList());
    }

    @Transactional
    public AccountEntity getAccountById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));
    }

    @Transactional
    public List<AccountEntity> getAllAccounts() {
        List<AccountEntity> accounts = accountRepository.findAll();
        if (accounts.isEmpty()) {
            throw new AccountNotFoundException("No accounts found");
        }
        return accounts;
    }

    @Transactional
    public AccountEntity createAccount(AccountEntity accountEntity) {

        // Codificar la contraseña antes de guardar
        if (accountEntity.getPassword() != null && !accountEntity.getPassword().isEmpty()) {
            accountEntity.setPassword(passwordEncoder.encode(accountEntity.getPassword()));
        } else {
            // Si no hay password, asegúrate de que providerId sea utilizado
            if (accountEntity.getProviderId() == null || accountEntity.getProviderId().isEmpty()) {
                // Si no hay password ni providerId, podrías lanzar un error
                throw new IllegalArgumentException("El password o providerId debe estar presente.");
            }
            // Si no hay password, mantenlo nulo (lo manejamos de forma segura)
            accountEntity.setPassword(null);
        }


        // Si la cuenta tiene dirección, guardarla también
        if (accountEntity.getAddress() != null) {
            AddressEntity address = accountEntity.getAddress();
            address = addressRepository.save(address);
            accountEntity.setAddress(address);
        }

        // Verificar si el correo electrónico ya está registrado
        if (accountRepository.existsByEmail(accountEntity.getEmail())) {
            throw new DuplicateFieldException("El correo electrónico ya está registrado.");
        }

        // Verificar si el nombre de usuario ya está en uso
        if (accountRepository.existsByUsername(accountEntity.getUsername())) {
            throw new DuplicateFieldException("El nombre de usuario ya está en uso.");
        }

        // Guardar la cuenta en la base de datos
        return accountRepository.save(accountEntity);
    }


    @Transactional
    public AccountEntity updateAccount(AccountEntity accountEntity) {
        if (accountEntity.getAddress() != null) {
            AddressEntity addressEntity = accountEntity.getAddress();
            addressRepository.save(addressEntity);
        }
        return accountRepository.save(accountEntity);
    }

    @Transactional
    public AddressEntity updateAddress(AddressEntity addressEntity) {
        return addressRepository.save(addressEntity);
    }

    @Transactional
    public AccountEntity updateAccountPartial(Long accountId, AccountRequest accountRequest) {
        AccountEntity accountEntity = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found with id: " + accountId));

        // Actualizar los campos que no son nulos
        if (accountRequest.getUsername() != null) {
            accountEntity.setUsername(accountRequest.getUsername());
        }
        if (accountRequest.getEmail() != null) {
            accountEntity.setEmail(accountRequest.getEmail());
        }
        if (accountRequest.getName() != null) {
            accountEntity.setName(accountRequest.getName());
        }
        if (accountRequest.getSurname() != null) {
            accountEntity.setSurname(accountRequest.getSurname());
        }
        if (accountRequest.getDateOfBirth() != null) {
            accountEntity.setDateOfBirth(LocalDate.parse(accountRequest.getDateOfBirth()));
        }
        if (accountRequest.getYearsOfExperience() != null) {
            accountEntity.setYearsOfExperience(accountRequest.getYearsOfExperience());
        }
        if (accountRequest.getIndustry() != null) {
            accountEntity.setIndustry(accountRequest.getIndustry());
        }
        if (accountRequest.getWallet() != null) {
            accountEntity.setWallet(accountRequest.getWallet());
        }
        if(accountRequest.getProviderId() != null){
            accountEntity.setProviderId(accountRequest.getProviderId());
        }

        return accountRepository.save(accountEntity);
    }

    @Transactional
    public AddressEntity updateAddressPartial(Long accountId, AddressRequest addressRequest) {
        AccountEntity accountEntity = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found with id: " + accountId));

        AddressEntity addressEntity = addressRepository.findByAccount(accountEntity)
                .orElseThrow(() -> new EntityNotFoundException("Address not found for account with id: " + accountId));

        // Actualizar los campos que no son nulos en Address
        if (addressRequest.getStreet() != null) {
            addressEntity.setStreet(addressRequest.getStreet());
        }
        if (addressRequest.getNumber() != 0) {
            addressEntity.setNumber(addressRequest.getNumber());
        }
        if (addressRequest.getLocality() != null) {
            addressEntity.setLocality(addressRequest.getLocality());
        }
        if (addressRequest.getProvince() != null) {
            addressEntity.setProvince(addressRequest.getProvince());
        }
        if (addressRequest.getType() != null) {
            addressEntity.setType(addressRequest.getType());
        }

        return addressRepository.save(addressEntity);
    }

    @Transactional
    public void deactivateAccountAndAddress(Long accountId) {
        AccountEntity accountEntity = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found with id: " + accountId));

        accountEntity.deactivate();

        if (accountEntity.getAddress() != null) {
            AddressEntity addressEntity = accountEntity.getAddress();
            addressEntity.deactivate();
            addressRepository.save(addressEntity);
        }

        accountRepository.save(accountEntity);
    }

    @Transactional()
    public AccountDataRequest getAccountData(String username) {
        AccountEntity accountEntity = accountRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Account not found with username: " + username));
        AccountMapper accountMapper = new AccountMapper();
        return accountMapper.toRequest(accountEntity);
    }

    @Transactional
    public Long getAccountIdByEmail(String email) {
        AccountEntity accountEntity = accountRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Account not found with email: " + email));
        return accountEntity.getId();  // Retornamos el ID de la cuenta
    }
    @Transactional
    public String getUsernameByEmail(String email) {
        // Busca la cuenta por el email
        AccountEntity accountEntity = accountRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Account not found with email: " + email));

        // Retorna el username de la cuenta encontrada
        return accountEntity.getUsername();
    }

    @Transactional
    public String getProviderIdByEmail(String email) {
        return accountRepository.findProviderIdByEmail(email);

    }
    @Transactional
    public String getProviderIdByUsername(String username) {
        return accountRepository.findProviderIdByUsername(username);
    }


    public boolean existsByUsername(String username) {
        return accountRepository.findByUsername(username).isPresent();
    }

    public boolean existsByEmail(String email) {
        return accountRepository.findByEmail(email).isPresent();
    }
}
