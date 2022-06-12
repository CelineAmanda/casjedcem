package com.casjedcem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.casjedcem.model.Document;
import com.casjedcem.model.Reservation;
import com.casjedcem.model.User;
import com.casjedcem.model.WishList;
import com.casjedcem.service.DocumentService;
import com.casjedcem.service.ReservationService;
import com.casjedcem.service.UserServices;

@Controller
public class ReservationController {

	@Autowired
	ReservationService reservationService;

	@Autowired
	UserServices userService;

	@Autowired
	DocumentService documentService;

	@GetMapping("/reservation")
	public String viewFavories(Model model, @AuthenticationPrincipal Authentication authentication) {

		model.addAttribute("pageTitle", "Reservation");
		if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
			return "redirect:/login";
		}

		User user = userService.getCurrentlyLoggedInUser(authentication);
		if (user == null)
			return "/login";

		List<Reservation> reservation = reservationService.findByUser(user);
		model.addAttribute("reservation", reservation);

		return "reservation";

	}

	@PostMapping("/reservation/add/{id}")
	public String reserveDocument(@PathVariable("pi") long documentId,
			@AuthenticationPrincipal Authentication authentication) {
		Document document = documentService.findById(documentId).get();
		if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
			return "/login";
		}

		User user = userService.getCurrentlyLoggedInUser(authentication);

		if (user == null) {
			return "login";
		}
		if (document.getQuantitéDispo() == 0) {
			return "document insdiponible pour le moment";
		} else if (!user.getEnable()) {
			return "Vous n'etes pas en regle";
		} else {
			Reservation reservation = new Reservation();
			reservation.setDocument(document);
			reservation.setUser(user);
			reservationService.addCategory(reservation);
		}
		return " redirect:/reservation";

	}

	@PostMapping("/reservation/remove/{id}")
	public String remove(@PathVariable("id") long documentId, @AuthenticationPrincipal Authentication authentication) {
		// Document Document=productRepository.findById(productId).get();
		Document document = documentService.findById(documentId).get();
		if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
			return "login";
		}

		User user = userService.getCurrentlyLoggedInUser(authentication);

		if (user == null) {
			return "login";
		}

		reservationService.deleteReservation(document.getId(), user.getId());

		return "redirect:/reservation";

	}
}
