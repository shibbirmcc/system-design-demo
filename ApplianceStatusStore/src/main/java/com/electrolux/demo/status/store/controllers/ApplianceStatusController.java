package com.electrolux.demo.status.store.controllers;

import com.electrolux.demo.status.store.dto.ApplianceDetail;
import com.electrolux.demo.status.store.models.Appliance;
import com.electrolux.demo.status.store.models.HeartbeatLog;
import com.electrolux.demo.status.store.services.ApplianceService;
import com.electrolux.demo.status.store.services.HeartBeatLogService;
import java.time.Instant;
import java.util.Optional;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class ApplianceStatusController {

  private static final Logger logger = LoggerFactory.getLogger(ApplianceStatusController.class);

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
    Optional<Appliance> appliance = applianceService.getByApplianceId(applianceId);
    if (appliance.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Appliance Not Found");
    }
    heartBeatLogService.save(new HeartbeatLog(appliance.get(), Instant.now()));
  }

  @GetMapping(value = "/appliances")
  @Produces(MediaType.APPLICATION_JSON_VALUE)
  public Page<ApplianceDetail> getApplianceDetails(
      @DefaultValue("0") @QueryParam("page") Integer page,
      @DefaultValue("25") @QueryParam("size") Integer size,
      @DefaultValue("id") @QueryParam("sortBy") String sortBy,
      @DefaultValue("ASC") @QueryParam("sortDirection") Direction sortDirection) {
    Pageable pageRequest = PageRequest.of(page, size, sortDirection, sortBy);
    return applianceService.getApplianceDetails(pageRequest);
  }

}
