package me.satyajit.gatekeeper.analytics.controller.health;



import me.satyajit.gatekeeper.analytics.dto.responseDto.HealthResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HealthController {


    private String serviceName;
    private String serviceDescription;


    public HealthController (
            @Value("${info.app.name}") String serviceName,
            @Value("${info.app.description}") String serviceDescription
    ){

        this.serviceDescription = serviceDescription;
        this.serviceName = serviceName;
    }

    @GetMapping("/health")

    public ResponseEntity<HealthResponseDto> healthCheck(){

       /* String message = "Service Name : "+serviceName+
                " Service Description: "+serviceDescription;
        */

        HealthResponseDto response = new HealthResponseDto(
                "UP",
                serviceName,
                serviceDescription
        );

        return ResponseEntity
                .ok(response);
    }
}
