#ifndef _COMPUTER_H_
#define _COMPUTER_H_
#include <iostream>
#include <vector>
#include "Player.h"
#include "GameStrategy.h"
#include "PrimaryGrid.h"
#include "SecondaryGrid.h"
#include "Ship.h"

using namespace std;

class Computer : public Player
{
private:
    GameStrategy *strategy;

public:
    Computer(PrimaryGrid primGrid, SecondaryGrid secGrid, vector<Ship*> ships, GameStrategy *strategy, Game *game);
    ~Computer();
    Ship *chooseShip();
    void shoot(Ship &ship, int *coordinates);
    void ArrangeShips();
};

#endif // _COMPUTER_H_
