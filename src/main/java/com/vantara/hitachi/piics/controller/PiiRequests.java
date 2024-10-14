package com.vantara.hitachi.piics.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
// Internal
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

// Imported libs
import com.hv.pc.dto.performance.MetricsReq;
import com.hv.pc.utils.DateUtils;
import com.vantara.hitachi.piics.config.ApplicationConfig;

// Entities
import com.vantara.hitachi.piics.entities.PiiRequest;
import com.vantara.hitachi.piics.entities.ZipMetricsRequest;
import com.vantara.hitachi.piics.entities.PiiResponse;

// Config
import com.vantara.hitachi.piics.config.DeviceEnroll;

// Services
import com.vantara.hitachi.piics.service.DataSender;

@RestController
public class PiiRequests {
	public static final Logger logger = Logger.getLogger(PiiRequests.class.toString());

	private static ApplicationConfig APP_CONFIG;
	private static DeviceEnroll DEVICE_ENROLL;
	private static DataSender DATA_SENDER;

	@Autowired
	public PiiRequests(ApplicationConfig applicationConfig, DeviceEnroll deviceEnroll) {
		PiiRequests.APP_CONFIG = applicationConfig;
		PiiRequests.DEVICE_ENROLL = deviceEnroll;
		PiiRequests.DATA_SENDER = new DataSender(applicationConfig.getProxySetting());
	}

	@RequestMapping(value = "/mqtt/send", method = RequestMethod.POST, consumes = "application/json")
	public PiiResponse sendToService(@RequestBody PiiRequest request) {
		String responseMessage = PiiResponse.RESPONSE_OK;

		MetricsReq metricsReq = request.toMetricsReq(APP_CONFIG.getTenantId(),
				APP_CONFIG.getSerialNumber(), DataSender.GetThingName(), APP_CONFIG.getMetricType());

		if (PiiRequests.DATA_SENDER.publishDataToMqtt(metricsReq)) {
			logger.info(metricsReq.toString());
			logger.info("Data published to MQTT!");
		} else {
			logger.log(Level.SEVERE, "Failed to publish data to MQTT, data: " + request);
			responseMessage = PiiResponse.RESPONSE_NOT_OK;
		}

		PiiResponse response = new PiiResponse(responseMessage);
		return response;
	}

	@RequestMapping(value = "/zip/send", method = RequestMethod.POST, consumes = "application/json")
	public PiiResponse sendZipMetrics(@RequestBody ZipMetricsRequest request) {
		String responseMessage = PiiResponse.RESPONSE_OK;

		if (PiiRequests.DATA_SENDER.publishMetricsFromZipAsyncUpload(request)) {
			logger.info("Zip sent!");
		} else {
			logger.log(Level.SEVERE, "Failed to send zip, request: " + request);
			responseMessage = PiiResponse.RESPONSE_NOT_OK;
		}

		PiiResponse response = new PiiResponse(responseMessage);
		return response;
	}

	@RequestMapping(value = "/healthz", method = RequestMethod.GET)
	public PiiResponse healthz() {
		PiiResponse response = new PiiResponse(PiiResponse.RESPONSE_OK);
		return response;
	}

	// Unwanted methods to satisfy the linter and compiler
	public static ApplicationConfig getAPP_CONFIG() {
		return PiiRequests.APP_CONFIG;
	}

	public static DeviceEnroll getDEVICE_ENROLL() {
		return PiiRequests.DEVICE_ENROLL;
	}

	public static DataSender getDATA_SENDER() {
		return PiiRequests.DATA_SENDER;
	}
}
