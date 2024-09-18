package br.com.neurotech.user_management.service;

import br.com.neurotech.user_management.dto.CertificationDto;
import br.com.neurotech.user_management.dto.TechnicalCompetenceDto;
import br.com.neurotech.user_management.dto.UserCreationDto;
import br.com.neurotech.user_management.dto.UserUpdateDto;
import br.com.neurotech.user_management.model.Certification;
import br.com.neurotech.user_management.model.TechnicalCompetence;
import br.com.neurotech.user_management.model.User;
import br.com.neurotech.user_management.model.exception.UserNotFoundException;
import br.com.neurotech.user_management.repository.CertificationRepository;
import br.com.neurotech.user_management.repository.TechnicalCompetenceRepository;
import br.com.neurotech.user_management.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class UserServiceTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private CertificationRepository certificationRepository;

    @MockBean
    private TechnicalCompetenceRepository technicalCompetenceRepository;

    @Autowired
    private UserService userService;

    @Test
    public void create_WithValidData_ShouldReturnCreatedUser() {
        User user = new User();
        List<Certification> certifications = List.of(new Certification(
                "description",
                "institution",
                LocalDate.parse("01/01/2001", DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                10));
        List<TechnicalCompetence> technicalCompetences = List.of(new TechnicalCompetence(
                "description",
                "level"
        ));
        user.setName("name");
        user.setEmail("email@email.com");
        user.setCertifications(certifications);
        user.setTechnicalCompetences(technicalCompetences);

        Mockito.when(userRepository.save(any(User.class))).thenReturn(user);

        UserCreationDto userCreationDto = new UserCreationDto(
                user.getName(),
                user.getEmail(),
                user.getContact(),
                user.getTechnicalCompetences().stream().map(TechnicalCompetenceDto::new).toList(),
                user.getCertifications().stream().map(CertificationDto::new).toList(),
                user.getYearsOfExperience(),
                user.getLinkedinUrl());

        User createdUser = userService.create(userCreationDto);

        Assertions.assertNotNull(createdUser);
        Assertions.assertEquals("name", createdUser.getName());
        Assertions.assertEquals("email@email.com", createdUser.getEmail());
        Assertions.assertEquals(certifications, createdUser.getCertifications());
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
    }

    @Test
    public void listAllUsersWithFilters_NoFilter_ShouldReturnListOfAllUsers() {
        User user1 = new User();
        user1.setName("name");

        User user2 = new User();
        user2.setEmail("email@email.com");

        Pageable pageable = PageRequest.of(0, 10);

        Page<User> users = new PageImpl<>(List.of(user1, user2), pageable, 2);

        Mockito.when(userRepository.findAll(Mockito.any(Specification.class), Mockito.eq(pageable)))
                .thenReturn(users);

        var userList = userService.listAllUsersWithFilters(null, null, null, null, pageable);

        Assertions.assertEquals(2, userList.getTotalElements());
        Assertions.assertEquals("name", userList.getContent().get(0).getName());
        Assertions.assertEquals("email@email.com", userList.getContent().get(1).getEmail());
        Mockito.verify(userRepository, Mockito.times(1)).findAll(Mockito.any(Specification.class), Mockito.eq(pageable));
    }

    @Test
    public void listAllUsersWithFilters_TechnicalCompetenceFilter_ShouldReturnFilteredUsers() {
        User user1 = new User();
        user1.setName("name1");
        user1.setTechnicalCompetences(List.of(new TechnicalCompetence("Java", "level")));

        User user2 = new User();
        user2.setName("name2");
        user2.setTechnicalCompetences(List.of(new TechnicalCompetence("Python", "level")));

        Pageable pageable = PageRequest.of(0, 10);

        Mockito.when(userRepository.findAll(Mockito.any(Specification.class), Mockito.eq(pageable)))
                .thenAnswer(invocation -> {
                    Specification<User> spec = invocation.getArgument(0);
                    if (spec != null) {
                        return new PageImpl<>(List.of(user1), pageable, 1);
                    }
                    return new PageImpl<>(List.of(user1, user2), pageable, 2);
                });

        var userList = userService.listAllUsersWithFilters(List.of("Java"), null, null, null, pageable);

        Assertions.assertEquals(1, userList.getTotalElements());
        Assertions.assertEquals("name1", userList.getContent().get(0).getName());

        Mockito.verify(userRepository, Mockito.times(1)).findAll(Mockito.any(Specification.class), Mockito.eq(pageable));
    }

    @Test
    public void listAllUsersWithFilters_CertificationFilter_ShouldReturnFilteredUsers() {
        User user1 = new User();
        user1.setName("name1");
        user1.setCertifications(List.of(new Certification("AWS", "institution", LocalDate.now(), 10)));

        User user2 = new User();
        user2.setName("name2");
        user2.setCertifications(List.of(new Certification("Azure", "institution", LocalDate.now(), 10)));

        Pageable pageable = PageRequest.of(0, 10);

        Mockito.when(userRepository.findAll(Mockito.any(Specification.class), Mockito.eq(pageable)))
                .thenAnswer(invocation -> {
                    Specification<User> spec = invocation.getArgument(0);
                    if (spec != null) {
                        return new PageImpl<>(List.of(user1), pageable, 1);
                    }
                    return new PageImpl<>(List.of(user1, user2), pageable, 2);
                });

        var userList = userService.listAllUsersWithFilters(null, List.of("AWS"), null, null, pageable);

        Assertions.assertEquals(1, userList.getTotalElements());
        Assertions.assertEquals("name1", userList.getContent().get(0).getName());

        Mockito.verify(userRepository, Mockito.times(1)).findAll(Mockito.any(Specification.class), Mockito.eq(pageable));
    }

    @Test
    public void listAllUsersWithFilters_YearsOfExperienceFilter_ShouldReturnFilteredUsers() {
        User user1 = new User();
        user1.setName("name1");
        user1.setYearsOfExperience(5);

        User user2 = new User();
        user2.setName("name2");
        user2.setYearsOfExperience(10);

        Pageable pageable = PageRequest.of(0, 10);

        Mockito.when(userRepository.findAll(Mockito.any(Specification.class), Mockito.eq(pageable)))
                .thenAnswer(invocation -> {
                    Specification<User> spec = invocation.getArgument(0);
                    if (spec != null) {
                        return new PageImpl<>(List.of(user1), pageable, 1);
                    }
                    return new PageImpl<>(List.of(user1, user2), pageable, 2);
                });

        var userList = userService.listAllUsersWithFilters(null, null, 8, 10, pageable);

        Assertions.assertEquals(1, userList.getTotalElements());

        Mockito.verify(userRepository, Mockito.times(1)).findAll(Mockito.any(Specification.class), Mockito.eq(pageable));
    }


    @Test
    public void findUserById_WithValidId_ShouldReturnUser() throws UserNotFoundException {
        User user = new User();
        user.setId(1L);
        user.setName("name");
        user.setEmail("email@email.com");

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User foundUser = userService.findUserById(1L);

        Assertions.assertEquals("name", foundUser.getName());
        Mockito.verify(userRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    public void findUserById_WithNonExistingId_ShouldThrowException() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> userService.findUserById(1L));
        Mockito.verify(userRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    public void update_WhenUserExists_ShouldUpdateUserDetails() throws UserNotFoundException {
        User userFormerInfo = new User();
        userFormerInfo.setId(1L);
        userFormerInfo.setName("name");

        UserUpdateDto userUpdateDto = new UserUpdateDto(1L, "new name", null, null, null, null, null, null);
        User userUpdatedInfo = new User();
        userUpdatedInfo.setId(1L);
        userUpdatedInfo.setName("new name");

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(userFormerInfo));
        Mockito.when(userRepository.save(any(User.class))).thenReturn(userUpdatedInfo);

        User updatedUser = userService.update(userUpdateDto);

        Assertions.assertEquals("new name", updatedUser.getName());
        Mockito.verify(userRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(userRepository, Mockito.times(1)).save(userFormerInfo);
    }

    @Test
    public void delete_WhenUserExists_ShouldDeleteUser() throws UserNotFoundException {
        User user = new User();
        user.setId(1L);
        user.setName("name");

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Mockito.doNothing().when(userRepository).delete(user);

        userService.delete(1L);

        Mockito.verify(userRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(userRepository, Mockito.times(1)).delete(user);
    }

}