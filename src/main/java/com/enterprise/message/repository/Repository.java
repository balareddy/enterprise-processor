package com.enterprise.message.repository;

import com.enterprise.message.model.Message;
import com.enterprise.message.model.MessageType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

import static java.util.stream.Stream.of;

@Component
public class Repository implements IRepository {

    private final Map<MessageType, Queue<Message>> messageStore = new HashMap<>();
    private final Comparator<Message> priorityFifoComparator = Comparator.comparing(Message::getPriority)
            .thenComparing(Message::getSeqNum);

    public Repository(@Value("${queue.size:1000}") int queueSize) {
        of(MessageType.values()).forEach(type -> messageStore.put(type, new PriorityBlockingQueue<>(queueSize, priorityFifoComparator)));
    }

    @Override
    public boolean offer(@NotNull Message message) {
        return messageStore.get(message.getType()).offer(message);
    }

    @Override
    public Message poll(@NotNull MessageType messageType) {
        return messageStore.get(messageType).poll();
    }

    @Override
    public int size(@NotNull MessageType messageType) {
        return messageStore.get(messageType).size();
    }

    @Override
    public void reset() {
        of(MessageType.values()).forEach(type -> messageStore.get(type).clear());
    }

}
