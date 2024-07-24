package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Student;
import com.example.demo.producer.KafkaProducer;
import com.example.demo.repo.StudentRepository;

@RestController
@RequestMapping("/students")
public class StudentController {
	@Autowired
	private KafkaProducer kafkaProducer;

	
	
	
	@Autowired
	private StudentRepository studentRepository;

	@GetMapping("/")
	public List<Student> getAllStudents() {
		return studentRepository.findAll();
	}

	@PostMapping("/")
	public String createStudent(@RequestBody Student student) {
		kafkaProducer.sendMessage(student.toString());
		return "Message sent to Kafka: " + student;
	}

	@GetMapping("/{id}")
	public Student getStudentById(@PathVariable Long id) {
		return studentRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + id));
	}

	@PutMapping("/{id}")
	public Student updateStudent(@PathVariable Long id, @RequestBody Student studentDetails) {
		Student student = studentRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + id));

		student.setName(studentDetails.getName());
		student.setDept(studentDetails.getDept());
		student.setCgpa(studentDetails.getCgpa());

		return studentRepository.save(student);
	}

	@DeleteMapping("/{id}")
	public void deleteStudent(@PathVariable Long id) {
		studentRepository.deleteById(id);
	}
}
