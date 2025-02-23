package com.example.ebaytest2.service;

import com.example.ebaytest2.config.EbayConfig;
import com.example.ebaytest2.dto.EbayUserAuthResponse;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

@Service
public class UserToken {
    private final WebClient webClient;
    private final EbayConfig ebayConfig;

    public UserToken(EbayConfig ebayConfig) {
        this.ebayConfig = ebayConfig;
        this.webClient = WebClient.builder()
                .baseUrl(ebayConfig.getBaseUrl())
                .defaultHeaders(headers -> {
                    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                    headers.setBasicAuth(ebayConfig.getClientId(), ebayConfig.getClientSecret());
                })
                .build();;
    }

    public String getAgreementToken() {
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");

        WebDriver driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get((ebayConfig.getLoginUrl()));

        WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@type='text']")
        ));
        usernameField.sendKeys(ebayConfig.getLogin());

        WebElement continueButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(), 'Continue') or @id='signin-continue-btn']")
        ));
        continueButton.click();

        WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@type='password']")
        ));
        passwordField.sendKeys(ebayConfig.getPassword());

        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(), 'Sign in') or @id='sgnBt']")
        ));
        loginButton.click();

        wait.until(ExpectedConditions.urlContains("code"));
        String currentUrl = driver.getCurrentUrl();
        String unquotedUrl = URLDecoder.decode(currentUrl, StandardCharsets.UTF_8);
        return URLDecoder.decode(unquotedUrl.substring(unquotedUrl.indexOf("code=") + 5, unquotedUrl.indexOf("&expires_in")), StandardCharsets.UTF_8);
    }

    public EbayUserAuthResponse getUserToken(String userAgreementToken) {
        String requestBody = "grant_type=authorization_code" + "&" +
                "code=" + userAgreementToken + "&" +
                "redirect_uri=" + ebayConfig.getRedirectUri();

        return webClient.post().bodyValue(requestBody).retrieve().bodyToMono(EbayUserAuthResponse.class).block();
    }
}
