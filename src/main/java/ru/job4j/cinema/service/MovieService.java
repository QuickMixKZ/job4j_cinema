package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.Movie;
import ru.job4j.cinema.repository.MovieRepository;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<Movie> findAll() {
        return movieRepository.findAll();
    }

    public Movie add(Movie movie) {
        Optional<Movie> movieDb =
                movieRepository.findByNameAndYear(
                        movie.getName(),
                        movie.getYear());
        if (movieDb.isPresent()) {
            throw new IllegalArgumentException(
                    String.format("Movie with name \"%s\" and premiere date - %d", movie.getName(), movie.getYear())
            );
        }
        return movieRepository.add(movie);
    }

    public Movie findById(int id) {
        Optional<Movie> result = movieRepository.findById(id);
        if (result.isEmpty()) {
            throw new IllegalArgumentException(String.format("Movie with ID:%d not found.", id));
        }
        return result.get();
    }

    public boolean delete(Movie movie) {
        Movie movieDb = findById(movie.getId());
        return movieRepository.delete(movie);
    }

    public boolean update(Movie movie) {
        Movie movieById = findById(movie.getId());
        Optional<Movie> movieByNameAndYear = movieRepository.findByNameAndYear(movie.getName(), movie.getYear());
        if (movieByNameAndYear.isPresent() && movieByNameAndYear.get().getId() != movie.getId()) {
            throw new IllegalArgumentException(
                    String.format(
                            "Unable to update movie. Movie with name \"%s\" and year - %d already exists.",
                            movie.getName(),
                            movie.getYear()));
        }
        return movieRepository.update(movie);
    }

    public byte[] getMoviePosterById(int id) {
        return movieRepository.getMoviePosterById(id);
    }

    public void deleteAll() {
        movieRepository.deleteAll();
    }
}
