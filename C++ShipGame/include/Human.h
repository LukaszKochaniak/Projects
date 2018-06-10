#ifndef _HUMAN_H_
#define _HUMAN_H_

#include <iostream>
#include "Player.h"
#include "Ship.h"

using namespace std;

class Human : public Player
{
public:
    Human(PrimaryGrid primGrid, SecondaryGrid secGrid,vector <Ship*> ships, Game *game);
    ~Human();
    Ship *chooseShip();
    void shoot(Ship &ship, int *coordinates);
    void ArrangeShips();
};

#endif // _HUMAN_H_
