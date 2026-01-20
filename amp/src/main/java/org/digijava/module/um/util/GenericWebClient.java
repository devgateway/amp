package org.digijava.module.um.util;


import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.digijava.kernel.cache.AbstractCache;
import org.digijava.kernel.cache.ehcache.EhCacheWrapper;
import org.digijava.module.aim.dbentity.AmpGlobalSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.util.retry.Retry;

/**
 * This class provides a generic template for making POST and GET request
 */
public class GenericWebClient {

    private static final long TIMEOUT = 100000;
    private static final int CONNECT_TIMEOUT = 100000;

    private static  final Logger logger = LoggerFactory.getLogger(GenericWebClient.class);
    /**
     *
     * @param url - String endpoint
     * @param request -Object of type T
     * @param requestClass classType of T in the form T.class
     * @param responseClass classType of V in the form V.class
     * @return
     * @param <T>
     * @param <V>
     * @throws URISyntaxException
     * toDo: Define custom exceptions
     * NOTE: Custom Exceptions must in order of 4xx to 5xx
     * String...  -> Array of tokens for auth
     */

    public  static<T,V> Mono<V> postForSingleObjResponse(String url, T request, Class<T> requestClass, Class<V> responseClass, String... token) throws URISyntaxException {
        logger.info("Making post for single object: "+request);
        return executeWithTokenRefresh(url, request, requestClass, responseClass, token, 0);
    }
    
    private static <T, V> Mono<V> executeWithTokenRefresh(String url, T request, Class<T> requestClass, Class<V> responseClass, String[] token, int retryCount) throws URISyntaxException {
        return myWebClient().post()
                .uri(new URI(url))
                .headers(httpHeaders ->{
                    if (token.length>=1 && token[0] != null)
                    {
                        httpHeaders.setBearerAuth(token[0]);
                    }
                })
                .body(Mono.just(request), requestClass)
                .retrieve()
                .onStatus(HttpStatus::is5xxServerError, response ->
                        response.bodyToMono(String.class)
                                .flatMap(body -> Mono.error(new RuntimeException("Internal server error occurred. Response: " + body))))
                .onStatus(status -> status == HttpStatus.UNAUTHORIZED, response ->
                        response.bodyToMono(String.class)
                                .flatMap(body -> Mono.error(new UnauthorizedException("Unauthorized: " + body))))
                .onStatus(HttpStatus::is4xxClientError, response ->
                        response.bodyToMono(String.class)
                                .flatMap(body -> Mono.error(new RuntimeException("Bad Request Error. Response: " + body))))
                .toEntity(responseClass)
                .map(responseEntity -> {
                    V responseBody = responseEntity.getBody();
                    HttpHeaders headers = responseEntity.getHeaders();
                    List<String> cookies = headers.get(HttpHeaders.SET_COOKIE);
                    
                    // Extract token and refreshToken from cookies if present
                    if (cookies != null && responseBody != null 
                            && responseBody instanceof org.digijava.module.um.model.TruLoginResponse) {
                        org.digijava.module.um.model.TruLoginResponse truResponse = 
                            (org.digijava.module.um.model.TruLoginResponse) responseBody;
                        if (truResponse.getData() != null && truResponse.getData().getUser() != null) {
                            for (String cookie : cookies) {
                                // Look for token in cookie (common patterns: "token=...", "auth-token=...", etc.)
                                // Exclude refreshToken to avoid matching it here
                                if (cookie.contains("token=") && !cookie.contains("refreshToken=")) {
                                    String tokenValue = extractTokenFromCookie(cookie, "token");
                                    if (tokenValue != null) {
                                        truResponse.getData().getUser().setToken(tokenValue);
                                        logger.info("Token extracted from cookie and set in response");
                                    }
                                }
                                // Look for refreshToken in cookie
                                if (cookie.contains("refreshToken=")) {
                                    String refreshTokenValue = extractTokenFromCookie(cookie, "refreshToken");
                                    if (refreshTokenValue != null) {
                                        truResponse.getData().getUser().setRefreshToken(refreshTokenValue);
                                        logger.info("RefreshToken extracted from cookie and set in response");
                                    }
                                }
                            }
                        }
                    }
                    return responseBody;
                })
                .onErrorResume(UnauthorizedException.class, e -> {
                    // Handle 401 Unauthorized - try to refresh token if conditions are met
                    if (retryCount == 0 && isTruBudgetUrl(url) && token.length > 0) {
                        logger.info("Received 401 Unauthorized, attempting to refresh token");
                        return attemptTokenRefresh(url, request, requestClass, responseClass, token)
                                .flatMap(newToken -> {
                                    // Retry the original request with new token
                                    try {
                                        String[] newTokenArray = {newToken};
                                        return executeWithTokenRefresh(url, request, requestClass, responseClass, newTokenArray, 1);
                                    } catch (URISyntaxException ex) {
                                        return Mono.error(new RuntimeException("Failed to retry request after token refresh", ex));
                                    }
                                })
                                .switchIfEmpty(Mono.error(new RuntimeException("Unauthorized: Token refresh failed or no refresh token available")));
                    }
                    return Mono.error(e);
                })
                .doOnError(Throwable::printStackTrace)
                .retryWhen(Retry.backoff(3, Duration.of(2, ChronoUnit.SECONDS))
                        .onRetryExhaustedThrow(((retryBackoffSpec, retrySignal) -> new RuntimeException(retrySignal.failure()))));
    }
    
