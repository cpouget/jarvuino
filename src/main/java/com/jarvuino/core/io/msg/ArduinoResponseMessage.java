package com.jarvuino.core.io.msg;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Optional;

public class ArduinoResponseMessage {

    public final Status status;
    public final Optional<Integer> id;
    public final String msg;

    public ArduinoResponseMessage(Status status, Optional<Integer> id, String msg) {
        this.status = status;
        this.id = id;
        this.msg = msg;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("status", status)
                .append("id", id)
                .append("msg", msg)
                .toString();
    }
}
