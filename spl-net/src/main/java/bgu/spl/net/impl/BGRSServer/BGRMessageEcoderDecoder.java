package bgu.spl.net.impl.BGRSServer;
import bgu.spl.net.api.MessageEncoderDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class BGRMessageEcoderDecoder implements MessageEncoderDecoder<BGRMessage> {
    private byte[] bytes = new byte[1 << 10];
    private int len=0;
    private Short opCode=0;

    @Override
    public BGRMessage decodeNextByte(byte nextByte) {
        if (len == 2&&opCode==0) {
            byte[] opcodeBytes=Arrays.copyOfRange(bytes,0,2);
            opCode=bytesToShort(opcodeBytes);
        }
        if(!shouldPopString(nextByte)){
            pushByte(nextByte);//len++
            return null;
        }else
            System.out.println("we pop at lenght: "+len);
            return popString();
      //not a line yet
    }

    private Boolean shouldPopString(byte nextByte){
        if(opCode==0){
            return false;
        }
        else{
            if(opCode==1||opCode==2||opCode==3){
                if(nextByte=='\0'){
                    int count=0;
                    for (int i=0;i<len;i++){
                        if(bytes[i]=='\0')
                           count++;
                    }
                    if(count==2){
                        return true;
                    }
                }
            }
            if(opCode==5||opCode==6||opCode==7||opCode==9||opCode==10){
                if(len==3){
                    pushByte(nextByte);
                    return true;
                }
            }
            if(opCode==4||opCode==11){
                if(len==3)return true;
            }
            if(opCode==8){
                if(nextByte=='\0'){
                    return true;
                }
            }
        }
        return false;
    }
    @Override
    public byte[] encode(BGRMessage message) {
        System.out.println("send byets encode");
        byte[] opcode=shortToBytes(message.getOpcode());
        byte[] opcodeMessage=shortToBytes(message.getOpcodeMessage());
        byte[] opcodeAndopcodeMessage=merge(opcode,opcodeMessage);
        if(message.getOpcode()==13){//ERROR msg
             return addZero(opcodeAndopcodeMessage);
         }
        if(message.getOpcode()==12){//ACK msg
             if(message.getOpcodeMessage()==1||message.getOpcodeMessage()==2||message.getOpcodeMessage()==3||message.getOpcodeMessage()==4
               ||message.getOpcodeMessage()==5||message.getOpcodeMessage()==10){
                 return addZero(opcodeAndopcodeMessage);
             }
             if(message.getOpcodeMessage()==6) {
                 byte[] courseByte;
                 //When the server gets the message it returns the list of the KDAM courses, in the SAME  ORDER  as  in  the  courses  fil
                 for(int i=0;i<message.getCoursesList().size();i++){
                          courseByte=shortToBytes(message.getCoursesList().get(i));
                         opcodeAndopcodeMessage=merge(opcodeAndopcodeMessage,courseByte);
                 }
                 return addZero(opcodeAndopcodeMessage);
             }
             if(message.getOpcodeMessage()==7){
                 byte[] courseNum=shortToBytes(message.getCourseNum());
                 byte[] courseNam=message.getCourseName().getBytes(StandardCharsets.UTF_8);
                 byte[] studentsRegisted=String.valueOf(message.getCurrentRegistedStudents()).getBytes(StandardCharsets.UTF_8);
                 byte[] studentMaxRegisted=String.valueOf(message.getMaxstudentRegistedtoCourse()).getBytes(StandardCharsets.UTF_8);
                 opcodeAndopcodeMessage=addArray(addArray(addArray(merge(opcodeAndopcodeMessage,courseNum),courseNam),studentsRegisted),studentMaxRegisted);
                 byte[] Students=new byte[0];
                 byte[] studenteByte;
                for(int i=0;i<message.getStudentList().size();i++){
                     studenteByte=message.getStudentList().get(i).getBytes(StandardCharsets.UTF_8);
                     Students=addArray(Students,studenteByte);
                }
                return addZero(merge(opcodeAndopcodeMessage,Students));
             }
            if(message.getOpcodeMessage()==8){
                 byte[] userNam=message.getStudentNam().getBytes(StandardCharsets.UTF_8);
                 byte[] courses=new byte[0];
                 for(int i=0;i<message.getCoursesList().size();i++){
                     byte[] courseByte=shortToBytes(message.getCoursesList().get(i));
                     courses=merge(courses,courseByte);
                }
                 return addZero(merge(addArray(opcodeAndopcodeMessage,userNam),courses));
            }
            if (message.getOpcodeMessage()==9){
                 byte[] registerd=message.getREGISTERED().getBytes(StandardCharsets.UTF_8);
                return addZero(merge(opcodeAndopcodeMessage,registerd));
             }
             if (message.getOpcodeMessage()==11){
                 byte[] courses=new byte[0];
                 for(int i=0;i<message.getCoursesList().size();i++){
                     byte[] courseByte=shortToBytes(message.getCoursesList().get(i));
                     courses=merge(courses,courseByte);
                 }
                 return addZero(merge(opcodeAndopcodeMessage,courses));
           }
        }

        return new byte[0];
    }

    public short bytesToShort(byte[] byteArr)
    {
        short result = (short)((byteArr[0] & 0xff) << 8);
        result += (short)(byteArr[1] & 0xff);
        return result;
    }

    public byte[] shortToBytes(short num)
    {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte)((num >> 8) & 0xFF);
        bytesArr[1] = (byte)(num & 0xFF);
        return bytesArr;
    }
    public static byte[] addArray(byte[] first,byte[] secend){
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
   public byte[] addZero(byte[] bytes){
        byte[] result=Arrays.copyOf(bytes,bytes.length+1);
        result[bytes.length]='\0';
        return result;
   }

    public static byte[] merge(byte[] first,byte[] secend){
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

    private void pushByte(byte nextByte) {
        if (len >= bytes.length) {
            bytes = Arrays.copyOf(bytes, len * 2);
        }

        bytes[len++] = nextByte;
    }
    private BGRMessage popString() {
        //notice that we explicitly requesting that the string will be decoded from UTF-8
        //this is not actually required as it is the default encoding in java.
        BGRMessage bgrMessage = new BGRMessage(opCode);
        //if the message is register Admin or student or log in
        if(opCode==1||opCode==2||opCode==3){
            int userName=0;//userName started from byte2 until byte userName
            int userPassword=0;//userPassword started from userName+1 until userPassword
            for(int i=2;i<len;i++){
                if(bytes[i]=='\0'){
                    if(userName==0){
                    userName=i-1;
                    }
                    userPassword=i;
                    break;
                }
            }
            bgrMessage.setUsername(new String(bytes,2,userName));
            bgrMessage.setUserPassword(new String(bytes,userName+1,userPassword));
        }
        if (opCode == 5||opCode==6||opCode==7||opCode==9||opCode==10) {
          byte[] courseBytes=Arrays.copyOfRange(bytes,2,4);
          bgrMessage.setCourseNum(bytesToShort(courseBytes));
        }
        if(opCode==8){
            bgrMessage.setStudentNam(new String(bytes,2,len-1));
        }
        len = 0;
        return bgrMessage;
    }
}
