package application.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by henry on 1.05.17.
 */
@Controller
public class ViewController {

    @RequestMapping("")
    public String home() {
        return "index";
    }
}
