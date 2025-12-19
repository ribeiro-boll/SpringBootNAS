package com.bolota.springnas.Controller;

import com.bolota.springnas.Entities.FileUploadEntity;
import com.bolota.springnas.Entities.UUIDEntity;
import com.bolota.springnas.Resource.FileUploadResource;
import com.bolota.springnas.Service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ServerController {

    UUIDEntity uuidList = new UUIDEntity();

    @Autowired
    FileUploadResource fileDb;

    @PostMapping("/upload/init")
    public Map<String,String> uploadInit(@RequestBody Map<String, Object> json){
        HashMap<String,String> map;
        String generatedUUID = UUIDEntity.generateUUID(uuidList);
        System.out.println(json.toString());
        FileUploadEntity fue = new FileUploadEntity(null,
                new java.util.Date().toString(),
                (String)json.get("fileName"),
                Long.parseLong(json.get("fileSize").toString()),
                FileUploadService.getFilePath((String)json.get("fileName")),
                FileUploadService.getTempPath(generatedUUID)
        );
        System.out.println(fue.toString());

        uuidList.insertFile(generatedUUID, fue);

        FileUploadService.createFolder(fue.getFl_name(),generatedUUID);
        map = new HashMap<>();
        map.put("uploadId",generatedUUID);
        return map;
    }
    @PostMapping("/upload/chunk")
    public ResponseEntity<Void> uploadChunk(@RequestParam("uploadId") String uploadId, @RequestParam("chunkNumber") long chunkNumber, @RequestParam("file") MultipartFile file){
        FileUploadService.createTempFile(uploadId,chunkNumber,file);
        System.out.println();
        System.out.println(chunkNumber);
        System.out.println();
        return ResponseEntity.ok().build();
    }
    @PostMapping("/upload/finish")
    public ResponseEntity<Void> uploadFinish(@RequestBody Map<String, Object> json){
        String uuid = (String) json.get("uploadId");
        FileUploadService.createRealFile(uuid, uuidList.returnFile(uuid).getFl_name());
        fileDb.save(uuidList.returnFile(uuid));
        uuidList.removeFile((String) json.get("uploadId"));
        return ResponseEntity.ok().build();
    }
}