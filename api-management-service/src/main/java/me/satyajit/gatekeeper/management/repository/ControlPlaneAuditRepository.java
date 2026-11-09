package me.satyajit.gatekeeper.management.repository;

import me.satyajit.gatekeeper.management.entity.ControlPlaneAuditRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ControlPlaneAuditRepository extends JpaRepository<ControlPlaneAuditRecord, Long> {


}
