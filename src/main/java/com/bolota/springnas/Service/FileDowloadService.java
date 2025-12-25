package com.bolota.springnas.Service;

import com.bolota.springnas.Entities.FileDTO;
import com.bolota.springnas.Entities.FileUploadEntity;

public class FileDowloadService {
    public static FileDTO convert(FileUploadEntity source) {
        return new FileDTO(source.getId(),source.getFl_name(),source.getFl_size(),source.getDate());
    }

}
