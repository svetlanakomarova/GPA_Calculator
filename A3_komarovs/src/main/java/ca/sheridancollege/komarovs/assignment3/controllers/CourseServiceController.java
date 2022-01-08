package ca.sheridancollege.komarovs.assignment3.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.sheridancollege.komarovs.assignment3.beans.Course;
import ca.sheridancollege.komarovs.assignment3.database.DatabaseAccess;


@RestController
@RequestMapping("/course")
public class CourseServiceController {
	
	@Autowired 
	private DatabaseAccess da;
	
	// https://localhost:8443/course/SYST10049
	@GetMapping(value="/{code}")
	public Course getCourseByCode(@PathVariable String code) {	
		return da.getCourseByCode(code);	
	}
}
