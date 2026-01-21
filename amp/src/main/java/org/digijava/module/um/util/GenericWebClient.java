package org.digijava.module.um.util;


import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

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
                    
                    // Extract token and refreshToken from cookies if this is a TruLoginResponse
                    if (responseBody != null 
                            && responseBody instanceof org.digijava.module.um.model.TruLoginResponse) {
                        org.digijava.module.trubudget.util.TruBudgetAuthUtil.extractTokensFromCookies(
                            (org.digijava.module.um.model.TruLoginResponse) responseBody, headers);
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
        // TruBudget API doesn't support refresh tokens, so we need to re-login
        logger.info("Token expired, attempting to re-login to TruBudget");
        return org.digijava.module.trubudget.util.TruBudgetAuthUtil.reLoginToTruBudget();
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
        logger.info("Making get for single object: " + url);
        return executeGetWithTokenRefresh(url, responseClass, token, 0);
    }
    
    private static <V> Mono<V> executeGetWithTokenRefresh(String url, Class<V> responseClass, String[] token, int retryCount) throws URISyntaxException {
        return myWebClient().get()
                .uri(new URI(url))
                .headers(httpHeaders ->{
                    if (token.length>=1 && token[0] != null)
                    {
                        httpHeaders.setBearerAuth(token[0]);
                    }
                })
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
                    
                    // Extract token and refreshToken from cookies if this is a TruLoginResponse
                    if (responseBody != null 
                            && responseBody instanceof org.digijava.module.um.model.TruLoginResponse) {
                        org.digijava.module.trubudget.util.TruBudgetAuthUtil.extractTokensFromCookies(
                            (org.digijava.module.um.model.TruLoginResponse) responseBody, headers);
                    }
                    return responseBody;
                })
                .onErrorResume(UnauthorizedException.class, e -> {
                    // Handle 401 Unauthorized - try to refresh token if conditions are met
                    if (retryCount == 0 && isTruBudgetUrl(url) && token.length > 0) {
                        logger.info("Received 401 Unauthorized, attempting to refresh token");
                        return attemptTokenRefresh(url, null, null, responseClass, token)
                                .flatMap(newToken -> {
                                    // Retry the original request with new token
                                    try {
                                        String[] newTokenArray = {newToken};
                                        return executeGetWithTokenRefresh(url, responseClass, newTokenArray, 1);
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