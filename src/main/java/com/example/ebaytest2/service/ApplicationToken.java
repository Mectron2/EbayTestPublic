package com.example.ebaytest2.service;

import com.example.ebaytest2.config.EbayConfig;
import com.example.ebaytest2.dto.EbayAuthResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ApplicationToken {
    private final WebClient webClient;
    private final EbayConfig ebayConfig;

    ApplicationToken(EbayConfig ebayConfig) {
        this.ebayConfig = ebayConfig;
        this.webClient = WebClient.builder()
                .baseUrl(ebayConfig.getBaseUrl())
                .defaultHeaders(headers -> {
                    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                    headers.setBasicAuth(ebayConfig.getClientId(), ebayConfig.getClientSecret());
                })
                .build();
    }

    public EbayAuthResponse getToken() {
        String requestBody = "grant_type=client_credentials" + "&" +
                "scope=" + ebayConfig.getScopes();

        return webClient.post().bodyValue(requestBody).retrieve().bodyToMono(EbayAuthResponse.class).block();
    }
}
