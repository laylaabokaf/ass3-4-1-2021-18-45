//
// Created by spl211 on 22/12/2020.
//
#pragma once
#include "protocol.h"
#include "connectionHandler.h"
#ifndef BOOST_ECHO_CLIENT_KEYBOARDTHREAD_H
#define BOOST_ECHO_CLIENT_KEYBOARDTHREAD_H

#endif //BOOST_ECHO_CLIENT_KEYBOARDTHREAD_H
class keyboardthread{
private:
    protocol* proto;
    bool online;
    ConnectionHandler* connection;
    std::string &command;
public:
    keyboardthread(protocol* proto,ConnectionHandler* connection,std::string& com);
    void process();
    void shortToBytes(short num, char* bytesArr);
    std::vector<std::string> readingInput(std::string& s,char x);
};