package br.com.neurotech.user_management.repository;

import br.com.neurotech.user_management.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    Page<User> findAll(Pageable pageable);

    Optional<User> findByEmail(String email);
}
