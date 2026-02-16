package dev.benj.admin.api;

import dev.benj.common.model.*;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

@Path("/admin/bulk/")
@RequiredArgsConstructor
public class AdminBulkRestEndpoints {
    @Inject
    EntityManager entityManager;

    @POST
    @Path("/enroll/")
    @Transactional
    public Response bulkEnrollStudents(List<Student> students, @QueryParam("semesterId") Long semesterId){
        Semester semester = entityManager.find(Semester.class, semesterId);
        if(semester == null){
            return Response.status(400).build();
        }
        for(int i = 0; i < students.size(); i++){
            var studentToPersist = students.get(i);
            var query = entityManager.createQuery("from Student as s where s.name.firstName = :firstName and s.name.middleName = :middleName and s.name.lastName = :lastName", Student.class);
            query.setParameter("firstName", studentToPersist.getName().getFirstName());
            query.setParameter("middleName", studentToPersist.getName().getMiddleName());
            query.setParameter("lastName", studentToPersist.getName().getLastName());
            var existingStudent = query.getSingleResultOrNull();

            if(existingStudent != null){
                studentToPersist = existingStudent;
            }

            studentToPersist.registerForSemester(semester);
            entityManager.persist(studentToPersist);
        };
        return Response.ok(students).build();
    }

    @POST
    @Path("/course/")
    @Transactional
    public Response createCourses(List<Course> courses, @QueryParam("semester") String semesterName){
        var semesterQuery = entityManager.createQuery("from Semester where name = :semesterName", Semester.class);
        semesterQuery.setParameter("semesterName", semesterName);
        Semester semester = semesterQuery.getSingleResultOrNull();
        if(semester == null){
            return Response.status(404).entity("Semester named %s not found.".formatted(semesterName)).build();
        }
        for(int i = 0; i < courses.size(); i++){
            var courseToPersist = courses.get(i);
            var courseQuery = entityManager.createQuery("from Course where title = :title", Course.class);
            var savedCourse = courseQuery.getSingleResultOrNull();
            if(savedCourse != null){
                courseToPersist = savedCourse;
            }
            courseToPersist.offerCourseInSemester(semester);
            entityManager.persist(courses);
        }
        return Response.ok(courses).build();
    }


    @GET
    @Path("/students/")
    public Response listStudents(){
        var studentQuery = entityManager.createQuery("from Student", Student.class);
        List<Student> students = studentQuery.getResultList();
        return Response.ok(students).build();
    }


    @GET
    @Path("/faculty/")
    public Response listFaculty(){
        var studentQuery = entityManager.createQuery("from FacultyMember ", FacultyMember.class);
        List<FacultyMember> facultyMembers = studentQuery.getResultList();
        return Response.ok(facultyMembers).build();
    }

    @GET
    @Path("/semester/")
    public Response listSemesters(){
        var semesterQuery = entityManager.createQuery("from Semester", Semester.class);
        List<Semester> semesters = semesterQuery.getResultList();
        return Response.ok(semesters).build();
    }


    @GET
    @Path("/position/")
    public Response listFacultyPositions(){
        var positionQuery = entityManager.createQuery("from FacultyPosition ", FacultyPosition.class);
        List<FacultyPosition> positions = positionQuery.getResultList();
        return Response.ok(positions).build();
    }

}
