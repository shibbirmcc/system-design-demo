package com.electrolux.demo.status.store;

import static org.assertj.core.api.Assertions.assertThat;

import com.electrolux.demo.status.store.dto.ApplianceDetail;
import com.electrolux.demo.status.store.dto.ApplianceStatus;
import com.electrolux.demo.status.store.models.Appliance;
import com.electrolux.demo.status.store.models.Customer;
import com.electrolux.demo.status.store.services.ApplianceService;
import com.electrolux.demo.status.store.services.CustomerService;
import java.time.Duration;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
class ApplicationTests {

  private TestRestTemplate testRestTemplate = new TestRestTemplate();

  @Autowired
  private ApplianceService applianceService;

  @Autowired
  private CustomerService customerService;

  private Customer customer;
  private Appliance appliance;

  @BeforeEach
  public void setup() {
    customer = customerService.save(
        new Customer(TestDataConstants.CUSTOMER_NAME_1, TestDataConstants.CUSTOMER_ADDRESS_1));
    appliance = applianceService.save(new Appliance(customer, TestDataConstants.APPLIANCE_ID_1,
        TestDataConstants.FACTORY_NO_1));
  }

  @AfterEach
  public void tearDown() {
    applianceService.delete(appliance);
    customerService.delete(customer);
  }

  @Test
  public void testPingFailure() throws Exception {
    ResponseEntity<String> response = testRestTemplate.getForEntity(
        "http://localhost:8080/ping/" + TestDataConstants.APPLIANCE_ID_1 + "xx",
        String.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  public void testPingSuccess() throws Exception {
    ResponseEntity<String> response = testRestTemplate.getForEntity(
        "http://localhost:8080/ping/" + TestDataConstants.APPLIANCE_ID_1,
        String.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  public void testApplianceDetailsList() throws Exception {
    ResponseEntity<RestResponsePage<ApplianceDetail>> response = testRestTemplate.exchange(
        "http://localhost:8080/appliances?page=0&size=25&sortBy=id&sortDirection=ASC",
        HttpMethod.GET,
        null, new ParameterizedTypeReference<RestResponsePage<ApplianceDetail>>() {
        });
    assertThat(HttpStatus.OK).isEqualTo(response.getStatusCode());
    assertThat(1).isEqualTo(response.getBody().getTotalElements());
    assertThat(ApplianceStatus.DISCONNECTED).isEqualTo(
        response.getBody().getContent().get(0).getApplianceStatus());

    pingAppliance(TestDataConstants.APPLIANCE_ID_1);

    Awaitility.await().atMost(Duration.ofSeconds(2))
        .until(
            () -> hasApplianceStatusChangedToConnectedAfterPing(TestDataConstants.APPLIANCE_ID_1));
  }

  private void pingAppliance(String applianceId) {
    ResponseEntity<String> pingResponse = testRestTemplate.getForEntity(
        "http://localhost:8080/ping/" + applianceId,
        String.class);
    assertThat(HttpStatus.OK).isEqualTo(pingResponse.getStatusCode());
  }

  private boolean hasApplianceStatusChangedToConnectedAfterPing(String applianceId) {
    ApplianceDetail targetApplianceDetail = getApplianceDetails(applianceId);
    return targetApplianceDetail == null ? false
        : ApplianceStatus.CONNECTED.equals(targetApplianceDetail.getApplianceStatus());
  }

  private ApplianceDetail getApplianceDetails(String applianceId) {
    ResponseEntity<RestResponsePage<ApplianceDetail>> response = testRestTemplate.exchange(
        "http://localhost:8080/appliances?page=0&size=25&sortBy=id&sortDirection=ASC",
        HttpMethod.GET,
        null, new ParameterizedTypeReference<RestResponsePage<ApplianceDetail>>() {
        });
    return response.getBody().getContent().stream()
        .filter(details -> applianceId.equals(details.getApplianceId())).findAny().orElse(null);
  }

}
