package me.satyajit.gatekeeper.management.service;

import jakarta.servlet.http.HttpServletRequest;
import me.satyajit.gatekeeper.management.entity.ControlPlaneAuditRecord;
import me.satyajit.gatekeeper.management.entity.Organization;
import me.satyajit.gatekeeper.management.repository.ControlPlaneAuditRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;

@Service
public class AuditService {

    private final ControlPlaneAuditRepository auditRepository;

    public AuditService(ControlPlaneAuditRepository auditRepository){
        this.auditRepository = auditRepository;
    }

    public void record(Organization organization, String action,
                       String resourceType, String resourceId,
                       long startedAt, String details){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String actor = authentication == null ? "system" : authentication.getName();

        ControlPlaneAuditRecord auditRecord = new ControlPlaneAuditRecord();

        auditRecord.setOrganization(organization);
        auditRecord.setAction(action);
        auditRecord.setAction(action);
        auditRecord.setActor(actor);
        auditRecord.setResourceId(resourceId);
        auditRecord.setResourceType(resourceType);
        auditRecord.setDetails(details);
        auditRecord.setDurationMs((System.nanoTime() - startedAt)/1000000);
        auditRecord.setCorrelationId(getCorrelationId());
        auditRecord.setCreatedAt(LocalDateTime.now());
        auditRecord.setResult("SUCCESS");
        auditRepository.save(auditRecord);
    }


    private String getCorrelationId(){

        if (!(RequestContextHolder.currentRequestAttributes()
                instanceof ServletRequestAttributes)){
            return null;
        }

        HttpServletRequest request = ((ServletRequestAttributes)
        RequestContextHolder.currentRequestAttributes())
                .getRequest();

        return request.getHeader("X-Correlation-ID");
    }
}
