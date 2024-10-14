package com.vantara.hitachi.piics.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {
	@Value("${device.org.name}")
	private String orgName;

	@Value("${device.org.id}")
	private String orgId;

	@Value("${device.serial.number}")
	private String serialNumber;

	@Value("${device.sub.tenant.id}")
	private String subTenantId;

	@Value("${device.model}")
	private String model;

	@Value("${device.sub.model}")
	private String subModel;

	@Value("${device.device.location}")
	private String deviceLocation;

	@Value("${device.contact}")
	private String contact;

	@Value("${device.storage.system.name}")
	private String storageSystemName;

	@Value("${device.svp.ip4}")
	private String svpIp4;

	@Value("${device.svp.ipv6}")
	private String svpIpv6;

	@Value("${device.security.uuid}")
	private String securityUuid;

	@Value("${device.thingname}")
	private String thingName;

	public String getOrgName() {
		return orgName;
	}

	public String getOrgId() {
		return orgId;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public String getSubTenantId() {
		return subTenantId;
	}

	public String getModel() {
		return model;
	}

	public String getSubModel() {
		return subModel;
	}

	public String getDeviceLocation() {
		return deviceLocation;
	}

	public String getContact() {
		return contact;
	}

	public String getStorageSystemName() {
		return storageSystemName;
	}

	public String getSvpIp4() {
		return svpIp4;
	}

	public String getSvpIpv6() {
		return svpIpv6;
	}

	public String getSecurityUuid() {
		return securityUuid;
	}

	public String getThingName() {
		return thingName;
	}

	public void setThingName(String thingName) {
		this.thingName = thingName;
	}
}
