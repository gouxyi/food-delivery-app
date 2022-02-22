package com.cogent.fooddeliveryapp.advice;

import java.util.HashMap;
import java.util.Map;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.cogent.fooddeliveryapp.exception.NoDataFoundException;
import com.cogent.fooddeliveryapp.exception.NameAlreadyExistsException;
import com.cogent.fooddeliveryapp.exception.apierror.ApiError;
import com.cogent.fooddeliveryapp.exception.FoodNotFoundException;

@org.springframework.web.bind.annotation.ControllerAdvice 
// will handle all exceptions which are thrown by the controller/restcontroller using throws
public class ControllerAdvice extends ResponseEntityExceptionHandler{
	
	@ExceptionHandler(NoDataFoundException.class) // this is responsible for handling NameAlreadyExistsException.
	public ResponseEntity<?> NoDataFoundException(NoDataFoundException e){
		// Map<String, String> map = new HashMap<>();
		// map.put("message", "no data found");
		// System.out.println(e);
		// no content used for delete
		// not found used for here get method
		ApiError apiError = new ApiError(HttpStatus.NOT_FOUND,e.getMessage(),e);
		return buildResponseEntity(apiError);
	}
	
	@ExceptionHandler(NameAlreadyExistsException.class) // this is responsible for handling NameAlreadyExistsException.
	public ResponseEntity<?> NameAlreadyExistsException(NameAlreadyExistsException e){
		Map<String, String> map = new HashMap<>();
		map.put("message", "name already exists");
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST,"name already Exists",e);
		return ResponseEntity.badRequest().body(map);
	}
	
	@Override
	// work for @Valid annotation only (Post method)
	public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request){
		//ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
		ApiError apiError = new ApiError(status);
		apiError.setMessage("Validation Error");
		apiError.addValidationErrors(ex.getFieldErrors());
		apiError.addValidationError(ex.getBindingResult().getGlobalErrors());
		return buildResponseEntity(apiError);
	}
	
	private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
		return new ResponseEntity<Object>(apiError,apiError.getHttpStatus());
	}
	
	// will work for path transform to the argument
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	private ResponseEntity<?> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException e) {
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
		apiError.setMessage(e.getMessage());
		apiError.setDebugMessage(e.getRequiredType().getName());
		return buildResponseEntity(apiError);
	}
	
	@ExceptionHandler(ConstraintViolationException.class)
	protected ResponseEntity<?> handleMethodConstraintViolationException(ConstraintViolationException e){
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
		apiError.setMessage(e.getMessage());
		return buildResponseEntity(apiError);
	}
	
	//default handle
	@ExceptionHandler(Exception.class)
	protected ResponseEntity<?> handleMethodException(Exception e){
		ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR);
		apiError.setMessage(e.getMessage());
		return buildResponseEntity(apiError);
	}
	
	@ExceptionHandler(FoodNotFoundException.class)
	public ResponseEntity<?> FoodNotFoundException(FoodNotFoundException e){
		Map<String, String> map = new HashMap<>();
		map.put("message", e.getMessage());
		return ResponseEntity.badRequest().body(map);
	}
}
