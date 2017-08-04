package com.enterprise.message.service;

import com.enterprise.message.Application;
import com.enterprise.message.model.Message;
import com.enterprise.message.model.MessageType;
import com.enterprise.message.model.Priority;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.ContextConfiguration;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static com.enterprise.message.model.MessageType.*;
import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.stream.Stream.of;

@ContextConfiguration(classes = Application.class)
public class ProcessorServiceClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessorServiceClient.class);

    private static final Map<MessageType, Consumer<List<Message>>> consumerMap = new HashMap<>(3);

    private static final int REQUEST_TIMEOUT = 5;

    static {
        // create the consumers for different types
        consumerMap.put(BLUE, System.out::println);
        consumerMap.put(RED, System.out::println);
        consumerMap.put(YELLOW, System.out::println);
    }

    private static void injectMessages(@NotNull IProcessorService applicationService) {
        final AtomicInteger seq = new AtomicInteger();
        final String testMessageFmt = "test%d";

        // creates the same set of test data for different message types
        of(MessageType.values()).forEach(type ->
                of(new Message(seq.incrementAndGet(), format(testMessageFmt, seq.get()), type, Priority.LOW),
                        new Message(seq.incrementAndGet(), format(testMessageFmt, seq.get()), type, Priority.LOW),
                        new Message(seq.incrementAndGet(), format(testMessageFmt, seq.get()), type, Priority.MEDIUM),
                        new Message(seq.incrementAndGet(), format(testMessageFmt, seq.get()), type, Priority.MEDIUM),
                        new Message(seq.incrementAndGet(), format(testMessageFmt, seq.get()), type, Priority.HIGH),
                        new Message(seq.incrementAndGet(), format(testMessageFmt, seq.get()), type, Priority.HIGH))
                        .forEach(applicationService::offer));
    }

    private static void requestMessages(@NotNull IProcessorService applicationService, MessageType requestType) {
        try {
            CompletableFuture.supplyAsync(() -> applicationService.poll(requestType))
                    .exceptionally(ex -> {
                        LOGGER.error("Failed to get messages for the request type " + requestType, ex);
                        return emptyList();
                    })
                    .thenAccept(consumerMap.get(requestType)).get(REQUEST_TIMEOUT, SECONDS);
        } catch (TimeoutException e) {
            LOGGER.error("Request timed out for the request type " + requestType, e);
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.error("Request failed for the request type " + requestType, e);
        }
    }

    // Just to demonstrate usage of async callback as a client
    public static void simulateClientRequest(@NotNull IProcessorService applicationService) {
        // create the test data for different types and priorities
        injectMessages(applicationService);

        // client makes a call to the service to get messages for the blue type
        final MessageType requestType = BLUE;
        requestMessages(applicationService, requestType);
    }

    public static void main(String... args) {
        final ApplicationContext context = new AnnotationConfigApplicationContext(Application.class);
        final IProcessorService applicationService = context.getBean(ProcessorService.class);

        // simulate a client request
        simulateClientRequest(applicationService);
    }
}
