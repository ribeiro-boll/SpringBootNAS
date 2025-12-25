package com.bolota.springnas.Entities;

import jakarta.persistence.Entity;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class FileDTO {
    private long id;
    private String name;
    private long size;
    private String date;
}
