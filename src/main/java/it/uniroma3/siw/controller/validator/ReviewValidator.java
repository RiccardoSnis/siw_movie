package it.uniroma3.siw.controller.validator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.ReviewService;

@Component
public class ReviewValidator implements Validator {
		
		@Autowired
		private ReviewService reviewService;
		
		@Autowired
		private CredentialsService credentialsService;
	

		@Override
		public void validate(Object o, Errors errors) {
			
			Review review=(Review)o;
			UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
			Long userId = credentials.getUser().getId();
			
			List <Review> reviews = (List<Review>) this.reviewService.existsReviewedMovieByUser(userId, review.getMovie().getId());
			
			if(!reviews.isEmpty()) {
				errors.reject("usersreview.duplicate");
			}
			
		}
		@Override
		public boolean supports(Class<?> aClass) {
			return Review.class.equals(aClass);
		}

}
