package com.bolota.springnas.Resource;

import com.bolota.springnas.Entities.FileUploadEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileUploadResource extends JpaRepository<FileUploadEntity, Long> {

}
