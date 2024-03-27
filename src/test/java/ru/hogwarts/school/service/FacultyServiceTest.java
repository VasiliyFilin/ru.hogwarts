package ru.hogwarts.school.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.hogwarts.school.model.Faculty;
import static org.assertj.core.api.Assertions.assertThat;

class FacultyServiceTest {

    FacultyService facultyService = new FacultyService();
    @BeforeEach
    void setUp() {
        facultyService.addFaculty(new Faculty(0L,"Gryffindor", "Red"));
        facultyService.addFaculty(new Faculty(0L,"Slytherinn", "Green"));
        facultyService.addFaculty(new Faculty(0L,"Ravenclaw", "Blue"));
    }

    @Test
    void addStudentTest() {
        facultyService.addFaculty(new Faculty(0L,"Hufflepuff", "Yellow"));
        Faculty actual = facultyService.findFaculty(4L);
        assertThat(actual.getName()).isEqualTo("Hufflepuff");
        assertThat(actual.getColor()).isEqualTo("Yellow");
    }

    @Test
    void findStudentTest() {
        Faculty actual = facultyService.findFaculty(1);
        assertThat(actual.getName()).isEqualTo("Gryffindor");
        assertThat(actual.getColor()).isEqualTo("Red");
    }

    @Test
    void editStudentTest() {
        Faculty actual = facultyService.editFaculty(new Faculty(2L,"Slytherin", "Green"));
        assertThat(actual.getName()).isEqualTo("Slytherin");
        assertThat(actual.getColor()).isEqualTo("Green");
    }

    @Test
    void removeStudentTest() {
        facultyService.removeFaculty(3L);
        assertThat(facultyService.findFaculty(3L)).isEqualTo(null);
    }
}