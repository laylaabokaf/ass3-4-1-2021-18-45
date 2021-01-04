
package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.impl.BGRSServer.Database;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Mymain {
    public static void main(String[] args) {


        Database MyData = Database.getInstance();
        Boolean b = MyData.initialize("./Courses.txt");

        //test Protocol
        BGRMessage msg=new BGRMessage((short) 1);//AdminRegister
        msg.setUsername("layla");
        msg.setUserPassword("qwe123");
        BGRProtocol bgrProtocol=new BGRProtocol();
        BGRMessage returndMessage=bgrProtocol.process(msg);
        System.out.println(returndMessage.getOpcode());
        System.out.println(returndMessage.getOpcodeMessage());
        //Admin want to chick KDAM courses
        BGRMessage kdamChick=new BGRMessage((short) 6);
        kdamChick.setCourseNum((short) 202);
        BGRMessage returndKdamChick=bgrProtocol.process(kdamChick);
        System.out.println(returndKdamChick.getOpcode()+" for messsage opcode: "+ returndKdamChick.getOpcodeMessage()
        + " courses num: "+returndKdamChick.getCoursesList().size());
       // kdamChick.setOpcode();
        returndKdamChick= bgrProtocol.COURSESTAT(kdamChick);
        System.out.println(returndKdamChick.getOpcode()+" for messsage opcode: "+ returndKdamChick.getOpcodeMessage()+" "+returndKdamChick.getCurrentRegistedStudents()+" "+returndKdamChick.getMaxstudentRegistedtoCourse());

        }
    }

