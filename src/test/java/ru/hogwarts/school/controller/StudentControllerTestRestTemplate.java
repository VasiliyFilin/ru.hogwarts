package ru.hogwarts.school.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentControllerTestRestTemplate {
    @LocalServerPort
    private int port;

    @Autowired
    private StudentController studentController;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoadsTest() throws Exception {
        Assertions.assertThat(studentController).isNotNull();
    }

    @Test
    public void getStudentTest() throws Exception {
        Student testStudent = new Student("Test_Student", 16);
        ResponseEntity<Student> postResponse = restTemplate.postForEntity(
                "/student", testStudent, Student.class);
        Student postResult = postResponse.getBody();

        Student result = restTemplate.getForObject("/student/" + postResult.getId(), Student.class);
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Test_Student");
        assertThat(result.getAge()).isEqualTo(16);
        assertThat(this.restTemplate.exchange("/student/-1", HttpMethod.GET, null, Student.class)
                .getStatusCode().value()).isEqualTo(404);
    }

    @Test
    public void postStudentTest() throws Exception {
        Student testStudent = new Student("Test_Student", 16);
        ResponseEntity<Student> postResponse = restTemplate.postForEntity(
                "/student", testStudent, Student.class);
        Student postResult = postResponse.getBody();

        assertThat(restTemplate.postForObject(
                "/student", postResult, String.class))
                .isNotNull();
    }
    @Test
    void removeStudentTest() {
        Student testStudent = new Student("Test_Student", 16);
        ResponseEntity<Student> postResponse = restTemplate.postForEntity(
                "/student", testStudent, Student.class);
        Student postResult = postResponse.getBody();

        restTemplate.delete("/student/" + postResult.getId());

        ResponseEntity<Student> checkDelete = restTemplate.exchange(
                "/student/" + postResult.getId(), HttpMethod.GET, null, Student.class);
        assertThat(checkDelete.getStatusCode().value()).isEqualTo(404);
    }
    @Test
    void editStudentTest() {
        Student testStudent = new Student("Test_Student", 16);
        ResponseEntity<Student> postResponse = restTemplate.postForEntity(
                "/student", testStudent, Student.class);
        Student postResult = postResponse.getBody();
        postResult.setName("Tested_Student");
        postResult.setAge(17);

        restTemplate.put("/student", postResult);

        Student result = restTemplate.getForObject("/student/" + postResult.getId(), Student.class);
        assertThat(result.getName()).isEqualTo("Tested_Student");
        assertThat(result.getAge()).isEqualTo(17);
    }
    @Test
    void findByAgeTest() {
        Student ts1 = restTemplate.postForEntity("/student",
                new Student("Test_Student1", 15), Student.class).getBody();
        Student ts2 = restTemplate.postForEntity("/student",
                new Student("Test_Student2", 16), Student.class).getBody();
        Student ts3 = restTemplate.postForEntity("/student",
                new Student("Test_Student3", 17), Student.class).getBody();
        Student ts4 = restTemplate.postForEntity("/student",
                new Student("Test_Student4", 15), Student.class).getBody();

        Student[] students = restTemplate.getForObject("/student/ageFilter?age=15", Student[].class);
        assertThat(students.length).isEqualTo(2);
        assertThat(students).containsExactlyInAnyOrder(ts1, ts4);
    }
}