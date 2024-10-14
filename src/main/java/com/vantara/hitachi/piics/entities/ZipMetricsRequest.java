package com.vantara.hitachi.piics.entities;

public class ZipMetricsRequest {
	private String path;
	private String storage;
	private String bucketName;
	private String bucketKey;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getStorage() {
		return storage;
	}

	public void setStorage(String storage) {
		this.storage = storage;
	}

	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	public String getBucketKey() {
		return bucketKey;
	}

	public void setBucketKey(String bucketKey) {
		this.bucketKey = bucketKey;
	}

	@Override
	public String toString() {
		return "{ path: '" + path + "', storage: '" + storage + "', bucketName: '" + bucketName
				+ "', bucketKey: '" + bucketKey + "'}";
	}
}
