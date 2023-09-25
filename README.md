# Mini Porject05 : TKU


## 👓 프로젝트 소개
DKU 수강신청 사이트를 참고하여 만든 수강신청 시스템 입니다.

--------------------------------------------------------
### 1. 개발 환경
- Language : Java 11
- IDE : IntelliJ
- Framework : Springboot(2.5.6)
- Database : Maria DB
- ORM : Spring JPA

--------------------------------------------------------
### 2. 주요 기능 소개

  #### 로그인 기능
  - 학생 로그인
  - 교수 로그인
  
  #### 교수 페이지 기능
  - 과목 등록
  - 과목 목록(전체 과목/ 자신이 등록한 과목)
  - 과목 수정
  - 과목 삭제
  
  #### 학생 페이지 기능
  - 자신이 속한 학과의 수강 리스트
  - 수강신청
  - 수강신청 목록
  - 수강철회
  - 수강 시간표
-------------------------------------------------------------
### 3. DB 설계
- student Table ( 학생 목록 테이블 )
  학생의 아이디(학번), 비밀번호, 이름, 학년 정보
  
- professor Table( 교수 목록 테이블 )
  교수의 아이디(학번), 비밀번호 이름 정보

- course Table( 과목 테이블 )
  과목의 이름, 요일, 학점, 시작시간, 종료시간, 총 수강인원, 신청가능인원, 설명 정보

- department ( 학과 테이블 )
  학과 정보

- enrollment (수강 신청 테이블)
  수강신청한 학생의 아이디, 과목이름, 요일, 시작시간, 종료시간, 학점 정보
  
