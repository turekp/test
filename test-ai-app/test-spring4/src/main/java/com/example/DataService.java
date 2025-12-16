package com.example;

import com.google.common.collect.EvictingQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

/**
 * Created by piotr on 15/11/16.
 */
@Service
public class DataService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private Queue<Double> tickQueue = EvictingQueue.create(30);

    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    private Random generator = new Random();

    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public DataService(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public Stream<Double> ticks() {
        logger.info("Retrieving ticks: " + tickQueue);
        return tickQueue.stream();
    }

    @PostConstruct
    public void init() {
        executor.scheduleAtFixedRate(() -> emit(), 1000, 1000, TimeUnit.MILLISECONDS);
    }

    private void emit() {
        Double tick = makeTick();
        tickQueue.add(tick);
        simpMessagingTemplate.convertAndSend("/topic/ticks", new TickResource(tick));
    }

    private Double makeTick() {
        return generator.nextDouble();
    }

}
