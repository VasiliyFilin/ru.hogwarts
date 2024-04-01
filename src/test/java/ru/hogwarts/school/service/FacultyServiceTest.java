package ru.hogwarts.school.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FacultyServiceTest {
    @Mock
    FacultyRepository facultyRepository;
    @InjectMocks
    FacultyService facultyService;


    Faculty faculty1 = new Faculty("Gryffindor", "Red");
    Faculty faculty2 = new Faculty("Slytherinn", "Green");
    Faculty editedFaculty2 = new Faculty("Slytherin", "Green");
    Faculty faculty3 = new Faculty("Ravenclaw", "Blue");
    List<Faculty> faculties = new ArrayList<>(List.of(faculty1, faculty2, faculty3));
    Faculty faculty4 = new Faculty("Hufflepuff", "Yellow");


    @Test
    void addStudentTest() {
        when(facultyRepository.save(faculty4)).thenReturn(faculty4);
        assertEquals(faculty4, facultyService.addFaculty(faculty4));
    }

    @Test
    void findStudentTest() {
        when(facultyRepository.findById(1L)).thenReturn(Optional.ofNullable(faculty1));
        Faculty actual = facultyService.findFaculty(1);
        assertThat(actual.getName()).isEqualTo("Gryffindor");
        assertThat(actual.getColor()).isEqualTo("Red");
    }

    @Test
    void editStudentTest() {
        when(facultyRepository.save(editedFaculty2)).thenReturn(editedFaculty2);
        Faculty actual = facultyService.editFaculty(editedFaculty2);
        assertThat(actual.getName()).isEqualTo("Slytherin");
        assertThat(actual.getColor()).isEqualTo("Green");
    }


}