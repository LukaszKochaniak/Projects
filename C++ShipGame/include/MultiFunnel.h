#ifndef _MULTIFUNNEL_H_
#define _MULTIFUNNEL_H_
#include <vector>
#include <iostream>
#include "Ship.h"
using namespace std;

class MultiFunnel : public Ship
{
private:
    bool cooldown;
    int health;

public:
    MultiFunnel(int s, vector<int> column, vector<int> row);
    ~MultiFunnel();
    bool canDoubleShoot();
    bool checkCooldown();
    void changeCooldown(int s=0);
    int checkHealth();
    char getHit();
};



#endif // _MULTIFUNNEL_H_
