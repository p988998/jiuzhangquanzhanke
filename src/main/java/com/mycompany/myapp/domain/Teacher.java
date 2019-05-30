package com.mycompany.myapp.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "teacher")
@Data
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column( unique = true, nullable = false, columnDefinition = "bigint")
    private long teacherId;

    @Column(name = "teacher_name", nullable = false, length = 100, columnDefinition = "nvarchar(100)")
    private String teacherName;



}
