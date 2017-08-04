package com.enterprise.message.validator;

import com.enterprise.message.model.Message;
import com.enterprise.message.model.MessageType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Objects;

import static java.util.Optional.ofNullable;

@Component
public class AppValidator implements IValidator {

    @Override
    public boolean validate(Message message) {
        return ofNullable(message)
                .filter(m -> m.getSeqNum() > 0)
                .filter(m -> Objects.nonNull(m.getMsg()))
                .filter(m -> !StringUtils.isEmpty(m.getMsg()))
                .filter(m -> Objects.nonNull(m.getPriority()))
                .filter(m -> Objects.nonNull(m.getType()))
                .isPresent();
    }

    @Override
    public boolean validate(MessageType messageType) {
        return ofNullable(messageType).isPresent();
    }
}
