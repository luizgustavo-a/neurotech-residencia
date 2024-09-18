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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
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
    public void listAllUsers_Valid_ShouldReturnListOfAllUsers() {
        User user1 = new User();
        user1.setName("name");

        User user2 = new User();
        user2.setEmail("email@email.com");

        List<User> users = Arrays.asList(user1, user2);

        Mockito.when(userRepository.findAll()).thenReturn(users);

        List<User> userList = userService.listAllUsers();

        Assertions.assertEquals(2, userList.size());
        Assertions.assertEquals("name", userList.get(0).getName());
        Assertions.assertEquals("email@email.com", userList.get(1).getEmail());
        Mockito.verify(userRepository, Mockito.times(1)).findAll();
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