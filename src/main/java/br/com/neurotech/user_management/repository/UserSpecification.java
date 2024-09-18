package br.com.neurotech.user_management.repository;

import br.com.neurotech.user_management.model.User;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class UserSpecification {

    public static Specification<User> filterUser(
            List<String> technicalCompetences,
            List<String> certifications,
            Integer yearsOfExperienceMin,
            Integer yearsOfExperienceMax
    ) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(technicalCompetences != null && !technicalCompetences.isEmpty()) {
                Predicate techPredicate = criteriaBuilder.disjunction();
                for (String tech : technicalCompetences) {
                    techPredicate = criteriaBuilder.or(
                            techPredicate,
                            criteriaBuilder.like(
                                    criteriaBuilder.lower(root.join("technicalCompetences").get("description")),
                                    "%" + tech.toLowerCase() + "%"
                            )
                    );
                }
                predicates.add(techPredicate);
            }

            if(certifications != null && !certifications.isEmpty()) {
                Predicate certPredicate = criteriaBuilder.disjunction();
                for (String cert : certifications) {
                    certPredicate = criteriaBuilder.or(
                            certPredicate,
                            criteriaBuilder.like(
                                    criteriaBuilder.lower(root.join("certifications").get("description")),
                                    "%" + cert.toLowerCase() + "%"
                            )
                    );
                }
                predicates.add(certPredicate);
            }

            if(yearsOfExperienceMin != null && yearsOfExperienceMax != null) {
                predicates.add(criteriaBuilder.between(root.get("yearsOfExperience"), yearsOfExperienceMin, yearsOfExperienceMax));
            } else if (yearsOfExperienceMin != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("yearsOfExperience"), yearsOfExperienceMin));
            } else if (yearsOfExperienceMax != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("yearsOfExperience"), yearsOfExperienceMax));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
}
