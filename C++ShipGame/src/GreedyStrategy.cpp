#include <iostream>
#include <vector>
#include "Ship.h"
#include "Player.h"
#include "Computer.h"
#include "GreedyStrategy.h"

using namespace std;

Ship *GreedyStrategy::chooseShip(vector <Ship*> &ships)
{
    int index;
    int maximum=0;
    for(int i=0; i<ships.size(); i++)
    {
        if(ships[i]->getSize()>maximum)
        {
            if(ships[i]->canDoubleShoot())
            {
                maximum=ships[i]->getSize();
                index=i;
            }
        }
    }
    return ships[index];
}
