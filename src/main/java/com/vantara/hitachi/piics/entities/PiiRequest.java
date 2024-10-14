package com.vantara.hitachi.piics.entities;

import java.sql.Timestamp;
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
	private List<PiiInnerData> data;

	public PiiRequest(String date, String timestamp, String capturedTimestamp, String tenantId, String serialNumber,
			String resourceId, String metricsType, List<PiiInnerData> data) {
		this.data = data;
		this.timestamp = timestamp;
		this.capturedTimestamp = capturedTimestamp;
		this.tenantId = tenantId;
		this.serialNumber = serialNumber;
		this.resourceId = resourceId;
		this.metricsType = metricsType;
		this.data = data;
	}

	public Map<String, Object> toMap() {
		Map<String, Object> entry = new HashMap<>();

		entry.put("date", this.date);
		entry.put("timestamp", this.timestamp);
		entry.put("capturedTimestamp", this.capturedTimestamp);
		entry.put("tenantId", this.tenantId);
		entry.put("serialNumber", this.serialNumber);
		entry.put("resourceId", this.resourceId);
		entry.put("metricsType", this.metricsType);

		List<Map<String, Object>> data = new ArrayList<>();

		for (PiiInnerData pid : this.data) {
			data.add(pid.toMap());
		}

		entry.put("data", data);

		return entry;
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

		List<Map<String, Object>> data = new ArrayList<>();

		for (PiiInnerData pid : this.data) {
			data.add(pid.toMap());
		}

		entry.put("data", data);

		return new MetricsReq.Builder()
				.tenantId(thingName)
				.serialNumber(serialNumber)
				.capturedTimestamp(DateUtils.getCurrentTimeStamp())
				.data(dataToSend)
				.metricsType("PII")
				.build();
	}

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

	public List<PiiInnerData> getData() {
		return data;
	}

	public void setData(List<PiiInnerData> data) {
		this.data = data;
	}
}

class PiiInnerData {
	private String serialNumber;
	private Timestamp CapturedTimestamp;
	private List<ScanReportRequest> data;

