#ifndef _PLAYER_H_
#define _PLAYER_H_

#include <iostream>
#include "Grid.h"
#include "Ship.h"
#include "PrimaryGrid.h"
#include "SecondaryGrid.h"
#include <vector>

class Game;

using namespace std;

class Player
{
protected:
    int score;
    PrimaryGrid primGrid;
    SecondaryGrid secGrid;
    int shipsDestroyed;
    vector <Ship*> ships;
    Game *game;

public:
    Player();
    Player(PrimaryGrid primGrid, SecondaryGrid secGrid, vector<Ship*> ships, Game *game);
    ~Player();
    bool hasTargets();
    char checkPositionPrim(int x, int y);
    char checkPositionSec(int x, int y);
    void markPositionPrim(int x, int y, char newChar);
    void markPositionSec(int x, int y, char newChar);
    virtual void ArrangeShips() = 0;
    virtual Ship *chooseShip() = 0;
    virtual void shoot(Ship &ship, int *coordinates) = 0;
    bool canDoubleShoot(Ship ship);
    int getScore();
    bool areAllShipsDestroyed();
    char getShot(int column, int row);
    void decreasescore();
    Game *getGame();
    void changeCooldown(int s=0);
    Ship *getShip(int i);
};



#endif // _PLAYER_H_
