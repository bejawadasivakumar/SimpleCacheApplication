package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Student;
import com.example.demo.service.StudentService;

@RestController
@RequestMapping("/api")
public class StudentController {
	
	
	private final StudentService studentService;
	
	
	public StudentController(StudentService studentService) {
		super();
		this.studentService = studentService;
	}


	@PostMapping("/create")
	public ResponseEntity<?> createStudent(@RequestBody Student student){
		return new ResponseEntity<>(studentService.create(student), HttpStatus.CREATED);
	}
	
	
	@GetMapping("/get/{rollNo}")
	public ResponseEntity<?> getStudent(@PathVariable String rollNo){
		Student st = studentService.getStudentByRollNo(rollNo);
		if(st != null) {
		return new ResponseEntity<>(st,HttpStatus.OK);
		}
		return new ResponseEntity<>("student not found", HttpStatus.NOT_FOUND);
	}
	
	@PutMapping("/update")
	public ResponseEntity<?> updateStudent(@RequestBody Student student){
	    return new ResponseEntity<>(studentService.updateStudent(student),HttpStatus.OK);
	}
	
	@DeleteMapping("/delete/{rollNo}")
	public ResponseEntity<?> deleteStudent(@PathVariable String rollNo){

	    boolean deleted = studentService.deleteStudent(rollNo);

	    if(deleted) {
	        return new ResponseEntity<>("Deleted Successfully", HttpStatus.OK);
	    }

	    return new ResponseEntity<>("Student Not Found", HttpStatus.NOT_FOUND);
	}
	
	@GetMapping("/cache")
	public ResponseEntity<?> getCacheData(){
		return new ResponseEntity<>(studentService.printCacheData(), HttpStatus.OK);
		
	}
}
