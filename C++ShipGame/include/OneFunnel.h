#ifndef _ONEFUNNEL_H_
#define _ONEFUNNEL_H_

#include <iostream>
#include "Ship.h"
using namespace std;

class OneFunnel : public Ship
{
public:
    OneFunnel(int s, vector<int> column, vector<int> row);
    ~OneFunnel();
    bool canDoubleShoot();
    char getHit();
};



#endif // _ONEFUNNEL_H_
