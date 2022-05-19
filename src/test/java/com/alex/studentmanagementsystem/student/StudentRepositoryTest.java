package com.alex.studentmanagementsystem.student;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class StudentRepositoryTest {

    @Autowired
    private StudentRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void itShouldCheckIfStudentEmailExists() {
        final String EMAIL = "alex@gmail.com";
        // given
        Student student = new Student(
                "Alex",
                EMAIL,
                Gender.MALE
        );
        underTest.save(student);

        // when
        boolean existsByEmail = underTest.existsByEmail(EMAIL);

        // then
        assertThat(existsByEmail).isTrue();
    }

    @Test
    void itShouldCheckIfStudentEmailDoesNotExists() {
        final String EMAIL = "alex@gmail.com";
        final String WRONG_EMAIL = "john@gmail.com";

        // given
        Student student = new Student(
                "Alex",
                EMAIL,
                Gender.MALE
        );
        underTest.save(student);

        // when
        boolean existsByEmail = underTest.existsByEmail(WRONG_EMAIL);

        // then
        assertThat(existsByEmail).isFalse();
    }
}