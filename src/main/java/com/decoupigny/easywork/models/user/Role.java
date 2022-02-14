package com.decoupigny.easywork.models.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@Document(collection = "roles")
public class Role {
    @Id
    private String id;
    private ERole name;
}