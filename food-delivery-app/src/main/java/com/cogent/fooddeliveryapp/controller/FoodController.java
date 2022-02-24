package com.cogent.fooddeliveryapp.controller;

import java.util.Collections;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cogent.fooddeliveryapp.dto.Food;
import com.cogent.fooddeliveryapp.enums.FoodType;
import com.cogent.fooddeliveryapp.exception.FoodNotFoundException;
import com.cogent.fooddeliveryapp.exception.NoDataFoundException;
import com.cogent.fooddeliveryapp.repository.FoodRepository;


@RestController // = @Controller + @ResponseBody
@RequestMapping("/api/food")

@Validated // difference between @Valid and @Validated

public class FoodController {
	
	@Autowired
	FoodRepository foodRepository;
	//private FoodService foodService;
	
	@PostMapping(value = "/add")
	@PreAuthorize("hasRole('ADMIN')")

	public ResponseEntity<?> createFood(@Valid @RequestBody Food food){
		Food food2 = foodRepository.save(food);
		return ResponseEntity.status(201).body(food2);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> getFoodById(@PathVariable("id") @Min(1) Long id){
		Food food = foodRepository.findById(id).orElseThrow(() -> new NoDataFoundException("no data found"));
        return ResponseEntity.ok(food);
	}
	
	@GetMapping(value = "/")
	public ResponseEntity<?> findAllFoods() {
		List<Food> foods = foodRepository.findAll();
		if (foods.size() > 0 ) {
			return ResponseEntity.ok(foods);
		} else {
			throw new NoDataFoundException("no foods are there");
		}
	}
	
	@PutMapping(value = "/{foodId}")
	Food replaceEmployee(@RequestBody Food newFood, @PathVariable Long foodId) {

		return foodRepository.findById(foodId).map(food -> {
			food.setFoodName(newFood.getFoodName());
			food.setFoodPrice(newFood.getFoodPrice());
			food.setFoodType(newFood.getFoodType());
			food.setDescription(newFood.getDescription());
			food.setFoodPic(newFood.getFoodPic());
			return foodRepository.save(food);
		}).orElseThrow(() -> new FoodNotFoundException());
	}
	
//	@GetMapping(value = "/:{foodType}")
//	public ResponseEntity<?> getFoodByFoodType(@RequestParam("foodType") String foodType){
//		List<Food> foods = foodRepository.findByFoodType(foodType);
//		if (foods.size() > 0 ) {
//			return ResponseEntity.ok(foods);
//		} else {
//			throw new NoDataFoundException("no foods are there");
//		}
//	}
	
	@GetMapping(value = "all/desc")
	public ResponseEntity<?> getAllDescOrder(){
		List<Food> list = foodRepository.findAll();
		Collections.sort(list, (a,b) -> b.getId().compareTo(a.getId()));
		return ResponseEntity.status(200).body(list);
	}
	
	@GetMapping(value = "all/asc")
	public ResponseEntity<?> getAllAscOrder(){
		List<Food> list = foodRepository.findAll();
		Collections.sort(list, (a,b) -> a.getId().compareTo(b.getId()));
		return ResponseEntity.status(200).body(list);
	}
}
