package my_group.web.controller;

import my_group.myapp.TestService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLException;

@Controller
public class HelloController {
    TestService testService;

    public HelloController(TestService testService) {
        this.testService = testService;
    }

    @GetMapping("/greeting")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) throws SQLException {
        model.addAttribute("name", name);
        testService.testQuery();
        return "hello";
    }
}
