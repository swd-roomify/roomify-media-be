package com.roomify.detection_be.utility;

import java.util.concurrent.atomic.AtomicInteger;

public class SnowflakeGenerator {
    private final long epoch = 1609459200000L;
    private final int nodeId;
    private final AtomicInteger sequence = new AtomicInteger(0);
    private final int nodeIdBits = 10;
    private final int sequenceBits = 12;
    private final int maxNodeId = (1 << nodeIdBits) - 1;
    private final int maxSequence = (1 << sequenceBits) - 1;

    private long lastTimestamp = -1L;

    public SnowflakeGenerator(int nodeId) {
        if (nodeId < 0 || nodeId > maxNodeId) {
            throw new IllegalArgumentException("Node Is must be from 0 to " + maxNodeId);
        }
        this.nodeId = nodeId;
    }

    public synchronized long nextId() {
        long timestamp = currentTimestamp();
        if (timestamp < lastTimestamp) {
            throw new IllegalStateException("Clock is moving backwards, illegal.");
        }

        if (timestamp == lastTimestamp) {
            int seq = sequence.incrementAndGet() & maxSequence;
            if (seq == 0) {
                timestamp = waitNextMillis(lastTimestamp);
            }
        } else {
            sequence.set(0);
        }

        lastTimestamp = timestamp;

        return ((timestamp - epoch) << (nodeIdBits + sequenceBits))
                | ((long) nodeId << sequenceBits)
                | sequence.get();
    }

    private long currentTimestamp() {
        return System.currentTimeMillis();
    }

    private long waitNextMillis(long lastTimestamp) {
        long timestamp = currentTimestamp();
        while (timestamp <= lastTimestamp) {
            timestamp = currentTimestamp();
        }
        return timestamp;
    }
}
