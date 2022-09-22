package ru.job4j.cinema.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.UserService;

@Component
public class ControllerUtil {

    private final UserService userService;

    public ControllerUtil(UserService userService) {
        this.userService = userService;
    }

    public void addUserToModel(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SimpleGrantedAuthority roleAdmin = new SimpleGrantedAuthority("ROLE_ADMIN");
        User currentUser;
        if ("anonymousUser".equals(authentication.getName())) {
            currentUser = new User();
            currentUser.setUsername("Гость");
        } else {
            currentUser = userService.findByUsername(authentication.getName());
        }
        model.addAttribute("user", currentUser);
        model.addAttribute("isAdmin", authentication.getAuthorities().contains(roleAdmin));
    }


}
