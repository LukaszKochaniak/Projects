#include <iostream>
#include <vector>
#include "Ship.h"
#include "OneFunnel.h"

using namespace std;

OneFunnel::OneFunnel(int s, vector<int>  column, vector<int>  row) : Ship(s, column, row)
{

}

OneFunnel::~OneFunnel()
{

}


bool OneFunnel::canDoubleShoot()
{
    return false;
}

char OneFunnel::getHit()
{
    sunk=true;
    return 'D';
}
