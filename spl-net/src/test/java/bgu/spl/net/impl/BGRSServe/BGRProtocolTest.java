package bgu.spl.net.impl.BGRSServe;
import bgu.spl.net.impl.BGRSServer.BGRProtocol;
import bgu.spl.net.impl.BGRSServer.BGRMessage;
import bgu.spl.net.impl.BGRSServer.Database;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BGRProtocolTest {
    private BGRProtocol bgrProtocol;
    private BGRMessage bgrMessage;
    private Database myData;
    @BeforeEach
    void setUp() {
        myData=Database.getInstance();
        bgrProtocol=new BGRProtocol();
    }
    @Test
    void process2() {

    }

    @Test
    void process1() {//login logout register
        bgrMessage=new BGRMessage((short)3);//login whith user isnt exested
        bgrMessage.setUsername("ADMIN");
        bgrMessage.setUserPassword("qwe123");
        BGRMessage protocolMessage=bgrProtocol.process(bgrMessage);
        assertEquals((short) 13,protocolMessage.getOpcode());
        assertEquals((short)3,protocolMessage.getOpcodeMessage());

        bgrMessage.setOpcode((short)1);//registe
        bgrMessage.setUsername("ADMIN");
        bgrMessage.setUserPassword("qwe123");
        protocolMessage=bgrProtocol.process(bgrMessage);
        assertEquals((short) 12,protocolMessage.getOpcode());
        assertEquals((short) 1,protocolMessage.getOpcodeMessage());
        assertEquals(true,myData.chickUser("ADMIN"));


        bgrMessage.setOpcode((short)1);//registe twice
        bgrMessage.setUsername("ADMIN");
        bgrMessage.setUserPassword("e123");
        protocolMessage=bgrProtocol.process(bgrMessage);
        assertEquals((short) 13,protocolMessage.getOpcode());
        assertEquals((short) 1,protocolMessage.getOpcodeMessage());

        bgrMessage.setOpcode((short)2);//admin try to regist as student
        bgrMessage.setUsername("ADMIN");
        bgrMessage.setUserPassword("e123");
        protocolMessage=bgrProtocol.process(bgrMessage);
        assertEquals((short) 13,protocolMessage.getOpcode());
        assertEquals((short) 2,protocolMessage.getOpcodeMessage());

        bgrMessage.setOpcode((short)8);//admin didnt log in
        bgrMessage.setUsername("layla");
        protocolMessage=bgrProtocol.process(bgrMessage);
        assertEquals((short) 13,protocolMessage.getOpcode());
        assertEquals((short) 8,protocolMessage.getOpcodeMessage());
        assertEquals(bgrProtocol.shouldTerminate(),false);

        bgrMessage.setOpcode((short)3);//ADMIN loged in finlley
        bgrMessage.setUsername("ADMIN");
        bgrMessage.setUserPassword("qwe123");
        protocolMessage=bgrProtocol.process(bgrMessage);
        assertEquals((short) 12,protocolMessage.getOpcode());
        assertEquals((short) 3,protocolMessage.getOpcodeMessage());
        assertEquals(true,myData.getUser("ADMIN").getActive());
        assertEquals(bgrProtocol.shouldTerminate(),false);

        bgrMessage.setOpcode((short)4);//ADMIN logout
        protocolMessage=bgrProtocol.process(bgrMessage);
        assertEquals((short) 12,protocolMessage.getOpcode());
        assertEquals((short) 4,protocolMessage.getOpcodeMessage());
        assertEquals(false,myData.getUser("ADMIN").getActive());
        assertEquals(bgrProtocol.shouldTerminate(),true);

        bgrMessage.setOpcode((short)7);//ADMIN logout and try to get info
        protocolMessage=bgrProtocol.process(bgrMessage);
        assertEquals((short) 13,protocolMessage.getOpcode());
        assertEquals((short) 7,protocolMessage.getOpcodeMessage());
        assertEquals(false,myData.getUser("ADMIN").getActive());
        assertEquals(bgrProtocol.shouldTerminate(),true);
    }
}