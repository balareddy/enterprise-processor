package com.enterprise.message.validator;

import com.enterprise.message.model.Message;
import com.enterprise.message.model.MessageType;

public interface IValidator {
    boolean validate(Message message);

    boolean validate(MessageType messageType);
}
