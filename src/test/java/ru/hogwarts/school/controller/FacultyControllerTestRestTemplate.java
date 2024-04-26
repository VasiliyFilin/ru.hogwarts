package ru.hogwarts.school.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Faculty;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FacultyControllerTestRestTemplate {

    @LocalServerPort
    private int port;

    @Autowired
    private FacultyController facultyController;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoadsTest() throws Exception {
        assertThat(facultyController).isNotNull();
    }

    @Test
    void postFacultyTest() throws Exception {
        Faculty testFaculty = new Faculty("TestFaculty", "Test_color");
        ResponseEntity<Faculty> postResponse = restTemplate.postForEntity(
                "/faculty", testFaculty, Faculty.class);
        Faculty postResult = postResponse.getBody();

        assertThat(restTemplate.postForObject(
                "/faculty", postResult, String.class))
                .isNotNull();
    }

    @Test
    void getFacultyTest() {
        Faculty testFaculty = new Faculty("Test_Faculty", "Test_color");
        ResponseEntity<Faculty> postResponse = restTemplate.postForEntity(
                "/faculty", testFaculty, Faculty.class);
        Faculty postResult = postResponse.getBody();

        Faculty result = restTemplate.getForObject("/faculty/" + postResult.getId(), Faculty.class);
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Test_Faculty");
        assertThat(result.getColor()).isEqualTo("Test_color");
        assertThat(this.restTemplate.exchange("/faculty/-1", HttpMethod.GET, null, Faculty.class)
                .getStatusCode().value()).isEqualTo(404);
    }

    @Test
    void removeFacultyTest() {
        Faculty testFaculty = new Faculty("Test_Faculty", "Test_color");
        ResponseEntity<Faculty> postResponse = restTemplate.postForEntity(
                "/faculty", testFaculty, Faculty.class);
        Faculty postResult = postResponse.getBody();

        restTemplate.delete("/faculty/" + postResult.getId());

        ResponseEntity<Faculty> checkDelete = restTemplate.exchange(
                "/faculty/" + postResult.getId(), HttpMethod.GET, null, Faculty.class);
        assertThat(checkDelete.getStatusCode().value()).isEqualTo(404);
    }

    @Test
    void editFacultyTest() {
        Faculty testFaculty = new Faculty("Test_Faculty", "Test_color");
        ResponseEntity<Faculty> postResponse = restTemplate.postForEntity(
                "/faculty", testFaculty, Faculty.class);
        Faculty postResult = postResponse.getBody();
        postResult.setName("Tested_Faculty");
        postResult.setColor("Tested_Color");

        restTemplate.put("/faculty", postResult);

        Faculty result = restTemplate.getForObject("/faculty/" + postResult.getId(), Faculty.class);
        assertThat(result.getName()).isEqualTo("Tested_Faculty");
        assertThat(result.getColor()).isEqualTo("Tested_Color");
    }

    @Test
    void findByNameOrColorTest() {
        Faculty tf1 = restTemplate.postForEntity("/faculty",
                new Faculty("Test_Faculty1", "Test_Color1"), Faculty.class).getBody();
        Faculty tf2 = restTemplate.postForEntity("/faculty",
                new Faculty("Test_Faculty2", "Test_Color2"), Faculty.class).getBody();
        Faculty tf3 = restTemplate.postForEntity("/faculty",
                new Faculty("Test_Faculty3", "Test_Color3"), Faculty.class).getBody();
        Faculty tf4 = restTemplate.postForEntity("/faculty",
                new Faculty("Test_Faculty4", "Test_Color1"), Faculty.class).getBody();

        Faculty[] faculties = restTemplate.getForObject("/faculty/byNameOrColor?name=Test_Faculty1&color=Test_Color3", Faculty[].class);
        assertThat(faculties.length).isEqualTo(2);
        assertThat(faculties).containsExactlyInAnyOrder(tf1, tf3);
    }

//    @Test     // Не работает из-за @JsonIgnore в Student и Faculty
//    void getFacultyStudentsTest() {
//        Faculty tf = new Faculty("Test_Faculty", "Test_Color");
//        Faculty tf1 = restTemplate.postForEntity("/faculty", tf, Faculty.class).getBody();
//
//        Student student1 = new Student("student1", 15);
//        Student student2 = new Student("student2", 16);
//        student1.setFaculty(tf1);
//        student2.setFaculty(tf1);
//        Student s1 = restTemplate.postForEntity("/student", student1, Student.class).getBody();
//        Student s2 = restTemplate.postForEntity("/student", student2, Student.class).getBody();
//
//        ResponseEntity<List<Student>> result = restTemplate.exchange("/faculty/" + tf1.getId() + "/students",
//                HttpMethod.GET,
//                null,
//                new ParameterizedTypeReference<>() {
//                });
//        assertThat(result.getBody()).containsExactlyInAnyOrder(
//                new Student("student1", 15),
//                new Student("student2", 16));
//    }
}