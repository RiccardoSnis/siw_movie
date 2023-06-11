package it.uniroma3.siw.controller;

import java.io.IOException;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.service.ArtistService;
import it.uniroma3.siw.uploadUtil.FileUploadUtil;

@Controller
public class ArtistController {
	
	@Autowired 
	private ArtistService artistService;

	@GetMapping(value="/admin/formNewArtist")
	public String formNewArtist(Model model) {
		model.addAttribute("artist", new Artist());
		return "admin/formNewArtist.html";
	}
	
	@GetMapping(value="/admin/indexArtist")
	public String indexArtist() {
		return "admin/indexArtist.html";
	}
	
	@PostMapping("/admin/artist")
	public String newArtist(@ModelAttribute("artist") Artist artist, Model model, @RequestParam("image") MultipartFile multipartFile) throws IOException {
		if (!artistService.existsByNameAndSurname(artist.getName(), artist.getSurname())) {
	        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
	        artist.setPhotos(fileName);
	        this.artistService.saveArtist(artist); 
	        String uploadDir = "artist-photos/" + artist.getId();
	        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
			model.addAttribute("artist", artist);
			return "artist.html";
		} else {
			model.addAttribute("messaggioErrore", "Questo artista esiste già");
			return "admin/formNewArtist.html"; 
		}
	}

	@GetMapping("/artist/{id}")
	public String getArtist(@PathVariable("id") Long id, Model model) {
		model.addAttribute("artist", this.artistService.findArtistById(id));
		return "artist.html";
	}

	@GetMapping("/artist")
	public String getArtists(Model model) {
		model.addAttribute("artists", this.artistService.findAllArtist());
		return "artists.html";
	}
	
	@GetMapping(value="/admin/updateArtist/{artistId}")
	public String updateArtist(@PathVariable ("artistId") Long artistId,Model model) {
		model.addAttribute("artist", this.artistService.findArtistById(artistId));
		return "admin/updateArtist.html";
	}
	
	@GetMapping(value="/admin/manageArtists")
	public String manageArtists(Model model) {
		model.addAttribute("artists", this.artistService.findAllArtist());
		return "admin/manageArtists";
	}

	@GetMapping(value="/admin/formUpdateArtist/{artistId}")
	public String manageArtists(@PathVariable ("artistId") Long artistId,Model model) {
		model.addAttribute("artist", this.artistService.findArtistById(artistId));
		return "admin/formUpdateArtist.html";
	}
	
	@PostMapping(value="/admin/confirmUpdateArtist/{artistId}")
	public String confirmUpdateArtist(@PathVariable ("artistId") Long artistId, Model model, @RequestParam("image") MultipartFile multipartFile) throws IOException {
		String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
		Artist artist = this.artistService.findArtistById(artistId);
	    artist.setPhotos(fileName); 
	    String uploadDir = "artist-photos/" + artist.getId();
	    FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
	    this.artistService.saveArtist(artist);
	    model.addAttribute("artist", artist);
		return "artist.html";
	}
	
	@GetMapping(value="/admin/formUpdateArtistName/{artistId}")
	public String formUpdateArtistName(@PathVariable ("artistId") Long artistId, Model model) {
		model.addAttribute("artist",this.artistService.findArtistById(artistId));
		return "admin/formUpdateArtistName.html";
	}
	
	@PostMapping(value="/admin/updateName/{artistId}")
	public String updateArtistName(@PathVariable ("artistId") Long artistId, Model model, @RequestParam("name") String name) {
		Artist artist = this.artistService.findArtistById(artistId);
		artist.setName(name);
		this.artistService.saveArtist(artist);
		model.addAttribute("artist",artist);
		return "admin/formUpdateArtist.html";
		
	}
	
	@GetMapping(value="/admin/formUpdateArtistSurname/{artistId}")
	public String formUpdateArtistSurname(@PathVariable ("artistId") Long artistId, Model model) {
		model.addAttribute("artist",this.artistService.findArtistById(artistId));
		return "admin/formUpdateArtistSurname.html";
	}
	
	@PostMapping(value="/admin/updateSurname/{artistId}")
	public String updateArtistSurname(@PathVariable ("artistId") Long artistId, Model model, @RequestParam("surname") String surname) {
		Artist artist = this.artistService.findArtistById(artistId);
		artist.setSurname(surname);
		this.artistService.saveArtist(artist);
		model.addAttribute("artist",artist);
		return "admin/formUpdateArtist.html";
		
	}
	
	@GetMapping(value="/admin/formUpdateArtistDateOfBirth/{artistId}")
	public String formUpdateArtistDateOfBirth(@PathVariable ("artistId") Long artistId, Model model) {
		model.addAttribute("artist",this.artistService.findArtistById(artistId));
		return "admin/formUpdateArtistDateOfBirth.html";
	}
	
	@PostMapping(value="/admin/updateDateOfBirth/{artistId}")
	public String updateArtistDateOfBirth(@PathVariable ("artistId") Long artistId, Model model, @RequestParam("dateOfBirth")
	@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateOfBirth) {
		Artist artist = this.artistService.findArtistById(artistId);
		artist.setDateOfBirth(dateOfBirth);
		this.artistService.saveArtist(artist);
		model.addAttribute("artist",artist);
		return "admin/formUpdateArtist.html";
	}
	
	@GetMapping(value="/admin/formUpdateArtistDateOfDeath/{artistId}")
	public String formUpdateArtistDateOfDeath(@PathVariable ("artistId") Long artistId, Model model) {
		model.addAttribute("artist",this.artistService.findArtistById(artistId));
		return "admin/formUpdateArtistDateOfDeath.html";
	}
	
	@PostMapping(value="/admin/updateDateOfDeath/{artistId}")
	public String updateArtistDateOfDeath(@PathVariable ("artistId") Long artistId, Model model, @RequestParam("dateOfDeath")
	@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateOfDeath) {
		Artist artist = this.artistService.findArtistById(artistId);
		artist.setDateOfDeath(dateOfDeath);
		this.artistService.saveArtist(artist);
		model.addAttribute("artist",artist);
		return "admin/formUpdateArtist.html";
	}
	
	@GetMapping(value="/admin/formUpdateArtistImage/{artistId}")
	public String formUpdateArtistImage(@PathVariable ("artistId") Long artistId, Model model) {
		model.addAttribute("artist",this.artistService.findArtistById(artistId));
		return "admin/formUpdateArtistImage.html";
	}
	
	@PostMapping(value="/admin/updateImage/{artistId}")
	public String updateArtistDateImage(@PathVariable ("artistId") Long artistId, Model model, @RequestParam("image") MultipartFile multipartFile) throws IOException {
		String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
		Artist artist = this.artistService.findArtistById(artistId);
	    artist.setPhotos(fileName); 
	    String uploadDir = "artist-photos/" + artist.getId();
	    FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
	    this.artistService.saveArtist(artist);
	    model.addAttribute("artist", artist);
		return "admin/formUpdateArtist.html";
	}
	
	@GetMapping(value="/admin/deleteArtist/{artistId}")
	public String deleteArtist(@PathVariable ("artistId") Long artistId, Model model){
		Artist artist = this.artistService.findArtistById(artistId);
	    this.artistService.deleteArtist(artist);
	    model.addAttribute("artists", this.artistService.findAllArtist());
		return "/admin/manageArtists.html";
	}
}
 