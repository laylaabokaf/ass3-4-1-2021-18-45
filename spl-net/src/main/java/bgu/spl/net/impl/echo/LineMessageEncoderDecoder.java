package bgu.spl.net.impl.echo;

import bgu.spl.net.api.MessageEncoderDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class LineMessageEncoderDecoder implements MessageEncoderDecoder<String> {

    private byte[] bytes = new byte[1 << 10]; //start with 1k
    private int len = 0;

    @Override
    public String decodeNextByte(byte nextByte) {
        //notice that the top 128 ascii characters have the same representation as their utf-8 counterparts
        //this allow us to do the following comparison
        if (len==3) {
            pushByte(nextByte);
            return popString();
        }

        pushByte(nextByte);
        return null; //not a line yet
    }

    @Override
    public byte[] encode(String adminRegist) {
        byte[] opcode=shortToBytes((short)1);
        byte[] userName=("layla").getBytes(StandardCharsets.UTF_8);
        byte[] userPass=("123").getBytes(StandardCharsets.UTF_8);

        return addArray(addArray(opcode,userName),userPass); //uses utf8 by default
    }

    private void pushByte(byte nextByte) {
        if (len >= bytes.length) {
            bytes = Arrays.copyOf(bytes, len * 2);
        }

        bytes[len++] = nextByte;
    }
    public short bytesToShort(byte[] byteArr)
    {
        short result = (short)((byteArr[0] & 0xff) << 8);
        result += (short)(byteArr[1] & 0xff);
        return result;}

    private String popString() {
        //notice that we explicitly requesting that the string will be decoded from UTF-8
        //this is not actually required as it is the default encoding in java.
        short opcode=bytesToShort(Arrays.copyOfRange(bytes,0,2));
        short opcodeMessage=bytesToShort(Arrays.copyOfRange(bytes,2,4));
        String result = String.valueOf(opcode)+String.valueOf(opcodeMessage);
        len = 0;
        return result;
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
