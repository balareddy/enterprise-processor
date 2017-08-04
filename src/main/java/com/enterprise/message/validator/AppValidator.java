package com.enterprise.message.validator;

import com.enterprise.message.model.Message;
import com.enterprise.message.model.MessageType;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Optional;
import java.util.Set;

@Component
public class AppValidator implements IValidator {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

    @Override
    public boolean validate(Message message) {
        final Validator validator = factory.getValidator();
        final Set<ConstraintViolation<Message>> violations = validator.validate(message);
        return Optional.ofNullable(violations).isPresent();
    }

    @Override
    public boolean validate(MessageType messageType) {
        return Optional.ofNullable(messageType).isPresent();
    }
}
