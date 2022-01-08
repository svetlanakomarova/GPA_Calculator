/*
* Name: Svetlana Komarova
* File: MainController.java
* Other Files in this Project:
*	Evaluation.java
*	Course.java
*	index.html
*	evaluation.html
*	evalResult.html
*	course.html
*	cumGPA.html
*	termGPA.html
*	general.html
*	main.css
* Main class: A2KomarovsApplication.java
*/

package ca.sheridancollege.komarovs.assignment3.controllers;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.http.HttpSession;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import ca.sheridancollege.komarovs.assignment3.beans.Course;
import ca.sheridancollege.komarovs.assignment3.beans.Evaluation;
import ca.sheridancollege.komarovs.assignment3.database.DatabaseAccess;

@Controller
public class MainController {

	@Autowired
	private DatabaseAccess da;
	
	// if the URL is the root AND to either index.htm or index.html
	// and it's GET request, then execute this method
	// Load a main page
	@GetMapping("/{i:(?:index.html?)?}")
	public String loadMain() {
		return "index.html";
	}
	
	// Enter data into evaluation form and add the new evaluation object 
	// to the evaluations table in the database
	@GetMapping("/evalcalc")
	public String loadEvalCalc(Model model, HttpSession session) {

		session.setAttribute("courses", da.getCourses());

		model.addAttribute("evaluation", new Evaluation());
		return "evaluation.html";
	}
	
	// Display evaluations for all assignments/courses
	@PostMapping("/eval")
	public String submitEvaluation(Model model, 
					@ModelAttribute Evaluation evaluation, 
					HttpSession session) {

		// if edit attribute doesn't exist, assume false
		boolean edit = (session.getAttribute("edit") == null) ? false 
				: (boolean) session.getAttribute("edit");

		if (edit)
			da.updateEvaluation(evaluation);
		else
			da.insertEvaluation(evaluation);

		session.removeAttribute("edit");
		session.setAttribute("evaluation", da.getEvaluations());
			
		return "evalResult.html";
	}

	// Edit an existing evaluation
	@GetMapping("/edit/{id}")
	public String edit(HttpSession session, 
					Model model, 
					@PathVariable int id) {

		Evaluation e = da.getEvaluationById(id);
		model.addAttribute("evaluation", e);
		session.setAttribute("edit", true);

		return "evaluation.html";
	}
	
	// Stay on the same page to add more new courses
	@PostMapping("/insertCourse")
	public String insertCourse(HttpSession session, 
					Model model, 
					@ModelAttribute Course course) {

		int origNumRec = da.getCourses().size();
		da.insertCourse(course);
		int newNumRec = da.getCourses().size();

		if (newNumRec == origNumRec + 1)
			session.setAttribute("message", "Added successfully!");

		Course c = new Course();
		model.addAttribute("course", c);

		return "course.html";
	}

	// Load a new page to add a new course
	@GetMapping("/addCourse")
	public String addCourse(HttpSession session, Model model) {

		session.removeAttribute("message");

		Course c = new Course();
		model.addAttribute("course", c);

		return "course.html";
	}
	
	// Display all courses - completed and not completed
	// calculate and display the cumulative GPA for all completed courses
	@GetMapping("/cumGPA")
	public String calculateCumGpa(Model model,
					HttpSession session) {

		List<Course> courses = new CopyOnWriteArrayList<Course>();
		courses = da.getCourses();
		Collections.sort(courses);

		List<Course> terms = new CopyOnWriteArrayList<Course>();
		terms = da.getTerms();

		session.setAttribute("courses", courses);
		session.setAttribute("terms", terms);

		Map<Evaluation, Course> evaluations = new HashMap<Evaluation, Course>();
		evaluations = da.getEvaluations();

		double weightedCreditTotal = 0.0;
		double creditTotal = 0.0;
		
		// For each course with a final grade, determine the grade points.  
		// Calculate weighted credit value = 
		// the grade points x the course's credit value.  
		// For each course, add weighted credit value 
		// to a total, and also add the course's credit value to a total).  
		for (Map.Entry<Evaluation, Course> e : evaluations.entrySet()) {
			for (Course c : courses) {
				if (e.getKey().getCourse() == c.getCode() && c.isComplete()) {

					weightedCreditTotal += (c.gradePoints() * c.getCredits());
					creditTotal += c.getCredits();
				}
			}
		}
		// after all completed courses processed, calculate cumulative GPA 
		// as weighted credit total / total credits
		session.setAttribute("cumGPA", weightedCreditTotal / creditTotal);

		return "cumGPA.html";
	}

	// Display all courses - completed and not completed for a specific term
	// calculate and display the term GPA for all completed courses
	@GetMapping("/term")
	public String calculateTermGPA(Model model, 
					@RequestParam("term") int term, 
					HttpSession session) {

		model.addAttribute("term", term);

		List<Course> termCourses = new CopyOnWriteArrayList<Course>();
		termCourses = da.getCoursesByTerm(term);
		Collections.sort(termCourses);

		Map<Evaluation, Course> evaluations = new HashMap<Evaluation, Course>();
		evaluations = da.getEvaluations();

		double weightedTermCreditTotal = 0.0;
		double termCreditTotal = 0.0;

		session.setAttribute("courses", termCourses);
		
		// For each course for a specific term with a final grade, 
		// determine the grade points.  
		// Calculate weighted term credit value = 
		// the grade points x the course's credit value.  
		// For each course for for the particular term, 
		// add weighted term credit value to a total, 
		// and also add the term course's credit value to a total). 
		for (Map.Entry<Evaluation, Course> e : evaluations.entrySet()) {
			for (Course t : termCourses) {
				if (e.getKey().getCourse() == t.getCode() && t.isComplete() 
						&& t.getTerm() == term) {

					weightedTermCreditTotal += (t.gradePoints() * 
							t.getCredits());
					termCreditTotal += t.getCredits();
				}
			}
		}
		// after all completed courses processed for a specific term, 
		// calculate term GPA as weighted term credit total / total term credits
		session.setAttribute("termGPA", weightedTermCreditTotal / 
					termCreditTotal);

		return "termGPA.html";
	}
	
	@PostMapping("/logout")
	public String postLogout() {
		return"index.html";
	}
	
	@GetMapping("/login") 
	public String login() {
			return "login.html";
	}
	
	// returns value to the web response body
	@GetMapping(value="/{code}", produces="application/json")
	@ResponseBody
	public Map<String,Object> getCourseByCode(@PathVariable String code, 
				RestTemplate rest) {
			
		Map<String, Object> data = new HashMap<String, Object>();
		ResponseEntity<Course> responseEntity = 
				rest.getForEntity("https://localhost:8443/course/{code}", 
						Course.class, code);
		data.put("course", responseEntity.getBody());

		return data;	
	}
}
