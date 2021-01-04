package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.api.MessagingProtocol;

import java.util.ArrayList;
import java.util.List;

public class BGRProtocol implements MessagingProtocol<BGRMessage> {

private String UserName;
private Database myData=Database.getInstance();
private boolean shouldTerminate = false;

/*
after ecoderDecoder the message we response with ACK or ERROR
 */
    @Override
    public BGRMessage process(BGRMessage msg) {
        if(msg.getOpcode()==1){
           return ADMEINRGS(msg);
       }
        if(msg.getOpcode()==2){
            return STUDENTREG(msg);
       }
       if(msg.getOpcode()==3){
            return LOGIN(msg);
       }
        if(msg.getOpcode()==4){
            return LOGOUT(msg);
       }
        if(msg.getOpcode()==5){
            return COURSEREG(msg);
        }
        if(msg.getOpcode()==6){
            return KDAMCHECK(msg);

       }if(msg.getOpcode()==7){
            return COURSESTAT(msg);

      }if(msg.getOpcode()==8){
            return STUDENTSTST(msg);

       }if(msg.getOpcode()==9){
            return ISREGISTERED(msg);

      }if(msg.getOpcode()==10){
            return UNREGISTER(msg);

     }if(msg.getOpcode()==11){
            return MYCOURSES(msg);
       }
        return null;
    }

    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }

    public BGRMessage ADMEINRGS(BGRMessage msg){
        BGRMessage returndMsg;
        if(myData.chickUser(msg.getUsername())){//if the clint already registered
             returndMsg=new BGRMessage((short) 13);
             returndMsg.setOpcodeMessage((short) 1);
             return returndMsg;
        }
        returndMsg=new BGRMessage((short) 12);
        returndMsg.setOpcodeMessage((short) 1);
        User_Data newUser=new User_Data(msg.getUsername(), msg.getUserPassword(),false,true);
        myData.AddUser(newUser);
        UserName=msg.getUsername();//update the name of this clint
        return returndMsg;
    }

    public BGRMessage STUDENTREG(BGRMessage msg){
        BGRMessage returndMsg;
        if(myData.chickUser(msg.getUsername())){
            returndMsg=new BGRMessage((short) 13);
            returndMsg.setOpcodeMessage((short) 2);
            return returndMsg;
        }
        returndMsg=new BGRMessage((short) 12);
        returndMsg.setOpcodeMessage((short) 2);
        User_Data newUser=new User_Data(msg.getUsername(), msg.getUserPassword(),false,false);
        myData.AddUser(newUser);
        UserName=msg.getUsername();//update the name of this clint
        return returndMsg;

    }

    public BGRMessage LOGIN(BGRMessage msg){
        BGRMessage returndMsg;
        if(!myData.chickUser(msg.getUsername())){//if thr clint
            returndMsg=new BGRMessage((short) 13);
            returndMsg.setOpcodeMessage((short) 3);
            return returndMsg;
        }
        if(!(myData.getUser(msg.getUsername()).getUserpassword().equals(msg.getUserPassword()))){
            returndMsg=new BGRMessage((short) 13);
            returndMsg.setOpcodeMessage((short) 3);
            return returndMsg;
        }
        if((myData.getUser(msg.getUsername()).getActive())){
            returndMsg=new BGRMessage((short) 13);
            returndMsg.setOpcodeMessage((short) 3);
            return returndMsg;
        }
        returndMsg=new BGRMessage((short) 12);
        returndMsg.setOpcodeMessage((short) 3);
        myData.getUser(msg.getUsername()).setActive(true);
        UserName= msg.getUsername();
        return returndMsg;

    }

    public BGRMessage LOGOUT(BGRMessage msg){
     BGRMessage returndMsg;
     if(UserName==null){
         returndMsg=new BGRMessage((short) 13);
         returndMsg.setOpcodeMessage((short) 4);
         return returndMsg;
     }
     if(!myData.getUser(UserName).getActive()){
         returndMsg=new BGRMessage((short) 13);
         returndMsg.setOpcodeMessage((short) 4);
         return returndMsg;
     }
        shouldTerminate=true;
        myData.getUser(UserName).setActive(false);
        returndMsg=new BGRMessage((short) 12);
        returndMsg.setOpcodeMessage((short) 4);
        return returndMsg;
    }
    public BGRMessage COURSEREG(BGRMessage msg){
      BGRMessage returndMsg;
      if((UserName==null)||!myData.getCoursesList().containsKey(msg.getCourseNum())) {
          returndMsg=new BGRMessage((short) 13);
          returndMsg.setOpcodeMessage((short) 5);
          return returndMsg;
      }if(myData.getUser(UserName).getActive()){
          if(!myData.getUser(UserName).getAdmin()){
              if(myData.chikKDAMforStudent(UserName,msg.getCourseNum())){
                  returndMsg=new BGRMessage((short) 12);
                  returndMsg.setOpcodeMessage((short) 5);
                  myData.getUser(UserName).addCourse(msg.getCourseNum());
                  return returndMsg;
              }
          }
        }
        returndMsg=new BGRMessage((short) 13);
        returndMsg.setOpcodeMessage((short) 5);
        return returndMsg;
    }
    public BGRMessage KDAMCHECK(BGRMessage msg){//same order as in the list
        BGRMessage returndMsg;
        returndMsg=new BGRMessage((short) 12);
        returndMsg.setOpcodeMessage((short) 6);
        returndMsg.setCoursesList(myData.sortedList(myData.getCoursesList().get(msg.getCourseNum()).getKdamCoursesList()));

        return returndMsg;
        //Q- is it ok to return ack message if user isnet registed or logrd in ?
    }
    public BGRMessage COURSESTAT(BGRMessage msg){
        BGRMessage bgrMessage;
        if(!myData.getUser(UserName).getAdmin()||!myData.getUser(UserName).getActive()){
            bgrMessage=new BGRMessage((short) 13);
            bgrMessage.setOpcodeMessage((short) 7);
            return bgrMessage;
        }

       bgrMessage=new BGRMessage((short) 12);
        bgrMessage.setOpcodeMessage((short) 7);
        bgrMessage.setCourseNum(msg.getCourseNum());
       List<String> studentList;
       int MaxStudent=myData.getCoursesList().get(msg.getCourseNum()).getNumOfMaxStudents();
       bgrMessage.setMaxstudentRegistedtoCourse(MaxStudent);
       studentList=myData.StudentRegistedtoThisCourse(msg.getCourseNum());
       bgrMessage.setStudentList(studentList);
        int numberofRegistedStudent=studentList.size();
        bgrMessage.setCurrentRegistedStudents(numberofRegistedStudent);
       bgrMessage.setCourseName(myData.getCourseName(msg.getCourseNum()));
       return bgrMessage;
    }
    public BGRMessage STUDENTSTST(BGRMessage msg){
        BGRMessage bgrMessage;
        if(!myData.getUser(UserName).getAdmin()||!myData.getUser(UserName).getActive()){
            bgrMessage=new BGRMessage((short) 13);
            bgrMessage.setOpcodeMessage((short) 8);
            return bgrMessage;
        }
        bgrMessage=new BGRMessage((short) 12);
        bgrMessage.setOpcodeMessage((short) 8);
        bgrMessage.setStudentNam(msg.getStudentNam());
        bgrMessage.setCoursesList(myData.sortedList(myData.getUser(msg.getStudentNam()).getUserCourses()));
        return bgrMessage;
    }
    public BGRMessage ISREGISTERED(BGRMessage msg){
        BGRMessage bgrMessage;
        if(myData.getUser(UserName).getAdmin()||!myData.getUser(UserName).getActive()){
            bgrMessage=new BGRMessage((short) 13);
            bgrMessage.setOpcodeMessage((short) 9);
       //     bgrMessage.setREGISTERED("REGISTERED");
            return bgrMessage;
        }
        bgrMessage=new BGRMessage((short) 12);
        bgrMessage.setOpcodeMessage((short) 9);
        if(myData.getUser(UserName).getUserCourses().contains(msg.getCourseNum()))bgrMessage.setREGISTERED("REGISTERED");
        else {bgrMessage.setREGISTERED("UNREGISTERED");}
        return bgrMessage;
    }
    public BGRMessage UNREGISTER(BGRMessage msg){
        BGRMessage bgrMessage;
        if (UserName==null){
            bgrMessage=new BGRMessage((short) 13);
            bgrMessage.setOpcodeMessage((short) 10);
            //     bgrMessage.setREGISTERED("REGISTERED");
            return bgrMessage;
        }
        if(myData.getUser(UserName).getAdmin()||!myData.getUser(UserName).getActive()||!myData.getUser(UserName).getUserCourses().contains(msg.getCourseNum())){
            bgrMessage=new BGRMessage((short) 13);
            bgrMessage.setOpcodeMessage((short) 10);
            //     bgrMessage.setREGISTERED("REGISTERED");
            return bgrMessage;
        }
        bgrMessage=new BGRMessage((short) 12);
        bgrMessage.setOpcodeMessage((short) 10);
        myData.getUser(UserName).getUserCourses().remove(msg.getCourseNum());
        return bgrMessage;
    }
    public BGRMessage MYCOURSES(BGRMessage msg){
        BGRMessage bgrMessage;
        if(UserName==null||myData.getUser(UserName).getAdmin()||!myData.getUser(UserName).getActive()){
            bgrMessage=new BGRMessage((short) 13);
            bgrMessage.setOpcodeMessage((short) 11);
            //     bgrMessage.setREGISTERED("REGISTERED");
            return bgrMessage;
        }
        bgrMessage=new BGRMessage((short) 12);
        bgrMessage.setOpcodeMessage((short) 11);
        bgrMessage.setCoursesList(myData.getUser(UserName).getUserCourses());
        return bgrMessage;
    }
}
