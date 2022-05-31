package cn.annna.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

public class Admin implements Serializable {
    private Integer id;
    private String username;
    private String salt;
    private String password;
    private Integer state;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date createTime;
    private String enCode;

    public Admin() {
    }

    public Admin(Integer id, String username, String salt, String password, Integer state, Date createTime) {
        this.id = id;
        this.username = username;
        this.salt = salt;
        this.password = password;
        this.state = state;
        this.createTime = createTime;
    }

    public Admin(Integer id, String username, String salt, String password, Integer state, Date createTime, String enCode) {
        this.id = id;
        this.username = username;
        this.salt = salt;
        this.password = password;
        this.state = state;
        this.createTime = createTime;
        this.enCode = enCode;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getEnCode() {
        return enCode;
    }

    public void setEnCode(String enCode) {
        this.enCode = enCode;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", salt='" + salt + '\'' +
                ", password='" + password + '\'' +
                ", state=" + state +
                ", createTime=" + createTime +
                ", enCode='" + enCode + '\'' +
                '}';
    }
}
