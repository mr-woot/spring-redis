package com.paisabazaar.kafka_producer.model;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Contributed By: Tushar Mudgal
 * On: 10/5/19
 */
public class Response {
    private String status;

    private int code;

    private Object data;

    private String message;

    private Object error;

    public Response(String status, int code, Object data, String message) {
        this.status = status;
        this.code = code;
        if (data.getClass().getName() == "string") {
            this.data = data.toString();
        } else {
            this.data = data;
        }
        this.message = message;
    }

    public Response(String status, int code, Object error) {
        this.status = status;
        this.code = code;
        this.error = error;
    }

    @Override
    public String toString() {
        return "Response{" +
                "status='" + status + '\'' +
                ", code=" + code +
                ", data=" + data +
                ", message='" + message + '\'' +
                ", error=" + error +
                '}';
    }
}
