package com.jobapp.reviewms.review;

import java.util.List;

public interface ReviewService {

    public List<Review> getAllReviews(Long reviewId);
    public boolean addReviews(Long companyId, Review review);
    public Review getReviewById(Long reviewId);
    public boolean updateReviewById(Long reviewId, Review review);
    public boolean deleteReview(Long reviewId);
}
