package com.vantara.hitachi.piics.controller;

import java.util.List;
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
import com.vantara.hitachi.piics.config.ApplicationConfig;

// Entities
import com.vantara.hitachi.piics.entities.PiiRequest;
import com.vantara.hitachi.piics.entities.ZipMetricsRequest;
import com.vantara.hitachi.piics.entities.PiiResponse;
import com.vantara.hitachi.piics.entities.ProxySetting;

// Config
import com.vantara.hitachi.piics.config.DeviceEnroll;

// Services
import com.vantara.hitachi.piics.service.DataSender;

@RestController
public class PiiRequests {
	public static final Logger logger = Logger.getLogger(PiiRequests.class.toString());

	private static ApplicationConfig applicationConfig;
	private static DeviceEnroll deviceEnroll;
	private static ProxySetting proxySetting;
	private static DataSender dataSender;

	@Autowired
	public PiiRequests(ApplicationConfig applicationConfig, DeviceEnroll deviceEnroll, ProxySetting proxySetting) {
		PiiRequests.applicationConfig = applicationConfig;
		PiiRequests.deviceEnroll = deviceEnroll;
		PiiRequests.proxySetting = proxySetting;

		proxySetting.scheme = null;
		proxySetting.host = null;
		proxySetting.port = null;
		proxySetting.username = null;
		proxySetting.password = null;

		PiiRequests.dataSender = new DataSender(proxySetting);
	}

	@RequestMapping(value = "/mqtt/send", method = RequestMethod.POST, consumes = "application/json")
	public PiiResponse sendToService(@RequestBody List<PiiRequest> request) {
		String responseMessage = PiiResponse.RESPONSE_OK;

		PiiRequest req = request.get(0);

		if (req != null) {
			MetricsReq metricsReq = req.generateMetricsReq(DataSender.GetThingName(),
					DataSender.GetDeviceSerialNumber());

			if (PiiRequests.dataSender.publishDataToMqtt(metricsReq)) {
				logger.info(req.toString());
				logger.info("Data published to MQTT!");
			} else {
				logger.log(Level.SEVERE, "Failed to publish data to MQTT, data: " + request);
				responseMessage = PiiResponse.RESPONSE_NOTOK;
			}
		} else {
			responseMessage = PiiResponse.RESPONSE_NOTOK;
		}

		PiiResponse response = new PiiResponse(responseMessage);
		return response;
	}

	@RequestMapping(value = "/zip/send", method = RequestMethod.POST, consumes = "application/json")
	public PiiResponse sendZipMetrics(@RequestBody ZipMetricsRequest request) {
		String responseMessage = PiiResponse.RESPONSE_OK;

		if (PiiRequests.dataSender.publishMetricsFromZipAsyncUpload(request)) {
			logger.info("Zip sent!");
		} else {
			logger.log(Level.SEVERE, "Failed to send zip, request: " + request);
			responseMessage = PiiResponse.RESPONSE_NOTOK;
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
	public static ApplicationConfig getApplicationConfig() {
		return PiiRequests.applicationConfig;
	}

	public static DeviceEnroll getDeviceEnroll() {
		return PiiRequests.deviceEnroll;
	}

	public static ProxySetting getProxySetting() {
		return PiiRequests.proxySetting;
	}

	public static DataSender getDataSender() {
		return PiiRequests.dataSender;
	}
}
