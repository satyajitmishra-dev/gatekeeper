package me.satyajit.gatekeeper.management.repository;

import me.satyajit.gatekeeper.management.entity.ApiRoutes;
import me.satyajit.gatekeeper.management.entity.OrganizationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApiRoutesRepository extends JpaRepository<ApiRoutes, Long> {

    boolean existsByOrganizationIdAndRouteId(Long organizationId, String routeId);

    List<ApiRoutes> findAllByOrganizationId(Long organizationId);
     Optional<ApiRoutes> findByIdAndOrganizationId(Long id, Long organizationId);

    List<ApiRoutes> findAllByActiveAndOrganizationStatus(Boolean active,
            OrganizationStatus status);
}
