package com.decoupigny.easywork.models.ticket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Participant {
    private String userId;
    private String status;
    private String firstName;
    private String lastName;
    private String role;
}
