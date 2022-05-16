package com.alex.studentmanagementsystem.student;

import com.alex.studentmanagementsystem.exception.ApiRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public void addStudent(Student student) {
        if (studentRepository.existsByEmail(student.getEmail())) {
            throw new ApiRequestException(
                    String.format("The student with email %s already exists", student.getEmail())
            );
        }

        studentRepository.save(student);
    }

    public void deleteStudent(Long studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new ApiRequestException(
                    String.format("The student with id %d doesn't exist", studentId),
                    HttpStatus.NOT_FOUND
            );
        }

        studentRepository.deleteById(studentId);
    }
}
