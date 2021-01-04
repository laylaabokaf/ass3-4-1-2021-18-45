
#include <connectionHandler.h>
#include <mutex>
#include <sockerthread.h>
#include <thread>
#include <keyboardthread.h>


/**
* This code assumes that the server replies the exact text the client sent it (as opposed to the practical session example)
*/
int main (int argc, char *argv[]) {
    bool conn=false;
    const short bufsize = 1024;
    char buf[bufsize];
    protocol *clientprotocol;
    std::cin.getline(buf, bufsize);
    std::string command(buf);
    std::mutex mute;
    ConnectionHandler *connectionHandler;
        std::string w;
        std::vector<std::string> inputIP;
        std::istringstream parsing(command);
        while (getline(parsing, w, ' ')) {
            if (w != "") {
                inputIP.push_back(w);
            }
        }
        if (inputIP[0] == "BGRSclient") {
            std::cout << "connect to BGRcclient" << std::endl;
            std::string host = inputIP[1];
            short port = std::atoi(inputIP[2].c_str());
            connectionHandler = new ConnectionHandler(host, port);
            if (!connectionHandler->connect()) {
                std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
            } else {//success in connecting to the server
                clientprotocol = new protocol(connectionHandler, &mute);//initiate the reader from the server
                conn = true;
            }
        }
        if(conn) {
            sockerthread reader(clientprotocol, connectionHandler);
            const short bufsizeq = 1024;
            char bufq[bufsizeq];
            std::cin.getline(bufq, bufsizeq);
            command = bufq;
            keyboardthread writer(clientprotocol, connectionHandler, command);
            std::cout << "starting threads" << std::endl;
//initiate the writer keyboardthread to the server..
            std::thread th2(&keyboardthread::process, &writer);
            std::thread th1(&sockerthread::doing, &reader);
            th2.detach();
            th1.join();
        }
    return 0;
}
