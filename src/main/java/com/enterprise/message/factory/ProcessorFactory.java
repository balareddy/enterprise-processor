package com.enterprise.message.factory;

import com.enterprise.message.model.Message;
import com.enterprise.message.model.MessageType;
import com.enterprise.message.repository.IRepository;
import com.enterprise.message.validator.IValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;
import static java.util.stream.Stream.generate;

@Component
public class ProcessorFactory implements IProcessorFactory {

    private final IRepository repository;
    private final IValidator validator;

    @Autowired
    public ProcessorFactory(@NotNull IRepository repository, IValidator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    @Override
    public List<Message> poll(@NotNull MessageType messageType, int size) throws IllegalArgumentException {
        if (size < 0) {
            throw new IllegalArgumentException("queue size should be greater than 0");
        }

        if (!validator.validate(messageType)) {
            throw new IllegalArgumentException("Message is invalid");
        }

        return generate(() -> ofNullable(repository.poll(messageType))).
                limit(size).
                filter(Optional::isPresent).
                map(Optional::get).
                collect(Collectors.toList());
    }

    @Override
    public List<Message> poll(@NotNull MessageType messageType) throws IllegalArgumentException {
        if (!validator.validate(messageType)) {
            throw new IllegalArgumentException("MessageType is invalid");
        }
        return poll(messageType, repository.size(messageType));
    }

    @Override
    public boolean offer(@NotNull Message message) throws IllegalArgumentException {
        if (!validator.validate(message)) {
            throw new IllegalArgumentException("Message is invalid");
        }
        return repository.offer(message);
    }
}
