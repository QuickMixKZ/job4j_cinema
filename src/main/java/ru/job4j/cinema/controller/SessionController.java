package ru.job4j.cinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.service.CinemaHallService;
import ru.job4j.cinema.service.MovieService;
import ru.job4j.cinema.service.SessionService;
import ru.job4j.cinema.service.UserService;
import ru.job4j.cinema.util.ControllerUtil;

@Controller
public class SessionController {

    private final SessionService sessionService;
    private final MovieService movieService;
    private final CinemaHallService cinemaHallService;

    private final ControllerUtil controllerUtil;
    private final UserService userService;

    public SessionController(SessionService sessionService, MovieService movieService, CinemaHallService cinemaHallService, ControllerUtil controllerUtil, UserService userService) {
        this.sessionService = sessionService;
        this.movieService = movieService;
        this.cinemaHallService = cinemaHallService;
        this.controllerUtil = controllerUtil;
        this.userService = userService;
    }

    @GetMapping("/movies-today/{movie_id}")
    public String getSessionsByMovie(Model model,
                                     @PathVariable(name = "movie_id") int movieId) {
        controllerUtil.addUserToModel(model);
        model.addAttribute("sessions", sessionService.findTodayByMovieId(movieId));
        return "sessionsTime";
    }

    @GetMapping("/movies-today/{movie_id}/{session_id}")
    public String getAvailableSeats(Model model,
                                    @PathVariable(name = "movie_id") int movieId,
                                    @PathVariable(name = "session_id") int sessionId) {
        controllerUtil.addUserToModel(model);
        model.addAttribute("seats", sessionService.findAvailableSeats(sessionId));
        return "seats";
    }

    @GetMapping("/sessions")
    public String getAllSessions(Model model) {
        controllerUtil.addUserToModel(model);
        model.addAttribute("sessions", sessionService.findAll());
        return "sessions";
    }

    @GetMapping("/sessions/add")
    public String addSession(Model model) {
        controllerUtil.addUserToModel(model);
        model.addAttribute("session_", new Session());
        model.addAttribute("movies", movieService.findAll());
        model.addAttribute("halls", cinemaHallService.findAll());
        return "addSession";
    }

    @GetMapping("/sessions/edit/{id}")
    public String addSession(Model model,
                             @PathVariable(name = "id") int id) {
        controllerUtil.addUserToModel(model);
        model.addAttribute("session_", sessionService.findById(id));
        model.addAttribute("movies", movieService.findAll());
        model.addAttribute("halls", cinemaHallService.findAll());
        return "editSession";
    }

    @PostMapping("/sessions/addSession")
    public String addSession(@ModelAttribute(name = "session_") Session session) {
        sessionService.add(session);
        return "redirect:/sessions";
    }

    @PostMapping("/sessions/editSession")
    public String editSession(@ModelAttribute(name = "session_") Session session) {
        sessionService.update(session);
        return "redirect:/sessions";
    }
}
