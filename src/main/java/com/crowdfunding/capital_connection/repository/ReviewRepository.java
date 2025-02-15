package com.crowdfunding.capital_connection.repository;

import com.crowdfunding.capital_connection.repository.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
    List<ReviewEntity> findByEntrepreneurshipId(Long entrepreneurshipId);


    @Query("SELECT r FROM ReviewEntity r WHERE r.stars = :stars")
    List<ReviewEntity> findReviewsByStars(@Param("stars") float stars);



    @Query("SELECT r FROM ReviewEntity r WHERE r.account.id = :idUser")
    List<ReviewEntity> findByUserId(@Param("idUser") String idUser);

    boolean findByEntrepreneurshipIdAndAccountId(Long entrepreneurshipId, Long accountId);
}
