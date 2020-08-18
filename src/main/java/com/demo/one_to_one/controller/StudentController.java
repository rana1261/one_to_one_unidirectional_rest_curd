package com.demo.one_to_one.controller;

import java.util.List;

import com.demo.one_to_one.exception.NotFoundException;
import com.demo.one_to_one.model.Laptop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.demo.one_to_one.model.Student;
import com.demo.one_to_one.repository.LaptopRepository;
import com.demo.one_to_one.repository.StudentRepository;

import javax.validation.Valid;

@RestController
public class StudentController {
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    LaptopRepository laptopRepository;

    /*post student  and laptop data but do not add id for student and lid for laptop in json*/
    @PostMapping(value = "students")
    public void saveStudent(@Valid @RequestBody Student student) {

        studentRepository.save(student);
    }

    /*get a student and laptop data*/
    @GetMapping(value = "students/{id}")
    public Student getStudent(@PathVariable Long id) {
        return studentRepository.getOne(id);
    }


    /*get all student and laptop data*/
    @GetMapping(value = "students")
    public List<Student> allStudent() {
        return studentRepository.findAll();
    }

    /*delete student and laptop using student id*/
    @DeleteMapping(value = "students/{id}")
    public void delete(@PathVariable Long id) {
        studentRepository.deleteById(id);
    }

    /*update  a Student data and update its child(laptop) data but not add id for student table and lid for laptop table in json*/
    @PutMapping("/students/{id}")
    public Student updateStudent(@PathVariable Long id,
                                 @Valid @RequestBody Student studentUpdated) {
        return studentRepository.findById(id)
                .map(student -> {
                    student.setStudentName(studentUpdated.getStudentName());
                    student.setStudentMark(studentUpdated.getStudentMark());
                    Laptop laptop = student.getLaptop();
                    laptop.setLaptopName(studentUpdated.getLaptop().getLaptopName());
                    student.setLaptop(laptop);
                    return studentRepository.save(student);
                }).orElseThrow(() -> new NotFoundException("Student not found with id " + id));
    }


/*	@PutMapping(value="students/update")
	public void updateStudent(@RequestBody Student student) {
		Student su=studentRepository.getOne(student.getId());
		su.setStudentName(student.getStudentName());
		su.setStudentMark(student.getStudentMark());
		su.setLaptop(student.getLaptop());
		studentRepository.save(su);
	}
	*/

}
