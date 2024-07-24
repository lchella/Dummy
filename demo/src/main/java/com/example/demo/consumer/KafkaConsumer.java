package com.example.demo.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Student;
import com.example.demo.repo.StudentRepository;

@Service
public class KafkaConsumer {
	
	@Autowired
	private StudentRepository studentRepository;

	@KafkaListener(topics = "my-topic", groupId = "my-group")
    public void listen(String message) {
        // Assume message format is "id:name:dept:cgpa"
        String[] parts = message.split(":");
        
        if (parts.length == 4) {
            try {
                Long id = Long.parseLong(parts[0]);
                String name = parts[1];
                String dept = parts[2];
                double cgpa = Double.parseDouble(parts[3]);
                
                // Create a new Student entity and save it
                Student student = new Student();
                student.setId(id);
                student.setName(name);
                student.setDept(dept);
                student.setCgpa(cgpa);
                
                studentRepository.save(student);
                
                System.out.println("Saved student: " + student);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format in message: " + message);
            }
        } else {
            System.out.println("Invalid message format: " + message);
        }
    }
}

