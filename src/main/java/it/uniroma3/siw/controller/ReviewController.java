package it.uniroma3.siw.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.controller.validator.ReviewValidator;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.MovieService;
import it.uniroma3.siw.service.ReviewService;

@Controller
public class ReviewController {
	
	@Autowired
	public ReviewService reviewService;
	
	@Autowired
	public MovieService movieService;
	
	@Autowired 
	public ReviewValidator reviewValidator;
	
	@Autowired
	private CredentialsService credentialsService;
	
	@GetMapping("/review")
	public String getReviews(Model model) {
		model.addAttribute("movies", this.movieService.findAllMovies());
		model.addAttribute("reviews", this.reviewService.findAllReviews());
		return "reviews.html";
	}
	
	@GetMapping(value="/registeredUser/formNewReview/{id}")
	public String formNewReview(@PathVariable("id") Long id, Model model) {
		model.addAttribute("movie", this.movieService.findMovieById(id));
		model.addAttribute("review", new Review());
		return "registeredUser/formNewReview.html";
	}
	
	@PostMapping(value="/registeredUser/addReviewToMovie/{movieId}")
	public String addReviewToMovie( @Valid @ModelAttribute(value = "review") Review review,
			BindingResult bindingResult, Model model, @PathVariable("movieId") Long movieId) {
		
		review.setMovie(this.movieService.findMovieById(movieId));
		this.reviewValidator.validate(review, bindingResult);
		if(!bindingResult.hasErrors()) {
			
			UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
			this.reviewService.saveReview(review);
			this.reviewService.saveReviewToMovie(review.getId(), movieId);
			this.reviewService.saveReviewToUser(review.getId(), credentials.getUser());
			
			return "registeredUser/indexRegisteredUser.html";
		}
		model.addAttribute("movie", this.movieService.findMovieById(movieId));
		return "registeredUser/formNewReview.html";
	}
	
	@GetMapping(value = "/review/{movieId}")
	public String review (Model model, @PathVariable("movieId") Long movieId) {
		model.addAttribute("movie", this.movieService.findMovieById(movieId));	
		return "review.html";
		
	}
	
	@GetMapping("/admin/reviewsAdmin")
	public String reviewsAdmin (Model model) {
		model.addAttribute("movies", this.movieService.findAllMovies());
		model.addAttribute("reviews",this.reviewService.findAllReviews());
		return "/admin/reviewsAdmin.html";
		
	}
	
	@GetMapping(value = "/reviewsToDelete/{movieId}")
	public String reviewToDelete (Model model, @PathVariable("movieId") Long movieId) {
		model.addAttribute("movie", this.movieService.findMovieById(movieId));	
		return "/admin/reviewsToDelete.html";
	}
	
	@GetMapping(value = "/deleteReview/{reviewId}")
	public String deleteReview (Model model, @PathVariable("reviewId") Long reviewId) {
		Movie movie = this.reviewService.findReviewById(reviewId).getMovie();
		this.reviewService.deleteReview(this.reviewService.findReviewById(reviewId));
		model.addAttribute("movie",movie);
		return "/admin/reviewsToDelete.html";
	}
	

}
