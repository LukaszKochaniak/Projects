#ifndef _SHIP_H_
#define _SHIP_H_

#include <iostream>
#include <vector>
using namespace std;

class SecondaryGrid;

class Ship
{
protected:
    int s;
    vector<int> row;
    vector<int> column;
    bool sunk;
    vector<int> shootX;
    vector<int> shootY;

public:
    Ship(int s, vector<int> column, vector<int> row);
    ~Ship();
    bool setTargets(vector<int> &shootX, vector<int> &shootY);
    void decreaseTargets(int column, int row);
    bool hasTargets();
    int getRow(int index);
    int getColumn(int index);
    int getSize();
    bool isDestroyed();
    bool isMultifunnel();
    bool canShoot(int column, int row, SecondaryGrid &secGrid);
    bool shoot(int column, int row, SecondaryGrid &secGrid);
    virtual char getHit();
    virtual bool canDoubleShoot();
    virtual void changeCooldown(int s=0);
    vector<int> &getShootX();
    vector<int> &getShootY();
};



#endif // _SHIP_H_
