package com.electrolux.demo.status.store.repositories;

import com.electrolux.demo.status.store.models.HeartbeatLog;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HeartbeatLogRepository extends JpaRepository<HeartbeatLog, String> {
  List<HeartbeatLog> findFirst25ByOrderByHeartbeatReceivedAt();
}
