package cn.bugfish.realestatemanagement.UserMannageSystem.Model;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class User {
    private Integer id;
    private String role;
    private String name;
    private String idnumber;
    private String phonenumber;
    private String email;
    private String password;
    private Timestamp created_at;
}
