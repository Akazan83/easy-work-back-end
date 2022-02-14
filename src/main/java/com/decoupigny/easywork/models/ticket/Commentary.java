package com.decoupigny.easywork.models.ticket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Commentary {
    private String userId;
    private String firstName;
    private String lastName;
    private String text;
    private String  sendingDate;
}
