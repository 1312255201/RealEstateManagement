package cn.bugfish.realestatemanagement.UserMannageSystem.Beans;

import java.io.Serializable;

public class UserBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private String phonenumber;
    private String email;
    private String password;

    public UserBean() {}

    // 有参构造器
    public UserBean(String phonenumber, String email, String password) {
        this.phonenumber = phonenumber;
        this.email = email;
        this.password = password;
    }

    // Getter 和 Setter
    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
