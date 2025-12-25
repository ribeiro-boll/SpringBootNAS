package com.bolota.springnas.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class WebController {
    @GetMapping("/")
    public String defaultHomapage(){
        return "upload";
    }
    @GetMapping("/upload")
    public String uploadPage(){
        return "upload";
    }
    @GetMapping("/download")
    public String downloadPage(){
        return "download";
    }
}
