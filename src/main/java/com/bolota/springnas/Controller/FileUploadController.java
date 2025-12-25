package com.bolota.springnas.Controller;

import com.bolota.springnas.Entities.FileUploadEntity;
import com.bolota.springnas.Entities.UuidEntity;
import com.bolota.springnas.Resource.FileResource;
import com.bolota.springnas.Service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
public class FileUploadController {

    UuidEntity uuidListInstance = new UuidEntity();

    @Autowired
    FileResource fileDb;

    @PostMapping("/upload/init")
    public Map<String,String> uploadInit(@RequestBody Map<String, Object> json){
        HashMap<String,String> map;
        String generatedUUID = UuidEntity.generateUUID(uuidListInstance);
        FileUploadEntity fue = new FileUploadEntity(null,
                new java.util.Date().toString(),
                (String)json.get("fileName"),
                Long.parseLong(json.get("fileSize").toString()),
                FileUploadService.getFilePath((String)json.get("fileName")),
                FileUploadService.getTempPath(generatedUUID)
        );
        uuidListInstance.insertFile(generatedUUID, fue);
        FileUploadService.createFolder(fue.getFl_name(),generatedUUID);
        map = new HashMap<>();
        map.put("uploadId",generatedUUID);
        return map;
    }
    @PostMapping("/upload/chunk")
    public ResponseEntity<Void> uploadChunk(@RequestParam("uploadId") String uploadId, @RequestParam("chunkNumber") long chunkNumber, @RequestParam("file") MultipartFile file){
        FileUploadService.createTempFile(uploadId,chunkNumber,file);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/upload/finish")
    public ResponseEntity<Void> uploadFinish(@RequestBody Map<String, Object> json){
        String uuid = (String) json.get("uploadId");
        FileUploadService.createRealFile(uuid, uuidListInstance.returnFile(uuid).getFl_name());
        fileDb.save(uuidListInstance.returnFile(uuid));
        uuidListInstance.removeFile((String) json.get("uploadId"));

        return ResponseEntity.ok().build();
    }
}