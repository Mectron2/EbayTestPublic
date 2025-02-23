package com.example.ebaytest2;

import com.example.ebaytest2.service.ApplicationToken;
import com.example.ebaytest2.service.UserToken;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@AllArgsConstructor
public class EbayTest2Application implements CommandLineRunner {

    private final ApplicationToken applicationToken;
    private final UserToken userToken;

    public static void main(String[] args) {
        SpringApplication.run(EbayTest2Application.class, args);
    }

    @Override
    public void run(String... args) {
        System.out.println(applicationToken.getToken().getAccessToken());
        System.out.println(userToken.getUserToken(userToken.getAgreementToken()).getAccessToken());
    }
}
