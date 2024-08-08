package com.nothing.stella.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nothing.stella.entity.ReviewAndRating;
import com.nothing.stella.exception.CommentExceedsLimitException;
import com.nothing.stella.exception.InvalidRatingException;
import com.nothing.stella.exception.ReviewNotAllowedException;
import com.nothing.stella.model.ReviewAndRatingViewModel;
import com.nothing.stella.repository.ReviewAndRatingRepository;

@Service
public class ReviewAndRatingServiceImpl implements ReviewAndRatingService {

    @Autowired
    private ReviewAndRatingRepository reviewAndRatingRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;

    @Override
    public void create(String reference, int productId, int rating, String comment) {
        // Verify rating and comment
        if (rating < 0 || rating > 5) {
            throw new InvalidRatingException("Error: Invalid rating value");
        } else if (comment.length() > 1000) {
            throw new CommentExceedsLimitException("Error: Comment must be between 0 and 1000");
        }

        // fetch UserId
        int userId = userService.findUserIdByReference(reference);

        if (!reviewAndRatingRepository.existsByUserIdAndProductId(userId, productId)) {
            // Verify that user is allowed to do Rating..
            Boolean isApplicable = orderService.isApplicableForReview(userId, productId);

            if (isApplicable) {
                // save the review
                reviewAndRatingRepository.save(new ReviewAndRating(userId, productId, rating, comment));
            } else {
                throw new ReviewNotAllowedException("Warning: you cannot review this product");
            }
        } else {
            throw new ReviewNotAllowedException("Warning: you cannot review a product twice");
        }
    }

    @Override
    public List<ReviewAndRatingViewModel> fetchProductReviews(int productId) {
        List<ReviewAndRating> reviews = reviewAndRatingRepository.findByProductId(productId);
        List<ReviewAndRatingViewModel> viewModels = new ArrayList<>();

        for (ReviewAndRating review : reviews) {
            viewModels.add(new ReviewAndRatingViewModel(userService.getUserName(review.getUserId()), review));
        }

        return viewModels;
    }

    @Override
    public List<ReviewAndRatingViewModel> fetchUserReviews(String reference) {
        int userId = userService.findUserIdByReference(reference);

        List<ReviewAndRating> reviews = reviewAndRatingRepository.findByUserId(userId);
        List<ReviewAndRatingViewModel> viewModels = new ArrayList<>();
        String name = userService.getUserName(userId);

        for (ReviewAndRating review : reviews) {
            viewModels.add(new ReviewAndRatingViewModel(name, review));
        }

        return viewModels;
    }

    @Override
    public Double getRating(int productId) {
        List<Integer> ratings = reviewAndRatingRepository.findRatingByProductId(productId);

        int sum = 0;
        for (int rating : ratings) {
            sum += rating;
        }

        Double averageRating = Double.valueOf(sum) / Double.valueOf(ratings.size());
        return averageRating;
    }

    @Override
    public void update(String reference, int reviewAndRatingId, int rating, String comment) {
        // Verify rating and comment
        if (rating < 0 || rating > 5) {
            throw new InvalidRatingException("Error: Invalid rating value");
        } else if (comment.length() > 1000) {
            throw new CommentExceedsLimitException("Error: Comment must be between 0 and 1000");
        }

        // fetch UserId
        int userId = userService.findUserIdByReference(reference);
        // fetch User review
        Optional<ReviewAndRating> optionalRR = reviewAndRatingRepository.findById(reviewAndRatingId);

        if (optionalRR.isPresent()) {
            // Verify that user is allowed to do Rating..
            ReviewAndRating reviewAndRating = optionalRR.get();

            if (reviewAndRating.getUserId() == userId) {
                // save the review
                reviewAndRating.setRating(rating);
                reviewAndRating.setComment(comment);

                reviewAndRatingRepository.save(reviewAndRating);
            } else {
                throw new ReviewNotAllowedException("Warning: you cannot update review for this product");
            }
        } else {
            throw new ReviewNotAllowedException("Error: Invalid review Id");
        }
    }

}
