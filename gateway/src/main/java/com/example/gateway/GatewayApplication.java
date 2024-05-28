package com.example.gateway;

import com.example.core.route.ModulePort;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.web.client.RestClient;


@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    private final String url = "http://127.0.0.1:";
    private final String userRoute = url + ModulePort.USER.getType().toString();
    private final String msgRoute = url + ModulePort.MSG.getType().toString();

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user_route", r -> r.path("/user/**")
                        .uri(userRoute))
                .route("msg_route", r -> r.path("/msg/**")
                        .uri(msgRoute))
                .build();
    }

    @Bean
    public RestClient.Builder restClientBuilder() {
        return RestClient.builder();
    }

}
