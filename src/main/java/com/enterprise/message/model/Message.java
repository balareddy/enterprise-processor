package com.enterprise.message.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Set;

public class Message {

    @Min(value = 1, message = "seqNum should not be less than 1")
    @Max(value = Long.MAX_VALUE, message = "seqNum exceeded max value")
    private final long seqNum;

    @NotNull(message = "Message cannot be null")
    private final String msg;

    @NotNull(message = "Priority cannot be null")
    private final Priority priority;

    @NotNull(message = "MessageType cannot be null")
    private final MessageType type;

    public Message(long seqNum, String msg, MessageType type, Priority priority) {
        this.seqNum = seqNum;

        this.msg = msg;
        this.priority = priority;
        this.type = type;
    }

    public static void main(String args[]) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Message>> violations = validator.validate(new Message(0, null, null, null));
        System.out.println(violations.isEmpty());
    }

    public long getSeqNum() {
        return seqNum;
    }

    public String getMsg() {
        return msg;
    }

    public Priority getPriority() {
        return priority;
    }

    public MessageType getType() {
        return type;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("seqNum", seqNum)
                .add("msg", msg)
                .add("priority", priority)
                .add("type", type)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (getClass() != o.getClass()) return false;
        final Message other = (Message) o;
        return Objects.equal(this.seqNum, other.seqNum)
                && Objects.equal(this.msg, other.msg)
                && Objects.equal(this.priority, other.priority)
                && Objects.equal(this.type, other.type);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.seqNum, this.msg, this.priority, this.type);
    }
}
