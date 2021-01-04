//
// Created by spl211 on 22/12/2020.
//
#pragma once
#include "protocol.h"

#ifndef BOOST_ECHO_CLIENT_SOCKETTHREAD_H
#define BOOST_ECHO_CLIENT_SOCKETTHREAD_H

#endif //BOOST_ECHO_CLIENT_SOCKETTHREAD_H
class sockerthread{
private:
    protocol* Protocol;
    ConnectionHandler* Connect;
public:
    sockerthread(protocol* protocol,ConnectionHandler* connect);
    void doing();

};