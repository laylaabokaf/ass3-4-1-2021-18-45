package bgu.spl.net.impl.BGRSServer;

import java.util.ArrayList;
import java.util.List;

public class BGRMessage {
    private Short Opcode;
    private String Username;
    private String UserPassword;
    private Short courseNum;
    private String courseName;
    private Short OpcodeMessage;//for ACK Message or ERROR Message
    private List<Short> coursesList;//for ACK message
    private List<String> studentList;
    private int MaxstudentRegistedtoCourse;
    private int currentRegistedStudents;
    private String studentNam;
    private String REGISTERED;
    public BGRMessage(Short Opcode){
        courseName=null;
        this.Opcode=Opcode;
        REGISTERED=null;
        studentNam=null;
        Username=null;
        UserPassword=null;
        courseNum =null;
        OpcodeMessage=0;
        coursesList=new ArrayList<>();
        studentList=new ArrayList<>();
        MaxstudentRegistedtoCourse=0;
        currentRegistedStudents=0;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getREGISTERED() {
        return REGISTERED;
    }

    public void setREGISTERED(String REGISTERED) {
        this.REGISTERED = REGISTERED;
    }

    public String getStudentNam() {
        return studentNam;
    }

    public void setStudentNam(String studentNam) {
        this.studentNam = studentNam;
    }

    public Short getOpcode() {
        return Opcode;
    }

    public void setOpcode(Short opcode) {
        Opcode = opcode;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getUserPassword() {
        return UserPassword;
    }

    public void setUserPassword(String userPassword) {
        UserPassword = userPassword;
    }

    public Short getCourseNum() {
        return courseNum;
    }

    public void setCourseNum(Short courseNum) {
        this.courseNum = courseNum;
    }

    public Short getOpcodeMessage() {
        return OpcodeMessage;
    }

    public void setOpcodeMessage(Short opcodeMessage) {
        OpcodeMessage = opcodeMessage;
    }

    public List<Short> getCoursesList() {
        return coursesList;
    }

    public void setCoursesList(List<Short> coursesList) {
        this.coursesList = coursesList;
    }

    public List<String> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<String> studentList) {
        this.studentList = studentList;
    }

    public int getMaxstudentRegistedtoCourse() {
        return MaxstudentRegistedtoCourse;
    }

    public void setMaxstudentRegistedtoCourse(int maxstudentRegistedtoCourse) {
        MaxstudentRegistedtoCourse = maxstudentRegistedtoCourse;
    }

    public int getCurrentRegistedStudents() {
        return currentRegistedStudents;
    }

    public void setCurrentRegistedStudents(int currentRegistedStudents) {
        this.currentRegistedStudents = currentRegistedStudents;
    }
}
