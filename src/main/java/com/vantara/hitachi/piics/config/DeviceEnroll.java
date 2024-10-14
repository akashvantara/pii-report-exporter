package com.vantara.hitachi.piics.config;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Component;

import com.hv.pc.dto.Device;
import com.hv.pc.dto.DeviceEnrollmentRequest;
import com.hv.pc.dto.DeviceEnrollmentResponse;
import com.hv.pc.dto.Tenant;
import com.hv.pc.service.DeviceProvisioningService;
import com.hv.pc.service.DeviceProvisioningServiceImpl;

@Component
public class DeviceEnroll {
	public static final Logger logger = Logger.getLogger(DeviceEnroll.class.toString());

	private static ApplicationConfig PULSE_PROPERTIES;
	private static String THING_NAME = "";
	private static DeviceEnrollmentRequest request;
	private static boolean createdDeviceEnrollment = false;

	public DeviceEnroll(ApplicationConfig applicationConfig) {
		DeviceEnroll.PULSE_PROPERTIES = applicationConfig;
	}

	public static DeviceEnrollmentRequest constructExistingTestDevice() {
		String orgName = PULSE_PROPERTIES.getOrgName();
		String orgId = PULSE_PROPERTIES.getOrgId();
		String registeredSerialNumber = PULSE_PROPERTIES.getSerialNumber() + ":" + orgId;
		String registeredTenantId = orgName + ":" + orgId;

		Tenant tenant = new Tenant.Builder()
				.withTenantId(registeredTenantId)
				.withSubTenantId(PULSE_PROPERTIES.getSubTenantId())
				.build();

		Device device = new Device.Builder()
				.withDeviceId("device789")
				.withSerialNumber(registeredSerialNumber)
				.withModel(PULSE_PROPERTIES.getModel())
				.withSubModel(PULSE_PROPERTIES.getSubModel())
				.withDeviceLocation(PULSE_PROPERTIES.getDeviceLocation())
				.build();

		DeviceEnrollmentRequest request = new DeviceEnrollmentRequest.Builder()
				.withTenant(tenant)
				.withDevice(device)
				.build();

		setThingName(registeredSerialNumber);

		return request;
	}

	public static DeviceEnrollmentRequest constructTestEnrollDevice() {
		if (createdDeviceEnrollment)
			return request;
		String serialNumber = PULSE_PROPERTIES.getSerialNumber() + ":" + PULSE_PROPERTIES.getOrgId();
		String tenantId = PULSE_PROPERTIES.getOrgName() + ":" + PULSE_PROPERTIES.getOrgId();

		Tenant tenant = new Tenant.Builder()
				.withTenantId(tenantId)
				.withSubTenantId(PULSE_PROPERTIES.getSubTenantId())
				.build();

		Device device = new Device.Builder()
				.withDeviceId(serialNumber)
				.withSerialNumber(serialNumber)
				.withModel(PULSE_PROPERTIES.getModel())
				.withSubModel(PULSE_PROPERTIES.getSubModel())
				.withDeviceLocation(PULSE_PROPERTIES.getDeviceLocation())
				.withContact(PULSE_PROPERTIES.getContact())
				.withStorageSystemName(PULSE_PROPERTIES.getStorageSystemName())
				.withServiceIp4(PULSE_PROPERTIES.getSvpIp4())
				.withServiceIp6(PULSE_PROPERTIES.getSvpIpv6())
				.build();

		request = new DeviceEnrollmentRequest.Builder()
				.withTenant(tenant)
				.withDevice(device)
				.withUUID(java.util.UUID.fromString(PULSE_PROPERTIES.getSecurityUuid()))
				.build();
		setThingName(serialNumber);

		HashMap<String, String> proxySettings = new HashMap<>();
		proxySettings.put("scheme", PULSE_PROPERTIES.getProxyScheme());
		proxySettings.put("host", PULSE_PROPERTIES.getProxyHost());
		proxySettings.put("port", PULSE_PROPERTIES.getProxyPort());
		proxySettings.put("username", PULSE_PROPERTIES.getProxyUsername());
		proxySettings.put("password", PULSE_PROPERTIES.getProxyPassword());

		DeviceProvisioningService deviceProvisioningService = new DeviceProvisioningServiceImpl(serialNumber,
				proxySettings);
		DeviceEnrollmentResponse response = deviceProvisioningService.enrollDevice(request);

		if (response.isSuccess()) {
			logger.log(Level.INFO, "Device enrolled successfully: " + response.getThingName());
		} else {
			logger.log(Level.SEVERE, "Device enrollment failed: " + response.getMessage());
		}
		createdDeviceEnrollment = Boolean.parseBoolean("true");
		return request;
	}

	public static String getThingName() {
		return DeviceEnroll.THING_NAME;
	}

	public static void setThingName(String thingName) {
		DeviceEnroll.THING_NAME = thingName;
	}

	public static Boolean getCreatedDeviceEnrollment() {
		return createdDeviceEnrollment;
	}

	public static void setCreatedDeviceEnrollment(Boolean createdDeviceEnrollment) {
		DeviceEnroll.createdDeviceEnrollment = createdDeviceEnrollment;
	}
}
