package ru.hogwarts.school.service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

@Service
@Transactional
public class StudentService {
    private final static Logger logger = LoggerFactory.getLogger(StudentService.class);
    private final StudentRepository studentRepository;


    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
        logger.info("@Bean StudentService is created");
    }

    public Student addStudent(Student student) {
        logger.info("addStudent was invoked.");
        return studentRepository.save(student);
    }

    public Student findStudent(long id) {
        logger.info("findStudent was invoked.");
        return studentRepository.findById(id).orElse(null);
    }

    public Student editStudent(Student student) {
        logger.info("editStudent was invoked.");
        return studentRepository.findById(student.getId())
                .map(e -> studentRepository.save(student))
                .orElse(null);
    }

    public void removeStudent(long id) {
        logger.info("removeStudent was invoked.");
        studentRepository.deleteById(id);
    }

    public Collection<Student> findByAge(int age) {
        logger.info("findByAge was invoked.");
        return studentRepository.findByAge(age);
    }

    public Collection<Student> findByAgeBetween(int age, Integer maxAge) {
        logger.info("findByAgeBetween was invoked.");
        return studentRepository.findByAgeBetween(age, maxAge);
    }

    public Collection<Student> findAll() {
        logger.info("findAll was invoked.");
        return studentRepository.findAll();
    }

    public Integer getAllStudentsAmount() {
        logger.info("getAllStudentsAmount was invoked.");
        return studentRepository.getAllStudentsAmount();
    }

    public Integer getStudentsAvgAge() {
        logger.info("getStudentsAvgAge was invoked.");
        return studentRepository.getStudentsAvgAge();
    }

    public List<Student> getLastStudents() {
        logger.info("getLastStudents was invoked.");
        return studentRepository.getLastStudents();
    }

    public Collection<String> findNamesStartWithCharA() {
        logger.info("findNamesStartWithCharA was invoked.");
        return studentRepository.findAll()
                .stream()
                .map(Student::getName)
                .map(String::toUpperCase)
                .filter(name -> name.startsWith("A"))
                .sorted()
                .toList();
    }

    public double findAllAvgAge() {
        logger.info("findAllAvgAge was invoked.");
        return studentRepository.findAll()
                .stream()
                .mapToDouble(Student::getAge)
                .average()
                .orElse(0);
    }
}
