package ru.hogwarts.school.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.IntStream;
import java.util.stream.Stream;

@RestController
@RequestMapping("/info")
public class InfoController {
    private final static Logger logger = LoggerFactory.getLogger(InfoController.class);
    @Value("${server.port}")
    private int port;

    @GetMapping("/port")
    public int port() {
        logger.info("Received server.port number {}", port);
        return port;
    }

    @GetMapping("/sum")
    public int sum() {
        long start = System.currentTimeMillis();
        int sum = IntStream.iterate(1, a -> a + 1).limit(1_000_000).sum();
        long end = System.currentTimeMillis() - start;
        logger.info("Elapsed time: {} ms", end);
        return sum;
    }
}
