package com.crowdfunding.capital_connection.repository;

import com.crowdfunding.capital_connection.repository.entity.DonationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DonationRepository extends JpaRepository<DonationEntity, Long> {
    List<DonationEntity> findByAccountId(Long accountId);

    @Query(value = "SELECT * FROM donations d WHERE d.entrepreneurship_id IN (SELECT e.id FROM entrepreneurship e WHERE e.account_id = :ownerId)", nativeQuery = true)
    List<DonationEntity> findReceivedDonationsByOwner(@Param("ownerId") Long ownerId);
}