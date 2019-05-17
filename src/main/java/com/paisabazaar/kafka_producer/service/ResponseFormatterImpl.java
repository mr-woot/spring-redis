package com.paisabazaar.kafka_producer.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

/**
 * Contributed By: Tushar Mudgal
 * On: 10/5/19
 */
@Service
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

    @Override
    public JSONObject buildResponse(String status, Integer code, JSONArray data, String message) {
        JSONObject response = new JSONObject();
        response.put("status", status);
        response.put("code", code);
        response.put("data", data);
        response.put("message", message);
        return response;
    }

    @Override
    public JSONObject buildErrorResponse(String status, Integer code, JSONObject error) {
        JSONObject response = new JSONObject();
        response.put("status", status);
        response.put("code", code);
        response.put("error", error);
        return response;
    }
}
