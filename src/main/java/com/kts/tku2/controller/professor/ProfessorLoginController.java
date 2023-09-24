package com.kts.tku2.controller.professor;

import com.kts.tku2.data.entity.Professor;
import com.kts.tku2.service.professor.ProfessorLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class ProfessorLoginController {

    @Autowired
    private ProfessorLoginService professorLoginService;

    @GetMapping("/professor/login")
    public String proLoginForm(){
        return "/professor/login";
    }

    @PostMapping("/professor/login")
    public String proLogin(@RequestParam("unid") Integer unid, @RequestParam("pw") String pw, HttpSession session){
        Professor professor = professorLoginService.findByProunid(unid);

        if(professor !=null && professor.getPw().equals(pw)){
            session.setAttribute("admin", professor);
            return "redirect:/professor/home";
        }else{
            return "redirect:/professor/login?error";
        }
    }

    @GetMapping("/professor/home")
    public String proHome(HttpSession session){
        Professor professor = (Professor) session.getAttribute("admin");
        if (professor != null){
            professor.getPdepartment();
        }
        return "professor/home";
    }

    @GetMapping("/proLogout")
    public String proLogout(HttpSession session){
        session.invalidate();
        return "redirect:/professor/login?proLogout";
    }
}
