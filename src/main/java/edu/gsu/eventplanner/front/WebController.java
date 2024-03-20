package edu.gsu.eventplanner.front;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/web/main")
    public String getMain(){
        return "index";
    }
}
