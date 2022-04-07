package com.electrolux.demo.status.store.services;

import com.electrolux.demo.status.store.models.Appliance;
import java.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ApplianceStatusUpdaterService {

  private HeartBeatLogService heartBeatLogService;
  private ApplianceService applianceService;

  @Autowired
  public ApplianceStatusUpdaterService(
      HeartBeatLogService heartBeatLogService,
      ApplianceService applianceService) {
    this.heartBeatLogService = heartBeatLogService;
    this.applianceService = applianceService;
  }

  @Scheduled(fixedDelayString = "${statusupdater.fixedDelay.in.milliseconds}")
  public void updateApplianceConnectivityStatus() {
    heartBeatLogService.getFirst25HeartBeats().forEach(heartBeat -> {
      heartBeatLogService.delete(heartBeat);
      Appliance appliance = heartBeat.getAppliance();
      Instant time = heartBeat.getHeartbeatReceivedAt();
      appliance.setLastHeartBeatReceiveTime(time);
      applianceService.save(appliance);
    });
  }

}
