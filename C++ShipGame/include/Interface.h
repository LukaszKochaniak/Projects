#ifndef _INTERFACE_H_
#define _INTERFACE_H_
#include <string>
#include <iostream>

using  namespace std;

class Player;

class Interface
{
public:
    Interface();
    void printGrids(Player* player);
    void nextl();
    int inputInt();
    char inputChar();
    void clearScr();
    template<class T> void displayMessage(T message)
    {
        cout<<message;
    }
};

#endif
