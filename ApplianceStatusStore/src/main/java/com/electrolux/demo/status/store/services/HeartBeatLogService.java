package com.electrolux.demo.status.store.services;

import com.electrolux.demo.status.store.models.HeartbeatLog;
import com.electrolux.demo.status.store.repositories.HeartbeatLogRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HeartBeatLogService {

  private HeartbeatLogRepository heartbeatLogRepository;

  @Autowired
  public HeartBeatLogService(
      HeartbeatLogRepository heartbeatLogRepository) {
    this.heartbeatLogRepository = heartbeatLogRepository;
  }

  @Transactional
  public HeartbeatLog save(HeartbeatLog heartbeatLog) {
    return heartbeatLogRepository.save(heartbeatLog);
  }

  @Transactional(readOnly = true)
  public List<HeartbeatLog> getFirst25HeartBeats() {
    return heartbeatLogRepository.findFirst25ByOrderByHeartbeatReceivedAt();
  }

  @Transactional
  public void delete(HeartbeatLog heartbeatLog) {
    heartbeatLogRepository.delete(heartbeatLog);
  }

}