#### 테이블 관계도
- enrollment, course : ManyToOne Mapping
- course, professor : ManyToOne Mapping
- professor, department : ManyToOne Mapping
- student, department : ManyToOne Mapping
![image](https://github.com/Kim-taeseop/TKU/assets/137260250/326aaeb1-eb48-427d-90ba-f9194a02c715)

---------------------------------------------------------
### 4. JPA Repository 활용


          // 전 과목 찾기
          List<T> findAll();

          // 아이디를 이용하여 정보찾기
          Optional<T> findById(ID id);

          // 아이디를 이용하여 삭제하기
          void deleteById(ID id);
          
          // 로그인을 위한 학번 찾기
          Professor findByUnid(Integer unid);
          Student findByUnid(Integer unid);

          // 학과에 포함된 교수 찾기
          List<Professor> findByPdepartment(Department department);

          // 학생 아이디를 이용하여 수강신청한 과목찾기
          List<Enrollment> findByStudentId(Long studentId);

          // 이미 신청한 과목 찾기
          Enrollment findByStudentIdAndCourseName(Long studentId, String courseName);

          // 담당교수가 가르치는 과목찾기
          List<Course> findByProfessorId(Long professorId);

          

-------------------------------------------------------
## 💻 개발 과정

### 1. 로그인/ 로그아웃 기능

Session을 사용


    // Repository
    public interface ProfessorRepository extends JpaRepository <Professor, Long> {
    Professor findByUnid(Integer unid);
    }

    //Service
    public Professor findByProunid(Integer unid){
        return professorRepository.findByUnid(unid);
    }

    //Controller
    @PostMapping("/professor/login")
    public String proLogin(@RequestParam("unid") Integer unid, @RequestParam("pw")        String pw, HttpSession session){
        Professor professor = professorLoginService.findByProunid(unid);

        if(professor !=null && professor.getPw().equals(pw)){
            session.setAttribute("admin", professor);
            return "redirect:/professor/home";
        }else{
            return "redirect:/professor/login?error";
        }
    }

    @GetMapping("/proLogout")
    public String proLogout(HttpSession session){
        session.invalidate();
        return "redirect:/professor/login?proLogout";
    }
-----------------------------------------------------------

### 2. 교수페이지 기능 (CRUD)
과목의 등록, 수정, 삭제 기능은 자신이 등록한 과목에 한해서만 가능해야 함

=> 로그인시 session에 담긴 교수정보를 이용하여 CRUD 기능 진행

예시코드(과목등록)


       @PostMapping("/professor/coursecreate")
        public String insertCourse(CourseCreateDTO courseCreateDTO, 
        HttpSession session) {

        Course course = new Course();
        course.setName(courseCreateDTO.getName());
        course.setDay(courseCreateDTO.getDay());
        course.setGrades(courseCreateDTO.getGrades());
        course.setStarttime(courseCreateDTO.getStarttime());
        course.setEndtime(courseCreateDTO.getEndtime());
        course.setPersonnel(courseCreateDTO.getPersonnel());
        course.setSeat(courseCreateDTO.getPersonnel());
        course.setComment(courseCreateDTO.getComment());

        Professor loggedInProfessor = (Professor) 
        session.getAttribute("admin");
        course.setProfessor(loggedInProfessor);

        courseService.createCourse(course);

        return "redirect:/professor/home";
    }

--------------------------------------------------------

### 3. 학생페이지 기능


#### 1. 자신이 속한 학과의 수강 리스트
자신이 속한 학과의 과목들만 보여야함.

=> 로그인시 session에 담긴 정보를 들고옴 > 자신이 속한 학과 찾기 > 학과에 속한 교수들 찾기 > 교수별 과목을 리스트로 저장

        @GetMapping("/student/studentcourselist")
        public String studentCourseList(Model model, HttpSession session){
            Student loggedInStudent = (Student) session.getAttribute("user");
        if (loggedInStudent != null) {
            // 학생이 속한 학과 찾기
            Department studentDepartment =   
            loggedInStudent.getSdepartment();

            // 학과에 속한 교수들 찾기
            List<Professor> professors = 
            courseService.professorListByDepartment(studentDepartment);

            // 교수별 과목 리스트를 저장할 리스트
            List<List<Course>> professorCoursesList = new ArrayList<>();

            for (Professor professor : professors) {
                Long professorId = professor.getId();
                // 교수별 과목 리스트 가져오기
                List<Course> professorCourses = courseService.studentCourseList(professorId);
                professorCoursesList.add(professorCourses);
            }

            model.addAttribute("professors", professors);
            model.addAttribute("professorCoursesList", professorCoursesList);

            // 수강 신청을 위한 id 가져옴
            Long studentId = loggedInStudent.getId();
            model.addAttribute("studentId", studentId);

            return "student/studentcourselist";
        } else {
            // 로그인하지 않은 경우 로그인 페이지로 리다이렉트 또는 다른 처 
        리 수행
            return "redirect:/student/login";
        }
    }

#### 2. 수강신청
수강신청이 성공하기 위한 조건
- 수강신청 기간이여야 함
- 수강 가능한 자리가 있어야함
- 중복된 과목은 두번 신청 불가
- 수강 시간대가 서로 겹치면 불가


1. 수강신청 기간이여야 함
   
   LocalDateTime 을 이용하여 신청 가능한 시간을 지정

         @PostMapping("/student/addcourse/{id}")
          public String addToCourse(@PathVariable Long id, HttpSession session, Model model) {
          Student loggedInStudent = (Student) session.getAttribute("user");
  
          // 현재 시간 구하기
          LocalDateTime currentDateTime = LocalDateTime.now();
  
          // 허용되는 수강 신청 시간 범위 설정
          LocalDateTime startDateTime = LocalDateTime.of(2023, 9, 21, 11, 0); // 시작 시간
          LocalDateTime endDateTime = LocalDateTime.of(2023, 9, 25, 20, 0); // 종료 시간
  
          // 현재 시간이 허용 범위 내에 있는지 확인
          if (currentDateTime.isBefore(startDateTime) || currentDateTime.isAfter(endDateTime)) {
              return "redirect:/student/studentcourselist?timeError";
          }

2. 수강 가능한 자리가 있어야함.
   
  course 테이블의 현재 신청 가능인원이 0보다 크지 않으면 null 값을 반환 하여 0초과인 경우만 수강신청을 진행

   
        public Course enrollStudentInCourse(Long courseId) {
        Course course = courseRepository.findById(courseId).orElse(null);

        if (course != null && course.getSeat() > 0) {
            course.updateSeat();
            courseRepository.save(course);
            return course;
        }
        return null;
        }

3. 중복되는 시간 검사
   
   중복검사 메서드 따로 분리

   완전중복, 시작시간 중복, 종료시간 중복 3가지로 검사

          // 중복 검사 메서드
        private boolean isCourseOverlapping(List<Enrollment> studentEnrollments, Course currentCourse) {
        int currentCourseStartTime = currentCourse.getStarttime();
        int currentCourseEndTime = currentCourse.getEndtime();
        String currentCourseDay = currentCourse.getDay();

        for (Enrollment enrollment : studentEnrollments) {
            Course enrolledCourse = enrollment.getCourse();
            if (enrolledCourse != null) {
                int enrolledCourseStartTime = enrolledCourse.getStarttime();
                int enrolledCourseEndTime = enrolledCourse.getEndtime();
                String enrolledCourseDay = enrolledCourse.getDay();

                if (currentCourseDay.equals(enrolledCourseDay)) {
                    if (currentCourseStartTime == enrolledCourseStartTime && currentCourseEndTime == enrolledCourseEndTime) {
                        return true; // 완전히 중복되는 경우
                    } else if (currentCourseStartTime >= enrolledCourseStartTime && currentCourseStartTime < enrolledCourseEndTime) {
                        return true; // 시작 시간 중복
                    } else if (currentCourseEndTime > enrolledCourseStartTime && currentCourseEndTime <= enrolledCourseEndTime) {
                        return true; // 종료 시간 중복
                    }
                }
            }
        }
        return false; // 중복 없음
        }  

#### 3. 현재 자리 업데이트
수강신청 성공시 신청가능한 자리가 -1, 철회시 +1이 되야함.

              // 수강신청시
                    if (course.getSeat() > 0) {
                        course.setSeat(course.getSeat() - 1);
                    } else {
                        return "redirect:/student/studentcourselist?seatError";
                    }
              // 수강 철회시
              if (course != null) {
                    course.setSeat(course.getSeat() + 1);
                    courseRepository.save(course);
                }

#### 4. 수강시간표
수강 시간표는 html에서 각 시간별로 칸을 나눈후 신청한 과목들의 starttime, endtime을 비교하여 조건에 맞으면 과목명을 나타내는 방식

      <td>9:00 - 10:00</td>
        <td>
          <span th:each="course : ${timetable}"
                th:if="${course.starttime <= 9 and course.endtime > 9 and course.day == '월'}"
                th:text="${course.name}">
          </span>
        </td>


---------------------------------------------------------

## 🛠 더 추가하고 싶은 기능

학년별로도 나눠서 각 학년에 맞는 과목들을 지정

과목 검색기능 추가 ( 카테로리로 학년, 학과 선택 가능)

전공과목 / 교양 과목 분리
