package br.com.neurotech.employees_management.controller;

import br.com.neurotech.employees_management.dto.CertificationDto;
import br.com.neurotech.employees_management.dto.TechnicalCompetenceDto;
import br.com.neurotech.employees_management.dto.EmployeeCreationDto;
import br.com.neurotech.employees_management.model.Certification;
import br.com.neurotech.employees_management.model.Employee;
import br.com.neurotech.employees_management.model.TechnicalCompetence;
import br.com.neurotech.employees_management.model.exception.EmployeeNotFoundException;
import br.com.neurotech.employees_management.service.EmployeeService;
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
@WebMvcTest(EmployeeController.class)
@AutoConfigureMockMvc
class EmployeeControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private EmployeeService employeeService;

    private final DateTimeFormatter sdf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final Employee employee = new Employee(
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
    public void createEmployee_WithValidData_ShouldCreateEmployee() throws Exception {
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

        EmployeeCreationDto employeeCreationDto = new EmployeeCreationDto(
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

        when(employeeService.createEmployee(employeeCreationDto)).thenReturn(employee);

        mvc.perform(
                        post("/employee")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.contact").value("(12) 34567-8901"));
    }

    @Test
    public void createEmployee_WithInvalidData_ShouldThrowException() throws Exception {
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
                        post("/employee")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.[0].field").value("email"))
                .andExpect(jsonPath("$.[0].messages").value("The email must be valid"));
    }

    @Test
    public void listAllEmployees_WithoutParams_ShouldListAllEmployees() throws Exception {
        Employee employee2 = new Employee();
        employee2.setName("Employee 2");
        employee2.setCertifications(List.of());
        employee2.setTechnicalCompetences(List.of());
        List<Employee> employeeList = List.of(employee, employee2);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Employee> page = new PageImpl<>(employeeList, pageable, employeeList.size());

        when(employeeService.listAllEmployeesWithFilters(
                Mockito.isNull(),
                Mockito.isNull(),
                Mockito.isNull(),
                Mockito.isNull(),
                Mockito.any(Pageable.class)))
                .thenReturn(page);

        mvc.perform(
                        get("/employee")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].name").value("John Doe"))
                .andExpect(jsonPath("$.content[1].name").value("Employee 2"));
    }

    @Test
    public void listAllEmployees_WithParams_ShouldListOnlyOneEmployee() throws Exception {
        List<Employee> employeeList = List.of(employee);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Employee> page = new PageImpl<>(employeeList, pageable, employeeList.size());

        when(employeeService.listAllEmployeesWithFilters(
                Mockito.eq(List.of("Java")),
                Mockito.isNull(),
                Mockito.isNull(),
                Mockito.isNull(),
                Mockito.any(Pageable.class)))
                .thenReturn(page);

        mvc.perform(
                        get("/employee")
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
    public void findEmployee_WithIdAsVariable_ShouldReturnEmployee() throws Exception {
        when(employeeService.findEmployeeById(1L)).thenReturn(employee);

        mvc.perform(
                        get("/employee/1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    public void findEmployee_WithEmailAsVariable_ShouldReturnEmployee() throws Exception {
        when(employeeService.findEmployeeByEmail("john.doe@example.com")).thenReturn(employee);

        mvc.perform(
                        get("/employee/john.doe@example.com")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    public void findEmployee_WithInvalidVariable_ShouldThrowEmployeeNotFoundException() throws Exception {
        when(employeeService.findEmployeeByEmail("invalid")).thenThrow(new EmployeeNotFoundException());

        Assertions.assertThrows(EmployeeNotFoundException.class, () -> employeeService.findEmployeeByEmail("invalid"));

        mvc.perform(
                        get("/employee/invalid")
                )
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteEmployee_WithValidId_ShouldDeleteEmployee() throws Exception {
        when(employeeService.delete(1L)).thenReturn(employee);

        mvc.perform(
                        delete("/employee/1")
                )
                .andExpect(status().isOk());
    }

    @Test
    public void deleteEmployee_WithInvalidId_ShouldThrowEmployeeNotFoundException() throws Exception {
        when(employeeService.delete(1L)).thenThrow(new EmployeeNotFoundException());

        mvc.perform(
                        delete("/employee/1")
                )
                .andExpect(status().isNotFound());
    }

}