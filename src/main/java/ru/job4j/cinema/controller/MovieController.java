package ru.job4j.cinema.controller;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.cinema.model.Genre;
import ru.job4j.cinema.model.Movie;
import ru.job4j.cinema.service.GenreService;
import ru.job4j.cinema.service.MovieService;
import ru.job4j.cinema.service.SessionService;
import ru.job4j.cinema.util.ControllerUtil;

import java.io.IOException;
import java.util.*;

@Controller
public class MovieController {

    private final SessionService sessionService;
    private final MovieService movieService;
    private final GenreService genreService;
    private final ControllerUtil controllerUtil;

    public MovieController(SessionService sessionService, MovieService movieService, GenreService genreService, ControllerUtil controllerUtil) {
        this.sessionService = sessionService;
        this.movieService = movieService;
        this.genreService = genreService;
        this.controllerUtil = controllerUtil;
    }

    @GetMapping("/movies-today")
    public String getTodayMovies(Model model) {
        controllerUtil.addUserToModel(model);
        model.addAttribute("movies", sessionService.findTodayMovies());
        return "moviesToday";
    }

    @GetMapping("/movies")
    public String getAllMovies(Model model) {
        controllerUtil.addUserToModel(model);
        model.addAttribute("movies", movieService.findAll());
        return "movies";
    }

    @GetMapping("/movies/add")
    public String addMovie(Model model) {
        controllerUtil.addUserToModel(model);
        model.addAttribute("movie", new Movie());
        model.addAttribute("genres", genreService.findAll());
        return "addMovie";
    }

    @PostMapping("/addMovie")
    public String addMovie(Model model,
                           @ModelAttribute(name = "movie") Movie movie,
                           @RequestParam String[] genreIds,
                           @RequestParam("file") MultipartFile poster) throws IOException {
        movie.setPoster(poster.getBytes());
        Set<Genre> genres = new HashSet<>();
        for (String stringId : genreIds) {
            genres.add(genreService.findById(Integer.parseInt(stringId)));
        }
        movie.setGenres(genres);
        movieService.add(movie);
        return "redirect:/movies";
    }

    @PostMapping("/editMovie")
    public String editMovie(Model model,
                           @ModelAttribute(name = "movie") Movie movie,
                           @RequestParam String[] genreIds,
                           @RequestParam("file") MultipartFile poster) throws IOException {
        if (movie.getId() != 0 && poster.isEmpty()) {
            movie.setPoster(movieService.getMoviePosterById(movie.getId()));
        } else {
            movie.setPoster(poster.getBytes());
        }
        Set<Genre> genres = new HashSet<>();
        for (String stringId : genreIds) {
            genres.add(genreService.findById(Integer.parseInt(stringId)));
        }
        movie.setGenres(genres);
        movieService.update(movie);
        return "redirect:/movies";
    }

    @GetMapping("/movies/edit/{movie_id}")
    public String editMovie(Model model,
                            @PathVariable(name = "movie_id") int id) {
        Movie movie = movieService.findById(id);
        model.addAttribute("movie", movie);
        Map<Genre, Boolean> genres = new TreeMap<>(Comparator.comparingInt(Genre::getId));
        for (Genre genre : genreService.findAll()) {
            genres.put(genre, false);
        }
        for (Genre genre : movie.getGenres()) {
            genres.put(genre, true);
        }
        controllerUtil.addUserToModel(model);
        model.addAttribute("genres", genres);
        return "editMovie";
    }

    @PostMapping("/deleteMovie")
    public String deleteMovie(@ModelAttribute(name = "movie") Movie movie) {
        movieService.delete(movie);
        return "redirect:/movies";
    }

    @GetMapping("/movies-today/poster/{movieId}")
    public ResponseEntity<Resource> download(@PathVariable("movieId") Integer movieId) {
        Movie movie = movieService.findById(movieId);
        return ResponseEntity.ok()
                .headers(new HttpHeaders())
                .contentLength(movie.getPoster().length)
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new ByteArrayResource(movie.getPoster()));
    }
}
