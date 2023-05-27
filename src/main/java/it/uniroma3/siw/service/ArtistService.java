package it.uniroma3.siw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.repository.ArtistRepository;

@Service
public class ArtistService {
	
	@Autowired
	ArtistRepository artistRepository;
	
	@Transactional
	public Iterable<Artist> findAllArtist() {
		return this.artistRepository.findAll();
	}
	
	public Artist findArtistById(Long id) {
		return this.artistRepository.findById(id).get();
	}
	
	public boolean existsByNameAndSurname(String name, String surname) {
		return this.artistRepository.existsByNameAndSurname(name, surname);
	}
	
	@Transactional
	public void saveArtist(Artist artist) {
		this.artistRepository.save(artist);
	}
	

}
