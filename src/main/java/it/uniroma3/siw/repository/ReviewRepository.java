package it.uniroma3.siw.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.model.Review;

public interface ReviewRepository extends CrudRepository<Review, Long>  {
	
	@Query(value="select * "
			+ "from review r "
			+ "where user_id = :userId "
			+ "and movie_id = :movieId ", nativeQuery=true)
	public Iterable<Review> findUsersReviewPerMovie(@Param("userId") Long userId, @Param("movieId") Long movieId);
	
	@Query(value="select * "
			+ "from review r "
			+ "where movie_id = :movieId ", nativeQuery=true)
	public Iterable<Review> selectReviewByMovieIdToDelete(@Param("movieId") Long movieId);
}
