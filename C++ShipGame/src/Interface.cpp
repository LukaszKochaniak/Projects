#include "Interface.h"
#include <iostream>
#include <typeinfo>
#include <limits>
#include "Player.h"
#include "PrimaryGrid.h"
#include "SecondaryGrid.h"
#include "Game.h"

Interface::Interface()
{

}

void Interface::printGrids(Player* player)
{
    char c;
    cout<<"   ";
    for(int i = 0; i < 10; i++)
    {
        c = 65 + i;
        cout<<c;
        if(i != 9) cout<<" ";
    }
    cout<<"       ";
    for(int i = 0; i < 10; i++)
    {
        c = 65 + i;
        cout<<c;
        if(i != 9) cout<<" ";
    }
    cout<<"     Player score: "<<player->getScore();
    cout<<endl;

    for(int i=0; i<10; i++)
    {
        for(int j=0; j<24; j++)
        {
            c = 49 + i;
            if((j == 0 || j == 13) && i != 9) cout<<" "<<c<<"|";
            else if((j == 0 || j == 13) && i == 9) cout<<"10"<<"|";
            else if (j < 11)
            {
                cout<<player->checkPositionPrim(j-1, i);
                if(j != 10) cout<<" ";
            }
            else if (j == 12) cout<<"    ";
            else if (j > 13)
            {
                cout<<player->checkPositionSec(j - 14, i)<<" ";
                if(j == 23 && i == 2)
                {
                    cout<<"   Rounds left: ";
                    cout<<player->getGame()->getRounds();
                }
            }
        }
        cout<<endl;
    }
}

void Interface::nextl()
{
    cout<<endl;
}

int Interface::inputInt()
{
    int x;

    if(!(cin>>x))
        //cout << "You didn't type a number, please try again" << endl;

    cin.clear();
    cin.ignore(numeric_limits<streamsize>::max(), '\n');

    return x;
}

char Interface::inputChar()
{
    char X;
    while(!(cin>>X))
    {
        cout << "You didn't type a correct position, please try again" << endl;

        cin.clear();
        cin.ignore(numeric_limits<streamsize>::max(), '\n');
    }
    return X;
}



void Interface::clearScr()
{
    system("clear");
}

