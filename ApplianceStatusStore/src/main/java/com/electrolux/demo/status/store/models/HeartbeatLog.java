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
@Table(name = "heartbeat_logs")
public class HeartbeatLog {

  @Id
  @Column(name = "id", nullable = false, length = 100)
  private String id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "applianceId", nullable = false, referencedColumnName = "applianceId")
  private Appliance appliance;

  @Column(name = "heartbeatReceivedAt")
  private Instant heartbeatReceivedAt;

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