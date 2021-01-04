//
// Created by spl211 on 23/12/2020.
//
#pragma once
#include <unordered_map>

#ifndef BOOST_ECHO_CLIENT_PROTOCOL_H
#define BOOST_ECHO_CLIENT_PROTOCOL_H
#include <mutex>
#include "connectionHandler.h"


#endif //BOOST_ECHO_CLIENT_PROTOCOL_H
class protocol{
private:
    ConnectionHandler* connectionHandler;
    std::mutex *mute;
    bool online;
    int getAndSend;
    std::unordered_map<std::string,std::string> *receiptRequests;

public:
    protocol(ConnectionHandler* connect,std::mutex *mutex); //build
    bool isonline(); //connect or not
    short bytesToShort(char* bytesArr);  //from the web
    std::string convertToString(char* a, int size,int from) ;//from byts to string
    void doing(char *bays );
//    void addReceipt(std::string receiptID,std::string receipt);
    void ADMINREG(std::string username,std::string pass);
    void LOGOUT();
    std::vector<std::string> readingInput(std::string& s,char x);
    int getAtomic();
    void setAtomic(int atomic);


};