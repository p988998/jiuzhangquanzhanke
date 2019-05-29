package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Course;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.domain.UserCourse;
import com.mycompany.myapp.domain.dto.CourseDto;
import com.mycompany.myapp.domain.dto.CourseWithTNDto;
import com.mycompany.myapp.repository.CourseRepository;
import com.mycompany.myapp.repository.UserCourseRepository;
import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mycompany.myapp.domain.Teacher;
import com.mycompany.myapp.domain.dto.TeacherDto;
import com.mycompany.myapp.repository.TeacherRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserCourseRepository userCourseRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private TeacherRepository teacherRepository;

    List<CourseDto> courseDtos = new ArrayList<>();

    List<TeacherDto> teacherDto = new ArrayList<>();

    Teacher teacher = null;

    public List<CourseDto> findAllCourses() {

        //Cache
        if (courseDtos.isEmpty()) {
            List<Course> courses = courseRepository.findAll();
            for (Course c : courses) {
                courseDtos.add(new CourseDto(c.getCourseName(), c.getCourseLocation(), c.getCourseContent(), c.getTeacherId()));
            }

            return courseDtos;
        }else{
            courseDtos.clear();
            List<Course> courses = courseRepository.findAll();
            for (Course c : courses) {
                courseDtos.add(new CourseDto(c.getCourseName(), c.getCourseLocation(), c.getCourseContent(), c.getTeacherId()));
            }
            return courseDtos;
        }


    }

    public List<CourseDto> findAllCoursesDtoFromDB(){
        return courseRepository.findAllCoursesDto();
    }

    public List<CourseWithTNDto> findAllCoursesDtoWithTeacherNameFromDB(){
        return courseRepository.findAllCoursesDtoWithTeacherName();
    }


    public void registerCourse(String courseName) throws Exception{
        Optional<User> curUser = userService.getUserWithAuthorities();
        Optional<Course> curCourse = courseRepository.findCourseByCourseName(courseName);

        if (curUser.isPresent() && curCourse.isPresent()){
            userCourseRepository.save(UserCourse.builder()
                .user(curUser.get())
                .course(curCourse.get())
                .build());
        } else {
            throw new Exception("UnExpected Exception");
        }
    }



//    public void addCourse(CourseDto course) throws Exception{
//        Optional<Course> courseDto = courseRepository.findCourseByCourseName(course.getCourseName());
//
//        if(courseDto.isPresent()){
//            throw new Exception("Course is existing.");
//        }
//
//        Course courseBeingSaved = Course.builder()
//            .courseName(course.getCourseName())
//            .courseContent(course.getCourseContent())
//            .courseLocation(course.getCourseLocation())
//            .teacherId(course.getTeacherId())
//            .build();
//
//        try {
//            courseRepository.saveAndFlush(courseBeingSaved);
//        } catch (Exception e){
//            throw new Exception(e.getMessage());
//        }
//
//    }

    public void addCourse(CourseWithTNDto course) throws Exception{
        Optional<Course> courseDto = courseRepository.findCourseByCourseName(course.getCourseName());

        if(courseDto.isPresent()){
            throw new Exception("Course is existing.");
        }
        addTeacher(course.getTeacherName());
        Course courseBeingSaved = Course.builder()
            .courseName(course.getCourseName())
            .courseContent(course.getCourseContent())
            .courseLocation(course.getCourseLocation())
            .teacherId(findTeacherIdByName(course.getTeacherName()))
            .build();

        try {
            courseRepository.saveAndFlush(courseBeingSaved);
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }

    }


    public void deleteCourse(String courseName) throws Exception{
        Optional<Course> OptionalExistingCourse = courseRepository.findCourseByCourseName(courseName);

        if(!OptionalExistingCourse.isPresent()){
            throw new Exception("Course is not exist.");
        }

        try {

            courseRepository.delete(OptionalExistingCourse.get());


        } catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }


    public void updateCourse(CourseDto course) throws Exception{
        Optional<Course> OptionalExistingCourse = courseRepository.findCourseByCourseName(course.getCourseName());

        if(!OptionalExistingCourse.isPresent()){
            throw new Exception("Course is not exist.");
        }

        Course existingCourse = OptionalExistingCourse.get();
        existingCourse.setCourseContent(course.getCourseContent());
        existingCourse.setCourseLocation(course.getCourseLocation());
        existingCourse.setCourseName(course.getCourseName());
        existingCourse.setTeacherId(course.getTeacherId());
        //existingCourse.setTeacherId(course.getTeacherId());

    }

    public long findTeacherIdByName(String name) throws Exception{
        this.teacher = teacherRepository.findTeacherByTeacherName(name);
        if(this.teacher == null){
            throw new Exception("Teacher is not exist.");
        }

        return this.teacher.getTeacherId();
    }

    public void addTeacher(String name) throws Exception{
        Teacher teacher = teacherRepository.findTeacherByTeacherName(name);

        if(teacher != null){
            return;
        }

        Teacher teacherBeingSaved = Teacher.builder()
            .teacherName(name)
            .build();

        try {
            teacherRepository.saveAndFlush(teacherBeingSaved);
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }

    }



}
