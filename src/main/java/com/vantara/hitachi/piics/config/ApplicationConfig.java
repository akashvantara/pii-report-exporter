package com.vantara.hitachi.piics.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.vantara.hitachi.piics.entities.ProxySetting;

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

	@Value("${proxy.scheme}")
	private String proxyScheme;

	@Value("${proxy.host}")
	private String proxyHost;

	@Value("${proxy.port}")
	private String proxyPort;

	@Value("${proxy.username}")
	private String proxyUsername;

	@Value("${proxy.password}")
	private String proxyPassword;

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

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public void setSubTenantId(String subTenantId) {
		this.subTenantId = subTenantId;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public void setSubModel(String subModel) {
		this.subModel = subModel;
	}

	public void setDeviceLocation(String deviceLocation) {
		this.deviceLocation = deviceLocation;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public void setStorageSystemName(String storageSystemName) {
		this.storageSystemName = storageSystemName;
	}

	public void setSvpIp4(String svpIp4) {
		this.svpIp4 = svpIp4;
	}

	public void setSvpIpv6(String svpIpv6) {
		this.svpIpv6 = svpIpv6;
	}

	public void setSecurityUuid(String securityUuid) {
		this.securityUuid = securityUuid;
	}

	public String getProxyScheme() {
		return proxyScheme;
	}

	public void setProxyScheme(String proxyScheme) {
		this.proxyScheme = proxyScheme;
	}

	public String getProxyHost() {
		return proxyHost;
	}

	public void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
	}

	public String getProxyPort() {
		return proxyPort;
	}

	public void setProxyPort(String proxyPort) {
		this.proxyPort = proxyPort;
	}

	public String getProxyUsername() {
		return proxyUsername;
	}

	public void setProxyUsername(String proxyUsername) {
		this.proxyUsername = proxyUsername;
	}

	public String getProxyPassword() {
		return proxyPassword;
	}

	public void setProxyPassword(String proxyPassword) {
		this.proxyPassword = proxyPassword;
	}

	public ProxySetting getProxySetting() {
		return new ProxySetting(this.proxyScheme, this.proxyHost, this.proxyPort, this.proxyUsername,
				this.proxyPassword);
	}
}
