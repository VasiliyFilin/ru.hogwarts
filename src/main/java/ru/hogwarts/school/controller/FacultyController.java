package ru.hogwarts.school.controller;

import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
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
    public Faculty getFaculty(@PathVariable Long id) {
        return facultyService.findFaculty(id);
    }

    @PostMapping // POST http://localhost:8080/faculty
    public Faculty addFaculty(@RequestBody Faculty faculty) {
        return facultyService.addFaculty(faculty);
    }

    @PutMapping// PUT http://localhost:8080/faculty
    public Faculty editFaculty(@RequestBody Faculty faculty) {
        return facultyService.editFaculty(faculty);
    }

    @DeleteMapping("{id}") // DELETE http://localhost:8080/faculty/2
    public Faculty removeFaculty(@PathVariable Long id) {
        return facultyService.removeFaculty(id);
    }
    @GetMapping("colorFilter") // GET http://localhost:8080/faculty
    public Collection<Faculty> colorFilter(@RequestParam String color) {
        return facultyService.colorFilter(color);
    }

}
