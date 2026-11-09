package me.satyajit.gatekeeper.management.repository;


import me.satyajit.gatekeeper.management.entity.ApiRoutes;
import me.satyajit.gatekeeper.management.entity.RouteConfigurationVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteConfigurationVersionRepository extends JpaRepository
        <RouteConfigurationVersion, Long> {

    List<RouteConfigurationVersion> findAllByOrganizationIdAndRouteIdOrderByVersionNumberDesc(Long id, String routeId);

}
