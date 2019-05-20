package com.paisabazaar.kafka_producer.service;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Contributed By: Tushar Mudgal
 * On: 10/5/19
 */
public interface ResponseFormatter {
    JSONObject buildResponse(String status, Integer code, JSONObject data, String message);

    JSONObject buildErrorResponse(String status, Integer code, JSONObject error);

    JSONObject buildResponse(String status, Integer code, JSONArray data, String message);
}
