package br.com.neurotech.user_management.service;

import br.com.neurotech.user_management.dto.CertificationDto;
import br.com.neurotech.user_management.dto.TechnicalCompetenceDto;
import br.com.neurotech.user_management.dto.UserCreationDto;
import br.com.neurotech.user_management.dto.UserUpdateDto;
import br.com.neurotech.user_management.model.User;
import br.com.neurotech.user_management.model.exception.UserNotFoundException;
import br.com.neurotech.user_management.repository.CertificationRepository;
import br.com.neurotech.user_management.repository.TechnicalCompetenceRepository;
import br.com.neurotech.user_management.repository.UserRepository;
import br.com.neurotech.user_management.repository.UserSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final TechnicalCompetenceRepository technicalCompetenceRepository;
    private final CertificationRepository certificationRepository;

    public UserService(UserRepository userRepository, TechnicalCompetenceRepository technicalCompetenceRepository, CertificationRepository certificationRepository) {
        this.userRepository = userRepository;
        this.technicalCompetenceRepository = technicalCompetenceRepository;
        this.certificationRepository = certificationRepository;
    }

    public User create(UserCreationDto creationDto) {
        User user = new User(
                creationDto.name(),
                creationDto.email(),
                creationDto.contact(),
                creationDto.technicalCompetences()
                        .stream()
                        .map(TechnicalCompetenceDto::toModel)
                        .toList(),
                creationDto.certifications()
                        .stream()
                        .map(CertificationDto::toModel)
                        .toList(),
                creationDto.workExperience(),
                creationDto.linkedinUrl()
        );

        technicalCompetenceRepository.saveAll(user.getTechnicalCompetences());
        certificationRepository.saveAll(user.getCertifications());
        userRepository.save(user);

        return user;
    }

    public Page<User> listAllUsersWithFilters(List<String> technicalCompetences, List<String> certifications,
                                              Integer yearsOfExperienceMin, Integer yearsOfExperienceMax, Pageable pageable) {
        return userRepository.findAll(
                UserSpecification.filterUser(technicalCompetences,
                        certifications,
                        yearsOfExperienceMin,
                        yearsOfExperienceMax), pageable);
    }

    public User findUserById(Long id) throws UserNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
    }

    public User findUserByEmail(String email) throws UserNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);
    }

    public User update(UserUpdateDto updateDto) throws UserNotFoundException {
        User user = findUserById(updateDto.id());

        if(updateDto.name() != null && !updateDto.name().isEmpty()) {
            user.setName(updateDto.name());
        }
        if(updateDto.email() != null && !updateDto.email().isEmpty()) {
            user.setEmail(updateDto.email());
        }
        if(updateDto.contact() != null && !updateDto.contact().isEmpty()) {
            user.setContact(updateDto.contact());
        }
        if(updateDto.technicalCompetences() != null && !updateDto.technicalCompetences().isEmpty()) {
            user.setTechnicalCompetences(updateDto.technicalCompetences()
                    .stream()
                    .map(TechnicalCompetenceDto::toModel)
                    .toList());
        }
        if(updateDto.certifications() != null && !updateDto.certifications().isEmpty()) {
            user.setCertifications(updateDto.certifications()
                    .stream()
                    .map(CertificationDto::toModel)
                    .toList());
        }
        if(updateDto.yearsOfExperience() != null) {
            user.setYearsOfExperience(updateDto.yearsOfExperience());
        }
        if(updateDto.linkedinUrl() != null && !updateDto.linkedinUrl().isEmpty()) {
            user.setLinkedinUrl(updateDto.linkedinUrl());
        }

        userRepository.save(user);

        return user;
    }

    public User delete(Long id) throws UserNotFoundException {
        User user = findUserById(id);

        userRepository.delete(user);

        return user;
    }
}
