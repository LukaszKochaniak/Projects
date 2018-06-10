#include <iostream>
#include <vector>
#include <stdlib.h>
#include "Computer.h"
#include "Player.h"
#include "Grid.h"
#include "PrimaryGrid.h"
#include "SecondaryGrid.h"
#include "OneFunnel.h"
#include "MultiFunnel.h"
#include "GameStrategy.h"
#include <vector>

using namespace std;

Computer::Computer(PrimaryGrid primGrid, SecondaryGrid secGrid, vector <Ship*> ships, GameStrategy *strat, Game *game) : Player(primGrid, secGrid, ships, game)
{
    strategy=strat;
}

Computer::~Computer()
{
    for(int i=0; i<ships.size(); i++)
    {
        delete ships[i];
    }
    ships.clear();
}

Ship *Computer::chooseShip()
{
    return strategy->chooseShip(ships);
}

void Computer::shoot(Ship &ship, int *coordinates)
{
    int x, y, randPosition;
    vector<int> possibleX = ship.getShootX();
    vector<int> possibleY = ship.getShootY();
    randPosition = rand()%possibleX.size();
    x = possibleX[randPosition];
    y = possibleY[randPosition];
    for(int i=0; i<ships.size(); i++)
    {
        ships[i]->decreaseTargets(x, y);
    }
    coordinates[0]=x;
    coordinates[1]=y;
}

void Computer::ArrangeShips()
{
    vector<int> x, y;
    bool ending=false;
    x.push_back(rand()%10);
    y.push_back(rand()%10);
    OneFunnel *ship1 = new OneFunnel(1, x, y);
    primGrid.putShipDown(x[0], y[0]);
    ships.push_back(ship1);


    vector<int> possiblePositions;
    int x1, y1, x2, y2, x3, y3;
    int LeftPos = 1, RightPos = 2, UpPos = 3, DownPos = 4;
    bool canPutSecond = false;
    do
    {
        x1 = rand()%10;
        y1 = rand()%10;
        if(primGrid.canPutShipDown(x1 - 1, y1)) possiblePositions.push_back(LeftPos);
        if(primGrid.canPutShipDown(x1, y1 - 1)) possiblePositions.push_back(UpPos);
        if(primGrid.canPutShipDown(x1 + 1, y1)) possiblePositions.push_back(RightPos);
        if(primGrid.canPutShipDown(x1, y1 + 1)) possiblePositions.push_back(DownPos);
    }
    while(!primGrid.canPutShipDown(x1, y1) || possiblePositions.size() == 0);
    int choosePos = rand()%possiblePositions.size();
    if(possiblePositions[choosePos] == LeftPos) x2 = x1 - 1, y2 = y1;
    if(possiblePositions[choosePos] == UpPos) x2 = x1, y2 = y1 - 1;
    if(possiblePositions[choosePos] == RightPos) x2 = x1 + 1, y2 = y1;
    if(possiblePositions[choosePos] == DownPos) x2 = x1, y2 = y1 + 1;

    vector<int> s2x;
    vector<int> s2y;
    s2x.push_back(x1);
    s2x.push_back(x2);
    s2y.push_back(y1);
    s2y.push_back(y2);
    primGrid.putShipDown(s2x[0], s2y[0]);
    primGrid.putShipDown(s2x[1], s2y[1]);
    MultiFunnel *ship2 = new MultiFunnel(2, s2x, s2y);
    ships.push_back(ship2);

    possiblePositions.clear();
    int LeftRight = 1, UpDown = 2, Left2 = 3, Right2 = 4, Up2 = 5, Down2 = 6;
    do
    {
        x1 = rand()%10;
        y1 = rand()%10;
        if(primGrid.canPutShipDown(x1 - 1, y1) && primGrid.canPutShipDown(x1 + 1, y1)) possiblePositions.push_back(LeftRight);
        if(primGrid.canPutShipDown(x1, y1 - 1) && primGrid.canPutShipDown(x1, y1 + 1)) possiblePositions.push_back(UpDown);
        if(primGrid.canPutShipDown(x1 + 1, y1) && primGrid.canPutShipDown(x1 + 2, y1)) possiblePositions.push_back(Right2);
        if(primGrid.canPutShipDown(x1, y1 + 1) && primGrid.canPutShipDown(x1, y1 + 2)) possiblePositions.push_back(Down2);
        if(primGrid.canPutShipDown(x1 - 1, y1) && primGrid.canPutShipDown(x1 - 2, y1)) possiblePositions.push_back(Left2);
        if(primGrid.canPutShipDown(x1, y1 - 1) && primGrid.canPutShipDown(x1, y1 - 2)) possiblePositions.push_back(Up2);
    }
    while(!primGrid.canPutShipDown(x1, y1) || possiblePositions.size() == 0);
    choosePos = rand()%possiblePositions.size();
    if(possiblePositions[choosePos] == LeftRight) x2 = x1 - 1, x3 = x1 + 1, y2 = y1, y3 = y1;
    if(possiblePositions[choosePos] == UpDown) x2 = x1, x3 = x1, y2 = y1 - 1, y3 = y1 + 1;
    if(possiblePositions[choosePos] == Right2) x2 = x1 + 1, x3 = x1 + 2, y2 = y1, y3 = y1;
    if(possiblePositions[choosePos] == Down2) x2 = x1, x3 = x1, y2 = y1 + 1, y3 = y1 + 2;
    if(possiblePositions[choosePos] == Left2) x2 = x1 - 1, x3 = x1 - 2, y2 = y1, y3 = y1;
    if(possiblePositions[choosePos] == Up2) x2 = x1, x3 = x1, y2 = y1 - 1, y3 = y1 - 2;

    vector<int> s3x;
    vector<int> s3y;
    s3x.push_back(x1);
    s3x.push_back(x2);
    s3x.push_back(x3);
    s3y.push_back(y1);
    s3y.push_back(y2);
    s3y.push_back(y3);
    primGrid.putShipDown(s3x[0], s3y[0]);
    primGrid.putShipDown(s3x[1], s3y[1]);
    primGrid.putShipDown(s3x[2], s3y[2]);
    MultiFunnel *ship3 = new MultiFunnel(3, s3x, s3y);
    ships.push_back(ship3);
}