    private static class UnauthorizedException extends RuntimeException {
        public UnauthorizedException(String message) {
            super(message);
        }
    }
    
    private static boolean isTruBudgetUrl(String url) {
        return url != null && url.contains("api/");
    }
    
    private static <T, V> Mono<String> attemptTokenRefresh(String originalUrl, T originalRequest, Class<T> requestClass, Class<V> responseClass, String[] oldToken) {
        try {
            AbstractCache cache = new EhCacheWrapper("trubudget");
            String refreshToken = (String) cache.get("truBudgetRefreshToken");
            String userId = (String) cache.get("truBudgetUser");
            
            if (refreshToken == null || userId == null) {
                logger.warn("Cannot refresh token: refreshToken or userId not found in cache");
                return Mono.empty();
            }
            
            // Get settings from cache or retrieve them
            List<AmpGlobalSettings> settings = 
                DbUtil.getGlobalSettingsBySection("trubudget");
            
            return DbUtil.refreshTruBudgetToken(userId, refreshToken, settings)
                    .doOnSuccess(truLoginResponse -> {
                        // Update cache with new tokens
                        if (truLoginResponse.getData() != null && truLoginResponse.getData().getUser() != null) {
                            String newToken = truLoginResponse.getData().getUser().getToken();
                            String newRefreshToken = truLoginResponse.getData().getUser().getRefreshToken();
                            
                            cache.put("truBudgetToken", newToken);
                            if (newRefreshToken != null) {
                                cache.put("truBudgetRefreshToken", newRefreshToken);
                            }
                            logger.info("Token refreshed successfully and cached");
                        }
                    })
                    .map(truLoginResponse -> {
                        if (truLoginResponse.getData() != null && truLoginResponse.getData().getUser() != null) {
                            return truLoginResponse.getData().getUser().getToken();
                        }
                        return null;
                    })
                    .onErrorResume(e -> {
                        logger.error("Failed to refresh token: " + e.getMessage(), e);
                        return Mono.empty();
                    });
        } catch (Exception e) {
            logger.error("Error attempting token refresh: " + e.getMessage(), e);
            return Mono.empty();
        }
    }
    
