package com.roomify.detection_be.utility;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public class SnowflakeIdGenerator implements IdentifierGenerator {
    private final long epoch = 1609459200000L;
    private final int nodeId;
    private static final int NODE_ID_BITS = 10;
    private static final int SEQUENCE_BITS = 12;
    private static final int MAX_SEQUENCE = (1 << SEQUENCE_BITS) - 1;

    private static long lastTimestamp = -1L;
    private static long sequence = 0L;

    public SnowflakeIdGenerator() {
        // You can get this from configuration or environment
        this.nodeId = 1; // Default node ID
    }

    @Override
    public Object generate(SharedSessionContractImplementor session, Object object)
            throws HibernateException {
        return nextId();
    }

    private synchronized String nextId() {
        long timestamp = currentTimestamp();

        if (timestamp < lastTimestamp) {
            throw new HibernateException("Clock moved backwards. Refusing to generate ID.");
        }

        if (timestamp == lastTimestamp) {
            sequence = (sequence + 1) & MAX_SEQUENCE;
            if (sequence == 0) {
                timestamp = waitNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0;
        }

        lastTimestamp = timestamp;

        long id = ((timestamp - epoch) << (NODE_ID_BITS + SEQUENCE_BITS))
                | ((long) nodeId << SEQUENCE_BITS)
                | sequence;

        return String.valueOf(id);
    }

    private long currentTimestamp() {
        return System.currentTimeMillis();
    }

    private long waitNextMillis(long lastTimestamp) {
        long timestamp;
        do {
            timestamp = currentTimestamp();
        } while (timestamp <= lastTimestamp);
        return timestamp;
    }
}