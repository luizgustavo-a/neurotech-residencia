package br.com.neurotech.user_management.controller;

import br.com.neurotech.user_management.dto.UserCreationDto;
import br.com.neurotech.user_management.dto.UserPublicDto;
import br.com.neurotech.user_management.dto.UserUpdateDto;
import br.com.neurotech.user_management.model.exception.UserNotFoundException;
import br.com.neurotech.user_management.service.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public ResponseEntity<UserPublicDto> createUser(@RequestBody @Valid UserCreationDto creationDto,
                                                    UriComponentsBuilder builder) {
        var user = userService.create(creationDto);
        var uri = builder.path("/user/{id}").buildAndExpand(user.getId()).toUri();

        return ResponseEntity.created(uri).body(new UserPublicDto(user));
    }

    @GetMapping
    public ResponseEntity<Page<UserPublicDto>> listAllUsers(
            @RequestParam(value = "technical competence", required = false) List<String> technicalCompetences,
            @RequestParam(value = "certification", required = false) List<String> certifications,
            @RequestParam(value = "years of experience (min)", required = false) Integer yearsOfExperienceMin,
            @RequestParam(value = "years of experience (max)", required = false) Integer yearsOfExperienceMax,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "id") String sort
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        var paginatedUsers = userService.listAllUsersWithFilters(technicalCompetences,
                certifications,
                yearsOfExperienceMin,
                yearsOfExperienceMax,
                pageable).map(UserPublicDto::new);

        return ResponseEntity.ok(paginatedUsers);
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
    @Transactional
    public ResponseEntity<UserPublicDto> updateUser(@RequestBody @Valid UserUpdateDto updateDto) throws UserNotFoundException {
        var user = userService.update(updateDto);

        return ResponseEntity.ok(new UserPublicDto(user));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<UserPublicDto> deleteUser(@PathVariable Long id) throws UserNotFoundException {
        var user = userService.delete(id);

        return ResponseEntity.ok(new UserPublicDto(user));
    }
}
