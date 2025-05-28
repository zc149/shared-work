package com.todo.performance;

import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ChatPerformanceTest {

    private static final int MESSAGE_COUNT = 10000;
    private static final String REDIS_KEY = "chat:testroom";

    // 1. Static Map 기반
    public static void testStaticMap() {
        Map<String, List<String>> map = new HashMap<>();
        List<String> messages = new ArrayList<>();

        long startWrite = System.nanoTime();
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            messages.add("Message " + i);
        }
        map.put("room1", messages);
        long endWrite = System.nanoTime();

        long startRead = System.nanoTime();
        List<String> result = map.get("room1");
        for (String msg : result) {
            // simulate read
        }
        long endRead = System.nanoTime();

        System.out.println("Static Map 저장 시간: " + toMillis(endWrite - startWrite) + "ms");
        System.out.println("Static Map 조회 시간: " + toMillis(endRead - startRead) + "ms");
    }

    // 2. Redis 기반
    public static void testRedis() {
        try (Jedis jedis = new Jedis("localhost", 6379)) {
            jedis.del(REDIS_KEY);

            long startWrite = System.nanoTime();
            for (int i = 0; i < MESSAGE_COUNT; i++) {
                jedis.rpush(REDIS_KEY, "Message " + i);
            }
            long endWrite = System.nanoTime();

            long startRead = System.nanoTime();
            List<String> result = jedis.lrange(REDIS_KEY, 0, -1);
            for (String msg : result) {
                // simulate read
            }
            long endRead = System.nanoTime();

            System.out.println("Redis 저장 시간: " + toMillis(endWrite - startWrite) + "ms");
            System.out.println("Redis 조회 시간: " + toMillis(endRead - startRead) + "ms");

            jedis.del(REDIS_KEY); // 테스트 종료 후 정리
        }
    }

    private static long toMillis(long nanos) {
        return TimeUnit.NANOSECONDS.toMillis(nanos);
    }

    public static void main(String[] args) {
        System.out.println("== Static Map 테스트 ==");
        testStaticMap();
        System.out.println("== Redis 테스트 ==");
        testRedis();
    }

}
