package ru.hogwarts.school.service;

import java.util.Collection;
import java.util.List;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;

@Service
@Transactional
public class StudentService {
    @Value("avatars")
    private String avatarsDir;
    private final StudentRepository studentRepository;
    private final AvatarRepository avatarRepository;

    public StudentService(StudentRepository studentRepository, AvatarRepository avatarRepository) {
        this.studentRepository = studentRepository;
        this.avatarRepository = avatarRepository;
    }

    public Student addStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student findStudent(long id) {
        return studentRepository.findById(id).orElse(null);
    }

    public Student editStudent(Student student) {
        return studentRepository.findById(student.getId())
                .map(e -> studentRepository.save(student))
                .orElse(null);
    }

    public void removeStudent(long id) {
        studentRepository.deleteById(id);
    }

    public Collection<Student> findByAge(int age) {
        return studentRepository.findByAge(age);
    }

    public Collection<Student> findByAgeBetween(int age, Integer maxAge) {
        return studentRepository.findByAgeBetween(age, maxAge);
    }

    public Collection<Student> findAll() {
        return studentRepository.findAll();
    }

    public Integer getAllStudentsAmount() {
        return studentRepository.getAllStudentsAmount();
    }

    public Integer getStudentsAvgAge() {
        return studentRepository.getStudentsAvgAge();
    }

    public List<Student> getLastStudents() {
        return studentRepository.getLastStudents();
    }

    public List<Avatar> getAvatarPage(Integer pageNum, Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNum - 1, pageSize);
        return avatarRepository.findAll(pageRequest).getContent();
    }
}
