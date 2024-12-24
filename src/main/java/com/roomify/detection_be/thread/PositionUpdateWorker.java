package com.roomify.detection_be.thread;

import com.roomify.detection_be.web.entity.Position;
import java.util.Map;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PositionUpdateWorker {
  private final PositionUpdateQueue positionUpdateQueue;
  private final SimpMessagingTemplate messagingTemplate;

  public PositionUpdateWorker(
      PositionUpdateQueue positionUpdateQueue, SimpMessagingTemplate messagingTemplate) {
    this.positionUpdateQueue = positionUpdateQueue;
    this.messagingTemplate = messagingTemplate;
  }

  @Scheduled(fixedRate = 100) // Every 100ms
  public void processQueue() {
    Map<String, Position> batch = positionUpdateQueue.getAndClearBatch();

    if (!batch.isEmpty()) {
      messagingTemplate.convertAndSend("/topic/positions", batch);
    }
  }
}