    private static String extractTokenFromCookie(String cookie, String cookieName) {
        // Extract token value from cookie string
        // Format: "token=value; Path=/; HttpOnly" or similar
        if (cookie == null || cookie.isEmpty()) {
            return null;
        }
        
        // Look for the specified cookie name
        int tokenStart = cookie.indexOf(cookieName + "=");
        if (tokenStart == -1 && "token".equals(cookieName)) {
            // Try other common cookie names for token
            tokenStart = cookie.indexOf("auth-token=");
            if (tokenStart == -1) {
                tokenStart = cookie.indexOf("authToken=");
            }
        }
        
        if (tokenStart != -1) {
            int valueStart = cookie.indexOf("=", tokenStart) + 1;
            int valueEnd = cookie.indexOf(";", valueStart);
            if (valueEnd == -1) {
                valueEnd = cookie.length();
            }
            return cookie.substring(valueStart, valueEnd).trim();
        }
        
        return null;
    }

    /**
     *
     * @param url - String endpoint
     * @param request -Object of type T
     * @param requestClass classType of T in the form T.class
     * @param responseClass classType of V in the form V.class
     * @return
     * @param <T>
     * @param <V>
     * @throws URISyntaxException
     */
    @SafeVarargs
    public  static<T ,V, E extends Exception> Flux<V> postForCollectionResponse(String url, T request, Class<T> requestClass, Class<V> responseClass, E... exceptions) throws URISyntaxException {

        return myWebClient().post()
                .uri(new URI(url))
                .body(Mono.just(request),requestClass)
                .retrieve()
                .onStatus(HttpStatus::is5xxServerError, response ->
                        response.bodyToMono(String.class)
                                .flatMap(body -> Mono.error(new RuntimeException("Internal server error occurred. Response: " + body))))
                .onStatus(HttpStatus::is4xxClientError, response ->
                        response.bodyToMono(String.class)
                                .flatMap(body -> Mono.error(new RuntimeException("Bad Request Error. Response: " + body))))
                .bodyToFlux(responseClass);

    }

    /**
     *
     * @param url - String endpoint
     * @param responseClass classType of V in the form V.class
     * @return
     * @param <V>
     * @throws URISyntaxException
     */
    @SafeVarargs
    public  static<V, E extends Exception> Flux<V> getForCollectionResponse(String url, Class<V> responseClass, E... exceptions) throws URISyntaxException {
        return myWebClient().get()
                .uri(new URI(url))
                .retrieve()
                .onStatus(HttpStatus::is5xxServerError, response ->
                        response.bodyToMono(String.class)
                                .flatMap(body -> Mono.error(new RuntimeException("Internal server error occurred. Response: " + body))))
                .onStatus(HttpStatus::is4xxClientError, response ->
                        response.bodyToMono(String.class)
                                .flatMap(body -> Mono.error(new RuntimeException("Bad Request Error. Response: " + body))))
                .bodyToFlux(responseClass);

    }

    /**
     *
     * @param url - String endpoint
     * @param responseClass classType of V in the form V.class
     * @return
     * @param <V>
     * @throws URISyntaxException
     */
    public  static<V> Mono<V> getForSingleObjResponse(String url, Class<V> responseClass, String... token) throws URISyntaxException {
        return myWebClient().get()
                .uri(new URI(url))
                .headers(httpHeaders ->{
                    if (token.length>=1)
                    {
                        httpHeaders.setBearerAuth(token[0]);
                    }
                })
                .retrieve()

                .onStatus(HttpStatus::is5xxServerError, response ->
                        response.bodyToMono(String.class)
                                .flatMap(body -> Mono.error(new RuntimeException("Internal server error occurred. Response: " + body))))
                .onStatus(HttpStatus::is4xxClientError, response ->
                        response.bodyToMono(String.class)
                                .flatMap(body -> Mono.error(new RuntimeException("Bad Request Error. Response: " + body))))
                .bodyToMono(responseClass);

    }

    private static WebClient myWebClient() {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, CONNECT_TIMEOUT)
                .responseTimeout(Duration.ofMillis(TIMEOUT))
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(TIMEOUT, TimeUnit.MILLISECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(TIMEOUT, TimeUnit.MILLISECONDS)));

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();



    }



}