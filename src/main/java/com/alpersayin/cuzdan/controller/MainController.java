package com.alpersayin.cuzdan.controller;

import com.alpersayin.cuzdan.entity.AuthenticationResult;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Collections;

@RestController
@RequestMapping("/api")
@Slf4j
public class MainController {

    static final String baseURL = "https://api-dev.btcturk.com/api/v2/ticker?pairSymbol=BTC_TRY";
    static final String basePrivateURL = "https://api-dev.btcturk.com/api/v1/users/balances";
    static final String publicKey = "XXXXX";
    static final String privateKey = "XXXXX";
    private RestTemplate restTemplate = new RestTemplate();

    @Autowired
    public MainController(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public AuthenticationResult getAuthentication()
            throws IOException,
            InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {

        String unixTimeStamp = String.valueOf(System.currentTimeMillis());
        String data = publicKey + unixTimeStamp;

        byte[] secretKey = DatatypeConverter.parseBase64Binary(privateKey);
        SecretKeySpec signingKey = new SecretKeySpec(secretKey, "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);
        byte[] bytes = data.getBytes("UTF-8");
        byte[] rawHmac = mac.doFinal(bytes);
        return new AuthenticationResult(unixTimeStamp, Base64.getEncoder().encodeToString(rawHmac));
    }


    @SneakyThrows
    @GetMapping("/getAssets")
    public ResponseEntity<Object> getPrivateAsset() {
        AuthenticationResult authenticationResult = getAuthentication();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("X-PCK", publicKey);
        headers.set("X-Stamp", authenticationResult.getTimestamp());
        headers.set("X-Signature", authenticationResult.getAuthenticationSignature());
        HttpEntity request = new HttpEntity(headers);
        return restTemplate.exchange(
            basePrivateURL,
            HttpMethod.GET,
            request,
            Object.class
        );
    }

    @GetMapping("/getBTCTRY")
    public ResponseEntity<Object> getPublicDataFromBtcTURK() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity request = new HttpEntity(headers);
        return restTemplate.exchange(
                baseURL,
                HttpMethod.GET,
                request,
                Object.class
        );
    }

    @GetMapping("/getRandomValue")
    public ResponseEntity<String> getValue() {
        String url = "https://jsonplaceholder.typicode.com/posts/1";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity request = new HttpEntity(headers);
        ResponseEntity<String> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            request,
            String.class
        );
        return response;

    }

}
