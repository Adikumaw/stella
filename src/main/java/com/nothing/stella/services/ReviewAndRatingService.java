package com.nothing.stella.services;

import java.util.List;

import com.nothing.stella.model.ReviewAndRatingViewModel;

public interface ReviewAndRatingService {
    void create(String reference, int productId, int rating, String comment);

    List<ReviewAndRatingViewModel> fetchProductReviews(int productId);

    List<ReviewAndRatingViewModel> fetchUserReviews(String reference);

    Double getRating(int productId);

    void update(String reference, int reviewAndRatingId, int rating, String comment);

}
