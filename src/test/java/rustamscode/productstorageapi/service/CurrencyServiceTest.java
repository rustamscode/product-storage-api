package rustamscode.productstorageapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import rustamscode.productstorageapi.motherobject.ObjectMother;
import rustamscode.productstorageapi.service.dto.CurrencyRateDetails;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 0)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CurrencyServiceTest {

  @Autowired
  WebClient.Builder webClientBuilder;

  @Autowired
  ObjectMapper objectMapper;

  @Value("${wiremock.server.port}")
  int wireMockPort;

  WebClient webClient;

  @BeforeEach
  public void setup() {
    webClient = webClientBuilder.baseUrl("http://localhost:" + wireMockPort).build();
  }

  @Test
  public void shouldReturnValidCurrencyRateResponse() throws JsonProcessingException {
    CurrencyRateDetails expected = ObjectMother.currencyRateDetails().build();
    stubFor(get(urlEqualTo("/currency-rate"))
        .willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withBody(objectMapper.writeValueAsString(expected))
        ));

    CurrencyRateDetails actual = webClient.get()
        .uri("/currency-rate")
        .retrieve()
        .bodyToMono(CurrencyRateDetails.class)
        .block();

    Assertions.assertNotNull(actual);
    Assertions.assertEquals(expected.getRUB(), actual.getRUB());
    Assertions.assertEquals(expected.getUSD(), actual.getUSD());
    Assertions.assertEquals(expected.getCNY(), actual.getCNY());
  }

  @Test
  public void shouldThrowExceptionWhenResponseIsInvalid() {
    stubFor(get(urlEqualTo("/currency-rate"))
        .willReturn(aResponse()
            .withStatus(404))
    );

    assertThrows(
        WebClientResponseException.class,
        () -> webClient.get()
            .uri("/currency_rate")
            .retrieve()
            .bodyToMono(CurrencyRateDetails.class)
            .block());
  }

}

