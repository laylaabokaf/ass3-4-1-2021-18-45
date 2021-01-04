//
// Created by spl211 on 22/12/2020.
//
#include "sockerthread.h"
#include "connectionHandler.h"
#include <iostream>

using namespace std;
sockerthread::sockerthread(protocol* protocol, ConnectionHandler* connect):Protocol(protocol),Connect(connect) {}

void sockerthread::doing() {
    while(Protocol->isonline()) {
        if (Protocol->getAtomic() > 0) {
            std::cout << "starting thread sockerThread" << std::endl;
            char answer[1024];
            Connect->getFromBytes(answer);
            std::cout <<"SockerThread: after getFromByets" << std::endl;
            //make it a frame
            Protocol->doing(answer);
            Protocol->setAtomic(Protocol->getAtomic()-1);
            std::cout <<"SockerThred: done while" << std::endl;
        }
    }
}