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

    private final long seqNum;

    private final String msg;

    private final Priority priority;

    private final MessageType type;

    public Message(long seqNum, @NotNull String msg, @NotNull MessageType type, @NotNull Priority priority) {
        this.seqNum = seqNum;

        this.msg = msg;
        this.priority = priority;
        this.type = type;
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
