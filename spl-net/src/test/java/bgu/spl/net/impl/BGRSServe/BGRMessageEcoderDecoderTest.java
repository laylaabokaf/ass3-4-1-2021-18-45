package bgu.spl.net.impl.BGRSServer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BGRMessageEcoderDecoderTest {
    BGRMessageEcoderDecoder bgrMessageEcoderDecoder;
    BGRMessage bgrMessage ;
    @BeforeEach
    void setUp() {
      bgrMessage =new BGRMessage((short)0);
       bgrMessageEcoderDecoder=new BGRMessageEcoderDecoder();

    }
    @Test
    void decodeNextByteOpcode1() {//test for all register and login
        byte[] opcode=shortToBytes((short) 1);
        byte[] userName=("layla").getBytes(StandardCharsets.UTF_8);
        byte[] userPass=("w1234r").getBytes(StandardCharsets.UTF_8);
        byte[] decode=addArray(addArray(opcode,userName),userPass);
        System.out.println(decode.length);
        for (int i=0;i<decode.length;i++) {
            bgrMessage = bgrMessageEcoderDecoder.decodeNextByte(decode[i]);
            if (bgrMessage!=null){
                short opcode1=1;
               assertEquals(bgrMessage.getOpcode(),opcode1);
               assertEquals("layla".trim(),bgrMessage.getUsername().trim());
               assertEquals("w1234r".trim(),bgrMessage.getUserPassword().trim());

            }
        }
    }
    @Test
    void decodeNextByteOpcode8() {//test for all register and login
        byte[] opcode=shortToBytes((short) 8);
        byte[] userName=("layla").getBytes(StandardCharsets.UTF_8);
        byte[] decode=addArray(opcode,userName);
        System.out.println(decode.length);
        BGRMessage bgrMessage ;
        for (int i=0;i<decode.length;i++) {
            bgrMessage = bgrMessageEcoderDecoder.decodeNextByte(decode[i]);
            if (bgrMessage!=null){
                short opcode1=8;
                assertEquals((short)8,bgrMessage.getOpcode());
                assertEquals("layla".trim(),bgrMessage.getStudentNam().trim());

            }
        }
    }
    @Test
    void decodeNextByteOpcode5(){
        byte[] opcode=shortToBytes((short) 5);
        byte[] coursenum=shortToBytes((short) 23);
        byte[] decode=merge(opcode,coursenum);
        BGRMessage bgrMessage ;
        for (int i=0;i<decode.length;i++) {
            bgrMessage = bgrMessageEcoderDecoder.decodeNextByte(decode[i]);
            if (bgrMessage != null) {
                short opcode1 = 23;
                short opcode2=5;
                assertEquals(bgrMessage.getOpcode(), opcode2);
                assertEquals(bgrMessage.getCourseNum(),opcode1);
            }
        }
    }
    void decodeNextByteOpcode4(){
        byte[] opcode=shortToBytes((short) 4);

        BGRMessage bgrMessage ;
        for (int i=0;i<opcode.length;i++) {
            bgrMessage = bgrMessageEcoderDecoder.decodeNextByte(opcode[i]);
            if (bgrMessage != null) {
                short opcode1 = 4;
                assertEquals(bgrMessage.getOpcode(), opcode1);
            }
        }
    }

    @Test
    void encodeAck1_2_3_4_5_10() {
        bgrMessage.setOpcode((short)12);//ack msg
        bgrMessage.setOpcodeMessage((short) 1);//answer for 1
      byte[] encoded=bgrMessageEcoderDecoder.encode(bgrMessage);
      byte[] exepted=addArray(shortToBytes((short) 12),shortToBytes((short) 1));
        assertArrayEquals(encoded,exepted);
    }
    @Test
    void encodeAck6() {
        bgrMessage.setOpcode((short)12);//ack msg
        bgrMessage.setOpcodeMessage((short) 6);//answer for 1
        List<Short> coursesList=new ArrayList<>();
        coursesList.add((short)12);//0
        coursesList.add((short)98);//1
        coursesList.add((short)123);//2
        coursesList.add((short)1);//3
        bgrMessage.setCoursesList(coursesList);
        byte[] encoded=bgrMessageEcoderDecoder.encode(bgrMessage);
        byte[] courses=merge(merge(merge(shortToBytes(coursesList.get(0)),shortToBytes(coursesList.get(1))),shortToBytes(coursesList.get(2))),shortToBytes(coursesList.get(3)));
        byte[] exepted=merge(shortToBytes((short) 12),shortToBytes((short) 6));
        assertArrayEquals(encoded,addArray(exepted,courses));
    }

    @Test
    void encodeAck8(){
        bgrMessage.setOpcode((short)12);//ack msg
        bgrMessage.setOpcodeMessage((short) 8);//answer for 1
        List<Short> coursesList=new ArrayList<>();
        coursesList.add((short)12);//0
        coursesList.add((short)98);//1
        coursesList.add((short)123);//2
        coursesList.add((short)1);//3
        bgrMessage.setCoursesList(coursesList);
        bgrMessage.setStudentNam("layla");
        byte[] studentName=("layla").getBytes(StandardCharsets.UTF_8);
        byte[] encoded=bgrMessageEcoderDecoder.encode(bgrMessage);
        byte[] courses=merge(merge(merge(shortToBytes(coursesList.get(0)),shortToBytes(coursesList.get(1))),shortToBytes(coursesList.get(2))),shortToBytes(coursesList.get(3)));
        byte[] exepted=add_n(merge(shortToBytes((short) 12),shortToBytes((short) 8)),studentName);
        assertArrayEquals(encoded,addArray(exepted,courses));
    }
    @Test
    void encodeAck9(){
        bgrMessage.setOpcode((short)12);//ack msg
        bgrMessage.setOpcodeMessage((short) 9);//answer for 1

        bgrMessage.setREGISTERED(("REGISTERED").trim());
        byte[] Registred=("REGISTERED").trim().getBytes(StandardCharsets.UTF_8);
        byte[] encoded=bgrMessageEcoderDecoder.encode(bgrMessage);
        byte[] exepted=merge(shortToBytes((short) 12),shortToBytes((short) 9));
        System.out.println(encoded.length);
        System.out.println(addArray(exepted,Registred).length);
        assertArrayEquals(encoded,addArray(exepted,Registred));
    }
    @Test
    void encodeAck11(){
        bgrMessage.setOpcode((short)12);//ack msg
        bgrMessage.setOpcodeMessage((short) 11);//answer for 1
        List<Short> coursesList=new ArrayList<>();
        coursesList.add((short)12);//0
        coursesList.add((short)98);//1
        coursesList.add((short)123);//2
        coursesList.add((short)1);//3
        bgrMessage.setCoursesList(coursesList);
        byte[] encoded=bgrMessageEcoderDecoder.encode(bgrMessage);
        byte[] courses=merge(merge(merge(shortToBytes(coursesList.get(0)),shortToBytes(coursesList.get(1))),shortToBytes(coursesList.get(2))),shortToBytes(coursesList.get(3)));
        byte[] exepted=merge(shortToBytes((short) 12),shortToBytes((short) 11));
        assertArrayEquals(encoded,addArray(exepted,courses));
    }
@Test
void encodeAck7(){

    bgrMessage.setOpcode((short)12);//ack msg
    bgrMessage.setOpcodeMessage((short) 7);//answer for 1
    List<String> studentList=new ArrayList<>();
    studentList.add("kkkkk");//0
    studentList.add("wwwd ww");//1
//    studentList.add("lkok");//2
//    studentList.add("lk skd kmf kmksw");//3
    bgrMessage.setStudentList(studentList);
    bgrMessage.setCourseNum((short)11);
    bgrMessage.setCourseName("time to sleep .. ");
    bgrMessage.setMaxstudentRegistedtoCourse(20);
    bgrMessage.setCurrentRegistedStudents(3);

    byte[] encoded=bgrMessageEcoderDecoder.encode(bgrMessage);
    byte[] students=add_n((add_n(new byte[0],studentList.get(0).getBytes(StandardCharsets.UTF_8))),(studentList.get(1)).getBytes(StandardCharsets.UTF_8));
    byte[] opcods=merge(shortToBytes((short) 12),shortToBytes((short) 7));
    byte[] courseNumName=merge(opcods,add_n(shortToBytes((short) 11),("time to sleep .. ").getBytes(StandardCharsets.UTF_8)));
    byte[] maxR=add_n(add_n(courseNumName,String.valueOf(3).getBytes(StandardCharsets.UTF_8)),String.valueOf(20).getBytes(StandardCharsets.UTF_8));
    assertArrayEquals(encoded,addArray(maxR,students));
}

    public  byte[] addArray(byte[] first,byte[] secend){
        byte[] result=new byte[first.length+secend.length+1];
        for(int i=0;i< first.length;i++){
            result[i]=first[i];
        }
        int j=0;
        for (int i=first.length;i< result.length-1;i++){
            result[i]=secend[j];j++;
        }
        result[result.length-1]='\0';
        return result;
    }
    public  byte[] add_n(byte[] first,byte[] secend){
        byte[] result=new byte[first.length+secend.length+1];
        for(int i=0;i< first.length;i++){
            result[i]=first[i];
        }
        int j=0;
        for (int i=first.length;i< result.length-1;i++){
            result[i]=secend[j];j++;
        }
        result[result.length-1]='\n';
        return result;
    }

    public  byte[] merge(byte[] first,byte[] secend){
        byte[] result=new byte[first.length+secend.length];
        for(int i=0;i< first.length;i++){
            result[i]=first[i];
        }
        int j=0;
        for (int i=first.length;i< result.length;i++){
            result[i]=secend[j];j++;
        }
        return result;
    }
    public byte[] shortToBytes(short num)
    {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte)((num >> 8) & 0xFF);
        bytesArr[1] = (byte)(num & 0xFF);
        return bytesArr;
    }
}