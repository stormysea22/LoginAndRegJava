package com.theismann.loginAndReg.controllers;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.theismann.loginAndReg.models.User;
import com.theismann.loginAndReg.service.UserService;
import com.theismann.loginAndReg.validator.UserValidator;

@Controller
public class HomeController {
	private final UserValidator userValidator;
	private final UserService userService;
	
	public HomeController(UserService userService, UserValidator userValidator) {
		this.userService = userService;
		this.userValidator = userValidator;
	}
		
	@RequestMapping("/")
	public String index(@ModelAttribute("user") User user) {
		return "index.jsp";
	}
	
	@PostMapping("/registration")
	public String register(@Valid @ModelAttribute("user") User user, BindingResult result, HttpSession session) {
		
		userValidator.validate(user, result);
		if(result.hasErrors() ) {
			return "index.jsp";
		}
		
		//add validation for duplicate emails
		
		User u = this.userService.registerUser(user);
		session.setAttribute("userid", u.getId());
		
		return "redirect:/dashboard";
	}
	
	@RequestMapping("/dashboard")
	public String dashboard(Model model, HttpSession session) {
		Long id = (Long) session.getAttribute("userid");
		User loggedinuser = this.userService.findUserById(id);
		model.addAttribute("loggedinuser", loggedinuser);
		
		return "dashboard.jsp";
	}
	
	@RequestMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}
	
	@RequestMapping("/login")
	public String login(@RequestParam("email") String email, @RequestParam("password") String password, HttpSession session, RedirectAttributes redirectAttributes) {
		Boolean isLegit = this.userService.authenticateUser(email, password);
		
		if(isLegit) {
			
			User user = this.userService.findByEmail(email);
			session.setAttribute("userid", user.getId());
			return "redirect:/dashboard";
		}
		redirectAttributes.addFlashAttribute("error", "Invalid login Attempt");
		return "redirect:/";
	}
	

}
