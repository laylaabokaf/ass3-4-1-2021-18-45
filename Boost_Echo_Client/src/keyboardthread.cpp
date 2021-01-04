//
// Created by spl211 on 22/12/2020.
//
#include "keyboardthread.h"
#include <string>
#include <bits//stdc++.h>
#include <boost/lexical_cast.hpp>
using namespace std;

keyboardthread::keyboardthread(protocol* proto,ConnectionHandler* connection,std::string& com):proto(proto),online(true),connection(connection),command(com) {
}
void keyboardthread::process() {
    std::cout << "starting thread keybord" << std::endl;
    while (proto->isonline())
    {
        std::cout << "starting while keyoard" << std::endl;

        vector<string> commandVec=readingInput(command,' ');
        std::cout << commandVec[0] << std::endl;
        if (commandVec[0]=="ADMINREG"||commandVec[0]=="STUDENTREG"||commandVec[0]=="LOGIN")
        {
            std::cout << "ADMINREg started" << std::endl;
            std::string s = commandVec[1];
            std::string p = commandVec[2];
            char x='\0';
            int n = s.length();
            int m= p.length();
            char userA[n+1];
            char passA[m+1];
            strcpy(userA,s.c_str());
            strcpy(passA,p.c_str());
            userA[n]=x;
            passA[m]=x;
            char fulls[n+m+2];
            for (int i = 0; i < n+1; ++i) {
                fulls[i]=userA[i];
            }
            for (int i = 0; i < m+1; ++i) {
                fulls[i+n+1]=passA[i];
            }
            char opcodeArray[2];
            if(commandVec[0]=="ADMINREG"){
                shortToBytes(1,opcodeArray);
            }
           if(commandVec[0]=="STUDENTREG"){
               shortToBytes(2,opcodeArray);
           }
           else{
               shortToBytes(3,opcodeArray);
           }
           char byteArray[n+m+4];
           byteArray[0]=opcodeArray[0];
           byteArray[1]=opcodeArray[1];
           for(int i=2;i<n+m+4;i++){
               byteArray[i]=fulls[i-2];
           }
           connection->sendBytes(byteArray,n+m+4);
           proto->setAtomic(proto->getAtomic()+1);
            std::cout << proto->getAtomic() << std::endl;
        }
        else if (commandVec[0]=="LOGOUT"||commandVec[0]=="MYCOURSES")
        {
            char opcodeArray[2];
            if(commandVec[0]=="LOGOUT")
                shortToBytes(4,opcodeArray);
            else
                shortToBytes(11,opcodeArray);
            connection->sendBytes(opcodeArray,2);
            proto->setAtomic(proto->getAtomic()+1);
        }
        else if (commandVec[0]=="COURSEREG"||commandVec[0]=="KDAMCHECK"||commandVec[0]=="COURSESTAT"||commandVec[0]=="ISREGISTERED"||commandVec[0]=="UNREGISTER")
        {
            char opcodeArray[2];
            if(commandVec[0]=="COURSEREG")shortToBytes(4,opcodeArray);
            if(commandVec[0]=="KDAMCHECK") shortToBytes(6,opcodeArray);
            if(commandVec[0]=="COURSESTAT") shortToBytes(7,opcodeArray);
            if(commandVec[0]=="ISREGISTERED")shortToBytes(9,opcodeArray);
            if(commandVec[0]=="UNREGISTER")shortToBytes(10,opcodeArray);
            char coursenamberArray[2];
            short myShort = boost::lexical_cast<short>(commandVec[1]);
            shortToBytes(myShort,coursenamberArray);
            char all[4];all[0]=opcodeArray[0];all[1]=opcodeArray[1];all[2]=coursenamberArray[0];all[3]=coursenamberArray[1];
            connection->sendBytes(all,4);
            proto->setAtomic(proto->getAtomic()+1);
        }
        else if(commandVec[0]=="STUDENTSTAT"){
            string studentName=commandVec[1];
            int lenght=studentName.length();
            char sArray[lenght+1];
            strcpy(sArray,studentName.c_str());
            char x='\0';
            sArray[lenght]=x;
            char opcodeArray[2];
            shortToBytes(8,opcodeArray);
            char byteArray[lenght+3];
            byteArray[0]=opcodeArray[0];
            byteArray[1]=opcodeArray[1];
            for(int i=2;i<lenght+3;i++){
                byteArray[i]=sArray[i-2];
            }
            connection->sendBytes(byteArray,lenght+3);
            proto->setAtomic(proto->getAtomic()+1);
        }
        online=proto->isonline();
        const short bufsize = 1024;
        char buf[bufsize];
        std::cin.getline(buf, bufsize);
        command=buf;

    }
}
void keyboardthread::shortToBytes(short num, char *bytesArr) {
    {
        bytesArr[0] = ((num >> 8) & 0xFF);
        bytesArr[1] = (num & 0xFF);
    }
}
vector<string> keyboardthread::readingInput(string& s,char x){
    string w;
    vector<string> input;
    istringstream parsing(s);
    while(getline(parsing,w,x)){
        if(w!=""){
            input.push_back(w);
        }
    }
    return input;
}
