package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.srv.Server;

public class ReactorMain {

    public static void main(String[] args) {
//Integer.parseInt(args[0])
        Database myData=Database.getInstance();
        myData.initialize("./Courses.txt");
        Server.reactor( Integer.parseInt(args[1]),Integer.parseInt(args[0]),()->new BGRProtocol(),()->new BGRMessageEcoderDecoder()).serve();

    }
}

