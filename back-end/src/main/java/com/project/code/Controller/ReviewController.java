package com.project.code.Controller;

import java.util.*;

import org.springframework.web.bind.annotation.*;

import com.project.code.Model.Customer;
import com.project.code.Model.Review;
import com.project.code.Repo.CustomerRepository;
import com.project.code.Repo.ReviewRepository;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewRepository reviewRepository;
    private final CustomerRepository customerRepository;

    public ReviewController(ReviewRepository reviewRepository,
                            CustomerRepository customerRepository) {
        this.reviewRepository = reviewRepository;
        this.customerRepository = customerRepository;
    }

    @GetMapping("/{storeId}/{productId}")
    public Map<String, Object> getReviews(@PathVariable long storeId,
                                          @PathVariable long productId) {

        List<Review> reviews =
                reviewRepository.findByStoreIdAndProductId(storeId, productId);

        List<Map<String, Object>> responseList = new ArrayList<>();

        for (Review review : reviews) {
            Map<String, Object> reviewMap = new HashMap<>();

            reviewMap.put("review", review.getComment());
            reviewMap.put("rating", review.getRating());

            Customer customer =
                    customerRepository.findById(review.getCustomerId())
                                      .orElse(null);

            reviewMap.put(
                    "customerName",
                    customer != null ? customer.getName() : "Unknown"
            );

            responseList.add(reviewMap);
        }

        return Map.of("reviews", responseList);
    }

    @GetMapping
    public Map<String, Object> getAllReviews() {
        return Map.of("reviews", reviewRepository.findAll());
    }
}