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


