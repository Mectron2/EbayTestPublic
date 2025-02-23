package com.example.ebaytest2.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "ebay")
public class EbayConfig {
    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String loginUrl;
    private String login;
    private String password;
    private String baseUrl;
    private String scopes;
}
