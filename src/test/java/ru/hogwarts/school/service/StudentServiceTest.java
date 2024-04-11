package ru.hogwarts.school.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {
    @Mock
    StudentRepository studentRepository;
    @Mock
    AvatarRepository avatarRepository;
    @InjectMocks
    StudentService studentService;
    Avatar avatar = new Avatar();
    Long studentId = 1L;
    String mediaType = "image/png";

    Student student1 = new Student("Harry Potter", 15);
    Student student2 = new Student("Germione Granger", 15);
    Student editedStudent2 = new Student("Hermione Granger", 15);
    Student student3 = new Student("Ron Weasley", 15);
    List<Student> students = new ArrayList<>(List.of(student1, student2, student3));
    Student student4 = new Student("Draco Malfoy", 14);


    @Test
    void addStudentTest() {
        when(studentRepository.save(student4)).thenReturn(student4);
        assertEquals(student4, studentService.addStudent(student4));
    }

    @Test
    void findStudentTest() {
        when(studentRepository.findById(1L)).thenReturn(Optional.ofNullable(student1));
        Student actual = studentService.findStudent(1);
        assertThat(actual.getName()).isEqualTo(student1.getName());
        assertThat(actual.getAge()).isEqualTo(15);
    }

    @Test
    void editStudentTest() {
        when(studentRepository.findById(student2.getId())).thenReturn(Optional.ofNullable(student2));
        when(studentRepository.save(student2)).thenReturn(editedStudent2);
        Student actual = studentService.editStudent(student2);
        assertThat(actual.getName()).isEqualTo(student2.getName());
        assertThat(actual.getAge()).isEqualTo(15);
    }
    @BeforeEach
    void setUp() {
        avatar.setId(1L);
        avatar.setMediaType(mediaType);
    }
    @Test
    void findAvatarTest() {
        when(avatarRepository.findByStudentId(studentId)).thenReturn(Optional.ofNullable(avatar));
        Avatar actual = studentService.findAvatar(studentId);
        assertEquals(mediaType, actual.getMediaType());
    }
}