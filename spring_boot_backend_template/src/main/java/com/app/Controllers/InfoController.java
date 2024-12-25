package com.app.Controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/info")
public class InfoController {
	
	@GetMapping("")
	public String getMethodName() {
		return "this is somewhat fucking working!!";
	}
	
}
