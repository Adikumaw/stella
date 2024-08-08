package com.nothing.stella.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nothing.stella.exception.InvalidJWTHeaderException;
import com.nothing.stella.exception.UnknownErrorException;
import com.nothing.stella.exception.UserException;
import com.nothing.stella.model.CreateReviewAndRatingRequest;
import com.nothing.stella.model.ReviewAndRatingViewModel;
import com.nothing.stella.services.JWTService;
import com.nothing.stella.services.ReviewAndRatingService;

@RestController
@RequestMapping("/api")
public class ReviewAndRatingController {

    @Autowired
    private ReviewAndRatingService reviewAndRatingService;
    @Autowired
    private JWTService jwtService;

    private static final Logger logger = LoggerFactory.getLogger(ReviewAndRatingController.class);

    @PostMapping("/review-rating")
    public void createReview(@RequestBody CreateReviewAndRatingRequest request,
            @RequestHeader("Authorization") String jwtHeader) {
        if (jwtService.verifyJwtHeader(jwtHeader)) {

            String jwtToken = jwtHeader.substring(7);
            try {
                String reference = jwtService.fetchReference(jwtToken);

                reviewAndRatingService.create(reference, request.getProductId(), request.getRating(),
                        request.getComment());
            } catch (UserException e) {
                throw e;
            } catch (Exception e) {
                logger.error("Unknown error: " + e.getMessage(), e);
                throw new UnknownErrorException("Error: unknown error");
            }
        } else {
            throw new InvalidJWTHeaderException("Error: Invalid JWTHeader");
        }
    }

    @GetMapping("/products/review-rating")
    public List<ReviewAndRatingViewModel> fetchProductReviews(@RequestParam int productId) {
        try {

            return reviewAndRatingService.fetchProductReviews(productId);
        } catch (UserException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Unknown error: " + e.getMessage(), e);
            throw new UnknownErrorException("Error: unknown error");
        }
    }

    @GetMapping("/users/review-rating")
    public List<ReviewAndRatingViewModel> fetchUserReviews(@RequestHeader("Authorization") String jwtHeader) {
        if (jwtService.verifyJwtHeader(jwtHeader)) {

            String jwtToken = jwtHeader.substring(7);
            try {
                String reference = jwtService.fetchReference(jwtToken);

                return reviewAndRatingService.fetchUserReviews(reference);
            } catch (UserException e) {
                throw e;
            } catch (Exception e) {
                logger.error("Unknown error: " + e.getMessage(), e);
                throw new UnknownErrorException("Error: unknown error");
            }
        } else {
            throw new InvalidJWTHeaderException("Error: Invalid JWTHeader");
        }
    }

    @GetMapping("/products/average-rating")
    public Double getAverageRating(@RequestParam int productId) {
        try {

            return reviewAndRatingService.getRating(productId);
        } catch (UserException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Unknown error: " + e.getMessage(), e);
            throw new UnknownErrorException("Error: unknown error");
        }
    }

    @PostMapping("/review-rating/{reviewAndRatingId}")
    public void updateReview(@PathVariable int reviewAndRatingId, @RequestBody CreateReviewAndRatingRequest request,
            @RequestHeader("Authorization") String jwtHeader) {
        if (jwtService.verifyJwtHeader(jwtHeader)) {

            String jwtToken = jwtHeader.substring(7);
            try {
                String reference = jwtService.fetchReference(jwtToken);

                reviewAndRatingService.update(reference, reviewAndRatingId, request.getRating(),
                        request.getComment());
            } catch (UserException e) {
                throw e;
            } catch (Exception e) {
                logger.error("Unknown error: " + e.getMessage(), e);
                throw new UnknownErrorException("Error: unknown error");
            }
        } else {
            throw new InvalidJWTHeaderException("Error: Invalid JWTHeader");
        }
    }

}
