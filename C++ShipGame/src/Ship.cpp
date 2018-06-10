#include <iostream>
#include <stdlib.h>
#include "Ship.h"
#include "SecondaryGrid.h"

using namespace std;

Ship::Ship(int s, vector<int>  column, vector<int>  row)
{
    this->s=s;
    this->row=row;
    this->column=column;
    this->sunk=false;
    setTargets(this->shootX, this->shootY);
}

Ship::~Ship()
{

}


bool Ship::setTargets(vector<int> &shootX, vector<int> &shootY)
{
    bool grid[10][10];

    for(int i=0; i<10; i++)
        for(int j=0; j<10; j++)
        {
            grid[j][i]=false;
        }

    if(s==1)
    {
        for(int i=row[0]-s-1; i<=row[0]+s+1; i++)
            for(int j=column[0]-s-1; j<=column[0]+s+1; j++)
            {
                if(i<0 || i>=10 || j<0 || j>=10)
                    continue;

                shootX.push_back(j);
                shootY.push_back(i);
            }
    }

    if(s==2)
    {
        for(int m=0; m<2; m++)
        {
            for(int i=row[m]-s-1; i<=row[m]+s+1; i++)
                for(int j=column[m]-s-1; j<=column[m]+s+1; j++)
                {
                    if(i<0 || i>=10 || j<0 || j>=10)
                        continue;
                    if(grid[j][i]==false)
                    {
                        shootX.push_back(j);
                        shootY.push_back(i);
                        grid[j][i]=true;
                    }

                }
        }
    }

    if(s==3)
    {
        for(int m=0; m<3; m++)
        {
            for(int i=row[m]-s-1; i<=row[m]+s+1; i++)
                for(int j=column[m]-s-1; j<=column[m]+s+1; j++)
                {
                    if(i<0 || i>=10 || j<0 || j>=10)
                        continue;
                    if(grid[j][i]==false)
                    {
                        shootX.push_back(j);
                        shootY.push_back(i);
                        grid[j][i]=true;
                    }
                }
        }
    }
}

void Ship::decreaseTargets(int column, int row)
{
    for(int i=0; i<shootX.size(); i++)
    {
        if(shootX[i]==column && shootY[i]==row)
        {
            this->shootX.erase(shootX.begin()+i);
            this->shootY.erase(shootY.begin()+i);
        }
    }
}

bool Ship::hasTargets()
{
    if(shootX.size()==0)
        return false;

    return true;
}

int Ship::getSize()
{
    return s;
}

int Ship::getRow(int index)
{
    return row[index];
}

int Ship::getColumn(int index)
{
    return column[index];
}

bool Ship::isDestroyed()
{
    if(sunk==true)
        return true;
    else
        return false;
}

bool Ship::isMultifunnel()
{
    if(s>1)
        return true;
    else
        return false;
}

bool Ship::shoot(int column, int row, SecondaryGrid &secGrid)
{
    decreaseTargets(column, row);
    return true;
}

bool Ship::canShoot(int column, int row, SecondaryGrid &secGrid)
{
    if(!isDestroyed())
    {
        if(secGrid.canBecomeTarget(column, row))
            return true;
    }

    return false;
}

vector<int> &Ship::getShootX()
{
    return shootX;
}

vector<int> &Ship::getShootY()
{
    return shootY;
}


bool Ship::canDoubleShoot()
{
    //virtual
}

char Ship::getHit()
{

}

void Ship::changeCooldown(int s)
{
    //virtual
}

