package me.satyajit.gatekeeper.management.dto.api_routes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.satyajit.gatekeeper.management.entity.RouteHeaderRules;

@Getter
@Setter
@AllArgsConstructor
public class HeaderRulesResponse {

    private Long id;
    private String direction;
    private String action;
    private String headerName;
    private String headerValue;


    public static HeaderRulesResponse from(RouteHeaderRules headerRules){

        return new HeaderRulesResponse(
                headerRules.getId(),
                headerRules.getDirection(),
                headerRules.getAction(),
                headerRules.getHeaderName(),
                headerRules.getHeaderValue()
        );
    }

}
