#ifndef _GAMESTRATEGY_H_
#define _GAMESTRATEGY_H_

#include <iostream>
#include <vector>
#include "Ship.h"

using namespace std;

class GameStrategy
{
public:
    virtual Ship *chooseShip(vector <Ship*> &ships);
};

#endif // _GAMESTRATEGY_H_
