package com.fastride.infrastructure.api.exceptionhandler;

import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fastride.domain.shared.ResourceNotFoundException;
import com.fastride.domain.shared.ValidationException;

@RestControllerAdvice
public class ApiExceptionHandlerController extends ResponseEntityExceptionHandler {

	private static final String INVALID_DATA_MESSAGE = "One or more fields where filled incorrectly. Please, provide the correct information and try again.";
	private static final String GENERIC_MESSAGE_FOR_FINAL_USER = "There has been an unexpected internal error. Try again and if the problem still persists, contact our support team.";

	@Autowired
	private MessageSource messageSource;

	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<?> handleValidation(ValidationException ex, WebRequest request) {
		HttpStatusCode statusCode = HttpStatusCode.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value());
		ApiErrorType errorType = ApiErrorType.BUSINESS_VALIDATION_ERROR;
		String detail = ex.getMessage();
		ApiError error = createApiErrorBuilder(statusCode, errorType, detail).userMessage(detail).build();
		return handleExceptionInternal(ex, error, new HttpHeaders(), statusCode, request);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> handleAccountNotFound(ResourceNotFoundException ex, WebRequest request) {
		HttpStatusCode statusCode = HttpStatusCode.valueOf(HttpStatus.NOT_FOUND.value());
		ApiErrorType errorType = ApiErrorType.RESOURCE_NOT_FOUND;
		String detail = ex.getMessage();
		ApiError error = createApiErrorBuilder(statusCode, errorType, detail).userMessage(detail).build();
		return handleExceptionInternal(ex, error, new HttpHeaders(), statusCode, request);
	}

	private ApiErrorBuilder createApiErrorBuilder(HttpStatusCode statusCode, ApiErrorType errorType, String detail) {
		return ApiErrorBuilder.getInstance().status(statusCode.value()).type(errorType.getUri())
				.title(errorType.getTitle()).detail(detail).timestamp(OffsetDateTime.now());
	}

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatusCode statusCode, WebRequest request) {
		if (body == null) {
			body = ApiErrorBuilder.getInstance().status(statusCode.value())
					.title(HttpStatus.valueOf(statusCode.value()).getReasonPhrase())
					.userMessage(GENERIC_MESSAGE_FOR_FINAL_USER).timestamp(OffsetDateTime.now());
		} else if (body instanceof String) {
			body = ApiErrorBuilder.getInstance().status(statusCode.value()).title((String) body)
					.userMessage(GENERIC_MESSAGE_FOR_FINAL_USER).timestamp(OffsetDateTime.now());
		}
		return super.handleExceptionInternal(ex, body, headers, statusCode, request);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
		return handleValidationInternal(ex, ex.getBindingResult(), headers, statusCode, request);
	}

	private ResponseEntity<Object> handleValidationInternal(Exception ex, BindingResult bindingResult,
			HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
		ApiErrorType errorType = ApiErrorType.INVALID_DATA;
		String detail = INVALID_DATA_MESSAGE;
		List<ApiError.Object> errorObjects = extractErrors(bindingResult);
		ApiError error = createApiErrorBuilder(statusCode, errorType, detail).userMessage(detail).objects(errorObjects)
				.build();
		return handleExceptionInternal(ex, error, headers, statusCode, request);
	}

	private List<ApiError.Object> extractErrors(BindingResult bindingResult) {
		List<ApiError.Object> errors = bindingResult.getAllErrors().stream().map(objectError -> {
			String message = messageSource.getMessage(objectError, LocaleContextHolder.getLocale());
			String name = objectError.getObjectName();
			if (objectError instanceof FieldError) {
				name = ((FieldError) objectError).getField();
			}
			return new ApiError.Object(name, message);
		}).collect(Collectors.toList());
		errors.sort(Comparator.comparing(ApiError.Object::getUserMessage));
		return errors;
	}

}
