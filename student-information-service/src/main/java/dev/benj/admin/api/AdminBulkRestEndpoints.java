package dev.benj.admin.api;

import dev.benj.common.model.Semester;
import dev.benj.common.model.Student;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

import java.util.List;
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

}
