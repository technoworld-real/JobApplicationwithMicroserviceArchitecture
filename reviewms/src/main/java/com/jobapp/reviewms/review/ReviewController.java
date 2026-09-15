package com.jobapp.reviewms.review;

import com.jobapp.reviewms.review.messaging.ReviewMessageProducer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private ReviewService reviewService;
    private ReviewMessageProducer reviewMessageProducer;

    public ReviewController(ReviewService reviewService, ReviewMessageProducer reviewMessageProducer) {
        this.reviewService = reviewService;
        this.reviewMessageProducer=reviewMessageProducer;
    }

    @GetMapping
    public ResponseEntity<List<Review>> getAllReviews(@RequestParam Long companyId){
       List<Review> rev=reviewService.getAllReviews(companyId);

       return new ResponseEntity<>(rev, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> addReviews(@RequestParam Long companyId,
                                             @RequestBody Review review){

      boolean isadded=  reviewService.addReviews(companyId, review);

      if(isadded){
          reviewMessageProducer.sendMessage(review);
        return new ResponseEntity<>("Review added successfully", HttpStatus.OK);
      }else{
          return new ResponseEntity<>("something went wrong",HttpStatus.NOT_FOUND);
      }
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<Review> getReviewById(@PathVariable Long reviewId){
        return new ResponseEntity<>(reviewService.getReviewById(reviewId),HttpStatus.OK);

    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<String> updateReviewById(@PathVariable Long reviewId,
                                                   @RequestBody Review review){

        boolean ifupdated= reviewService.updateReviewById(reviewId, review);
        if(ifupdated){
            return new ResponseEntity<>("review against particular company is updated successfully",
                    HttpStatus.OK);
        }else {
            return new ResponseEntity<>("review is not updated successfully",
                    HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable Long reviewId){

        boolean isDeleted=reviewService.deleteReview(reviewId);

                if(isDeleted){
                    return new ResponseEntity<>("Row deleted successfully",HttpStatus.OK);
                }else{
                    return new ResponseEntity<>("Something went wrong, deletion not done successfully",HttpStatus.NOT_FOUND);
                }
    }

    @GetMapping("/averageRating")
    public Double getAverageReview(@RequestParam Long companyId){
       List<Review>reviewList= reviewService.getAllReviews(companyId);
       return reviewList.stream().mapToDouble(Review::getRating).average()
               .orElse(0.0);
    }
}
