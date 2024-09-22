package com.example.demo;

import java.net.URI;
import java.util.Base64;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
@RestController

class UrlController {

    @Autowired
    private UrlMappingRepository urlMappingRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserUrlRepository userUrlRepository;


    @GetMapping("/retrieve")
    public ResponseEntity<List<UrlDto>> retrieveUrls(@RequestHeader("Authorization") String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7).trim();
        }

        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Long userId = jwtUtil.getUserIdFromToken(token);
        List<UrlDto> urlMappings = userUrlRepository.findByUserid(userId);
        return ResponseEntity.ok(urlMappings)
        ;
    }

    @PostMapping("/shorten")
public String shortenUrl(@RequestBody String originalUrl,
                        @RequestHeader("Authorization") String token,
                        @RequestHeader("Is-Authenticated") String isAuthenticatedHeader) {

    boolean isAuthenticated = Boolean.parseBoolean(isAuthenticatedHeader);
    
    
    if (!isAuthenticated) {
        String existingMapping = urlMappingRepository.findByOriginalUrl(originalUrl);
        
        if (existingMapping != null) {
            return existingMapping;
        }
        
        String shortUrl = generateShortUrl();
        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setOriginalUrl(originalUrl);
        urlMapping.setShortUrl(shortUrl);
        urlMappingRepository.save(urlMapping);
        return shortUrl;
    }


    if (token.startsWith("Bearer ")) {
        token = token.substring(7).trim();
    }

    if (!jwtUtil.validateToken(token)) {
        throw new RuntimeException("Invalid or expired JWT token");
    }
    Long userId = null;
    userId = jwtUtil.getUserIdFromToken(token);

    String existingMapping = urlMappingRepository.findByOriginalUrl(originalUrl);
    Long urlId = null;
    if (existingMapping != null) {
        
        urlId = urlMappingRepository.findIdByOriginalUrl(originalUrl);
        //String record = urlMappingRepository.findByUseridAndOriginalUrl(userId, originalUrl);
        Long urlrecord = userUrlRepository.findUrlId(userId,urlId);

        if (urlrecord != null) {
            return existingMapping;
        } else {
            UrlMapping urlMapping = new UrlMapping();
            urlMapping.setOriginalUrl(originalUrl);
            urlMapping.setShortUrl(existingMapping);
            //urlMapping.setUserid(userId);
            //urlMappingRepository.save(urlMapping);

            UserUrl userurl = new UserUrl();
            userurl.setUserId(userId);
            userurl.setUrlId(urlId);
            userUrlRepository.save(userurl);


            return existingMapping;
        }
    }


    String shortUrl = generateShortUrl();
    UrlMapping urlMapping = new UrlMapping();
    urlMapping.setOriginalUrl(originalUrl);
    urlMapping.setShortUrl(shortUrl);
    //urlMapping.setUserid(userId);
    urlMappingRepository.save(urlMapping);

    urlId = urlMappingRepository.findIdByOriginalUrl(originalUrl);

    UserUrl userurl = new UserUrl();
    userurl.setUserId(userId);
    userurl.setUrlId(urlId);
    userUrlRepository.save(userurl);


    return shortUrl;
}

    @GetMapping("/{shortUrl}")
    public ResponseEntity<Object> redirectToOriginalUrl(@PathVariable String shortUrl) {
        UrlMapping urlMapping = urlMappingRepository.findByShortUrl(shortUrl);
        if (urlMapping != null) {
            return ResponseEntity.status(HttpStatus.FOUND)
                                .location(URI.create(urlMapping.getOriginalUrl()))
                                .build();
        }
        return ResponseEntity.status(HttpStatus.FOUND)
                        .location(URI.create("http://localhost:3000/error"  ))
                        .build();
        
    }
    private String generateShortUrl() {
        
        byte[] randomBytes = new byte[6];
        new Random().nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    
}
