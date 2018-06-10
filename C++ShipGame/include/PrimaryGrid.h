#include "Grid.h"
#ifndef _PRIMARYGRID_H_
#define _PRIMARYGRID_H_

#include <iostream>
#include "Grid.h"
#include "OneFunnel.h"

class Ship;
class MultiFunnel;
using namespace std;

class PrimaryGrid : public Grid
{
public:
    PrimaryGrid();
    ~PrimaryGrid();
    void putShipDown(int row, int column);
    bool canPutShipDown(int row, int column);
};



#endif // _PRIMARYGRID_H_
