package it.uniroma3.siw.controller;


import java.io.IOException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.controller.validator.MovieValidator;
import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.service.ArtistService;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.MovieService;
import it.uniroma3.siw.service.ReviewService;
import it.uniroma3.siw.uploadUtil.FileUploadUtil;

@Controller
public class MovieController {
	@Autowired 
	private MovieService movieService;
	
	@Autowired 
	private ReviewService reviewService;
	
	@Autowired 
	private ArtistService artistService;
	
	@Autowired
	private CredentialsService credentialsService;

	@Autowired 
	private MovieValidator movieValidator;
	
	
	@GetMapping(value="/admin/formNewMovie")
	public String formNewMovie(Model model) {
		model.addAttribute("movie", new Movie());
		return "admin/formNewMovie.html";
	}

	@GetMapping(value="/admin/formUpdateMovie/{id}")
	public String formUpdateMovie(@PathVariable("id") Long id, Model model) {
		model.addAttribute("movie", this.movieService.findMovieById(id));
		return "admin/formUpdateMovie.html";
	}

	@GetMapping(value="/admin/indexMovie")
	public String indexMovie() {
		return "admin/indexMovie.html";
	}
	
	@GetMapping(value="/admin/manageMovies")
	public String manageMovies(Model model) {
		model.addAttribute("movies", this.movieService.findAllMovies());
		return "admin/manageMovies.html";
	}
	
	@GetMapping(value="/admin/setDirectorToMovie/{directorId}/{movieId}")
	public String setDirectorToMovie(@PathVariable("directorId") Long directorId, @PathVariable("movieId") Long movieId, Model model) {
		
		Movie movie = this.movieService.saveDirectorToMovie(movieId, directorId);
		model.addAttribute("movie", movie);
		return "admin/formUpdateMovie.html";
	}
	
	
	@GetMapping(value="/admin/addDirector/{id}")
	public String addDirector(@PathVariable("id") Long id, Model model) {
		model.addAttribute("artists", artistService.findAllArtist());
		model.addAttribute("movie", movieService.findMovieById(id));
		return "admin/directorsToAdd.html";
	}

	@PostMapping("/admin/movie")
	public String newMovie(@Valid @ModelAttribute("movie") Movie movie, BindingResult bindingResult, Model model, @RequestParam("image") MultipartFile multipartFile) throws IOException  {
		
		this.movieValidator.validate(movie, bindingResult);
		if (!bindingResult.hasErrors()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
	        movie.addPhotos(fileName);
			Movie saved = this.movieService.saveMovie(movie);
			String uploadDir = "movie-photos/" + saved.getId();
	        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
			model.addAttribute("movie", movie);
			return "movie.html";
		} else {
			return "admin/formNewMovie.html"; 
		}
	}

	@GetMapping("/movie/{id}")
	public String getMovie(@PathVariable("id") Long id, Model model) {
		model.addAttribute("movie", this.movieService.findMovieById(id));
		return "movie.html";
	}

	@GetMapping("/movie")
	public String getMovies(Model model) {
		model.addAttribute("movies", this.movieService.findAllMovies());
		return "movies.html";
	}
	
	@GetMapping("/registeredUser/moviesAndReview")
	public String getMoviesAndReview(Model model) {
		model.addAttribute("movies", this.movieService.findAllMovies());
		return "registeredUser/moviesAndReview.html";
	}
	
	
	@GetMapping("/admin/formSearchMovies")
	public String formSearchMovies() {
		return "/admin/formSearchMovies.html";
	}

	@PostMapping("/searchMovies")
	public String searchMovies(Model model, @RequestParam int year) {
		model.addAttribute("movies", this.movieService.findByYear(year));
		return "/admin/foundMovies.html";
	}
	
	@GetMapping("/admin/updateActors/{id}")
	public String updateActors(@PathVariable("id") Long id, Model model) {

		List<Artist> actorsToAdd = this.movieService.findActorsNotInMovie(id);
		model.addAttribute("actorsToAdd", actorsToAdd);
		model.addAttribute("movie", this.movieService.findMovieById(id));

		return "admin/actorsToAdd.html";
	}
	
	@PostMapping("/admin/addImage/{id}")
	public String addImage(@PathVariable("id") Long id, Model model, @RequestParam("image") MultipartFile multipartFile ) throws IOException {
		
		Movie movie = this.movieService.findMovieById(id);
		//model.addAttribute("movie", this.movieService.findMovieById(id));
		String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        movie.addPhotos(fileName);
		Movie saved = this.movieService.saveMovie(movie);
		String uploadDir = "movie-photos/" + saved.getId();
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		model.addAttribute("movie", movie);

		return "admin/formUpdateMovie.html";
	}


	@GetMapping(value="/admin/addActorToMovie/{actorId}/{movieId}")
	public String addActorToMovie(@PathVariable("actorId") Long actorId, @PathVariable("movieId") Long movieId, Model model) {
		
		Movie movie = this.movieService.saveActorToMovie(movieId,actorId);
		List<Artist> actorsToAdd = this.movieService.findActorsNotInMovie(movieId);
		
		model.addAttribute("movie", movie);
		model.addAttribute("actorsToAdd", actorsToAdd);

		return "admin/actorsToAdd.html";
	}
	
	
	@GetMapping(value="/admin/removeActorFromMovie/{actorId}/{movieId}")
	public String removeActorFromMovie(@PathVariable("actorId") Long actorId, @PathVariable("movieId") Long movieId, Model model) {
	
		Movie movie = movieService.deleteActorFromMovie(movieId, actorId);
		List<Artist> actorsToAdd = this.movieService.findActorsNotInMovie(movieId);
		
		model.addAttribute("movie", movie);
		model.addAttribute("actorsToAdd", actorsToAdd);

		return "admin/actorsToAdd.html";
	}
	
	@GetMapping("/admin/deleteMovies")
	public String deleteMovies(Model model) {
		model.addAttribute("movies", this.movieService.findAllMovies());
		return "/admin/deleteMovies.html";
	}
	
	@GetMapping("/admin/deleteMovie/{movieId}")
	public String deleteMovies(@PathVariable("movieId") Long movieId, Model model) {
		List <Review> reviews = (List<Review>) this.reviewService.reviewByMovieIdToDelete(movieId);
		for(Review review : reviews) {
			this.reviewService.deleteReview(review);
		}
		this.movieService.deleteMovie(this.movieService.findMovieById(movieId));
		model.addAttribute("movies", this.movieService.findAllMovies());
		return "/admin/deleteMovies.html";
	}
	
	@GetMapping("/admin/formUpdateMovieImage/{movieId}")
	public String updateMovieImage(@PathVariable("movieId") Long movieId, Model model) {
		model.addAttribute("movie", this.movieService.findMovieById(movieId));
		return "/admin/formUpdateMovieImage.html";
	}
}
