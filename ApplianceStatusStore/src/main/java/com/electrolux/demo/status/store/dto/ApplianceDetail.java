package com.electrolux.demo.status.store.dto;

import com.electrolux.demo.status.store.ApplicationConstants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplianceDetail implements Serializable {

  private static final long serialVersionUID = 1L;
  private String name;
  private String address;
  private String applianceId;
  private String factoryNr;
  private ApplianceStatus applianceStatus;
  @JsonIgnore
  private Instant lastHeartBeatReceiveTime;

  public ApplianceDetail() {
  }

  public ApplianceDetail(String name, String address, String applianceId, String factoryNr,
      Instant lastHeartBeatReceiveTime) {
    this.name = name;
    this.address = address;
    this.applianceId = applianceId;
    this.factoryNr = factoryNr;
    this.lastHeartBeatReceiveTime = lastHeartBeatReceiveTime;

    if (this.lastHeartBeatReceiveTime == null) {
      applianceStatus = ApplianceStatus.DISCONNECTED;
    } else if (Duration.between(this.lastHeartBeatReceiveTime, Instant.now()).abs().toSeconds()
        > ApplicationConstants.ACCEPTED_PING_LAG_IN_SECONDS) {
      applianceStatus = ApplianceStatus.DISCONNECTED;
    } else {
      applianceStatus = ApplianceStatus.CONNECTED;
    }
  }

  public String getName() {
    return name;
  }

  public String getAddress() {
    return address;
  }

  public String getApplianceId() {
    return applianceId;
  }

  public String getFactoryNr() {
    return factoryNr;
  }


  public ApplianceStatus getApplianceStatus() {
    return applianceStatus;
  }
}