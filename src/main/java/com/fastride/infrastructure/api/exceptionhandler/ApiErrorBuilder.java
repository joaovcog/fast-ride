package com.fastride.infrastructure.api.exceptionhandler;

import java.time.OffsetDateTime;
import java.util.List;

public class ApiErrorBuilder {

	private ApiError apiError;

	private ApiErrorBuilder() {
		this.apiError = new ApiError();
	}

	public static ApiErrorBuilder getInstance() {
		return new ApiErrorBuilder();
	}

	public ApiErrorBuilder status(Integer status) {
		this.apiError.setStatus(status);
		return this;
	}

	public ApiErrorBuilder title(String title) {
		this.apiError.setTitle(title);
		return this;
	}

	public ApiErrorBuilder type(String type) {
		this.apiError.setType(type);
		return this;
	}

	public ApiErrorBuilder detail(String detail) {
		this.apiError.setDetail(detail);
		return this;
	}

	public ApiErrorBuilder userMessage(String userMessage) {
		this.apiError.setUserMessage(userMessage);
		return this;
	}

	public ApiErrorBuilder timestamp(OffsetDateTime timestamp) {
		this.apiError.setTimestamp(timestamp);
		return this;
	}

	public ApiErrorBuilder objects(List<ApiError.Object> objects) {
		this.apiError.setObjects(objects);
		return this;
	}

	public ApiError build() {
		return this.apiError;
	}

}
