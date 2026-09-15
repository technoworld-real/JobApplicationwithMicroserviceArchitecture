package com.jobapp.reviewms.review.Impl;


import com.jobapp.reviewms.review.Review;
import com.jobapp.reviewms.review.ReviewRepository;
import com.jobapp.reviewms.review.ReviewService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
  //  private final CompanyService companyService;

    public ReviewServiceImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;

    }



    @Override
    public List<Review> getAllReviews(Long companyId) {
        List<Review> reviews=reviewRepository.findByCompanyId(companyId);
        return reviews;
    }

    @Override
    public boolean addReviews(Long companyId, Review review) {


        if (companyId != null && review!=null){
            review.setCompanyId(companyId);
        reviewRepository.save(review);
        return true;
    }
      return false;

    }

    @Override
    public Review getReviewById(Long reviewId) {
       return reviewRepository.findById(reviewId).orElse(null);
    }

    @Override
    public boolean updateReviewById(Long reviewId, Review updatedreview) {
        Review review=reviewRepository.findById(reviewId).orElse(null);
        if(review!=null){
            review.setTitle(updatedreview.getTitle());
            review.setDescription(updatedreview.getDescription());
            review.setRating(updatedreview.getRating());
            review.setCompanyId(updatedreview.getCompanyId());
            reviewRepository.save(review);
            return true;
        }

        return false;
    }

    @Override
    public boolean deleteReview( Long reviewId) {
        Review review=reviewRepository.findById(reviewId).orElse(null);
        if(review!=null) {
            reviewRepository.delete(review);

            return true;
        }

        return false;
    }
}
