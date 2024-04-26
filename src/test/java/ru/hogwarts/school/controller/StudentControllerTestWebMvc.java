package ru.hogwarts.school.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;
import java.util.List;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class StudentControllerTestWebMvc {
    @Autowired
    MockMvc mvc;
    @MockBean
    StudentRepository studentRepository;
    @SpyBean
    StudentService studentService;
    @MockBean
    FacultyService facultyService;
    @MockBean
    AvatarRepository avatarRepository;

    @InjectMocks
    StudentController controller;

    Student testStudent = new Student();
    Student testedStudent = new Student();
    Student addedStudent = new Student("Added_Student", 15);
    Faculty testFaculty = new Faculty("Test_Faculty", "Test_Color");

    @BeforeEach
    void setUp() {
        testStudent.setName("Test_Student");
        testStudent.setAge(15);
        testStudent.setId(1L);
        testStudent.setFaculty(testFaculty);
        testFaculty.setId(1L);
        testedStudent.setName("Tested_Student");
        testedStudent.setAge(16);
        testedStudent.setId(1L);
    }

    @Test
    void getStudentTest() throws Exception {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(testStudent));
        mvc.perform(MockMvcRequestBuilders.get("/student/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test_Student"))
                .andExpect(jsonPath("$.age").value(15));
    }

    @Test
    void editStudentTest() throws Exception {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(testStudent));

        when(studentRepository.save(any(Student.class))).thenReturn(testedStudent);

        ObjectMapper objectMapper = new ObjectMapper();

        mvc.perform(MockMvcRequestBuilders.put("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testedStudent)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Tested_Student"))
                .andExpect(jsonPath("$.age").value(16));
    }

    @Test
    void removeStudentTest() throws Exception {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(testStudent));
        mvc.perform(MockMvcRequestBuilders.delete("/student/1"))
                .andExpect(status().isOk());
    }

    @Test
    void addStudentTest() throws Exception {
        when(studentRepository.save(any(Student.class))).then((invocationOnMock -> {
            Student input = invocationOnMock.getArgument(0, Student.class);
            Student f = new Student();
            f.setId(999L);
            f.setName(input.getName());
            f.setAge(input.getAge());
            return f;
        }));

        ObjectMapper objectMapper = new ObjectMapper();
        mvc.perform(MockMvcRequestBuilders.post("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addedStudent)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(999L))
                .andExpect(jsonPath("$.name").value("Added_Student"))
                .andExpect(jsonPath("$.age").value(15));
    }

    @Test
    void findByAgeTest() throws Exception {
        when(studentRepository.findByAge(anyInt()))
                .thenReturn(List.of(
                        testStudent,
                        addedStudent
                ));
        mvc.perform(MockMvcRequestBuilders.get("/student/ageFilter?age=15"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test_Student"))
                .andExpect(jsonPath("$[0].age").value(15))
                .andExpect(jsonPath("$[1].name").value("Added_Student"))
                .andExpect(jsonPath("$[1].age").value(15));
    }

    @Test
    void getStudentFacultyTest() throws Exception {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(testStudent));

        mvc.perform(MockMvcRequestBuilders.get("/student/1/faculty"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test_Faculty"))
                .andExpect(jsonPath("$.color").value("Test_Color"));
    }

}