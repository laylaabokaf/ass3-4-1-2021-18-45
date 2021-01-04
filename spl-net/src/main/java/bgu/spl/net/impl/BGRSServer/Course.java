package bgu.spl.net.impl.BGRSServer;

import java.util.ArrayList;
import java.util.List;

public class Course {
    //course name could be string also
    private short courseNum;
    private String courseName;
    private List<Short> KdamCoursesList;
    private int numOfMaxStudents;
    
    public Course(){
        this.KdamCoursesList=new ArrayList<>();
//        this.courseName=courseName;
//        this.courseNum=courseNum;
//        this.KdamCoursesList=KdamCoursesList;
//        this.numOfMaxStudents=numOfMaxStudents;
    }

    public short getCourseNum() {
        return courseNum;
    }

    public void setCourseNum(short courseNum) {
        this.courseNum = courseNum;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public List<Short> getKdamCoursesList() {
        return KdamCoursesList;
    }

    public void setKdamCoursesList(List<Short> kdamCoursesList) {
        KdamCoursesList = kdamCoursesList;
    }

    public int getNumOfMaxStudents() {
        return numOfMaxStudents;
    }

    public void setNumOfMaxStudents(int numOfMaxStudents) {
        this.numOfMaxStudents = numOfMaxStudents;
    }
}
