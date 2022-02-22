package com.cogent.fooddeliveryapp.exception;

public class FoodNotFoundException extends RuntimeException{
	public FoodNotFoundException() {
		super("Sorry Food Not Found");
	}
}
