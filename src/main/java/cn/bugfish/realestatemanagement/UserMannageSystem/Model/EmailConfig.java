package cn.bugfish.realestatemanagement.UserMannageSystem.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
使用Lombok节约代码
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmailConfig {
    private String emailAddress;
    private String emailPassword;
    private String smtpHost;
    private int smtpPort;
    private boolean sslEnabled;

}
