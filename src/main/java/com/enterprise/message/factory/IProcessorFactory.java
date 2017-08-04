package com.enterprise.message.factory;

import com.enterprise.message.model.Message;
import com.enterprise.message.model.MessageType;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface IProcessorFactory {
    List<Message> poll(@NotNull MessageType messageType, int size) throws IllegalArgumentException;

    List<Message> poll(@NotNull MessageType messageType) throws IllegalArgumentException;

    boolean offer(@NotNull Message message) throws IllegalArgumentException;
}
