package com.bolota.springnas.Controller;

import jakarta.annotation.Resource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;


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

