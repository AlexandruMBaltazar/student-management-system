package com.alex.studentmanagementsystem.student;

import com.alex.studentmanagementsystem.exception.ApiRequestException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    // Create a mock for StudentRepository
    @Mock
    private StudentRepository studentRepository;
    private StudentService underTest;

    @BeforeEach
    void setUp() {
        underTest = new StudentService(studentRepository);
    }

    @Test
    void canGetAllStudents() {
        underTest.getAllStudents();

        verify(studentRepository).findAll();
    }

    @Test
    void canAddStudent() {
        final String EMAIL = "alex@gmail.com";
        Student student = new Student(
                "Alex",
                EMAIL,
                Gender.MALE
        );

        // Call the tested method addStudent with a student param
        underTest.addStudent(student);

        // This will capture any param that is a student
        ArgumentCaptor<Student> studentArgumentCaptor =
                ArgumentCaptor.forClass(Student.class);

        // Verify that the student repository calls the save method and, we capture the student param
        verify(studentRepository)
                .save(studentArgumentCaptor.capture());

        // Check if the student param is the same as the student we set into the addStudent method
        Student capturedStudent = studentArgumentCaptor.getValue();
        assertThat(capturedStudent).isEqualTo(student);
    }

    @Test
    void addStudentThrowsExceptionWhenEmailTaken() {
        final String EMAIL = "alex@gmail.com";
        Student student = new Student(
                "Alex",
                EMAIL,
                Gender.MALE
        );

        given(studentRepository.existsByEmail(EMAIL))
                .willReturn(true);

        assertThatThrownBy(() -> underTest.addStudent(student))
                .isInstanceOf(ApiRequestException.class)
                .hasMessageContaining(String.format("The student with email %s already exists", student.getEmail()));

        verify(studentRepository, never())
                .save(any());
    }

    @Test
    void canDeleteStudent() {
        final long ID = 10;

        given(studentRepository.existsById(ID))
                .willReturn(true);

        underTest.deleteStudent(ID);

        verify(studentRepository).deleteById(ID);
    }

    @Test
    void deleteStudentThrowsExceptionWhenStudentDoesNotExist() {
        final long ID = 10;
        given(studentRepository.existsById(ID))
                .willReturn(false);

        assertThatThrownBy(() -> underTest.deleteStudent(ID))
                .isInstanceOf(ApiRequestException.class)
                .hasMessageContaining(String.format("The student with id %d doesn't exist", ID));

        verify(studentRepository, never())
                .deleteById(any());
    }
}