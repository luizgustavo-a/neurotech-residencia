package br.com.neurotech.employees_management.service;

import br.com.neurotech.employees_management.dto.CertificationDto;
import br.com.neurotech.employees_management.dto.TechnicalCompetenceDto;
import br.com.neurotech.employees_management.dto.EmployeeCreationDto;
import br.com.neurotech.employees_management.dto.EmployeeUpdateDto;
import br.com.neurotech.employees_management.model.Certification;
import br.com.neurotech.employees_management.model.Employee;
import br.com.neurotech.employees_management.model.TechnicalCompetence;
import br.com.neurotech.employees_management.model.exception.EmployeeNotFoundException;
import br.com.neurotech.employees_management.repository.CertificationRepository;
import br.com.neurotech.employees_management.repository.TechnicalCompetenceRepository;
import br.com.neurotech.employees_management.repository.EmployeeRepository;
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
class EmployeeServiceTest {

    @MockBean
    private EmployeeRepository employeeRepository;

    @MockBean
    private CertificationRepository certificationRepository;

    @MockBean
    private TechnicalCompetenceRepository technicalCompetenceRepository;

    @Autowired
    private EmployeeService employeeService;

    @Test
    public void create_Employee_WithValidData_ShouldReturnCreatedEmployee() {
        Employee employee = new Employee();
        List<Certification> certifications = List.of(new Certification(
                "description",
                "institution",
                LocalDate.parse("01/01/2001", DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                10));
        List<TechnicalCompetence> technicalCompetences = List.of(new TechnicalCompetence(
                "description",
                "level"
        ));
        employee.setName("name");
        employee.setEmail("email@email.com");
        employee.setCertifications(certifications);
        employee.setTechnicalCompetences(technicalCompetences);

        Mockito.when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        EmployeeCreationDto employeeCreationDto = new EmployeeCreationDto(
                employee.getName(),
                employee.getEmail(),
                employee.getContact(),
                employee.getTechnicalCompetences().stream().map(TechnicalCompetenceDto::new).toList(),
                employee.getCertifications().stream().map(CertificationDto::new).toList(),
                employee.getYearsOfExperience(),
                employee.getLinkedinUrl());

        Employee createdEmployee = employeeService.createEmployee(employeeCreationDto);

        Assertions.assertNotNull(createdEmployee);
        Assertions.assertEquals("name", createdEmployee.getName());
        Assertions.assertEquals("email@email.com", createdEmployee.getEmail());
        Assertions.assertEquals(certifications, createdEmployee.getCertifications());
        Mockito.verify(employeeRepository, Mockito.times(1)).save(employee);
    }

    @Test
    public void listAllEmployeesWithFilters_NoFilter_ShouldReturnListOfAllEmployees() {
        Employee employee1 = new Employee();
        employee1.setName("name");

        Employee employee2 = new Employee();
        employee2.setEmail("email@email.com");

        Pageable pageable = PageRequest.of(0, 10);

        Page<Employee> employees = new PageImpl<>(List.of(employee1, employee2), pageable, 2);

        Mockito.when(employeeRepository.findAll(Mockito.any(Specification.class), Mockito.eq(pageable)))
                .thenReturn(employees);

        var employeeList = employeeService.listAllEmployeesWithFilters(null, null, null, null, pageable);

        Assertions.assertEquals(2, employeeList.getTotalElements());
        Assertions.assertEquals("name", employeeList.getContent().get(0).getName());
        Assertions.assertEquals("email@email.com", employeeList.getContent().get(1).getEmail());
        Mockito.verify(employeeRepository, Mockito.times(1)).findAll(Mockito.any(Specification.class), Mockito.eq(pageable));
    }

    @Test
    public void listAllEmployeesWithFilters_TechnicalCompetenceFilter_ShouldReturnFilteredEmployees() {
        Employee employee1 = new Employee();
        employee1.setName("name1");
        employee1.setTechnicalCompetences(List.of(new TechnicalCompetence("Java", "level")));

        Employee employee2 = new Employee();
        employee2.setName("name2");
        employee2.setTechnicalCompetences(List.of(new TechnicalCompetence("Python", "level")));

        Pageable pageable = PageRequest.of(0, 10);

        Mockito.when(employeeRepository.findAll(Mockito.any(Specification.class), Mockito.eq(pageable)))
                .thenAnswer(invocation -> {
                    Specification<Employee> spec = invocation.getArgument(0);
                    if (spec != null) {
                        return new PageImpl<>(List.of(employee1), pageable, 1);
                    }
                    return new PageImpl<>(List.of(employee1, employee2), pageable, 2);
                });

        var employeeList = employeeService.listAllEmployeesWithFilters(List.of("Java"), null, null, null, pageable);

        Assertions.assertEquals(1, employeeList.getTotalElements());
        Assertions.assertEquals("name1", employeeList.getContent().get(0).getName());

        Mockito.verify(employeeRepository, Mockito.times(1)).findAll(Mockito.any(Specification.class), Mockito.eq(pageable));
    }

    @Test
    public void listAllEmployeesWithFilters_CertificationFilter_ShouldReturnFilteredEmployees() {
        Employee employee1 = new Employee();
        employee1.setName("name1");
        employee1.setCertifications(List.of(new Certification("AWS", "institution", LocalDate.now(), 10)));

        Employee employee2 = new Employee();
        employee2.setName("name2");
        employee2.setCertifications(List.of(new Certification("Azure", "institution", LocalDate.now(), 10)));

        Pageable pageable = PageRequest.of(0, 10);

        Mockito.when(employeeRepository.findAll(Mockito.any(Specification.class), Mockito.eq(pageable)))
                .thenAnswer(invocation -> {
                    Specification<Employee> spec = invocation.getArgument(0);
                    if (spec != null) {
                        return new PageImpl<>(List.of(employee1), pageable, 1);
                    }
                    return new PageImpl<>(List.of(employee1, employee2), pageable, 2);
                });

        var employeeList = employeeService.listAllEmployeesWithFilters(null, List.of("AWS"), null, null, pageable);

        Assertions.assertEquals(1, employeeList.getTotalElements());
        Assertions.assertEquals("name1", employeeList.getContent().get(0).getName());

        Mockito.verify(employeeRepository, Mockito.times(1)).findAll(Mockito.any(Specification.class), Mockito.eq(pageable));
    }

    @Test
    public void listAllEmployeesWithFilters_YearsOfExperienceFilter_ShouldReturnFilteredEmployees() {
        Employee employee1 = new Employee();
        employee1.setName("name1");
        employee1.setYearsOfExperience(5);

        Employee employee2 = new Employee();
        employee2.setName("name2");
        employee2.setYearsOfExperience(10);

        Pageable pageable = PageRequest.of(0, 10);

        Mockito.when(employeeRepository.findAll(Mockito.any(Specification.class), Mockito.eq(pageable)))
                .thenAnswer(invocation -> {
                    Specification<Employee> spec = invocation.getArgument(0);
                    if (spec != null) {
                        return new PageImpl<>(List.of(employee1), pageable, 1);
                    }
                    return new PageImpl<>(List.of(employee1, employee2), pageable, 2);
                });

        var employeeList = employeeService.listAllEmployeesWithFilters(null, null, 8, 10, pageable);

        Assertions.assertEquals(1, employeeList.getTotalElements());

        Mockito.verify(employeeRepository, Mockito.times(1)).findAll(Mockito.any(Specification.class), Mockito.eq(pageable));
    }


    @Test
    public void findEmployeeById_WithValidId_ShouldReturnEmployee() throws EmployeeNotFoundException {
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setName("name");
        employee.setEmail("email@email.com");

        Mockito.when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        Employee foundEmployee = employeeService.findEmployeeById(1L);

        Assertions.assertEquals("name", foundEmployee.getName());
        Mockito.verify(employeeRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    public void findEmployeeById_WithNonExistingId_ShouldThrowException() {
        Mockito.when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(EmployeeNotFoundException.class, () -> employeeService.findEmployeeById(1L));
        Mockito.verify(employeeRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    public void update_WhenEmployeeExists_ShouldUpdateEmployeeEmployeeDetails() throws EmployeeNotFoundException {
        Employee employeeFormerInfo = new Employee();
        employeeFormerInfo.setId(1L);
        employeeFormerInfo.setName("name");

        EmployeeUpdateDto employeeUpdateDto = new EmployeeUpdateDto(1L, "new name", null, null, null, null, null, null);
        Employee employeeUpdatedInfo = new Employee();
        employeeUpdatedInfo.setId(1L);
        employeeUpdatedInfo.setName("new name");

        Mockito.when(employeeRepository.findById(1L)).thenReturn(Optional.of(employeeFormerInfo));
        Mockito.when(employeeRepository.save(any(Employee.class))).thenReturn(employeeUpdatedInfo);

        Employee updatedEmployee = employeeService.updateEmployee(employeeUpdateDto);

        Assertions.assertEquals("new name", updatedEmployee.getName());
        Mockito.verify(employeeRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(employeeRepository, Mockito.times(1)).save(employeeFormerInfo);
    }

    @Test
    public void delete_WhenEmployeeExists_ShouldDeleteEmployee() throws EmployeeNotFoundException {
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setName("name");

        Mockito.when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        Mockito.doNothing().when(employeeRepository).delete(employee);

        employeeService.delete(1L);

        Mockito.verify(employeeRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(employeeRepository, Mockito.times(1)).delete(employee);
    }

}