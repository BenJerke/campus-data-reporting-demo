create table application.course (
                                    tableoid oid not null,
                                    cmax cid not null,
                                    xmax xid not null,
                                    cmin cid not null,
                                    xmin xid not null,
                                    ctid tid not null,
                                    id bigint primary key not null,
                                    instructor_id bigint,
                                    lastupdated timestamp(6) without time zone,
                                    semester_id bigint,
                                    title character varying(255) not null,
                                    foreign key (semester_id) references application.semester (id)
                                        match simple on update no action on delete no action,
                                    foreign key (instructor_id) references application.facultymember (id)
                                        match simple on update no action on delete no action
);
create unique index course_semester_id_key on course using btree (semester_id);

create table application.department (
                                        tableoid oid not null,
                                        cmax cid not null,
                                        xmax xid not null,
                                        cmin cid not null,
                                        xmin xid not null,
                                        ctid tid not null,
                                        id bigint primary key not null,
                                        name character varying(255) not null
);
create unique index department_name_key on department using btree (name);

create table application.facultymember (
                                           tableoid oid not null,
                                           cmax cid not null,
                                           xmax xid not null,
                                           cmin cid not null,
                                           xmin xid not null,
                                           ctid tid not null,
                                           id bigint primary key not null,
                                           position_id bigint not null,
                                           firstname character varying(255),
                                           lastname character varying(255),
                                           middlename character varying(255),
                                           title character varying(255),
                                           foreign key (position_id) references application.facultyposition (id)
                                               match simple on update no action on delete no action
);
create unique index facultymember_position_id_key on facultymember using btree (position_id);
create unique index facultymember_firstname_lastname_middlename_key on facultymember using btree (firstname, lastname, middlename);

create table application.facultymember_course (
                                                  tableoid oid not null,
                                                  cmax cid not null,
                                                  xmax xid not null,
                                                  cmin cid not null,
                                                  xmin xid not null,
                                                  ctid tid not null,
                                                  facultymember_id bigint not null,
                                                  courses_id bigint not null,
                                                  primary key (facultymember_id, courses_id),
                                                  foreign key (facultymember_id) references application.facultymember (id)
                                                      match simple on update no action on delete no action,
                                                  foreign key (courses_id) references application.course (id)
                                                      match simple on update no action on delete no action
);
create unique index facultymember_course_courses_id_key on facultymember_course using btree (courses_id);

create table application.facultyposition (
                                             tableoid oid not null,
                                             cmax cid not null,
                                             xmax xid not null,
                                             cmin cid not null,
                                             xmin xid not null,
                                             ctid tid not null,
                                             salary numeric(38,2) not null,
                                             department_id bigint not null,
                                             facultymember_id bigint,
                                             id bigint primary key not null,
                                             title character varying(255) not null,
                                             foreign key (facultymember_id) references application.facultymember (id)
                                                 match simple on update no action on delete no action,
                                             foreign key (department_id) references application.department (id)
                                                 match simple on update no action on delete no action
);
create unique index facultyposition_facultymember_id_key on facultyposition using btree (facultymember_id);

create table application.financialaidpackage (
                                                 tableoid oid not null,
                                                 cmax cid not null,
                                                 xmax xid not null,
                                                 cmin cid not null,
                                                 xmin xid not null,
                                                 ctid tid not null,
                                                 amount numeric(38,2) not null,
                                                 id bigint primary key not null,
                                                 semesteroffered_id bigint,
                                                 packagename character varying(255),
                                                 foreign key (semesteroffered_id) references application.semester (id)
                                                     match simple on update no action on delete no action
);
create unique index financialaidpackage_packagename_semesteroffered_id_key on financialaidpackage using btree (packagename, semesteroffered_id);

create table application.registrationrecord (
                                                tableoid oid not null,
                                                cmax cid not null,
                                                xmax xid not null,
                                                cmin cid not null,
                                                xmin xid not null,
                                                ctid tid not null,
                                                course_id bigint not null,
                                                id bigint primary key not null,
                                                student_id bigint not null,
                                                lettergrade character varying(255),
                                                foreign key (course_id) references application.course (id)
                                                    match simple on update no action on delete no action,
                                                foreign key (student_id) references application.student (id)
                                                    match simple on update no action on delete no action
);

create table application.semester (
                                      tableoid oid not null,
                                      cmax cid not null,
                                      xmax xid not null,
                                      cmin cid not null,
                                      xmin xid not null,
                                      ctid tid not null,
                                      enddate date not null,
                                      startdate date not null,
                                      id bigint primary key not null,
                                      name character varying(255) not null
);

create table application.semester_course (
                                             tableoid oid not null,
                                             cmax cid not null,
                                             xmax xid not null,
                                             cmin cid not null,
                                             xmin xid not null,
                                             ctid tid not null,
                                             semester_id bigint not null,
                                             coursesoffered_id bigint not null,
                                             primary key (semester_id, coursesoffered_id),
                                             foreign key (coursesoffered_id) references application.course (id)
                                                 match simple on update no action on delete no action,
                                             foreign key (semester_id) references application.semester (id)
                                                 match simple on update no action on delete no action
);
create unique index semester_course_coursesoffered_id_key on semester_course using btree (coursesoffered_id);

create table application.semester_financialaidpackage (
                                                          tableoid oid not null,
                                                          cmax cid not null,
                                                          xmax xid not null,
                                                          cmin cid not null,
                                                          xmin xid not null,
                                                          ctid tid not null,
                                                          semester_id bigint not null,
                                                          financialaidpackagesoffered_id bigint not null,
                                                          primary key (semester_id, financialaidpackagesoffered_id),
                                                          foreign key (financialaidpackagesoffered_id) references application.financialaidpackage (id)
                                                              match simple on update no action on delete no action,
                                                          foreign key (semester_id) references application.semester (id)
                                                              match simple on update no action on delete no action
);

create table application.student (
                                     tableoid oid not null,
                                     cmax cid not null,
                                     xmax xid not null,
                                     cmin cid not null,
                                     xmin xid not null,
                                     ctid tid not null,
                                     enrollmentdate date not null,
                                     graduationdate date,
                                     id bigint primary key not null,
                                     firstname character varying(255),
                                     lastname character varying(255),
                                     middlename character varying(255)
);
create unique index student_firstname_lastname_middlename_key on student using btree (firstname, lastname, middlename);

create table application.student_financialaidpackage (
                                                         tableoid oid not null,
                                                         cmax cid not null,
                                                         xmax xid not null,
                                                         cmin cid not null,
                                                         xmin xid not null,
                                                         ctid tid not null,
                                                         student_id bigint not null,
                                                         financialaidpackages_id bigint not null,
                                                         primary key (student_id, financialaidpackages_id),
                                                         foreign key (student_id) references application.student (id)
                                                             match simple on update no action on delete no action,
                                                         foreign key (financialaidpackages_id) references application.financialaidpackage (id)
                                                             match simple on update no action on delete no action
);
create unique index student_financialaidpackage_financialaidpackages_id_key on student_financialaidpackage using btree (financialaidpackages_id);

