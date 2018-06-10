#include <iostream>
#include <vector>
#include "Ship.h"
#include "MultiFunnel.h"

using namespace std;

MultiFunnel::MultiFunnel(int s, vector<int> column, vector<int> row) : Ship(s, column, row)
{
    this->cooldown=false;
    this->health=s;
}


MultiFunnel::~MultiFunnel()
{
    //destructor
}

bool MultiFunnel::checkCooldown()
{
    if(cooldown==true)
        return true;
    else
        return false;
}

void MultiFunnel::changeCooldown(int s)
{
    if(cooldown==true)
        cooldown=false;

    if(getSize()==s)
        cooldown=true;
}

int MultiFunnel::checkHealth()
{
    return health;
}

bool MultiFunnel::canDoubleShoot()
{
    if(checkCooldown())
        return false;
    else
        return true;
}

char MultiFunnel::getHit()
{
    health--;
    if(!health)
    {
        sunk=true;
        return 'D';
    }
    return 'H';
}
