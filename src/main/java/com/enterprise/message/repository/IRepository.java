package com.enterprise.message.repository;

import com.enterprise.message.model.Message;
import com.enterprise.message.model.MessageType;

import javax.validation.constraints.NotNull;

public interface IRepository {
    boolean offer(@NotNull Message message);

    Message poll(@NotNull MessageType messageType);

    int size(@NotNull MessageType messageType);

    void reset();
}
