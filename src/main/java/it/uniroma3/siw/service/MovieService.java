package it.uniroma3.siw.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.repository.ArtistRepository;
import it.uniroma3.siw.repository.MovieRepository;


@Service
public class MovieService {
	
    @Autowired
    private MovieRepository movieRepository;
    
    @Autowired
    private ArtistRepository artistRepository;
    
    @Autowired
    private ArtistService artistService;
    
    @Transactional
    public void createNewMovie(Movie movie) {
    	this.movieRepository.save(movie);
    }
    
    @Transactional
    public Movie findMovieById(Long id) {
    	return this.movieRepository.findById(id).get();
    }
    
    @Transactional
    public void saveMovie(Movie movie) {
    	this.movieRepository.save(movie);
    }
    
    public Iterable<Movie> findAllMovies(){
    	return this.movieRepository.findAll();
    }
    
    @Transactional
    public Movie saveDirectorToMovie(Long movieId, Long artistId) {
		
    	Artist director = this.artistRepository.findById(artistId).get();
		Movie movie = this.findMovieById(movieId);
		movie.setDirector(director);
		this.saveMovie(movie);
		return movie;
    }
    
    public List<Artist> findActorsNotInMovie(Long movieId){
    	
    	List<Artist> actorsToAdd = new ArrayList<>();

		for (Artist a : artistRepository.findActorsNotInMovie(movieId)) {
			actorsToAdd.add(a);
		}
		return actorsToAdd;
    }
    
	public List<Movie> findByYear(int year) {
		return this.movieRepository.findByYear(year);
	}

	public boolean existsByTitleAndYear(String title, int year) {
		return this.movieRepository.existsByTitleAndYear(title, year);
	}
	
	
	@Transactional
	public Movie saveActorToMovie(Long movieId, Long actorId) {
		Movie movie = this.findMovieById(movieId);
		Artist actor = this.artistService.findArtistById(actorId);
		Set<Artist> actors = movie.getActors();
		actors.add(actor);
		return this.movieRepository.save(movie);
		
	}
	
	@Transactional
	public Movie deleteActorFromMovie(Long movieId, Long actorId) {
		Movie movie = this.findMovieById(movieId);
		Artist actor = this.artistService.findArtistById(actorId);
		Set<Artist> actors = movie.getActors();
		actors.remove(actor);
		return this.movieRepository.save(movie);
	}

}
