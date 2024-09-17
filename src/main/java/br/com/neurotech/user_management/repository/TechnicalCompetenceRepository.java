package br.com.neurotech.user_management.repository;

import br.com.neurotech.user_management.model.TechnicalCompetence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TechnicalCompetenceRepository extends JpaRepository<TechnicalCompetence, Long> {
}
