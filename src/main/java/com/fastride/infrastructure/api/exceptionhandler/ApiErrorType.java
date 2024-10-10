package com.fastride.infrastructure.api.exceptionhandler;

public enum ApiErrorType {
	INVALID_DATA("/invalid-data", "Invalid data"), RESOURCE_NOT_FOUND("/resource-not-found", "Resource not found"),
	BUSINESS_VALIDATION_ERROR("/business-validationr-error", "Business validation error"),
	SYSTEM_ERROR("/system-error", "System error");

	private String uri;
	private String title;

	private ApiErrorType(String path, String title) {
		this.uri = "https://fastride.com" + path;
		this.title = title;
	}

	public String getUri() {
		return this.uri;
	}

	public String getTitle() {
		return this.title;
	}

}
