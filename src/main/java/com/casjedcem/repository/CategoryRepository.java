package com.casjedcem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.casjedcem.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
	
	

}
