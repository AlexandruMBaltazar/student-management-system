package com.alex.studentmanagementsystem.integration;

import com.alex.studentmanagementsystem.student.Gender;
import com.alex.studentmanagementsystem.student.Student;
import com.alex.studentmanagementsystem.student.StudentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestPropertySource(
        locations = "classpath:application-it.properties"
)
@AutoConfigureMockMvc
public class StudentIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private StudentRepository studentRepository;

    private final Faker faker = new Faker();

    @AfterEach
    void tearDown() {
        studentRepository.deleteAll();
    }

    @Test
    void canRegisterNewStudent() throws Exception {
        Student student = new Student(
                String.format(
                        "%s %s",
                        faker.name().firstName(),
                        faker.name().lastName()
                ),
                faker.internet().emailAddress(),
                Gender.MALE
        );

        ResultActions resultActions = mockMvc
                .perform(post("/api/v1/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(student)));

        resultActions.andExpect(status().isOk());

        List<Student> students = studentRepository.findAll();
        assertThat(students)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(student);
    }

    @Test
    void canDeleteStudent() throws Exception {
        Student student = new Student(
                String.format(
                        "%s %s",
                        faker.name().firstName(),
                        faker.name().lastName()
                ),
                faker.internet().emailAddress(),
                Gender.MALE
        );

        Student studentInDB = studentRepository.save(student);
        ResultActions resultActions = mockMvc
                .perform(delete("/api/v1/students/" + studentInDB.getId()));

        resultActions.andExpect(status().isOk());

        List<Student> students = studentRepository.findAll();
        assertThat(students).isEmpty();
    }
}
