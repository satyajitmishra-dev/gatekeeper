package me.satyajit.gatekeeper.management.repository;

import me.satyajit.gatekeeper.management.entity.ManagementUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ManagementUserRepository extends JpaRepository<ManagementUsers, Long> {

    Optional<ManagementUsers> findByUsername(String username);
    Optional<ManagementUsers> findByOrganizationId(Long id);
    boolean existsByUsername (String username);

}
