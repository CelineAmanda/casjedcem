package com.casjedcem.global;

import java.util.ArrayList;
import java.util.List;

import com.casjedcem.model.Product;

public class GlobalDara {
	public static List<Product> cart;
	static {
		cart = new ArrayList<Product>();
	}
}
