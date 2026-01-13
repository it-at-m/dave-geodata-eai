package de.muenchen.dave.geodataeai.configuration;

import de.muenchen.mobidam.eai.gen.api.MqMesswerteControllerApi;
import de.muenchen.mobidam.eai.gen.messwerte.ApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class MobidamEaiApiConfiguration {

    @Value("${mobidam.messwerte.eai.url}")
    public String mobidamMesswerteEaiUrl;

    @Value("${spring.codec.max-in-memory-size}")
    public int maxInMemorySizeBytes;

    @Bean
    @Profile("no-security")
    public MqMesswerteControllerApi mqMesswerteControllerApi() {
        final var webClient = WebClient.builder()
                .codecs(codecs -> {
                    codecs.defaultCodecs().maxInMemorySize(maxInMemorySizeBytes);
                    codecs.defaultCodecs().enableLoggingRequestDetails(false);
                })
                .build();
        final var apiClient = this.mobidamEaiApiClient(webClient);
        return new MqMesswerteControllerApi(apiClient);
    }

    @Bean
    @Profile("!no-security")
    public MqMesswerteControllerApi securedMqMesswerteControllerApi(
            final ClientRegistrationRepository clientRegistrationRepository,
            final OAuth2AuthorizedClientService authorizedClientService) {
        final var webClient = this.webClient(
                clientRegistrationRepository,
                authorizedClientService,
                "sso-mobidam-messwerte");
        final var apiClient = this.mobidamEaiApiClient(webClient);
        return new MqMesswerteControllerApi(apiClient);
    }

    private ApiClient mobidamEaiApiClient(final WebClient webClient) {
        final var apiClient = new ApiClient(webClient);
        apiClient.setBasePath(mobidamMesswerteEaiUrl);
        return apiClient;
    }

    private WebClient webClient(
            final ClientRegistrationRepository clientRegistrationRepository,
            final OAuth2AuthorizedClientService authorizedClientService,
            final String clientRegistrationId) {
        final var oauth = new ServletOAuth2AuthorizedClientExchangeFilterFunction(
                new AuthorizedClientServiceOAuth2AuthorizedClientManager(
                        clientRegistrationRepository, authorizedClientService));
        oauth.setDefaultClientRegistrationId(clientRegistrationId);
        return WebClient.builder()
                .codecs(codecs -> {
                    codecs.defaultCodecs().maxInMemorySize(maxInMemorySizeBytes);
                    codecs.defaultCodecs().enableLoggingRequestDetails(false);
                })
                .apply(oauth.oauth2Configuration())
                .build();
    }
}
