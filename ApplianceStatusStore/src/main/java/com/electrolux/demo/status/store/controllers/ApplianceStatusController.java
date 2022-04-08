package com.electrolux.demo.status.store.controllers;

import com.electrolux.demo.status.store.dto.ApplianceDetail;
import com.electrolux.demo.status.store.models.Appliance;
import com.electrolux.demo.status.store.models.HeartbeatLog;
import com.electrolux.demo.status.store.services.ApplianceService;
import com.electrolux.demo.status.store.services.HeartBeatLogService;
import java.time.Instant;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApplianceStatusController {

  private ApplianceService applianceService;
  private HeartBeatLogService heartBeatLogService;

  @Autowired
  public ApplianceStatusController(ApplianceService applianceService,
      HeartBeatLogService heartBeatLogService) {
    this.applianceService = applianceService;
    this.heartBeatLogService = heartBeatLogService;
  }

  @GetMapping("/ping/{applianceId}")
  public void ping(@PathVariable String applianceId) {
    Appliance appliance = applianceService.getByApplianceId(applianceId).get();
    heartBeatLogService.save(new HeartbeatLog(appliance, Instant.now()));
  }

  @GetMapping("/appliances")
  public Page<ApplianceDetail> getApplianceDetails(
      @DefaultValue("0") @QueryParam("page") Integer page,
      @DefaultValue("25") @QueryParam("size") Integer size,
      @DefaultValue("id") @QueryParam("sortBy") String sortBy,
      @DefaultValue("ASC") @QueryParam("sortDirection") Direction sortDirection) {
    return applianceService.getApplianceDetails(PageRequest.of(page, size, sortDirection, sortBy));
  }

}
