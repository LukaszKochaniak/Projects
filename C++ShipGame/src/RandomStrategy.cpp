#include <iostream>
#include <cstdlib>
#include <vector>
#include "Ship.h"
#include "Player.h"
#include "Computer.h"
#include "RandomStrategy.h"

using namespace std;

Ship *RandomStrategy::chooseShip(vector <Ship*> &ships)
{
    int index=rand()%ships.size();
    return ships[index];
}
