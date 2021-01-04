CFLAGS:=-c -Wall -Weffc++ -g -std=c++11 -Iinclude
LDFLAGS:=-lboost_system -lpthread

all: Boost_Echo_Client
	g++ -o bin/Boost_Echo_Client bin/connectionHandler.o bin/echoClient.o bin/protocol.o bin/keyboardthread.o bin/sockerthread.o $(LDFLAGS)

Boost_Echo_Client: bin/connectionHandler.o bin/echoClient.o bin/protocol.o bin/keyboardthread.o bin/sockerthread.o

bin/connectionHandler.o: src/connectionHandler.cpp
	g++ $(CFLAGS) -o bin/connectionHandler.o src/connectionHandler.cpp

bin/echoClient.o: src/echoClient.cpp
	g++ $(CFLAGS) -o bin/echoClient.o src/echoClient.cpp

bin/protocol.o: src/protocol.cpp
	g++ $(CFLAGS) -o bin/protocol.o src/protocol.cpp

bin/keyboardthread.o: src/keyboardthread.cpp
	g++ $(CFLAGS) -o bin/keyboardthread.o src/keyboardthread.cpp

bin/sockerthread.o: src/sockerthread.cpp
	g++ $(CFLAGS) -o bin/sockerthread.o src/sockerthread.cpp

.PHONY: clean
clean:
	rm -f bin/*
