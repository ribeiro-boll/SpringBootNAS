package com.bolota.springnas.Controller;

import com.bolota.springnas.Entities.FileDTO;
import com.bolota.springnas.Entities.FileUploadEntity;
import com.bolota.springnas.Resource.FileResource;
import com.bolota.springnas.Service.FileDowloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.springframework.http.ResponseEntity.ok;

@RestController
public class FileDownloadController {

    @Autowired
    FileResource fileDb;

    @GetMapping("/files")
    public Page<FileDTO> listFiles(@PageableDefault(size = 10) Pageable pageable){
        return fileDb.findBy(pageable).map(FileDowloadService::convert);
    }
    @GetMapping("/files/{temp_id}/content")
    public ResponseEntity<StreamingResponseBody> fileDownloader(@PathVariable String temp_id){
        Long id = Long.parseLong(temp_id);
        FileUploadEntity fl = fileDb.findById(id).get();
        StreamingResponseBody body = outputStream -> {
            Path path = Path.of(fl.getFl_path()+'/'+fl.getFl_name());
            System.out.println(fl.getFl_path()+'/'+fl.getFl_name());
            try(InputStream is = Files.newInputStream(path)){
                is.transferTo(outputStream);
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).contentLength(fl.getFl_size()).header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fl.getFl_name() + "\"").body(body);
    }
}

