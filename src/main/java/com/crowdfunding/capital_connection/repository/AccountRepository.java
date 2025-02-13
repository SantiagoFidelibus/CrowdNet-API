package com.crowdfunding.capital_connection.repository;

import com.crowdfunding.capital_connection.repository.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {

    Optional<AccountEntity> findByUsername(String username);
    Optional<AccountEntity> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    @Query("SELECT a.providerId FROM AccountEntity a WHERE a.email = :email")
    String findProviderIdByEmail(@Param("email") String email);


    @Query("SELECT a.providerId FROM AccountEntity a WHERE a.username = :username")
    String findProviderIdByUsername(@Param("username") String username);
}