package ru.hogwarts.school.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.hogwarts.school.model.Student;
import static org.assertj.core.api.Assertions.assertThat;


class StudentServiceTest {
    StudentService studentService = new StudentService();
    @BeforeEach
    void setUp() {
        studentService.addStudent(new Student(0L,"Harry Potter", 15));
        studentService.addStudent(new Student(0L,"Germione Granger", 15));
        studentService.addStudent(new Student(0L,"Ron Weasley", 15));
    }

    @Test
    void addStudentTest() {
        studentService.addStudent(new Student(0L,"Draco Malfoy", 14));
        Student actual = studentService.findStudent(4L);
        assertThat(actual.getName()).isEqualTo("Draco Malfoy");
        assertThat(actual.getAge()).isEqualTo(14);
    }

    @Test
    void findStudentTest() {
        Student actual = studentService.findStudent(1);
        assertThat(actual.getName()).isEqualTo("Harry Potter");
        assertThat(actual.getAge()).isEqualTo(15);
    }

    @Test
    void editStudentTest() {
        Student actual = studentService.editStudent(new Student(2L,"Hermione Granger", 15));
        assertThat(actual.getName()).isEqualTo("Hermione Granger");
        assertThat(actual.getAge()).isEqualTo(15);
    }

    @Test
    void removeStudentTest() {
        studentService.removeStudent(3L);
        assertThat(studentService.findStudent(3L)).isEqualTo(null);
    }
}