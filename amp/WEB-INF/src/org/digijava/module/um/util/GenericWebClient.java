package org.digijava.module.um.util;


import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import io.netty.resolver.DefaultAddressResolverGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.util.retry.Retry;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

/**
 * This class provides a generic template for making POST and GET request
 */
public class GenericWebClient {

    private static final long TIMEOUT = 50000;
    private static final int CONNECT_TIMEOUT = 50000;

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
        return myWebClient().post()
                .uri(new URI(url))
                .headers(httpHeaders ->{
                    if (token.length>=1)
                    {
                        httpHeaders.setBearerAuth(token[0]);
                    }
                })
                .body(Mono.just(request), requestClass)
                .retrieve()
                .onStatus(HttpStatus::is5xxServerError, response ->
                        response.bodyToMono(String.class)
                                .flatMap(body -> Mono.error(new RuntimeException("Internal server error occurred. Response: " + body))))
                .onStatus(HttpStatus::is4xxClientError, response ->
                        response.bodyToMono(String.class)
                                .flatMap(body -> Mono.error(new RuntimeException("Bad Request Error. Response: " + body))))
                .bodyToMono(responseClass)
                .doOnError(Throwable::printStackTrace)
                .retryWhen(Retry.backoff(3, Duration.of(2, ChronoUnit.SECONDS))
                        .onRetryExhaustedThrow(((retryBackoffSpec, retrySignal) -> new RuntimeException(retrySignal.failure()))));


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
    @SafeVarargs
    public  static<V, E extends Exception> Mono<V> getForSingleObjResponse(String url, Class<V> responseClass, E... exceptions) throws URISyntaxException {
        return myWebClient().get()
                .uri(new URI(url))
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
