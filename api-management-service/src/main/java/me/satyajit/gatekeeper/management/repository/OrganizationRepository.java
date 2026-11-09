package me.satyajit.gatekeeper.management.repository;

import me.satyajit.gatekeeper.management.entity.Organization;
import me.satyajit.gatekeeper.management.entity.OrganizationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {

boolean existsByNameIgnoreCase(String name);

boolean existsBySlugIgnoreCase(String slug);

List<Organization> findAllByStatusNot(OrganizationStatus status);

Optional<Organization> findByIdAndStatusNot(Long id, OrganizationStatus status);


}
