package com.electrolux.demo.status.store.models;

import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "heartbeat_logs")
public class HeartbeatLog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false, length = 100)
  private String id;

  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "applianceId", nullable = false, referencedColumnName = "applianceId")
  private Appliance appliance;

  @Column(name = "heartbeatReceivedAt")
  private Instant heartbeatReceivedAt;

  public HeartbeatLog() {
  }

  public HeartbeatLog(String id, Appliance appliance, Instant heartbeatReceivedAt) {
    this.id = id;
    this.appliance = appliance;
    this.heartbeatReceivedAt = heartbeatReceivedAt;
  }

  public HeartbeatLog(Appliance appliance, Instant heartbeatReceivedAt) {
    this.appliance = appliance;
    this.heartbeatReceivedAt = heartbeatReceivedAt;
  }

  public Instant getHeartbeatReceivedAt() {
    return heartbeatReceivedAt;
  }

  public void setHeartbeatReceivedAt(Instant heartbeatReceivedAt) {
    this.heartbeatReceivedAt = heartbeatReceivedAt;
  }

  public Appliance getAppliance() {
    return appliance;
  }

  public void setAppliance(Appliance appliance) {
    this.appliance = appliance;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
}