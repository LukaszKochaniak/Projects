#include <iostream>
#include "Grid.h"
#include "Ship.h"

using namespace std;

Grid::Grid()
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

Grid::~Grid()
{

}

void Grid::markPosition(int x, int y, char newChar)
{
    grid[x][y]=newChar;
}

char Grid::checkPosition(int x, int y)
{
    return grid[x][y];
}

char Grid::shootPosition(int x, int y)
{

}
