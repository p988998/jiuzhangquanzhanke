import { Component, OnInit } from '@angular/core';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';
import { NgForm } from '@angular/forms';
import { LoginModalService, Principal, Account } from 'app/core';
import { CourseService } from 'app/shared/service/CourseService';
import { CourseDto } from 'app/shared/model/course-dto.model';
import { CourseWithTNDto } from 'app/shared/model/courseWithTN-dto.model';
import { toString } from '@ng-bootstrap/ng-bootstrap/util/util';

@Component({
    selector: 'jhi-home',
    templateUrl: './home.component.html',
    styleUrls: ['home.css']
})
export class HomeComponent implements OnInit {
    account: Account;
    modalRef: NgbModalRef;
    classeNameNeedToReg: string;
    searchClass: string;

    constructor(
        private principal: Principal,
        private loginModalService: LoginModalService,
        private eventManager: JhiEventManager,
        private courseService: CourseService
    ) {}

    newCourse: CourseDto;
    newCourseWithTNDto: CourseWithTNDto;
    courses: CourseDto[] = [];

    coursesWithTN: CourseWithTNDto[] = [];

    ngOnInit() {
        this.principal.identity().then(account => {
            this.account = account;
        });
        this.registerAuthenticationSuccess();
    }

    registerAuthenticationSuccess() {
        this.eventManager.subscribe('authenticationSuccess', message => {
            this.principal.identity().then(account => {
                this.account = account;
            });
        });
    }

    isAuthenticated() {
        return this.principal.isAuthenticated();
    }

    login() {
        this.modalRef = this.loginModalService.open();
    }

    getAllCourses() {
        this.courseService.getCourseInfo().subscribe(curDto => {
            if (!curDto) {
                this.courses = [];
            } else {
                this.courses = curDto;
            }
        });
    }

    getAllCoursesWithTN() {
        this.courseService.getCourseInfoWithTN().subscribe(curDto => {
            if (!curDto) {
                this.coursesWithTN = [];
            } else {
                this.coursesWithTN = curDto;
            }
        });
    }

    // registerCourse(courseName) {
    //
    // }

    clearAllCourses() {
        this.courses = [];
    }
    clearAllCoursesWithTN() {
        this.coursesWithTN = [];
    }
    deleteCourse(name: string) {
        //delete
        this.courseService.delete(name).subscribe();
    }
    addCourse(f: NgForm) {
        //  debugger;
        this.newCourseWithTNDto = {
            courseName: f.value.newClassName,
            courseLocation: f.value.newLocation,
            courseContent: f.value.newContent,
            teacherName: f.value.newTeacher
        };
        this.courseService.add(this.newCourseWithTNDto).subscribe();
    }

    updateCourse(f: NgForm) {
        this.newCourse = {
            courseName: f.value.updateName,
            courseLocation: f.value.updateLocation,
            courseContent: f.value.updateContent,
            courseTeacher: f.value.updateTeacher
        };
        this.courseService.update(this.newCourse).subscribe();
    }
}
