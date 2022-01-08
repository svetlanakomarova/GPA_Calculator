package ca.sheridancollege.komarovs.assignment3.database;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import ca.sheridancollege.komarovs.assignment3.beans.Course;
import ca.sheridancollege.komarovs.assignment3.beans.Evaluation;
import ca.sheridancollege.komarovs.assignment3.beans.User;


@Repository
public class DatabaseAccess {
	
	@Autowired
	protected NamedParameterJdbcTemplate jdbc;
	
	/* Retrieves all courses and sort by title and then by code. */
	public List<Course> getCourses() {
		
		// build an sql query to retrieve all records from courses table
		// and sort them by course title and then course code
		String sql = "SELECT * FROM courses ORDER BY title, code;";
		
		// execute query and build the list of courses
		ArrayList<Course> courses = (ArrayList<Course>) jdbc.query(sql,
				new BeanPropertyRowMapper<Course>(Course.class));
		
		return courses;
	}
	
	/* Retrieves all courses for a specific term */
	public List<Course> getCoursesByTerm(int term) {
		
		// build an sql query to retrieve all courses for specific term 
		// from courses table	
		String sql = "SELECT * FROM COURSES WHERE term=:term;";		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("term", term);
		
		// execute query and build the list of courses for specific term
		ArrayList<Course> courses = (ArrayList<Course>) jdbc.query(sql,params,
				new BeanPropertyRowMapper<Course>(Course.class));

		return courses;
	}
	
	/* Inserts a new course to the courses table */
	public void insertCourse(Course c) {
		
		// build an sql query to insert the course
		String sql ="INSERT INTO courses (code, title, credits) "
				+ "VALUES (:code, :title, :credits);";
		
		// map params to course object members
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("code", c.getCode()).addValue("title", c.getTitle())
		.addValue("credits", c.getCredits());

		// execute query
		jdbc.update(sql, params);	
	}
	
	/* Inserts a new evaluation to the evaluations table */
	public void insertEvaluation(Evaluation e) {
		
		// build an sql query to insert the evaluation
		String sql ="INSERT INTO evaluations (title, course, grade, "
				+ "max, weight, duedate) VALUES (:title, :course, :grade, :max,"
				+ " :weight, :duedate);";
		
		// map params to evaluation object members
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("title", e.getTitle()).addValue("course", e.getCourse())
		.addValue("grade", e.getGrade()).addValue("max", e.getMax())
		.addValue("weight", e.getWeight()).addValue("duedate", e.getDueDate());
		
		// execute query
		jdbc.update(sql, params);	
	}
	
	/* Retrieves evaluations for all assignments/courses */
	public Map<Evaluation, Course> getEvaluations() {
		
		// build an sql query to retrieve all evaluations for all courses
		String sql = "SELECT * FROM evaluations INNER JOIN courses ON "
				+ "evaluations.course = courses.code ORDER BY "
				+ "evaluations.duedate, courses.code";

		// construct evaluation object first
		// use it as a key in a hash map where value is the course obj
		Map<Evaluation, Course> map = new HashMap<Evaluation, Course>();
		List<Map<String, Object>> rows = jdbc.queryForList(sql, new HashMap());
		
		for (Map<String, Object> row : rows) {
			
			Evaluation eval = new Evaluation (((Integer)row.get("id"))
					.intValue(),
					(String)row.get("title"),
					(String)row.get("course"),
					((BigDecimal)row.get("grade")).doubleValue(),
					((BigDecimal)row.get("max")).doubleValue(),
					((BigDecimal)row.get("weight")).doubleValue(),
					((java.sql.Date)row.get("duedate")).toLocalDate());
			
			Course c = new Course ((String)row.get("code"),
					(String)row.get("title"),
					((BigDecimal)row.get("credits")).doubleValue());
			
			map.put(eval, c);
		}
		return map;
	}
	
	/* Retrieves evaluation for a specific assignment/course */
	public Evaluation getEvaluationById(int id) {
		
		// build an sql query to retrieve all memebers for specific evaluation 
		String sql = "SELECT * FROM evaluations WHERE id=:id;";
		
		// map params to evaluation object id
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id", id);
		
		// execute query
		ArrayList<Evaluation> list = (ArrayList<Evaluation>) jdbc.query(sql, 
			   params, new BeanPropertyRowMapper<Evaluation>(Evaluation.class));
		
		// one record with the index = 0 in the list
		// the evaluation object with specific id
		if (list.size() > 0)
			return list.get(0);
		else
			return null;		
	}
	
	/* Edits evaluation members */
	public int updateEvaluation(Evaluation e) {
		
		// build an sql query to edit all members except id member
		// for specific evaluation 
		String sql = "UPDATE evaluations SET title=:title, "
				+ "course=:course, grade=:grade, max=:max,"
				+ "weight=:weight, duedate=:duedate"
				+ " WHERE id=:id;";
		
		// map params to evaluation object members
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id", e.getId()).addValue("title", e.getTitle())
		.addValue("course", e.getCourse()).addValue("grade", e.getGrade())
		.addValue("max", e.getMax()).addValue("weight", e.getWeight())
		.addValue("duedate", e.getDueDate());
		
		// execute query
		return jdbc.update(sql, params);
	}
	
	/* Retrieves all terms */
	public List<Course> getTerms() {
		
		// build an sql query to retrieve all terms 
		String sql = "SELECT DISTINCT term FROM courses"
				+ " ORDER BY term;";
		
		// execute query and build the list of terms
		ArrayList<Course> terms = (ArrayList<Course>) jdbc.query(sql,
				new BeanPropertyRowMapper<Course>(Course.class));
		
		return terms;
	}	
	
	/* Searches for specific user by username */
	public User findUserAccount(String username) {
		
		// build an sql query to retrieve all memebers for specific user 
		String sql = "SELECT * FROM users WHERE username=:username;";
		
		// map params to user object username
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("username", username);
		
		// execute query
		ArrayList<User> users = (ArrayList<User>)jdbc.query(sql, params,
				new BeanPropertyRowMapper<User>(User.class));
		
		// one record with the index = 0 in the list
		// the user object with specific username
		if (users.size() > 0)
			return users.get(0);
		else
			return null;	
	}
	
	/* Retrieves all roles for a specific user */
	public List<String> getRolesById(long userId) {
		
		// build an sql query to retrieve user id and all role names from 
		//user_role and roles tables by matching roles to specific user id
		String sql = "SELECT user_role.userid, roles.rolename"
				+ " FROM user_role, roles WHERE user_role.roleid = roles.roleid"
				+ " AND user_role.userid=:userid;";
		
		// map params to user object userId
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("userid", userId);
		
		// execute query
		ArrayList<String> roles = new ArrayList<String>();
		
		// execute query and build the list of roles for specific user
		List<Map<String, Object>> rows = jdbc.queryForList(sql, params);
		for (Map<String, Object> row : rows) {
			roles.add((String)row.get("roleName"));
		}
		return roles;
	}
	
	/* Retrieves course info by a specific code */
	public Course getCourseByCode(String code) {
		
		// build an sql query to retrieve all members for specific course 
		// from courses table	
		String sql = "SELECT * FROM COURSES WHERE code=:code;";		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("code", code);
		
		ArrayList<Course> list = (ArrayList<Course>) jdbc.query(sql,params,
				new BeanPropertyRowMapper<Course>(Course.class));
		
		// one record with the index = 0 in the list
		// the course object with specific code
		if (list.size() > 0)
			return list.get(0);
		else
			return null;
	}
}
