package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.security.Principal;


@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

        @GetMapping()
        public String pageForAdmin(Model model, Principal principal) {
            StringBuilder roles = new StringBuilder();
            for (Role role : userService.findUserByUsername(principal.getName()).getRoles()) {
                roles.append(role.toString());
                roles.append(" ");
            }
            model.addAttribute("userRole", roles);
            model.addAttribute("users", userService.allUsers());
            model.addAttribute("user", userService.findUserByUsername(principal.getName()));
            model.addAttribute("newUser", new User());
            model.addAttribute("roles", roleService.findAllRoles());
            return "admin";
        }

        @PostMapping("/new")
        public String createUser(@ModelAttribute @Valid User user) {
            userService.add(user);
            return "redirect:/admin";
        }

        @DeleteMapping("/delete/{id}")
        public String delete(@PathVariable("id") int id) {
            userService.delete(id);
            return "redirect:/admin";
        }

        @PatchMapping("/edit/{id}")
        public String update(@PathVariable("id") int id, @ModelAttribute("editUser") @Valid User user) {
            userService.update(user.getId(), user);
            return "redirect:/admin";
        }
    }
