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
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;
import java.util.List;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest
class FacultyControllerTestWebMvc {
    @Autowired
    MockMvc mvc;
    @MockBean
    FacultyRepository facultyRepository;
    @SpyBean
    FacultyService facultyService;
    @MockBean
    StudentService studentService;

    @InjectMocks
    FacultyController controller;

    Faculty testFaculty = new Faculty();
    Faculty testedFaculty = new Faculty();

    Faculty addedFaculty = new Faculty("Added_Faculty", "Added_Color");

    @BeforeEach
    void setUp() {
        testFaculty.setName("Test_Faculty");
        testFaculty.setColor("Test_Color");
        testFaculty.setId(1L);
        testedFaculty.setName("Tested_Faculty");
        testedFaculty.setColor("Tested_Color");
        testedFaculty.setId(1L);
    }

    @Test
    void getFacultyTest() throws Exception {
        when(facultyRepository.findById(1L)).thenReturn(Optional.of(testFaculty));
        mvc.perform(MockMvcRequestBuilders.get("/faculty/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test_Faculty"))
                .andExpect(jsonPath("$.color").value("Test_Color"));
    }

    @Test
    void editFacultyTest() throws Exception {
        when(facultyRepository.findById(1L)).thenReturn(Optional.of(testFaculty));

        when(facultyRepository.save(any(Faculty.class))).thenReturn(testedFaculty);

        ObjectMapper objectMapper = new ObjectMapper();

        mvc.perform(MockMvcRequestBuilders.put("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testedFaculty)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Tested_Faculty"))
                .andExpect(jsonPath("$.color").value("Tested_Color"));
    }

    @Test
    void removeFacultyTest() throws Exception {
        when(facultyRepository.findById(1L)).thenReturn(Optional.of(testFaculty));
        mvc.perform(MockMvcRequestBuilders.delete("/faculty/1"))
                .andExpect(status().isOk());
    }

    @Test
    void addFacultyTest() throws Exception {
        when(facultyRepository.save(any(Faculty.class))).then((invocationOnMock -> {
            Faculty input = invocationOnMock.getArgument(0, Faculty.class);
            Faculty f = new Faculty();
            f.setId(999L);
            f.setName(input.getName());
            f.setColor(input.getColor());
            return f;
        }));

        ObjectMapper objectMapper = new ObjectMapper();
        mvc.perform(MockMvcRequestBuilders.post("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addedFaculty)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(999L))
                .andExpect(jsonPath("$.name").value("Added_Faculty"))
                .andExpect(jsonPath("$.color").value("Added_Color"));
    }

    @Test
    void findByNameOrColorTest() throws Exception {
        when(facultyRepository.findByNameIgnoreCaseOrColorIgnoreCase(anyString(), anyString()))
                .thenReturn(List.of(
                        testFaculty,
                        addedFaculty
                ));
        mvc.perform(MockMvcRequestBuilders.get("/faculty/byNameOrColor?name=name&color=color"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test_Faculty"))
                .andExpect(jsonPath("$[0].color").value("Test_Color"))
                .andExpect(jsonPath("$[1].name").value("Added_Faculty"))
                .andExpect(jsonPath("$[1].color").value("Added_Color"));
    }

    @Test
    void getFacultyStudentsTest() throws Exception {
        testFaculty.setStudents(List.of(
                new Student("Student1", 15),
                new Student("Student2", 16)
        ));

        when(facultyRepository.findById(1L)).thenReturn(Optional.of(testFaculty));

        mvc.perform(MockMvcRequestBuilders.get("/faculty/1/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Student1"))
                .andExpect(jsonPath("$[0].age").value(15))
                .andExpect(jsonPath("$[1].name").value("Student2"))
                .andExpect(jsonPath("$[1].age").value(16));
    }
}