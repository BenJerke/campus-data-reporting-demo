package dev.benj.admin.api;

import dev.benj.common.model.*;
import dev.benj.common.model.dto.StudentWithFinancialAidAwards;
import dev.benj.common.model.dto.StudentWithSemesters;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;

@Path("/admin")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Slf4j
@RequiredArgsConstructor
public class AdminRestEndpoints {

    @Inject
    EntityManager entityManager;

    @POST
    @Path("/student")
    @Transactional
    public Response createNewStudent(Student student){
        entityManager.persist(student);
        return Response.ok(student).build();
    }

    @PUT
    @Path("/enroll/")
    @Transactional
    public Response enrollStudentInSemester(@QueryParam("semesterId") Long semesterId, @QueryParam("studentId") Long studentId){
        Student student = entityManager.find(Student.class, studentId);
        Semester semester = entityManager.find(Semester.class, semesterId);
        if(student == null || semester == null){
            return Response.status(404).build();
        }
        student.registerForSemester(semester);
        entityManager.persist(student);

        return Response.ok(new StudentWithSemesters(studentId, student.getName(), student.getSemestersAttended())).build();
    }

    @POST
    @Path("/course/")
    @Transactional
    public Response createNewCourse(Course course, @QueryParam("semester") String semesterName){
        var query = entityManager.createQuery("from Semester where name = :semesterName", Semester.class);
        query.setParameter("semesterName", semesterName);
        Semester semester = query.getSingleResultOrNull();
        if(semester == null){
            return Response.status(404).entity("Semester named %s not found.".formatted(semesterName)).build();
        }
        entityManager.persist(course);
        return Response.ok(course).build();
    }

    @POST
    @Path("/semester")
    @Transactional
    public Response createNewSemester(Semester semester){
        entityManager.persist(semester);
        return Response.ok(semester).build();
    }

    @POST
    @Path("/student/register/")
    @Transactional
    public Response registerStudentForCourse(Student student, @QueryParam("courseId") long courseId){
        var savedStudent = entityManager.find(Student.class, student.getId());
        var savedCourse = entityManager.find(Course.class, courseId);
        RegistrationRecord newRegistrationRecord = new RegistrationRecord(savedStudent, savedCourse);
        entityManager.persist(newRegistrationRecord);
        return Response.ok(newRegistrationRecord).build();
    }

    @POST
    @Path("/financial-aid/")
    @Transactional
    public Response createFinancialAidPackage(FinancialAidPackage financialAidPackage, @QueryParam("semester") String semesterName){
        var query = entityManager.createQuery("from Semester where name = :semesterName", Semester.class);
        query.setParameter("semesterName", semesterName);
        Semester semester = query.getSingleResultOrNull();
        if(Objects.isNull(semester)){
            return Response
                    .status(404)
                    .entity("Semester %s not found".formatted(semesterName))
                    .build();
        }
        financialAidPackage.setSemesterOffered(semester);
        entityManager.persist(financialAidPackage);
        return Response.ok(financialAidPackage).build();
    }


    @PUT
    @Path("/financial-aid/award")
    @Transactional
    public Response awardFinancialAidToStudent(Student student, @QueryParam("packageId") long packageId){

        var savedPackage = entityManager.find(FinancialAidPackage.class, packageId);
        Student savedStudent = entityManager.find(Student.class, student.getId());
        savedStudent.awardFinancialAid(savedPackage);
        entityManager.persist(savedStudent);

        return Response.ok(new StudentWithFinancialAidAwards(
                savedStudent.getId(),
                savedStudent.getName(),
                savedStudent.getEnrollmentDate(),
                savedStudent.getGraduationDate(),
                savedStudent.getFinancialAidPackages()
        )).build();
    }

    // Faculty Domain Endpoints

    // Create Department
    @POST
    @Path("/department")
    @Transactional
    public Response createDepartment(Department department){
        entityManager.persist(department);
        return Response.ok(department).build();
    }

    // Create Position in Department
    @POST
    @Path("/position/")
    @Transactional
    public Response createFacultyPositionInDepartment(@QueryParam("deptId") Long deptId, @QueryParam("deptName") String deptName, FacultyPosition newPosition){
        if(deptId == null){
            var query = entityManager.createQuery("from Department where name = :deptName", Department.class);
            query.setParameter("deptName", deptName);
            Department department = query.getSingleResult();
            if(department == null){
                return Response.status(404).entity("Department not found").build();
            }
            newPosition.setDepartment(department);
            entityManager.persist(newPosition);
            return Response.ok(newPosition).build();
        } else {
            Department department = entityManager.find(Department.class, deptId);
            if (department == null) {
                return Response.status(404).entity("Department not found").build();
            }
            newPosition.setDepartment(department);
            entityManager.persist(newPosition);
            return Response.ok(newPosition).build();
        }
    }
    // Create faculty member with a position
    @POST
    @Path("/faculty/")
    @Transactional
    public Response createFacultyMember(@QueryParam("positionId") Long positionId, @QueryParam("positionTitle") String positionTitle, @QueryParam("departmentName")String departmentName, FacultyMember newFacultyMember){
        if(positionId == null){
            var query = entityManager.createQuery("from FacultyPosition as fp where title = :positionTitle and fp.department.name = :departmentName", FacultyPosition.class);
            query.setParameter("positionTitle", positionTitle);
            query.setParameter("departmentName", departmentName);
            var position = query.getSingleResult();
            newFacultyMember.setPosition(position);
            entityManager.persist(newFacultyMember);
            return Response.ok(position).build();
        } else {
            FacultyPosition position = entityManager.find(FacultyPosition.class, positionId);
            if (position == null) {
                return Response.status(404).entity("Position not found").build();
            }
            newFacultyMember.setPosition(position);
            entityManager.persist(newFacultyMember);
            return Response.ok(newFacultyMember).build();
        }
    }

    // Assign faculty member to course
    // TODO: this is probably definitely not the way to do something like this
    @PUT
    @Path("/faculty/")
    @Transactional
    public Response assignFacultyMemberToCourse(@QueryParam("facultyId") Long facultyId, @QueryParam("courseId") Long courseId){
        FacultyMember facultyMember = entityManager.find(FacultyMember.class, facultyId);
        Course course = entityManager.find(Course.class, courseId);
        if(facultyMember == null || course == null){
            return Response.status(404).build();
        }

        course.setInstructor(facultyMember);
        entityManager.persist(course);
        return Response.ok(course).build();
    }
}
