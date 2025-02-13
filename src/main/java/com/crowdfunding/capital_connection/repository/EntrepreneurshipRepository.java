package com.crowdfunding.capital_connection.repository;

import com.crowdfunding.capital_connection.repository.entity.EntrepreneurshipEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface EntrepreneurshipRepository extends JpaRepository<EntrepreneurshipEntity, Long> {
    @Query("SELECT e FROM EntrepreneurshipEntity e WHERE LOWER(e.name) LIKE LOWER(CONCAT('%', :name, '%')) AND e.goal = :goal")
    Page<EntrepreneurshipEntity> findByNameAndGoal(@Param("name") String name, @Param("goal") BigDecimal goal, Pageable pageable);

    @Query("SELECT e FROM EntrepreneurshipEntity e WHERE LOWER(e.description) LIKE LOWER(CONCAT('%', :description, '%'))")
    Page<EntrepreneurshipEntity> findByDescriptionContainingIgnoreCase(@Param("description") String description, Pageable pageable);

    @Query("SELECT e FROM EntrepreneurshipEntity e WHERE e.category = :category")
    Page<EntrepreneurshipEntity> findByCategory(@Param("category") String category, Pageable pageable);

    @Query("SELECT e FROM EntrepreneurshipEntity e WHERE e.goal = :goal")
    Page<EntrepreneurshipEntity> findByGoal(@Param("goal") BigDecimal goal, Pageable pageable);

    @Query("SELECT e FROM EntrepreneurshipEntity e WHERE LOWER(e.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<EntrepreneurshipEntity> findByNameContainingIgnoreCase(@Param("name") String name, Pageable pageable);

    @Query("SELECT e FROM EntrepreneurshipEntity e WHERE e.account.id = :idUser")
    List<EntrepreneurshipEntity> findByUserId(@Param("idUser") Long idUser);


    List<EntrepreneurshipEntity> findByAccountId(Long accountId);

    Page<EntrepreneurshipEntity> findByIsActivatedTrue(Pageable pageable);


    @Query("SELECT e FROM EntrepreneurshipEntity e WHERE e.isActivated = true AND (:category IS NULL OR e.category = :category) AND e.goal >= :goal")
    Page<EntrepreneurshipEntity> findByCategoryAndGoalGreaterThanEqualAndActive(@Param("category") String category, @Param("goal") BigDecimal goal, Pageable pageable);

    // Filtra por categoría y metas menores o iguales a un valor
    @Query("SELECT e FROM EntrepreneurshipEntity e WHERE e.isActivated = true AND (:category IS NULL OR e.category = :category) AND e.goal <= :goal")
    Page<EntrepreneurshipEntity> findByCategoryAndGoalLessThanEqualAndActive(@Param("category") String category, @Param("goal") BigDecimal goal, Pageable pageable);

    // Filtra por categoría y metas exactas
    @Query("SELECT e FROM EntrepreneurshipEntity e WHERE e.isActivated = true AND (:category IS NULL OR e.category = :category) AND e.goal = :goal")
    Page<EntrepreneurshipEntity> findByCategoryAndGoalExactAndActive(@Param("category") String category, @Param("goal") BigDecimal goal, Pageable pageable);

    // Filtra por categoría sin considerar el goal
    @Query("SELECT e FROM EntrepreneurshipEntity e WHERE e.isActivated = true AND (:category IS NULL OR e.category = :category)")
    Page<EntrepreneurshipEntity> findByCategoryAndActive(@Param("category") String category, Pageable pageable);


    // Nuevo método: filtra emprendimientos activos opcionalmente por categoría y/o goal.
    @Query("SELECT e FROM EntrepreneurshipEntity e " +
            "WHERE e.isActivated = true " +
            "AND (:category IS NULL OR e.category = :category) " +
            "AND (:goal IS NULL OR e.goal = :goal)")
    Page<EntrepreneurshipEntity> findByCategoryAndGoalAndActive(
            @Param("category") String category,
            @Param("goal") BigDecimal goal,
            Pageable pageable);
}
