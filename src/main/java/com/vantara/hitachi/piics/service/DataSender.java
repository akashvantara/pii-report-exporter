package com.vantara.hitachi.piics.service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.hv.pc.PulseMqttConnection;
import com.hv.pc.dto.DeviceEnrollmentRequest;
import com.hv.pc.dto.performance.MetricsReq;
import com.hv.pc.exception.PulseException;
import com.hv.pc.service.performance.MqttMetrics;
import com.hv.pc.service.performance.MqttMetricsImpl;
import com.hv.pc.service.storage.StorageMetricsPublisher;
import com.hv.pc.service.storage.StorageMetricsPublisherImpl;
import com.hv.pc.utils.ProxyUtils;
import com.vantara.hitachi.piics.entities.ProxySetting;
import com.vantara.hitachi.piics.entities.ZipMetricsRequest;

import java.io.IOException;

import com.vantara.hitachi.piics.config.DeviceEnroll;

public class DataSender {
	private static final Logger logger = Logger.getLogger(DataSender.class.toString());

	// Proxy related setting
	private Map<String, String> proxySettings;

	// Data sending related configuration
	private static DeviceEnrollmentRequest enrollmentRequest = DeviceEnroll.constructTestEnrollDevice();
	private static final String thingName = DeviceEnroll.getThingName();

	private StorageMetricsPublisher storageMetricsPublisher;
	private PulseMqttConnection pulseMqttConnection;
	private MqttMetrics mqttMetricsService;

	public DataSender(ProxySetting proxySetting) {
		// Configure proxy settings for DataSender object
		this.setProxySetting(proxySetting);

		// Initialize metrics publisher
		logger.info("Initializing storage metrics publisher!");
		this.storageMetricsPublisher = new StorageMetricsPublisherImpl();
		logger.info("Storage metrics publisher initialized!");

		// Initialize mqtt connection
		logger.info("Initializing pulse mqtt connection!");
		this.pulseMqttConnection = PulseMqttConnection.withDeviceCertificates(DataSender.thingName,
				Optional.ofNullable(ProxyUtils.getProxyForMQTT(this.proxySettings)));
		logger.info("Pulse mqtt connection initialized!");

		try {
			logger.info("Initializing pulse mqtt metrics service!");
			this.mqttMetricsService = new MqttMetricsImpl(this.pulseMqttConnection.getConnection());
			logger.info("Pulse mqtt metrics service initialized!");

			String channelName = DataSender.thingName;
			logger.info("subscribing to metrics channel " + channelName + "!");
			this.mqttMetricsService.subscribeMqttMetrics(DataSender.thingName,
					DataSender.enrollmentRequest);
			logger.info("subscribed to metrics channel " + channelName + "!");
		} catch (ExecutionException ee) {
			logger.log(Level.SEVERE, "Could not initialize the mqtt service, error: " + ee);
		} catch (InterruptedException ie) {
			this.pulseMqttConnection.disconnect();

			logger.log(Level.SEVERE, "Could not wait for messages from MQTT, error: " +
					ie);

			Thread.currentThread().interrupt();
		}

		// Add a mechanism to close the connection gracefully when done
		logger.info("Adding hook to close mqtt connection at exit!");
		PulseMqttConnection connection = this.pulseMqttConnection;
		Runtime.getRuntime().addShutdownHook(new Thread(connection::disconnect));
		logger.info("hook added to close mqtt connection at exit!");
	}

	public void setProxySetting(ProxySetting proxySetting) {
		if (proxySetting != null) {
			this.proxySettings = proxySetting.toMap();
		}
	}

	public boolean publishMetricsFromZipAsyncUpload(ZipMetricsRequest request) {
		boolean status = true;
		try {
			CompletableFuture<Void> uploadFuture = this.storageMetricsPublisher
					.publishMetricsFromZipUsingAsyncUpload(request.getPath(),
							DataSender.thingName, DataSender.enrollmentRequest,
							request.getStorage(),
							request.getBucketName(),
							request.getBucketKey(),
							this.proxySettings);
			logger.log(Level.FINE, "Upload is called!");

			uploadFuture.thenRun(() -> {
				logger.log(Level.FINE, "Upload completed asynchronously main!");
			}).exceptionally(e -> {
				logger.log(Level.SEVERE, "Error during uploadmain :" + e.getMessage());
				return null;
			}).join();
		} catch (IOException iox) {
			logger.log(Level.SEVERE, "Exception occured: " + iox.toString());
			status = false;
		}

		return status;
	}

	public boolean publishDataToMqtt(MetricsReq request) {
		boolean success = false;
		try {
			// logger.info("subscribing to metrics channel");
			// this.mqttMetricsService.subscribeMqttMetrics(DataSender.thingName,
			// DataSender.enrollmentRequest);

			logger.info("publishing to metrics channel");
			this.mqttMetricsService.publishMqttMetrics(request, DataSender.thingName,
					DataSender.enrollmentRequest);

			success = true;
		} catch (PulseException pe) {
			logger.log(Level.SEVERE, "Got PulseException while trying to push metrics to topic: " + pe);
		}
		return success;
	}

	public static String GetThingName() {
		return thingName;
	}
}
