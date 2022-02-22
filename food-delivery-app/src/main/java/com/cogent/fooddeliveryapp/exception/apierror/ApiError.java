package com.cogent.fooddeliveryapp.exception.apierror;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class ApiError {
	
	private HttpStatus httpStatus;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
	private LocalDateTime localDateTime;
	
	private String message;
	
	private String debugMessage;
	
	private List<ApiSubError> subErrors; // to hold validational related errors

	public List<ApiSubError> getSubErrors() {
		return subErrors;
	}

	public void setSubErrors(List<ApiSubError> subErrors) {
		this.subErrors = subErrors;
	}

	private ApiError() {
		localDateTime = LocalDateTime.now(); // @ the time of calling this constructor whatever the date time value is there,
		// it will use it
		
	}
	
	public ApiError(HttpStatus httpStatus) {
		this();
		this.httpStatus = httpStatus;
	}

	public ApiError(HttpStatus httpStatus, String message, Throwable ex) {
		super();
		this.httpStatus = httpStatus;
		this.message = message;
		this.debugMessage = ex.getLocalizedMessage();
	}
	
	//benefits of use one method for one task: (1) re-usability (2) ease of maintenance
	// every field validation in SubError
	// create a subError and add it into the list.
	private void addSubError(ApiSubError apiSubError) {
		if (subErrors == null) {
			subErrors = new ArrayList<>();
		}
		subErrors.add(apiSubError);
	}
	
	private void addValidationError(String object, String field, Object rejectedValue, String message) {
		addSubError(new ApiValidationError(object, field, rejectedValue, message));
	}
	
	private void addValidationError(String Object, String message) {
		addSubError(new ApiValidationError(Object, message));
	}
	
	private void addValidationError(FieldError fieldError) {
		this.addValidationError(fieldError.getObjectName(),fieldError.getField(),
				fieldError.getRejectedValue(),fieldError.getDefaultMessage());
	}
	
	public void addValidationErrors(List<FieldError> fieldErrors) {
		fieldErrors.forEach(e -> this.addValidationError(e));
	}
	
	private void addValidationError(ObjectError objectError) {
		this.addValidationError(objectError.getObjectName(),objectError.getDefaultMessage());
	}
	
	public void addValidationError(List<ObjectError> globalErrors) {
		globalErrors.forEach(e-> this.addValidationError(e));
	}	
	
	public void addValidationError(ConstraintViolation<?> cv) {
		this.addValidationError(cv.getRootBeanClass().getName(),
				((PathImpl)(cv.getPropertyPath())).getLeafNode().asString(),
				cv.getInvalidValue(), 
				cv.getMessage());
	}
	
	public void addValidationErrors(Set<ConstraintViolation<?>> constraintViolation) {
		constraintViolation.forEach(e-> addValidationError(e));
	}
}
