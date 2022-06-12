package com.casjedcem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.casjedcem.repository.LendRepository;

@Service
public class LendService {

	@Autowired
	LendRepository lendRepository;
}
