package com.bolota.springnas.Controller;

import com.bolota.springnas.Entities.FileUploadEntity;
import com.bolota.springnas.Resource.FileResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;

@RestController
public class FileRemoverController {
    @Autowired
    FileResource fr;
    @DeleteMapping("/remove/{id}/content")
    public ResponseEntity<Void> removeFile(@PathVariable Long id){
        FileUploadEntity file = fr.getById(id);
        String fileName = file.getFl_name();
        String filePath = file.getFl_path();
        File fl = new File(filePath+'/'+fileName);
        System.out.println(fl.getName());
        if (!fl.delete()){
            return ResponseEntity.status(HttpStatusCode.valueOf(409)).build();
        }
        fl = new File(filePath);
        System.out.println(fl.getName());
        if (!fl.delete()){
            return ResponseEntity.status(HttpStatusCode.valueOf(409)).build();
        }
        fr.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
