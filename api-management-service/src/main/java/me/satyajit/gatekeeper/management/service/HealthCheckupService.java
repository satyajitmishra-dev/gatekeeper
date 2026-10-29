package me.satyajit.gatekeeper.management.service;


import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HealthCheckupService {

    private final JdbcTemplate jdbcTemplate;


    public String getHealth (){

        String dbStatus = "DOWN";

        try {

           Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);

           if (Integer.valueOf(1).equals(result)){
               dbStatus = "UP";
           }
        } catch (Exception e){

            System.out.println("MySQL Connetion Failed " + e.getMessage());
        }

        return dbStatus;
    }

}
