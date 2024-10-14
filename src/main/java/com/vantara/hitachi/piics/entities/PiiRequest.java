package com.vantara.hitachi.piics.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hv.pc.dto.performance.MetricsReq;
import com.hv.pc.utils.DateUtils;

public class PiiRequest {
	private String date;
	private String timestamp;
	private String capturedTimestamp;
	private String tenantId;
	private String serialNumber;
	private String resourceId;
	private String metricsType;
	private List<Map<String, Object>> data;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getCapturedTimestamp() {
		return capturedTimestamp;
	}

	public void setCapturedTimestamp(String capturedTimestamp) {
		this.capturedTimestamp = capturedTimestamp;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public String getMetricsType() {
		return metricsType;
	}

	public void setMetricsType(String metricsType) {
		this.metricsType = metricsType;
	}

	public List<Map<String, Object>> getData() {
		return data;
	}

	public void setData(List<Map<String, Object>> data) {
		this.data = data;
	}

	public MetricsReq generateMetricsReq(String thingName, String serialNumber) {
		List<Map<String, Object>> dataToSend = new ArrayList<>();

		Map<String, Object> entry = new HashMap<>();

		entry.put("date", this.date);
		entry.put("timestamp", this.timestamp);
		entry.put("capturedTimestamp", this.capturedTimestamp);
		entry.put("tenantId", this.tenantId);
		entry.put("serialNumber", this.serialNumber);
		entry.put("resourceId", this.resourceId);
		entry.put("metricsType", this.metricsType);
		entry.put("data", this.data);

		return new MetricsReq.Builder()
				.tenantId(thingName)
				.serialNumber(serialNumber)
				.capturedTimestamp(DateUtils.getCurrentTimeStamp())
				.data(dataToSend)
				.metricsType("PII")
				.build();
	}

	@Override
	public String toString() {
		return "{ date: '" + this.date + "', timestamp: '" + this.timestamp + "', capturedTimestamp: '"
				+ this.capturedTimestamp + "', tenantId: '" + this.tenantId + "', serialNumber: '"
				+ this.serialNumber + "', resourceId: '" + this.resourceId + "', metricsType: '"
				+ this.metricsType + "', data: '" + this.data + " }";
	}
}
