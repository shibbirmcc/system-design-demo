package com.electrolux.demo.status.store.models;

import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "appliances")
public class Appliance {

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "customerId", nullable = false)
  private Customer customer;

  @Id
  @Column(name = "applianceId", nullable = false, length = 100)
  private String applianceId;

  @Column(name = "factoryNr", nullable = false, length = 100)
  private String factoryNr;

  @Column(name = "lastHeartBeatReceiveTime")
  private Instant lastHeartBeatReceiveTime;

  public Appliance(){

  }

  public Appliance(Customer customer, String applianceId, String factoryNr,
      Instant lastHeartBeatReceiveTime) {
    this.customer = customer;
    this.applianceId = applianceId;
    this.factoryNr = factoryNr;
    this.lastHeartBeatReceiveTime = lastHeartBeatReceiveTime;
  }

  public Appliance(Customer customer, String applianceId, String factoryNr) {
    this.customer = customer;
    this.applianceId = applianceId;
    this.factoryNr = factoryNr;
  }

  public Instant getLastHeartBeatReceiveTime() {
    return lastHeartBeatReceiveTime;
  }

  public void setLastHeartBeatReceiveTime(Instant lastHeartBeatReceiveTime) {
    this.lastHeartBeatReceiveTime = lastHeartBeatReceiveTime;
  }

  public String getFactoryNr() {
    return factoryNr;
  }

  public void setFactoryNr(String factoryNr) {
    this.factoryNr = factoryNr;
  }

  public String getApplianceId() {
    return applianceId;
  }

  public void setApplianceId(String applianceId) {
    this.applianceId = applianceId;
  }

  public Customer getCustomer() {
    return customer;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }
}