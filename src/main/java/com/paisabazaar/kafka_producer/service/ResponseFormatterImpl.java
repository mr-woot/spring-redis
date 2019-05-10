package com.paisabazaar.kafka_producer.service;

import com.paisabazaar.kafka_producer.model.Response;
import com.paisabazaar.kafka_producer.utils.ResponseCode;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * Contributed By: Tushar Mudgal
 * On: 10/5/19
 */
@Component
public class ResponseFormatterImpl implements ResponseFormatter {
    @Override
    public JSONObject buildResponse(String status, Integer code, JSONObject data, String message) {
        JSONObject response = new JSONObject();
        response.put("status", status);
        response.put("code", code);
        response.put("data", data);
        response.put("message", message);
        return response;
    }
}
