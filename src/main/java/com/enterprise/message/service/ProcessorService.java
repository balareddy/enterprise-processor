package com.enterprise.message.service;

import com.enterprise.message.factory.IProcessorFactory;
import com.enterprise.message.model.Message;
import com.enterprise.message.model.MessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;

@Service
public class ProcessorService implements IProcessorService {

    private IProcessorFactory processorFactory;

    @Autowired
    public ProcessorService(@NotNull IProcessorFactory processorFactory) {
        this.processorFactory = processorFactory;
    }

    @Override
    public List<Message> poll(@NotNull MessageType messageType) throws IllegalArgumentException {
        return processorFactory.poll(messageType);
    }

    @Override
    public List<Message> poll(@NotNull MessageType messageType, int size) throws IllegalArgumentException {
        return processorFactory.poll(messageType, size);
    }

    @Override
    public boolean offer(@NotNull Message message) throws IllegalArgumentException {
        return processorFactory.offer(message);
    }
}
