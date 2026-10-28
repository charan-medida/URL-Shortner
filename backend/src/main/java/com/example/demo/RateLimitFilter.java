
package com.example.demo;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private final ConcurrentMap<String, Bucket> buckets =
            new ConcurrentHashMap<>();

    private Bucket createBucket(String path) {
        Bandwidth limit;

        if ("/login".equals(path)) {
            limit = Bandwidth.classic(
                    5, Refill.intervally(5, Duration.ofMinutes(1)));
        } else {
            limit = Bandwidth.classic(
                    60, Refill.intervally(60, Duration.ofMinutes(1)));
        }

        return Bucket.builder().addLimit(limit).build();
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();

        // Keep the AWS load balancer health check unrestricted.
        return "/health".equals(path);
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        // Use the immediate peer address rather than trusting a
        // client-supplied X-Forwarded-For header.
        String clientIp = request.getRemoteAddr();
        String key = clientIp + ":" + pathCategory(path);

        Bucket bucket = buckets.computeIfAbsent(
                key, ignored -> createBucket(path));

        if (bucket.tryConsume(1)) {
            filterChain.doFilter(request, response);
        } else {
            org.slf4j.LoggerFactory.getLogger(RateLimitFilter.class)
            .warn("Rate limit exceeded: IP={}, path={}", clientIp, path);
            response.setStatus(429);
            response.setContentType("application/json");
            response.getWriter().write(
                    "{\"error\":\"Too many requests. Please try again later.\"}");
        }
    }

    private String pathCategory(String path) {
        return "/login".equals(path) ? "login" : "general";
    }
}
