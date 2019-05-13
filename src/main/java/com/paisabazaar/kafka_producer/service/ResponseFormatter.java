package com.paisabazaar.kafka_producer.service;

import com.paisabazaar.kafka_producer.model.Response;
import com.paisabazaar.kafka_producer.utils.ResponseCode;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * Contributed By: Tushar Mudgal
 * On: 10/5/19
 */
@Service
public interface ResponseFormatter {
    JSONObject buildResponse(String status, Integer code, JSONObject data, String message);
    JSONObject buildErrorResponse(String status, Integer code, JSONObject error);
    JSONObject buildResponse(String status, Integer code, JSONArray data, String message);
}
