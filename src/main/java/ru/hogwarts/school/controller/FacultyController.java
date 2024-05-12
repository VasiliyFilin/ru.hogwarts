package ru.hogwarts.school.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;
import java.util.Collection;

@RestController
@RequestMapping("faculty")
public class FacultyController {
    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping("{id}") // GET http://localhost:8080/faculty/2
    public ResponseEntity<Faculty> getFaculty(@PathVariable Long id) {
        Faculty faculty = facultyService.findFaculty(id);
        if (faculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(faculty);
    }

    @PostMapping // POST http://localhost:8080/faculty
    public Faculty addFaculty(@RequestBody Faculty faculty) {
        return facultyService.addFaculty(faculty);
    }

    @PutMapping// PUT http://localhost:8080/faculty
    public ResponseEntity<Faculty> editFaculty(@RequestBody Faculty faculty) {
        Faculty foundFaculty = facultyService.editFaculty(faculty);
        if (foundFaculty == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(foundFaculty);
    }

    @DeleteMapping("{id}") // DELETE http://localhost:8080/faculty/2
    public ResponseEntity<Faculty> removeFaculty(@PathVariable Long id) {
        facultyService.removeFaculty(id);
        return ResponseEntity.ok().build();
    }
    @GetMapping("byNameOrColor") // GET http://localhost:8080/faculty
    public Collection<Faculty> findByNameOrColor(@RequestParam(required = false) String name,
                                                 @RequestParam(required = false) String color) {
        if (name == null && color == null) {
            return facultyService.findAll();
        }
        return facultyService.findByNameOrColor(name, color);
    }
    @GetMapping("{id}/students") // GET http://localhost:8080/faculty/2/students
    public Collection<Student> getFacultyStudent(@PathVariable Long id) {
        return facultyService.findFaculty(id).getStudents();
    }

    @GetMapping("/longestName")
    public String findLongestFacultyName() {
        return facultyService.findLongestFacultyName();
    }
}
