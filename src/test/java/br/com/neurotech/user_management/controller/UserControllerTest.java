package br.com.neurotech.user_management.controller;

import br.com.neurotech.user_management.dto.CertificationDto;
import br.com.neurotech.user_management.dto.TechnicalCompetenceDto;
import br.com.neurotech.user_management.dto.UserCreationDto;
import br.com.neurotech.user_management.model.Certification;
import br.com.neurotech.user_management.model.TechnicalCompetence;
import br.com.neurotech.user_management.model.User;
import br.com.neurotech.user_management.model.exception.UserNotFoundException;
import br.com.neurotech.user_management.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    private final DateTimeFormatter sdf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final User user = new User(
            1L,
            "John Doe",
            "john.doe@example.com",
            "(12) 34567-8901",
            List.of(
                    new TechnicalCompetence("Java Development", "Expert"),
                    new TechnicalCompetence("Spring Boot", "Intermediate")
            ),
            List.of(
                    new Certification("Certified Java Programmer", "Oracle", LocalDate.parse("15/05/2022", sdf), 360),
                    new Certification("Spring Professional", "Pivotal", LocalDate.parse("01/08/2023", sdf), 200)
            ),
            5,
            "https://www.linkedin.com/in/johndoe"
    );

    @Test
    public void createUser_WithValidData_ShouldCreateUser() throws Exception {
        String json = """
                {
                  "name": "John Doe",
                  "email": "john.doe@example.com",
                  "contact": "(12) 34567-8901",
                  "technicalCompetences": [
                    {
                      "description": "Java Development",
                      "level": "Expert"
                    },
                    {
                      "description": "Spring Boot",
                      "level": "Intermediate"
                    }
                  ],
                  "certifications": [
                    {
                      "description": "Certified Java Programmer",
                      "institution": "Oracle",
                      "date": "15/05/2022",
                      "hours": 360
                    },
                    {
                      "description": "Spring Professional",
                      "institution": "Pivotal",
                      "date": "01/08/2023",
                      "hours": 200
                    }
                  ],
                  "workExperience": 5,
                  "linkedinUrl": "https://www.linkedin.com/in/johndoe"
                }
                """;

        UserCreationDto userCreationDto = new UserCreationDto(
                "John Doe",
                "john.doe@example.com",
                "(12) 34567-8901",
                List.of(
                        new TechnicalCompetenceDto("Java Development", "Expert"),
                        new TechnicalCompetenceDto("Spring Boot", "Intermediate")
                ),
                List.of(
                        new CertificationDto("Certified Java Programmer", "Oracle", "15/05/2022", 360),
                        new CertificationDto("Spring Professional", "Pivotal", "01/08/2023", 200)
                ),
                5,
                "https://www.linkedin.com/in/johndoe"
        );

        when(userService.create(userCreationDto)).thenReturn(user);

        mvc.perform(
                        post("/user")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.contact").value("(12) 34567-8901"));
    }

    @Test
    public void createUser_WithInvalidData_ShouldThrowException() throws Exception {
        String json = """
                {
                  "name": "John Doe",
                  "email": "INVALID EMAIL",
                  "contact": "(12) 34567-8901",
                  "technicalCompetences": [
                    {
                      "description": "Java Development",
                      "level": "Expert"
                    },
                    {
                      "description": "Spring Boot",
                      "level": "Intermediate"
                    }
                  ],
                  "certifications": [
                    {
                      "description": "Certified Java Programmer",
                      "institution": "Oracle",
                      "date": "15/05/2022",
                      "hours": 360
                    },
                    {
                      "description": "Spring Professional",
                      "institution": "Pivotal",
                      "date": "01/08/2023",
                      "hours": 200
                    }
                  ],
                  "workExperience": 5,
                  "linkedinUrl": "https://www.linkedin.com/in/johndoe"
                }
                """;

        mvc.perform(
                        post("/user")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.[0].field").value("email"))
                .andExpect(jsonPath("$.[0].messages").value("The email must be valid"));
    }

    @Test
    public void listAllUsers_WithoutParams_ShouldListAllUsers() throws Exception {
        User user2 = new User();
        user2.setName("User 2");
        user2.setCertifications(List.of());
        user2.setTechnicalCompetences(List.of());
        List<User> userList = List.of(user, user2);

        Pageable pageable = PageRequest.of(0, 10);
        Page<User> page = new PageImpl<>(userList, pageable, userList.size());

        when(userService.listAllUsersWithFilters(
                Mockito.isNull(),
                Mockito.isNull(),
                Mockito.isNull(),
                Mockito.isNull(),
                Mockito.any(Pageable.class)))
                .thenReturn(page);

        mvc.perform(
                        get("/user")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].name").value("John Doe"))
                .andExpect(jsonPath("$.content[1].name").value("User 2"));
    }

    @Test
    public void listAllUsers_WithParams_ShouldListOnlyOneUser() throws Exception {
        List<User> userList = List.of(user);

        Pageable pageable = PageRequest.of(0, 10);
        Page<User> page = new PageImpl<>(userList, pageable, userList.size());

        when(userService.listAllUsersWithFilters(
                Mockito.eq(List.of("Java")),
                Mockito.isNull(),
                Mockito.isNull(),
                Mockito.isNull(),
                Mockito.any(Pageable.class)))
                .thenReturn(page);

        mvc.perform(
                        get("/user")
                                .param("technical competence", "Java")
                                .param("page", "0")
                                .param("size", "10")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].name").value("John Doe"));
    }

    @Test
    public void findUser_WithIdAsVariable_ShouldReturnUser() throws Exception {
        when(userService.findUserById(1L)).thenReturn(user);

        mvc.perform(
                        get("/user/1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    public void findUser_WithEmailAsVariable_ShouldReturnUser() throws Exception {
        when(userService.findUserByEmail("john.doe@example.com")).thenReturn(user);

        mvc.perform(
                        get("/user/john.doe@example.com")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    public void findUser_WithInvalidVariable_ShouldThrowUserNotFoundException() throws Exception {
        when(userService.findUserByEmail("invalid")).thenThrow(new UserNotFoundException());

        Assertions.assertThrows(UserNotFoundException.class, () -> userService.findUserByEmail("invalid"));

        mvc.perform(
                        get("/user/invalid")
                )
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteUser_WithValidId_ShouldDeleteUser() throws Exception {
        when(userService.delete(1L)).thenReturn(user);

        mvc.perform(
                        delete("/user/1")
                )
                .andExpect(status().isOk());
    }

    @Test
    public void deleteUser_WithInvalidId_ShouldThrowUserNotFoundException() throws Exception {
        when(userService.delete(1L)).thenThrow(new UserNotFoundException());

        mvc.perform(
                        delete("/user/1")
                )
                .andExpect(status().isNotFound());
    }

}