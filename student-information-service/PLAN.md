# My Reference Material
## Jakarta/Quarkus
Quarkus is all-in on Jakarta EE.
- https://jakarta.ee/learn/specification-guides/restful-web-services-explained/

## ORM
https://quarkus.io/guides/hibernate-orm
- Hibernate properties can be added in application.properties

# 2026-02-09

- [x] Student Information Service
  - [x] **Faculty Domain**
    - [x] Department
      - Name 
      - Faculty Positions(One to many, bidirectional)
        - **Endpoints**
          - [x] Create Department
      
    - [x] **Faculty Position**
      - Title
      - Salary
      - Department(Many to one)
        - **Endpoints**
          - [x] Create faculty position  
          - [x] Assign position to department << these are the same
          
    - [x] **Faculty Member**
      - Position
      - Courses (One to many)
        - **Endpoints**
          - [x] Create faculty member
          - [x] Assign faculty member to position
          - [x] Assign faculty member to course
    
    - [x] Add tuition to Semester entity: this way we can get some revenue to plot against fin aid and faculty salaries for projections n stuff. 


- [ ] **Financial Management System**
  - [ ] **Account Domain**
    - [ ] **Account** 
      - Name
      - Credits (Transactions - Many to one)
      - Debits (Transactions - Many to one)
      - REST Client Mapping Info
        - **Endpoints**
          - [ ] Create Account 
          - [ ] Map Account to REST Endpoint
          
    - [ ] Transaction Domain
      - Amount
      - Credited Account (One to many)
      - Debited Account (One to many)

- [ ] Write a script to generate fake data

- [ ] Prepare Test Data
  - [ ] 4 years' worth of semesters
    - [ ] 2022/2023
      - Fall 22
      - Spring 23
    - [ ] 2023/2024
      - Fall 23
      - Spring 24
    - [ ] 2024/2025
      - Fall 24
      - Spring 25
    - [ ] 2025/2026
      - Fall 25
      - Spring 26 (IN PROGRESS)
  - [ ] Faculty Tree
    - [ ] History Dept. 
      - [ ] Full Professor
      - [ ] Assistant Professor
      - [ ] Visiting Professor
    - [ ] Philosophy
      - [ ] Professor Emeritus
      - [ ] Full Professor
      - [ ] Adjunct
    - [ ] Math
      - [ ] Full Professor
      - [ ] Full Professor
      - [ ] Postdoctorate Fellow
    - [ ] Physics
    - [ ] 
    - [ ] 

- [ ] Document everything so you don't get lost




# 2026-02-08
- [x] Create Students & Associate Them with Courses
  - [x] Rest Endpoints
    - [x] Create Student
    - [x] Create Course
    - [x] Register Student for Course
      Having some issues with this. I can create students and courses and I can register students for courses. 
    I see the course object getting the student, but I can't see the student object getting the course. If I explicitly add the course to the student, I get an infinite loop as hibernate tries to update both entities forever
    - [x] Get student by ID
      




# 2026-02-07
- [x] Get the Quarkus app building and running
  - [x] Launch in dev mode
  - [x] Configure Hibernate

- [ ] Create Students & Associate Them with Courses
  - [x] Entity Classes
    - [x] Student
    - [x] Course

