package com.roomify.detection_be.thread;

import com.roomify.detection_be.web.entity.Position;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class PositionUpdateQueue {
  private final Map<String, Position> latestPositions = new ConcurrentHashMap<>();

  public void addUpdate(String username, Position position) {
    latestPositions.put(username, position);
  }

  public Map<String, Position> getAndClearBatch() {
    Map<String, Position> batch = new ConcurrentHashMap<>(latestPositions);
    latestPositions.clear();
    return batch;
  }

  public boolean isEmpty() {
    return latestPositions.isEmpty();
  }
}
