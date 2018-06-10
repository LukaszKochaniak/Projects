#ifndef _GRID_H_
#define _GRID_H_

#include <iostream>

using namespace std;

class Grid
{
protected:
    int width;
    int height;
    char grid[10][10];

public:
    Grid();
    ~Grid();
    int getWidth()
    {
        return width;
    }
    int getHeight()
    {
        return height;
    }
    void markPosition(int x, int y, char newChar);
    char checkPosition(int x, int y);
    char shootPosition(int x, int y);
};



#endif // _GRID_H_
