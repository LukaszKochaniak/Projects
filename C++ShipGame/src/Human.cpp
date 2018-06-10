#include <iostream>
#include <vector>
#include "Player.h"
#include "Human.h"
#include "Grid.h"
#include "PrimaryGrid.h"
#include "SecondaryGrid.h"
#include "Ship.h"
#include "Game.h"
#include "OneFunnel.h"
#include "MultiFunnel.h"
#include <cmath>
#include "Interface.h"

using namespace std;

Human::Human(PrimaryGrid primGrid, SecondaryGrid secGrid, vector <Ship*> ships, Game *game) : Player(primGrid, secGrid, ships, game)
{
}

Human::~Human()
{
    for(int i=0; i<ships.size(); i++)
    {
        delete ships[i];
    }
    ships.clear();
}

Ship *Human::chooseShip()
{
    bool canreturn=false;
    if(ships.size()==0)
    {
        game->useInterface()->displayMessage("You have no ships!");
        game->useInterface()->nextl();

    }
    //I

    do
    {
        int sizee;
        game->useInterface()->displayMessage("Which ship you want to use? (1, 2, 3)");
        game->useInterface()->nextl();
        sizee = game->useInterface()->inputInt();

        for(int i=0; i<ships.size(); i++)
        {
            if(ships[i]->getSize()==sizee)
                return ships[i];
        }
    }
    while(!canreturn);
}

void Human::shoot(Ship &ship, int *coordinates)
{
    int row, column;
    char X;
    while(1)
    {
        game->useInterface()->displayMessage("Choose position to shoot.");
        game->useInterface()->nextl();
        bool endi=true;
        while(endi)
        {
            game->useInterface()->displayMessage("X: ");
            X = game->useInterface()->inputChar();
            if(int(X) >= 65 && int(X) <= 74)
            {
                column = int(X) - 65;
                endi =false;
            }
            if(int(X) >= 97 && int(X) <= 106)
            {
                column = int(X) - 97;
                endi =false;
            }
            if(int(X) >= 48 && int(X) <= 57)
            {
                column = int(X) - 49;
                endi =false;
            }
        }

        endi=true;
        while(endi)
        {
            game->useInterface()->displayMessage("Y: ");
            row = game->useInterface()->inputInt() - 1;
            if(row>=0 && row <=10)
                endi=false;
        }

        if(row<0 || row>=10 || column<0 || column>=10)
        {
            game->useInterface()->displayMessage("Wrong coordinates!");
            game->useInterface()->nextl();
        }
        else
        {
            vector<int> possibleX = ship.getShootX();
            vector<int> possibleY = ship.getShootY();
            for(int i=0; i<possibleX.size(); i++)
            {
                if(possibleX[i] == column && possibleY[i] == row)
                {
                    for(int i=0; i<ships.size(); i++)
                    {
                        ships[i]->decreaseTargets(column, row);
                    }
                    coordinates[0]=column;
                    coordinates[1]=row;
                    return;
                }
            }
            game->useInterface()->displayMessage("This ship cannot shoot there!");
            game->useInterface()->nextl();
        }
    }

}

