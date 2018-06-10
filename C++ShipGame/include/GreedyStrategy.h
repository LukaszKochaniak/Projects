#ifndef _GREEDYSTRATEGY_H_
#define _GREEDYSTRATEGY_H_

#include <iostream>
#include <vector>
#include "GameStrategy.h"

class Ship;

using namespace std;

class GreedyStrategy : public GameStrategy
{
public:
    Ship *chooseShip(vector <Ship*> &ships);
};

#endif // _GREEDYSTRATEGY_H_
