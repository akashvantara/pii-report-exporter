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
import java.util.HashMap;

import com.vantara.hitachi.piics.config.DeviceEnroll;;

public class DataSender {
	private static final Logger logger = Logger.getLogger(DataSender.class.toString());

	// Proxy related setting
	private Map<String, String> proxySettings;

	// Data sending related configuration
	private static DeviceEnrollmentRequest enrollmentRequest = DeviceEnroll.constructTestEnrollDevice();
	private static String thingName = DeviceEnroll.getThingName();

	private StorageMetricsPublisher storageMetricsPublisher;
	private PulseMqttConnection pulseMqttConnection;
	private MqttMetrics mqttMetricsService;

	public DataSender(ProxySetting proxySetting) {
		// Set proxy
		logger.info("Setting proxy!");
		this.setProxySetting(proxySetting);
		logger.info("Proxy set!");

		// Initialize metrics publisher
		logger.info("Initializing storage metrics publisher!");
		this.storageMetricsPublisher = new StorageMetricsPublisherImpl();
		logger.info("Storage metrics publisher initialized!");

		// Initialize mqtt connection
		logger.info("Initializing pulse mqtt connection!");
		this.pulseMqttConnection = PulseMqttConnection.withDeviceCertificates(DataSender.thingName,
				Optional.ofNullable(ProxyUtils.getProxyForMQTT(this.proxySettings)));
		logger.info("Pulse mqtt connection initialized!");

		// Enroll device (Certs adding!? at boot)
		// this is removed since `DeviceEnroll.java` already does it
		/*
		 * logger.info("Enrolling device!");
		 * this.enrollDevice();
		 * logger.info("Device enrolled!");
		 */

		try {
			logger.info("Initializing pulse mqtt metrics service!");
			this.mqttMetricsService = new MqttMetricsImpl(this.pulseMqttConnection.getConnection());
			logger.info("Pulse mqtt metrics service initialized!");
		} catch (ExecutionException | InterruptedException ee) {
			logger.log(Level.SEVERE, "Could not initialize the mqtt service, error: " + ee);
		}

		// Add a mechanism to close the connection gracefully when done
		logger.info("Adding hook to close mqtt connection at exit!");
		PulseMqttConnection connection = this.pulseMqttConnection;
		Runtime.getRuntime().addShutdownHook(new Thread(connection::disconnect));
		logger.info("hook added to close mqtt connection at exit!");
	}

	public void setProxySetting(ProxySetting proxySetting) {
		this.proxySettings = new HashMap<>();
		if (proxySetting != null) {
			this.proxySettings.put("scheme", proxySetting.scheme);
			this.proxySettings.put("host", proxySetting.host);
			this.proxySettings.put("port", proxySetting.port);
			this.proxySettings.put("username", proxySetting.username);
			this.proxySettings.put("password", proxySetting.password);
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
			logger.info("subscribing to metrics channel");
			this.mqttMetricsService.subscribeMqttMetrics(DataSender.thingName,
					DataSender.enrollmentRequest);

			logger.info("publishing to metrics channel");
			this.mqttMetricsService.publishMqttMetrics(request, DataSender.thingName,
					DataSender.enrollmentRequest);

			// Probably don't enable this we should not be waiting for messages
			// this.pulseMqttConnection.waitForMessages();
			success = true;
		} catch (ExecutionException ee) {
			logger.log(Level.SEVERE, "Failed to create MQTT connection or subscription, error: " + ee);
		} catch (PulseException pe) {
			logger.log(Level.SEVERE, "Got PulseException while trying to push metrics to topic: " + pe);
		} catch (InterruptedException e) {
			logger.log(Level.SEVERE, "Could not wait for messages from MQTT, error: " + e);
			logger.log(Level.WARNING, "Disconnecting MQTT client since error occured");
			this.pulseMqttConnection.disconnect();

			// Restore interrupted state...
			Thread.currentThread().interrupt();
		}

		return success;
	}

	public static String GetThingName() {
		return thingName;
	}

	public static String GetDeviceSerialNumber() {
		return DataSender.enrollmentRequest.getDevice().getSerialNumber();
	}
}