void Human::ArrangeShips()
{
    vector<int> x, y;
    bool ending=true;
    int x1, y1, x2, y2, x3, y3;
    char X;
    game->useInterface()->printGrids(this);
    do
    {
        game->useInterface()->displayMessage("Enter one funnel ship coordinates.");
        game->useInterface()->nextl();

        bool endi=true;
        while(endi)
        {
            game->useInterface()->displayMessage("X: ");
            X = game->useInterface()->inputChar();
            if(int(X) >= 65 && int(X) <= 74)
            {
                x1 = int(X) - 65;
                endi =false;
            }
            if(int(X) >= 97 && int(X) <= 106)
            {
                x1 = int(X) - 97;
                endi =false;
            }
            if(int(X) >= 48 && int(X) <= 57)
            {
                x1 = int(X) - 49;
                endi =false;
            }
        }

        endi=true;
        while(endi)
        {
            game->useInterface()->displayMessage("Y: ");
            y1 = game->useInterface()->inputInt() - 1;
            if(y1>=0 && y1 <=10)
                endi=false;
        }

        if(x1<0 || x1>=10 || y1<0 || y1>=10)
        {
            game->useInterface()->displayMessage("Wrong coordinates!");
            ending=false;
        }
        else ending = true;
    }
    while(!ending);

    x.push_back(x1);
    y.push_back(y1);
    OneFunnel *ship1 = new OneFunnel(1, x, y);
    primGrid.putShipDown(x[0], y[0]);
    ships.push_back(ship1);
    game->useInterface()->clearScr();
    game->useInterface()->printGrids(this);

    bool canPutSecond = false;
    do
    {
        game->useInterface()->displayMessage("Enter two funnel ship coordinates.");
        game->useInterface()->nextl();
        bool endi=true;
        while(endi)
        {
            game->useInterface()->displayMessage("X1: ");
            X = game->useInterface()->inputChar();
            if(int(X) >= 65 && int(X) <= 74)
            {
                x1 = int(X) - 65;
                endi =false;
            }
            if(int(X) >= 97 && int(X) <= 106)
            {
                x1 = int(X) - 97;
                endi =false;
            }
            if(int(X) >= 48 && int(X) <= 57)
            {
                x1 = int(X) - 49;
                endi =false;
            }
        }

        endi=true;
        while(endi)
        {
            game->useInterface()->displayMessage("Y1: ");
            y1 = game->useInterface()->inputInt() - 1;
            if(y1>=0 && y1 <=10)
                endi=false;
        }

        if(primGrid.canPutShipDown(x1-1, y1) || primGrid.canPutShipDown(x1, y1 - 1) || primGrid.canPutShipDown(x1 + 1, y1) || primGrid.canPutShipDown(x1, y1 + 1))
            canPutSecond = true;
        if(!primGrid.canPutShipDown(x1, y1) || !canPutSecond)
        {
            game->useInterface()->displayMessage("Wrong coordinates.");
            game->useInterface()->nextl();
        }
    }
    while(!primGrid.canPutShipDown(x1, y1) || !canPutSecond);

    do
    {
        bool endi=true;
        while(endi)
        {
            game->useInterface()->displayMessage("X2: ");
            X = game->useInterface()->inputChar();
            if(int(X) >= 65 && int(X) <= 74)
            {
                x2 = int(X) - 65;
                endi =false;
            }
            if(int(X) >= 97 && int(X) <= 106)
            {
                x2 = int(X) - 97;
                endi =false;
            }
            if(int(X) >= 48 && int(X) <= 57)
            {
                x2 = int(X) - 49;
                endi =false;
            }
        }

        endi=true;
        while(endi)
        {
            game->useInterface()->displayMessage("Y2: ");
            y2 = game->useInterface()->inputInt() - 1;
            if(y2>=0 && y2<=10)
                endi=false;
        }

        if(!((x1 == x2 && fabs(y2 - y1) == 1) || (y1 == y2 && fabs(x2 - x1) == 1)))
        {
            game->useInterface()->displayMessage("Wrong coordinates.");
            game->useInterface()->nextl();
        }
    }
    while(!primGrid.canPutShipDown(x2, y2) || !((x1 == x2 && fabs(y2 - y1) == 1) || (y1 == y2 && fabs(x2 - x1) == 1)));

    vector<int> s2x;
    vector<int> s2y;
    s2x.push_back(x1);
    s2x.push_back(x2);
    s2y.push_back(y1);
    s2y.push_back(y2);
    primGrid.putShipDown(s2x[0], s2y[0]);
    primGrid.putShipDown(s2x[1], s2y[1]);
    MultiFunnel *ship2 = new MultiFunnel(2, s2x, s2y);
    ships.push_back(ship2);
    game->useInterface()->clearScr();
    game->useInterface()->printGrids(this);

    bool CanPut3 = false;
    do
    {
        game->useInterface()->displayMessage("Enter three funnel ship coordinates.");
        game->useInterface()->nextl();
        bool endi=true;
        while(endi)
        {
            game->useInterface()->displayMessage("X1: ");
            X = game->useInterface()->inputChar();
            if(int(X) >= 65 && int(X) <= 74)
            {
                x1 = int(X) - 65;
                endi =false;
            }
            if(int(X) >= 97 && int(X) <= 106)
            {
                x1 = int(X) - 97;
                endi =false;
            }
            if(int(X) >= 48 && int(X) <= 57)
            {
                x1 = int(X) - 49;
                endi =false;
            }
        }

        endi=true;
        while(endi)
        {
            game->useInterface()->displayMessage("Y1: ");
            y1 = game->useInterface()->inputInt() - 1;
            if(y1>=0 && y1 <=10)
                endi=false;
        }

        if(primGrid.canPutShipDown(x1 - 1, y1) && primGrid.canPutShipDown(x1 + 1, y1)) CanPut3 = true;
        else if(primGrid.canPutShipDown(x1, y1 - 1) && primGrid.canPutShipDown(x1, y1 + 1)) CanPut3 = true;
        else if(primGrid.canPutShipDown(x1 + 1, y1) && primGrid.canPutShipDown(x1 + 2, y1)) CanPut3 = true;
        else if(primGrid.canPutShipDown(x1, y1 + 1) && primGrid.canPutShipDown(x1, y1 + 2)) CanPut3 = true;
        else if(primGrid.canPutShipDown(x1 - 1, y1) && primGrid.canPutShipDown(x1 - 2, y1)) CanPut3 = true;
        else if(primGrid.canPutShipDown(x1, y1 - 1) && primGrid.canPutShipDown(x1, y1 - 2)) CanPut3 = true;
        if(!primGrid.canPutShipDown(x1, y1) || !CanPut3)
        {
            game->useInterface()->displayMessage("Wrong coordinates.");
            game->useInterface()->nextl();
        }
    }
    while(!primGrid.canPutShipDown(x1, y1) || !CanPut3);

    bool CorrectCoordinate = false;
    do
    {
        bool endi=true;
        while(endi)
        {
            game->useInterface()->displayMessage("X2: ");
            X = game->useInterface()->inputChar();
            if(int(X) >= 65 && int(X) <= 74)
            {
                x2 = int(X) - 65;
                endi =false;
            }
            if(int(X) >= 97 && int(X) <= 106)
            {
                x2 = int(X) - 97;
                endi =false;
            }
            if(int(X) >= 48 && int(X) <= 57)
            {
                x2 = int(X) - 49;
                endi =false;
            }
        }

        endi=true;
        while(endi)
        {
            game->useInterface()->displayMessage("Y2: ");
            y2 = game->useInterface()->inputInt() - 1;
            if(y2>=0 && y2 <=10)
                endi=false;
        }

        endi=true;
        while(endi)
        {
            game->useInterface()->displayMessage("X3: ");
            X = game->useInterface()->inputChar();
            if(int(X) >= 65 && int(X) <= 74)
            {
                x3 = int(X) - 65;
                endi =false;
            }
            if(int(X) >= 97 && int(X) <= 106)
            {
                x3 = int(X) - 97;
                endi =false;
            }
            if(int(X) >= 48 && int(X) <= 57)
            {
                x3 = int(X) - 49;
                endi =false;
            }
        }

        endi=true;
        while(endi)
        {
            game->useInterface()->displayMessage("Y3: ");
            y3 = game->useInterface()->inputInt() - 1;
            if(y3>=0 && y3 <=10)
                endi=false;
        }

        if((x2 == x1 - 1 && x3 == x1 + 1 && y2 == y1 && y3 == y1) || (x3 == x1 - 1 && x2 == x1 + 1 && y3 == y1 && y2 == y1)) CorrectCoordinate = true;
        else if((x2 == x1 + 1 && x3 == x1 + 2 && y2 == y1 && y3 == y1) || (x3 == x1 + 1 && x2 == x1 + 2 && y3 == y1 && y2 == y1)) CorrectCoordinate = true;
        else if((x2 == x1 - 1 && x3 == x1 - 2 && y2 == y1 && y3 == y1) || (x3 == x1 - 1 && x2 == x1 - 2 && y3 == y1 && y2 == y1)) CorrectCoordinate = true;
        else if((x2 == x1 && x3 == x1 && y2 == y1 - 1 && y3 == y1 + 1) || (x3 == x1 && x2 == x1 && y3 == y1 - 1 && y2 == y1 + 1)) CorrectCoordinate = true;
        else if((x2 == x1 && x3 == x1 && y2 == y1 - 1 && y3 == y1 - 2) || (x3 == x1 && x2 == x1 && y3 == y1 - 1 && y2 == y1 - 2)) CorrectCoordinate = true;
        else if((x2 == x1 && x3 == x1 && y2 == y1 + 1 && y3 == y1 + 2) || (x3 == x1 && x2 == x1 && y3 == y1 + 1 && y2 == y1 + 2)) CorrectCoordinate = true;
        if(!CorrectCoordinate)
        {
            game->useInterface()->displayMessage("Wrong coordinates.");
            game->useInterface()->nextl();
        }
    }
    while(!CorrectCoordinate);

    vector<int> s3x;
    vector<int> s3y;
    s3x.push_back(x1);
    s3x.push_back(x2);
    s3x.push_back(x3);
    s3y.push_back(y1);
    s3y.push_back(y2);
    s3y.push_back(y3);
    primGrid.putShipDown(s3x[0], s3y[0]);
    primGrid.putShipDown(s3x[1], s3y[1]);
    primGrid.putShipDown(s3x[2], s3y[2]);
    MultiFunnel *ship3 = new MultiFunnel(3, s3x, s3y);
    ships.push_back(ship3);
    game->useInterface()->clearScr();
    game->useInterface()->printGrids(this);
}




