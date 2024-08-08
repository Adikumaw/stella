package com.nothing.stella.model;

import java.util.Date;

import com.nothing.stella.entity.ReviewAndRating;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewAndRatingViewModel {
    private int id;
    private String name;
    private int rating;
    private String comment;
    private Date date;

    public ReviewAndRatingViewModel(String name, ReviewAndRating review) {
        this.id = review.getId();
        this.name = name;
        this.rating = review.getRating();
        this.comment = review.getComment();
        this.date = review.getDate();
    }
}
