package com.skybridge.common.config;

import io.netty.channel.ChannelOption;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Slf4j
@Configuration
public class WebClientConfig {

    @Value("${api.url.base}")
    private String apiBaseUrl;

    @Bean
    public WebClient webClient() {

        /**
         * 통신시 timeout 세팅
         * - connect, read, write 를 모두 5000ms
         */
/*        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .responseTimeout(Duration.ofMillis(5000))
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS)));*/
        // timeout 설정
        ReactorClientHttpConnector httpConnector = new ReactorClientHttpConnector(
                HttpClient.create()
                        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1200000)
                        .responseTimeout(Duration.ofSeconds(1200)));

        return WebClient.builder()
                .baseUrl(apiBaseUrl)
                .clientConnector(httpConnector) //생성한 HttpClient 연결
                //Request Header 로깅 필터
                .filter(
                        ExchangeFilterFunction.ofRequestProcessor(
                                clientRequest -> {
                                    log.info(">>>>>>>>> REQUEST <<<<<<<<<<");
                                    log.info("Request: {} {}", clientRequest.method(), clientRequest.url());
                                    clientRequest.headers().forEach(
                                            (name, values) -> values.forEach(value -> log.info("{} : {}", name, value))
                                    );
                                    return Mono.just(clientRequest);
                                }
                        )
                )
                //Response Header 로깅 필터
                .filter(
                        ExchangeFilterFunction.ofResponseProcessor(
                                clientResponse -> {
                                    log.info(">>>>>>>>>> RESPONSE <<<<<<<<<<");
                                    clientResponse.headers().asHttpHeaders().forEach(
                                            (name, values) -> values.forEach(value -> log.info("{} {}", name, value))
                                    );
                                    return Mono.just(clientResponse);
                                }
                        )
                )
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
