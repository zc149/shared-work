package com.todo.chatting.test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ChatParallelTest {
    private static final int THREAD_COUNT = 10;
    private static final int MESSAGES_PER_ROOM = 10000;
    private static final String REDIS_PREFIX = "chat:room:";

    // 공유 static map
    private static final ConcurrentHashMap<String, List<String>> staticMap = new ConcurrentHashMap<>();

    // Redis 연결 풀
    private static final JedisPool jedisPool = new JedisPool("localhost", 6379);

    public static void main(String[] args) throws InterruptedException {
        System.out.println("== 병렬 처리: Static Map ==");
        runTest(ChatParallelTest::writeToStaticMap, ChatParallelTest::readFromStaticMap);

        System.out.println("== 병렬 처리: Redis ==");
        runTest(ChatParallelTest::writeToRedis, ChatParallelTest::readFromRedis);

        jedisPool.close();
    }

    private static void runTest(Runnable writeTask, Runnable readTask) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

        long startWrite = System.nanoTime();
        for (int i = 0; i < THREAD_COUNT; i++) {
            executor.submit(() -> writeTask.run());
        }
        executor.shutdown();
        executor.awaitTermination(2, TimeUnit.MINUTES);
        long endWrite = System.nanoTime();

        executor = Executors.newFixedThreadPool(THREAD_COUNT);
        long startRead = System.nanoTime();
        for (int i = 0; i < THREAD_COUNT; i++) {
            executor.submit(() -> readTask.run());
        }
        executor.shutdown();
        executor.awaitTermination(2, TimeUnit.MINUTES);
        long endRead = System.nanoTime();

        System.out.println("병렬 저장 시간: " + toMillis(endWrite - startWrite) + " ms");
        System.out.println("병렬 조회 시간: " + toMillis(endRead - startRead) + " ms");
    }

    // Static Map 작업
    private static void writeToStaticMap() {
        String roomId = UUID.randomUUID().toString();
        List<String> messages = new ArrayList<>(MESSAGES_PER_ROOM);
        for (int i = 0; i < MESSAGES_PER_ROOM; i++) {
            messages.add("Message " + i);
        }
        staticMap.put(roomId, messages);
    }

    private static void readFromStaticMap() {
        for (List<String> messages : staticMap.values()) {
            for (String msg : messages) {
                // simulate read
            }
        }
    }

    // Redis 작업
    private static void writeToRedis() {
        String roomId = UUID.randomUUID().toString();
        try (Jedis jedis = jedisPool.getResource()) {
            for (int i = 0; i < MESSAGES_PER_ROOM; i++) {
                jedis.rpush(REDIS_PREFIX + roomId, "Message " + i);
            }
        }
    }

    private static void readFromRedis() {
        try (Jedis jedis = jedisPool.getResource()) {
            Set<String> keys = jedis.keys(REDIS_PREFIX + "*");
            for (String key : keys) {
                List<String> messages = jedis.lrange(key, 0, -1);
                for (String msg : messages) {
                    // simulate read
                }
            }
        }
    }

    private static long toMillis(long nanos) {
        return TimeUnit.NANOSECONDS.toMillis(nanos);
    }


}