	public PiiInnerData(
			String serialNumber, Timestamp CapturedTimestamp, List<ScanReportRequest> data) {
		this.serialNumber = serialNumber;
		this.CapturedTimestamp = CapturedTimestamp;
		this.data = data;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public Timestamp getCapturedTimestamp() {
		return CapturedTimestamp;
	}

	public List<ScanReportRequest> getData() {
		return data;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public void setCapturedTimestamp(Timestamp capturedTimestamp) {
		CapturedTimestamp = capturedTimestamp;
	}

	public void setData(List<ScanReportRequest> data) {
		this.data = data;
	}

	public Map<String, Object> toMap() {
		Map<String, Object> r = new HashMap<>();

		r.put("serialNumber", serialNumber);
		r.put("CapturedTimestamp", CapturedTimestamp);

		List<Map<String, Object>> buckets = new ArrayList<>();

		for (ScanReportRequest srr : this.data) {
			buckets.add(srr.toMap());
		}

		r.put("data", data);

		return r;
	}

}

class ScanReportRequest {
	private Integer id;
	private String jobType;
	private String jobConfig;
	private Timestamp jobStartTime;
	private Timestamp jobEndTime;
	private String jobStatus;
	private String jobComment;
	private Boolean isDeleted;
	private String command; // U - update or I - Insert
	private List<BucketRequest> buckets;

	public ScanReportRequest(
			Integer id, String jobType, String jobConfig, Timestamp jobStartTime, Timestamp jobEndTime,
			String jobStatus, String jobComment, Boolean isDeleted, String command,
			List<BucketRequest> buckets) {
		this.id = id;
		this.jobType = jobType;
		this.jobConfig = jobConfig;
		this.jobStartTime = jobStartTime;
		this.jobEndTime = jobEndTime;
		this.jobStatus = jobStatus;
		this.jobComment = jobComment;
		this.isDeleted = isDeleted;
		this.command = command;
		this.buckets = buckets;
	}

	public Map<String, Object> toMap() {
		Map<String, Object> r = new HashMap<>();

		r.put("id", id);
		r.put("jobType", jobType);
		r.put("jobConfig", jobConfig);
		r.put("jobStartTime", jobStartTime);
		r.put("jobEndTime", jobEndTime);
		r.put("jobStatus", jobStatus);
		r.put("jobComment", jobComment);
		r.put("isDeleted", isDeleted);
		r.put("command", command);

		List<Map<String, Object>> buckets = new ArrayList<>();

		for (BucketRequest br : this.buckets) {
			buckets.add(br.toMap());
		}

		r.put("buckets", buckets);

		return r;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getJobType() {
		return jobType;
	}

	public void setJobType(String jobType) {
		this.jobType = jobType;
	}

	public String getJobConfig() {
		return jobConfig;
	}

	public void setJobConfig(String jobConfig) {
		this.jobConfig = jobConfig;
	}

	public Timestamp getJobStartTime() {
		return jobStartTime;
	}

	public void setJobStartTime(Timestamp jobStartTime) {
		this.jobStartTime = jobStartTime;
	}

	public Timestamp getJobEndTime() {
		return jobEndTime;
	}

	public void setJobEndTime(Timestamp jobEndTime) {
		this.jobEndTime = jobEndTime;
	}

	public String getJobStatus() {
		return jobStatus;
	}

	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}

	public String getJobComment() {
		return jobComment;
	}

	public void setJobComment(String jobComment) {
		this.jobComment = jobComment;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public List<BucketRequest> getBuckets() {
		return buckets;
	}

	public void setBuckets(List<BucketRequest> buckets) {
		this.buckets = buckets;
	}
}

class BucketRequest {
	private Integer id;
	private String bucketName;
	private Boolean isDeleted;
	private Timestamp lastScanDate;
	private String command; // U - update or I - Insert
	private List<FileRequest> files;

	public BucketRequest(
			Integer id, String command, String bucketName, Boolean isDeleted, Timestamp lastScanDate,
			List<FileRequest> files) {
		this.id = id;
		this.command = command;
		this.bucketName = bucketName;
		this.isDeleted = isDeleted;
		this.lastScanDate = lastScanDate;
		this.files = files;
	}

	public Map<String, Object> toMap() {
		Map<String, Object> r = new HashMap<>();

		r.put("id", id);
		r.put("command", command); // U - update or I - Insert
		r.put("bucketName", bucketName);
		r.put("isDeleted", isDeleted);
		r.put("lastScanDate", lastScanDate);

		List<Map<String, Object>> files = new ArrayList<>();

		for (FileRequest fr : this.files) {
			files.add(fr.toMap());
		}

		r.put("files", files);

		return r;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Timestamp getLastScanDate() {
		return lastScanDate;
	}

	public void setLastScanDate(Timestamp lastScanDate) {
		this.lastScanDate = lastScanDate;
	}

	public List<FileRequest> getFiles() {
		return files;
	}

	public void setFiles(List<FileRequest> files) {
		this.files = files;
	}
}

class FileRequest {
	private Integer id;
	private String command; // U - update or I - Insert
	private String fileName;
	private String mimeType;
	private Double fileSizeMb;
	private Double processingTime;
	private Boolean isDeleted;
	private Integer totalPiiFound;
	private List<PiiEntityRequest> piiEntities;

	public FileRequest(
			Integer id, String command, String fileName, String mimeType, Double fileSizeMb,
			Double processingTime, Boolean isDeleted, Integer totalPiiFound,
			List<PiiEntityRequest> piiEntities) {
		this.id = id;
		this.command = command;
		this.fileName = fileName;
		this.mimeType = mimeType;
		this.fileSizeMb = fileSizeMb;
		this.processingTime = processingTime;
		this.isDeleted = isDeleted;
		this.totalPiiFound = totalPiiFound;
		this.piiEntities = piiEntities;
	}

	public Map<String, Object> toMap() {
		Map<String, Object> r = new HashMap<>();

		r.put("id", id);
		r.put("command", command); // U - update or I - Insert
		r.put("fileName", fileName);
		r.put("mimeType", mimeType);
		r.put("fileSizeMb", fileSizeMb);
		r.put("processingTime", processingTime);
		r.put("isDeleted", isDeleted);
		r.put("totalPiiFound", totalPiiFound);

		List<Map<String, Object>> files = new ArrayList<>();

		for (PiiEntityRequest pir : this.piiEntities) {
			files.add(pir.toMap());
		}

		r.put("piiEntities", piiEntities);
		return r;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public Double getFileSizeMb() {
		return fileSizeMb;
	}

	public void setFileSizeMb(Double fileSizeMb) {
		this.fileSizeMb = fileSizeMb;
	}

	public Double getProcessingTime() {
		return processingTime;
	}

	public void setProcessingTime(Double processingTime) {
		this.processingTime = processingTime;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Integer getTotalPiiFound() {
		return totalPiiFound;
	}

	public void setTotalPiiFound(Integer totalPiiFound) {
		this.totalPiiFound = totalPiiFound;
	}

	public List<PiiEntityRequest> getPiiEntities() {
		return piiEntities;
	}

	public void setPiiEntities(List<PiiEntityRequest> piiEntities) {
		this.piiEntities = piiEntities;
	}
}

class PiiEntityRequest {
	private Integer id;
	private String command; // U - update or I - Insert
	private String entityType;
	private String entityText;
	private Double entityScore;
	private Integer start;
	private Integer end;
	private Boolean isDeleted;
	private Boolean isSensitive;
	private String analysis;

	public Map<String, Object> toMap() {
		Map<String, Object> r = new HashMap<>();

		r.put("id", id);
		r.put("command", command);
		r.put("entityType", entityType);
		r.put("entityText", entityText);
		r.put("entityScore", entityScore);
		r.put("start", start);
		r.put("end", end);
		r.put("isSensitive", isSensitive);
		r.put("isDeleted", isDeleted);
		r.put("analysis", analysis);

		return r;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public String getEntityText() {
		return entityText;
	}

	public void setEntityText(String entityText) {
		this.entityText = entityText;
	}

	public Double getEntityScore() {
		return entityScore;
	}

	public void setEntityScore(Double entityScore) {
		this.entityScore = entityScore;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getEnd() {
		return end;
	}

	public void setEnd(Integer end) {
		this.end = end;
	}

	public Boolean getIsSensitive() {
		return isSensitive;
	}

	public void setIsSensitive(Boolean isSensitive) {
		this.isSensitive = isSensitive;
	}

	public String getAnalysis() {
		return analysis;
	}

	public void setAnalysis(String analysis) {
		this.analysis = analysis;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
}
