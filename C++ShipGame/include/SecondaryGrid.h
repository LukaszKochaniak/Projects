#ifndef _SECONDARYGRID_H_
#define _SECONDARYGRID_H_

#include <iostream>
#include "Grid.h"

using namespace std;

class SecondaryGrid : public Grid
{
public:
    SecondaryGrid();
    ~SecondaryGrid();
    bool canBecomeTarget(int row, int column);
};



#endif // _SECONDARYGRID_H_
