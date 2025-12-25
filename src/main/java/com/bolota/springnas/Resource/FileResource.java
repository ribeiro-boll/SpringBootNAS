package com.bolota.springnas.Resource;

import com.bolota.springnas.Entities.FileUploadEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileResource extends JpaRepository<FileUploadEntity, Long> {
    Page<FileUploadEntity> findBy(Pageable pageable);
}
