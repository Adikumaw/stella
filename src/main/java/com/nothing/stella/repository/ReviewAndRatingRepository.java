package com.nothing.stella.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nothing.stella.entity.ReviewAndRating;

@Repository
public interface ReviewAndRatingRepository extends JpaRepository<ReviewAndRating, Integer> {

    boolean existsByUserIdAndProductId(int userId, int productId);

    List<ReviewAndRating> findByProductId(int productId);

    @Query("SELECT rr.rating FROM ReviewAndRating rr WHERE rr.productId = ?1")
    List<Integer> findRatingByProductId(int productId);

    List<ReviewAndRating> findByUserId(int userId);

}
