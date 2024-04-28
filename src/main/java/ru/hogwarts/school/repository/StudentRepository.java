package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Collection<Student> findByAge(int age);

    Collection<Student> findByAgeBetween(int age, Integer maxAge);

    @Query(value = "SELECT COUNT(id) FROM student", nativeQuery = true)
    Integer getAllStudentsAmount();

    @Query(value = "SELECT AVG(age) FROM student", nativeQuery = true)
    Integer getStudentsAvgAge();

    @Query(value = "SELECT * FROM student ORDER BY id DESC limit 5", nativeQuery = true)
    List<Student> getLastStudents();
}
