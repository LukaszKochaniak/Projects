#include <iostream>
#include "Grid.h"
#include "PrimaryGrid.h"
#include "OneFunnel.h"
#include "MultiFunnel.h"
#include "Ship.h"

using namespace std;

PrimaryGrid::PrimaryGrid() //: Grid(width, height, grid)
{
    this->width=10;
    this->height=10;
    for(int i=0; i<10; i++)
    {
        for(int j=0; j<10; j++)
        {
            grid[j][i]='.';
        }
    }
}

PrimaryGrid::~PrimaryGrid()
{
    //destructor
}


void PrimaryGrid::putShipDown(int column, int row)
{
    markPosition(column, row, 'S');
}


bool PrimaryGrid::canPutShipDown(int column, int row)
{
    const int r=1;

    if(row<0 || row>=10 || column<0 || column>=10)
    {
        return false;
    }

    for(int z=row-r; z<=row+r; z++)
    {
        if(z>=10 || z < 0) continue;
        for(int x=column-r; x<=column+r; x++)
        {
            if(x>=10 || x < 0) continue;
            if (grid[x][z]=='S') return false;
        }
    }
    return true;
}
