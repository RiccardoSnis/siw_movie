package it.uniroma3.siw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.MovieRepository;
import it.uniroma3.siw.repository.ReviewRepository;

@Service
public class ReviewService {
	
	@Autowired
	public ReviewRepository reviewRepository;
	
	@Autowired
	public MovieRepository movieRepository;
	
	@Transactional
	public Review saveReview(Review review) {
		review = this.reviewRepository.save(review);
		return review;
	}
	
	@Transactional
	public void deleteReview(Review review) {
		this.reviewRepository.delete(review);
	}
	
	@Transactional
	public void saveReviewToMovie(Long reviewId, Long movieId) {
		
		Movie movie = this.movieRepository.findById(movieId).get();
		Review review = this.reviewRepository.findById(reviewId).get();
		review.setMovie(movie);
		movie.addReview(review);
		this.movieRepository.save(movie);
		
	}
	
	@Transactional
	public void saveReviewToUser(Long reviewId, User user) {
		
		Review review = this.reviewRepository.findById(reviewId).get();
		review.setUser(user);
		user.addReview(review);
		this.reviewRepository.save(review);
		
	}
		
	public Review findReviewById(Long reviewId) {
		return this.reviewRepository.findById(reviewId).get();
	}
	
    public Iterable<Review> findAllReviews(){
    	return this.reviewRepository.findAll();
    }
    
	public Iterable<Review> existsReviewedMovieByUser(Long userId, Long movieId) {
		return this.reviewRepository.findUsersReviewPerMovie(userId, movieId);
	}
	
	public Iterable<Review> reviewByMovieIdToDelete(Long movieId) {
		return this.reviewRepository.selectReviewByMovieIdToDelete(movieId);
	}


}
