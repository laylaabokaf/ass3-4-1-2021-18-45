package bgu.spl.net.impl.BGRSServer;

import java.util.ArrayList;
import java.util.List;

public class User_Data {
    final private String UserName;
    final private String Userpassword;
    private List<Short> UserCourses;
    private Boolean Active;
    private Boolean isAdmin;
    public User_Data(String UserName, String UserPassword,Boolean Active,Boolean isADmin){
        this.isAdmin=isADmin;
        this.UserName=UserName;
        this.Userpassword=UserPassword;
        this.Active=Active;
        this.UserCourses=new ArrayList<>();
    }
    public void addCourse(Short courseNam){
        if(!isAdmin) {
            UserCourses.add(courseNam);
        }
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public String getUserName() {
        return UserName;
    }

    public String getUserpassword() {
        return Userpassword;
    }

    public List<Short> getUserCourses() {
        return UserCourses;
    }

    public void setUserCourses(List<Short> userCourses) {
        UserCourses = userCourses;
    }

    public Boolean getActive() {
        return Active;
    }

    public void setActive(Boolean active) {
        Active = active;
    }
}
