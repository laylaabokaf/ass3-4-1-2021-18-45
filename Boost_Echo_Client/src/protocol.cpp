
//
// Created by spl211 on 23/12/2020.
//
#include "protocol.h"
#include <vector>
using namespace std;

protocol::protocol(ConnectionHandler* connect,std::mutex *mutex):connectionHandler(connect),mute(mutex) {
online=true;
getAndSend=0;
}
bool protocol::isonline() {return online;} //done

std::vector<string> protocol::readingInput(string& s,char x){
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

short protocol::bytesToShort(char *bytesArr) {
    short result = (short)((bytesArr[0] & 0xff) << 8);
    result += (short)(bytesArr[1] & 0xff);
    return result;
}

void protocol::doing(char *bays) {
    std::cout <<"START doind" << std::endl;
    char bytesArr[2];
    bytesArr[0] = bays[0];
    bytesArr[1] = bays[1];
    short opcode = bytesToShort(bytesArr);
std::cout << opcode<< std::endl;
if (opcode == 13) {
std::cout << "true" << std::endl;
}
    if (opcode == 13) //ERROR
    {
std::cout << "Doing: erorr " << std::endl;
        char byt[2];
        byt[0] = bays[2];
        byt[1] = bays[3];
        short mopcode = bytesToShort(byt);
        std::cout << "ERROR " << mopcode;
std::cout << "ERROR " << mopcode << std::endl;
std::cout << "Erorr" << std::endl;
    } else  //ACK
    {
        char byt[2];
        byt[0] = bays[2];
        byt[1] = bays[3];
        short mopcode = bytesToShort(byt);
std::cout << mopcode<< std::endl;
        if (mopcode == 2 || mopcode == 1 || mopcode == 3 || mopcode == 5 ||
            mopcode == 10) //studentreg or adminreg or login or coursereg or unregister
            std::cout << "ACK " + mopcode << endl;
        if (mopcode == 9) //isregster
        {
            std::cout << "ACK " + mopcode << endl; //first line
            int j = 4;
            int lengtofunswer = 0;
            while (byt[j] != '/0') {
                lengtofunswer++;
                j++;
            }

            string ansforreg = convertToString(byt, lengtofunswer, 4);
            std::cout << ansforreg << endl; //the answer for isreg
        }
        if (mopcode == 6) {
            std::cout << "ACK " + mopcode << endl; //print first line
            std::cout << "{";
            int k = 4;
            while (byt[k] != '/0') {
                char bytesArr[2];
                bytesArr[0] = bays[k];
                bytesArr[1] = bays[k + 1];
                short numofcur = bytesToShort(bytesArr);
                std::cout << "," + numofcur;
                k = +2;
            }
            std::cout << "}";
        }
        if (mopcode == 7) //coursestate
        {
            std::cout << "ACK " + mopcode << endl; //print first line
//print for coures
            char bytesArr[2];
            bytesArr[0] = bays[4];
            bytesArr[1] = bays[5];
            short coursenum = bytesToShort(bytesArr);
            int l = 6;
            int lengtofcousrname = 0;
            while (byt[l] != '/0') {
                lengtofcousrname++;
                l++;
            }
            string coursename = convertToString(byt, lengtofcousrname, 6);
            std::cout << "Course:" << "(" << coursenum << ")" << coursename << endl;
//print for seats available
            int a = l + 1;
            int lengtofsetavalible = 0;
            while (byt[a] != '/n') {
                lengtofsetavalible++;
                a++;
            }
            string setavalible = convertToString(byt, lengtofsetavalible, l + 1);
            std::cout << "seats available:" << setavalible << "/" << endl;
            int g = a + 1;
            int lengtofallset = 0;
            while (byt[g] != '/n') {
                lengtofallset++;
                g++;
            }
            string allset = convertToString(byt, lengtofallset, a + 1);
            std::cout << allset << endl;

            std::cout << "Registerd:";

            std::cout << "[";
            int q = g;
            int lenght = 0;
            while (byt[q] != '/0') {
                q++;
                while (byt[q] != '/n' && byt[q] != '/0') {
                    lenght++;
                    q++;
                }
                string studnam = convertToString(byt, lenght, g + 1);
                std::cout << studnam << ",";

            }

        }
        std::cout << "]";

//print for studenr registered

        if (mopcode == 8) //coursestate
        {
            std::cout << "ACK " + mopcode << endl; //print first line

            //print the student name
            int l = 4;
            int lengtofstudentname = 0;
            while (byt[l] != '/n') {
                lengtofstudentname++;
                l++;
            }
            string studentname = convertToString(byt, lengtofstudentname, 4);
            std::cout << "student:" << lengtofstudentname << endl;

//print the coursers
            std::cout << "Course:";
            std::cout << "[";

            int k = lengtofstudentname + 4;
            while (byt[k] != '/0') {
                char bytesArr[2];
                bytesArr[0] = bays[k];
                bytesArr[1] = bays[k + 1];
                short numofcur = bytesToShort(bytesArr);
                std::cout << numofcur << ",";
                k = +2;
            }
            std::cout << "]";
        }
        if (mopcode == 11) //MYcourses
        {
            std::cout << "ACK " + mopcode << endl; //print first line
            int g = 4;
            std::cout << "[";

            while (byt[g] != '/0') {
                char bytesArr[2];
                bytesArr[0] = bays[g];
                bytesArr[1] = bays[g + 1];
                short numofcur = bytesToShort(bytesArr);
                std::cout << numofcur << ",";
                // k = +2;
            }
            std::cout << "]";


        }
        if (mopcode == 4) //LOGOUT
        {
            std::cout << "ACK " + mopcode << endl;
            online = false;

        }


    }
}


string protocol::convertToString(char *a, int size,int from) {
    int i;
    string s = "";
    for (i = from; i < size; i++) {
        s = s + a[i];
    }
    return s; }

int protocol::getAtomic() {
    return getAndSend;
}

void protocol::setAtomic(int atomic) {
    mute->lock();
    getAndSend=atomic;
    mute->unlock();
}



