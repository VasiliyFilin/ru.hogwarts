package ru.hogwarts.school.controller;

import java.util.Collection;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

@RestController
@RequestMapping("student")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("{id}") // GET http://localhost:8080/student/2
    public ResponseEntity<Student> getStudent(@PathVariable Long id) {
        Student student = studentService.findStudent(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @PostMapping // POST http://localhost:8080/student
    public Student addStudent(@RequestBody Student student) {
        return studentService.addStudent(student);
    }

    @PutMapping// PUT http://localhost:8080/student
    public ResponseEntity<Student> editStudent(@RequestBody Student student) {
        Student foundStudent = studentService.editStudent(student);
        if (student == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(foundStudent);
    }

    @DeleteMapping("{id}") // DELETE http://localhost:8080/student/2
    public ResponseEntity<Student> removeStudent(@PathVariable Long id) {
        studentService.removeStudent(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("ageFilter") // GET http://localhost:8080/student
    public Collection<Student> findByAge(@RequestParam int age,
                                         @RequestParam(required = false) Integer maxAge) {
        if (maxAge != null) {
            return studentService.findByAgeBetween(age, maxAge);
        }
        return studentService.findByAge(age);
    }

    @GetMapping("{id}/faculty") // GET http://localhost:8080/student/2/faculty
    public Faculty getStudentFaculty(@PathVariable Long id) {
        return studentService.findStudent(id).getFaculty();
    }
    @GetMapping("/amount")
    public Integer getAllStudentsAmount() {
        return studentService.getAllStudentsAmount();
    }
    @GetMapping("/avg-age")
    public Integer getStudentsAvgAge() {
        return studentService.getStudentsAvgAge();
    }
    @GetMapping("/lastStudents")
    public List<Student> getLastStudents() {
        return studentService.getLastStudents();
    }
    @GetMapping("/namesStartA")
    public Collection<String> findNamesStartWithCharA() {
        return studentService.findNamesStartWithCharA();
    }
    @GetMapping("/all-avg-age")
    public double findAllAvgAge() {
        return studentService.findAllAvgAge();
    }


}
