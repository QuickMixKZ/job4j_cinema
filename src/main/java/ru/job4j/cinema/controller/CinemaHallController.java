package ru.job4j.cinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.job4j.cinema.model.CinemaHall;
import ru.job4j.cinema.service.CinemaHallService;
import ru.job4j.cinema.util.ControllerUtil;

@Controller
public class CinemaHallController {

    private final CinemaHallService cinemaHallService;
    private final ControllerUtil controllerUtil;

    public CinemaHallController(CinemaHallService cinemaHallService, ControllerUtil controllerUtil) {
        this.cinemaHallService = cinemaHallService;
        this.controllerUtil = controllerUtil;
    }

    @GetMapping("/halls")
    public String getCinemaHalls(Model model) {
        model.addAttribute("halls", cinemaHallService.findAll());
        controllerUtil.addUserToModel(model);
        return "halls";
    }

    @GetMapping("/halls/add")
    public String addCinemaHall(Model model) {
        controllerUtil.addUserToModel(model);
        model.addAttribute("hall", new CinemaHall());
        return "addHall";
    }

    @PostMapping("/addHall")
    public String addCinemaHall(@ModelAttribute(name = "hall") CinemaHall cinemaHall) {
        cinemaHallService.add(cinemaHall);
        return "redirect:/halls";
    }

    @GetMapping("/halls/edit/{id}")
    public String editCinemaHall(Model model,
                                 @PathVariable(name = "id") int id) {
        controllerUtil.addUserToModel(model);
        model.addAttribute("hall", cinemaHallService.findById(id));
        return "editHall";
    }

    @PostMapping("/editHall")
    public String editCinemaHall(@ModelAttribute(name = "hall") CinemaHall cinemaHall) {
        cinemaHallService.update(cinemaHall);
        return "redirect:/halls";
    }

    @PostMapping("deleteHall")
    public String deleteCinemaHalls(@ModelAttribute(name = "hall") CinemaHall cinemaHall) {
        cinemaHallService.delete(cinemaHall);
        return "redirect:/halls";
    }

}
