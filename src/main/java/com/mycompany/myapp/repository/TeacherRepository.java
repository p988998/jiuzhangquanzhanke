package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Teacher;
import com.mycompany.myapp.domain.dto.TeacherDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.Cacheable;
import java.util.List;
import java.util.Optional;


@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
//    @Query("SELECT Course FROM course c  WHERE c.name = :courseName")
//    Course findCourseByCourseName(@Param("courseName") String courseName);

    @Query("SELECT new com.mycompany.myapp.domain.dto.TeacherDto(t.teacherName, t.teacherId) from Teacher t")
    List<TeacherDto> findAllTeachersDto();

    Teacher findTeacherByTeacherName(String teacherName);

}
