#include <iostream>
#include "Grid.h"
#include "SecondaryGrid.h"

using namespace std;

SecondaryGrid::SecondaryGrid() //: Grid(width, height, grid)
{
    //wszystko jest dziedziczone

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

SecondaryGrid::~SecondaryGrid()
{
    //destructor
}


bool SecondaryGrid::canBecomeTarget(int column, int row)
{
    if(grid[column][row]=='X' || grid[column][row]=='D' || grid[column][row]=='H')
        return false;
    else
        return true;
}
