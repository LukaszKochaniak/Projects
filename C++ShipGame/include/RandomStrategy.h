#ifndef _RANDOMSTRATEGY_H_
#define _RANDOMSTRATEGY_H_

#include <iostream>
#include <vector>
#include "GameStrategy.h"
class Ship;

using namespace std;

class RandomStrategy : public GameStrategy
{
public:
    Ship *chooseShip(vector <Ship*> &ships);
};

#endif // _RANDOMSTRATEGY_H_
