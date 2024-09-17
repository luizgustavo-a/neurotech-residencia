package br.com.neurotech.user_management.controller;

import br.com.neurotech.user_management.dto.UserCreationDto;
import br.com.neurotech.user_management.dto.UserPublicDto;
import br.com.neurotech.user_management.dto.UserUpdateDto;
import br.com.neurotech.user_management.model.exception.UserNotFoundException;
import br.com.neurotech.user_management.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserPublicDto> createUser(@RequestBody UserCreationDto creationDto,
                                                    UriComponentsBuilder builder) {
        var user = userService.create(creationDto);
        var uri = builder.path("/user/{id}").buildAndExpand(user.getId()).toUri();

        return ResponseEntity.created(uri).body(new UserPublicDto(user));
    }

    @GetMapping
    public ResponseEntity<List<UserPublicDto>> listAllUsers(
            @RequestParam(value = "technical competence", required = false) List<String> technicalCompetences,
            @RequestParam(value = "certification", required = false) List<String> certifications,
            @RequestParam(value = "years of experience", required = false) Integer yearsOfExperience
    ) {

        var userList = userService.listAllUsers()
                .stream()
                .map(UserPublicDto::new)
                .filter(user -> {
                    boolean matches = true;

                    if (technicalCompetences != null && !technicalCompetences.isEmpty()) {
                        matches = user.technicalCompetences().stream()
                                .anyMatch(t -> technicalCompetences.contains(t.description()));
                    }

                    if (matches && certifications != null && !certifications.isEmpty()) {
                        matches = user.certifications().stream()
                                .anyMatch(c -> certifications.contains(c.description()));
                    }

                    if (matches && yearsOfExperience != null) {
                        matches = user.yearsOfExperience().equals(yearsOfExperience);
                    }

                    return matches;
                })
                .toList();

        return ResponseEntity.ok(userList);
    }

    @GetMapping("/{idOrEmail}")
    public ResponseEntity<UserPublicDto> findUser(@PathVariable String idOrEmail) throws UserNotFoundException {
        try {
            Long id = Long.parseLong(idOrEmail);
            var user = userService.findUserById(id);
            return ResponseEntity.ok(new UserPublicDto(user));
        } catch (Exception e) {
            // if the input is not the id, it is the email
        }
        var user = userService.findUserByEmail(idOrEmail);
        return ResponseEntity.ok(new UserPublicDto(user));
    }

    @PutMapping
    public ResponseEntity<UserPublicDto> updateUser(@RequestBody UserUpdateDto updateDto) throws UserNotFoundException {
        var user = userService.update(updateDto);

        return ResponseEntity.ok(new UserPublicDto(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserPublicDto> deleteUser(@PathVariable Long id) throws UserNotFoundException {
        var user = userService.delete(id);

        return ResponseEntity.ok(new UserPublicDto(user));
    }
}
