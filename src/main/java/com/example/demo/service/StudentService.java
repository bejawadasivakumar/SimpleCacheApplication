package com.example.demo.service;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Student;
import com.example.demo.repository.StudentRepository;

@Service
public class StudentService {
	

	private final StudentRepository studentRepository;
	private final CacheManager cacheManager;
	
	public StudentService(StudentRepository studentRepository, CacheManager cacheManager) {
		super();
		this.studentRepository = studentRepository;
		this.cacheManager = cacheManager;
	}

	public String create(Student student) {
		Student st = new Student();
		st.setFirstName(student.getFirstName());
		st.setLastName(student.getLastName());
		st.setRollNo(student.getRollNo());
		studentRepository.save(st);
		return "created";
	}
	 
	
	//  unless = "#result == null" -> it is used because if the rollNo is not in DB then this method
	// returns null object and null object is stored in the cache. Without storing null objects we use
	// unless = "#result == null" to avoid those null objects.
	@Cacheable(value ="students", key = "#rollNo", unless = "#result == null")
	public Student getStudentByRollNo(String rollNo) {
		
		System.out.println("Fetching data from DB");
		
		return studentRepository.findByRollNo(rollNo);
		
	}
	
	@CachePut(value = "students", key = "#student.rollNo")
	public Student updateStudent(Student student) {
		
		System.out.println("Update method is executing...");//this line shows whether this method is executing or not
		
		Student existingStudent = studentRepository.findByRollNo(student.getRollNo());
		if(existingStudent != null) {
			existingStudent.setFirstName(student.getFirstName());
			existingStudent.setLastName(student.getLastName());
			studentRepository.save(existingStudent);
			return existingStudent;
		}
		return null;
	}
	
	@CacheEvict(value = "students", key = "#rollNo")
	public boolean deleteStudent(String rollNo) {
		
		System.out.println("delete method is executing...");//this line shows whether this method is executing or not
		
		Student st = studentRepository.findByRollNo(rollNo);
		if(st != null) {
		studentRepository.delete(st);
		return true;
	}
		return false;
	}
	
	
	// print the Data present in the cache (concurrentHashMap)
	public Object printCacheData(){
		Cache cache = cacheManager.getCache("students");
		if(cache != null) {
			return cache.getNativeCache();
		}
		return "No cache found";
	}
	
	/*See All Cache Names
	System.out.println(cacheManager.getCacheNames());

	Output:
	[students] */
}
