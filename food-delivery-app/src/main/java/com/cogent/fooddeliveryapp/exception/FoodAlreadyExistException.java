package com.cogent.fooddeliveryapp.exception;

public class FoodAlreadyExistException extends Exception {
	public FoodAlreadyExistException (String e) {
		super(e);
	}
	
	@Override
	public String toString() {
		return super.getMessage();
	}
}
