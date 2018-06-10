#include <iostream>
#include <string>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include "Game.h"
#include "Player.h"
#include "Computer.h"
#include "Ship.h"
#include "Human.h"
#include "GameStrategy.h"
#include "RandomStrategy.h"
#include "GreedyStrategy.h"
#include "Interface.h"

using namespace std;

Game::Game(int argc, char *arg1, char *arg2, char *arg3)
{
    this->rounds = CheckRounds(arg1);

    PrimaryGrid primGrid1;
    PrimaryGrid primGrid2;

    SecondaryGrid secGrid1;
    SecondaryGrid secGrid2;

    vector <Ship*> ships1;
    vector <Ship*> ships2;

    GameStrategy *strategy = new GameStrategy;
    strategy=checkStrategy(arg2);

    Computer *computer1 = new Computer(primGrid1, secGrid1, ships1, strategy, this);
    this->players.push_back(computer1);
    if(argc==4)
    {
        this->mode = checkMode(arg3);
        if(this->mode)
        {
            strategy=checkStrategy(arg3);
            Computer *computer2 = new Computer(primGrid2, secGrid2, ships2, strategy, this);
            this->players.push_back(computer2);
        }
        else
        {
            Human *human2 = new Human(primGrid2, secGrid2, ships2, this);
            this->players.push_back(human2);
        }
    }
    else
    {
        Human *human2 = new Human(primGrid2, secGrid2, ships2, this);
        this->players.push_back(human2);
        this->mode = false;
    }
}


GameStrategy* Game::checkStrategy(char* arg)
{
    GameStrategy *strategy;
    RandomStrategy *rstrat = new RandomStrategy;
    GreedyStrategy *gstrat = new GreedyStrategy;

    string str = arg;
    if(str=="random")
    {
        strategy=rstrat;
    }
    if(str=="greedy")
    {
        strategy=gstrat;
    }

    return strategy;
}

bool Game::checkMode(char* arg)
{
    string str = arg;
    if(str == "random" || str == "greedy")
        return true;
    else
        return false;
}

Game::~Game()
{
    for(int i=0; i<players.size(); i++)
    {
        delete players[i];
    }
    players.clear();
}

int Game::CheckRounds(char *rounds)
{
    int x = atoi(rounds);

    if(x<=20)
        return x;
    else
    {
        interface->displayMessage("You entered incorrect number of rounds!");
        exit(0);
    }
}

void Game::RoundCounter()
{
    rounds--;
}

void Game::CorrectArgNumber(int argc)
{
    if (argc == 3 || argc == 4)
    {
        return;
    }
    else
    {
        interface->displayMessage("Incorrect number of arguments!");
        exit(1);
    }
}

bool Game::IfGameEnds()
{
    for(int i=0; i<players.size(); i++)
    {
        if(players[i]->areAllShipsDestroyed())
        {
            cout<<"1"<<endl;
            return true;
        }
    }
    if(rounds==0)
        return true;
    for(int i=0; i<players.size(); i++)
    {
        if(players[i]->hasTargets()==false)
        {
            cout<<"2"<<endl;
            return true;
        }
    }
    return false;
}

void Game::play(int argc)
{
    CorrectArgNumber(argc);
    interface->clearScr();
    this->players[0]->ArrangeShips();
    this->players[1]->ArrangeShips();
    char markHit;
    do
    {
        interface->clearScr();
        if(this->mode)
        {
            interface->printGrids(players[0]);
            interface->displayMessage("--------------------------------------------------");
            interface->nextl();
        }
        interface->printGrids(players[1]);
        usleep(500000);

        int coordinates[2];
        Ship *temp=players[0]->chooseShip();

        if(temp->isMultifunnel())
        {
            if(!temp->canDoubleShoot())
            {
                while(temp->isMultifunnel() && !temp->canDoubleShoot())
                    temp=players[0]->chooseShip();
            }
        }

        players[0]->shoot(*temp, coordinates);
        markHit = players[1]->getShot(coordinates[0], coordinates[1]);
        if(markHit == 'D' || markHit == 'H')
            players[1]->decreasescore();
        if(markHit == 'D')
        {
            Ship *temp1;
            for(int i=0; i<3; i++)
            {
                temp1 = players[1]->getShip(i);
                for(int j=0; j<temp1->getSize(); j++)
                {
                    if(temp1->getColumn(j) == coordinates[0] && temp1->getRow(j) == coordinates[1])
                        for(int j=0; j<temp1->getSize(); j++)
                        {
                            players[0]->markPositionSec(temp1->getColumn(j) , temp1->getRow(j), markHit);
                            players[1]->markPositionPrim(temp1->getColumn(j) , temp1->getRow(j), markHit);
                        }
                }
            }
        }
        else
        {
            players[0]->markPositionSec(coordinates[0], coordinates[1], markHit);
            players[1]->markPositionPrim(coordinates[0], coordinates[1], markHit);
        }

        interface->clearScr();
        if(this->mode)
        {
            interface->printGrids(players[0]);
            interface->displayMessage("--------------------------------------------------");
            interface->nextl();
        }
        interface->printGrids(players[1]);
        usleep(500000);

        if(temp->canDoubleShoot())
        {
            players[0]->shoot(*temp, coordinates);
            markHit = players[1]->getShot(coordinates[0], coordinates[1]);
            if(markHit == 'D' || markHit == 'H')
                players[1]->decreasescore();
            if(markHit == 'D')
            {
                Ship *temp1;
                for(int i=0; i<3; i++)
                {
                    temp1 = players[1]->getShip(i);
                    for(int j=0; j<temp1->getSize(); j++)
                    {
                        if(temp1->getColumn(j) == coordinates[0] && temp1->getRow(j) == coordinates[1])
                            for(int j=0; j<temp1->getSize(); j++)
                            {
                                players[0]->markPositionSec(temp1->getColumn(j) , temp1->getRow(j), markHit);
                                players[1]->markPositionPrim(temp1->getColumn(j) , temp1->getRow(j), markHit);
                            }
                    }
                }
            }
            else
            {
                players[0]->markPositionSec(coordinates[0], coordinates[1], markHit);
                players[1]->markPositionPrim(coordinates[0], coordinates[1], markHit);
            }
            interface->clearScr();
            if(this->mode)
            {
                interface->printGrids(players[0]);
                interface->displayMessage("--------------------------------------------------");
                interface->nextl();
            }
            interface->printGrids(players[1]);
            usleep(500000);

            players[0]->changeCooldown(temp->getSize());
        }
        else
        {
            players[0]->changeCooldown();
        }

        //second player

        Human *human = dynamic_cast<Human *>(players[1]);
        temp=players[1]->chooseShip();
        if(human!=NULL)
        {
            if(temp->isMultifunnel())
            {
                if(!temp->canDoubleShoot())
                {
                    while(temp->isMultifunnel() && !temp->canDoubleShoot())
                    {
                        interface->displayMessage("This ship cannot shoot! Choose another ship.");
                        interface->nextl();
                        temp=players[1]->chooseShip();
                    }
                }
            }

        }
        else
        {
            if(temp->isMultifunnel())
            {
                if(!temp->canDoubleShoot())
                {
                    while(temp->isMultifunnel() && !temp->canDoubleShoot())
                        temp=players[0]->chooseShip();
                }
            }
        }

        players[1]->shoot(*temp, coordinates);
        markHit = players[0]->getShot(coordinates[0], coordinates[1]);
        if(markHit == 'D' || markHit == 'H')
            players[0]->decreasescore();
        if(markHit == 'D')
        {
            Ship *temp1;
            for(int i=0; i<3; i++)
            {
                temp1 = players[0]->getShip(i);
                for(int j=0; j<temp1->getSize(); j++)
                {
                    if(temp1->getColumn(j) == coordinates[0] && temp1->getRow(j) == coordinates[1])
                        for(int j=0; j<temp->getSize(); j++)
                        {
                            players[1]->markPositionSec(temp1->getColumn(j) , temp1->getRow(j), markHit);
                            players[0]->markPositionPrim(temp1->getColumn(j) , temp1->getRow(j), markHit);
                        }
                }
            }
        }
        else
        {
            players[1]->markPositionSec(coordinates[0], coordinates[1], markHit);
            players[0]->markPositionPrim(coordinates[0], coordinates[1], markHit);
        }

        interface->clearScr();
        if(this->mode)
        {
            interface->printGrids(players[0]);
            interface->displayMessage("--------------------------------------------------");
            interface->nextl();
        }
        interface->printGrids(players[1]);
        usleep(500000);

        if(temp->canDoubleShoot())
        {
            if (human != NULL)
            {
                useInterface()->displayMessage("Do you want to shoot again?(y/n) ");
                char dec = useInterface()->inputChar();
                if(dec == 'y' || dec == 'Y')
                {
                    players[1]->shoot(*temp, coordinates);
                    markHit = players[0]->getShot(coordinates[0], coordinates[1]);
                    if(markHit == 'D' || markHit == 'H')
                        players[0]->decreasescore();
                    if(markHit == 'D')
                    {
                        Ship *temp1;
                        for(int i=0; i<3; i++)
                        {
                            temp1 = players[0]->getShip(i);
                            for(int j=0; j<temp1->getSize(); j++)
                            {
                                if(temp1->getColumn(j) == coordinates[0] && temp1->getRow(j) == coordinates[1])
                                    for(int j=0; j<temp->getSize(); j++)
                                    {
                                        players[1]->markPositionSec(temp1->getColumn(j) , temp1->getRow(j), markHit);
                                        players[0]->markPositionPrim(temp1->getColumn(j) , temp1->getRow(j), markHit);
                                    }
                            }
                        }
                    }
                    else
                    {
                        players[1]->markPositionSec(coordinates[0], coordinates[1], markHit);
                        players[0]->markPositionPrim(coordinates[0], coordinates[1], markHit);
                    }

                    interface->clearScr();
                    if(this->mode)
                    {
                        interface->printGrids(players[0]);
                        interface->displayMessage("--------------------------------------------------");
                        interface->nextl();
                    }
                    interface->printGrids(players[1]);
                    usleep(500000);
                    players[1]->changeCooldown(temp->getSize());

                }
            }
            else
            {
                players[1]->shoot(*temp, coordinates);
                markHit = players[0]->getShot(coordinates[0], coordinates[1]);
                if(markHit == 'D' || markHit == 'H')
                    players[0]->decreasescore();
                if(markHit == 'D')
                {
                    Ship *temp1;
                    for(int i=0; i<3; i++)
                    {
                        temp1 = players[0]->getShip(i);
                        for(int j=0; j<temp1->getSize(); j++)
                        {
                            if(temp1->getColumn(j) == coordinates[0] && temp1->getRow(j) == coordinates[1])
                                for(int j=0; j<temp->getSize(); j++)
                                {
                                    players[1]->markPositionSec(temp1->getColumn(j) , temp1->getRow(j), markHit);
                                    players[0]->markPositionPrim(temp1->getColumn(j) , temp1->getRow(j), markHit);
                                }
                        }
                    }
                }
                else
                {
                    players[1]->markPositionSec(coordinates[0], coordinates[1], markHit);
                    players[0]->markPositionPrim(coordinates[0], coordinates[1], markHit);
                }
                interface->clearScr();
                if(this->mode)
                {
                    interface->printGrids(players[0]);
                    interface->displayMessage("--------------------------------------------------");
                    interface->nextl();
                }
                interface->printGrids(players[1]);
                usleep(500000);
                players[1]->changeCooldown(temp->getSize());

            }
        }
        else
        {
            players[1]->changeCooldown();
        }
        RoundCounter();

    }
    while(!IfGameEnds());
    EndGame();
}

void Game::EndGame()
{
    if(players[0]->getScore()>players[1]->getScore())
    {
        interface->displayMessage("Player1 score: ");
        interface->displayMessage(players[0]->getScore());
        interface->displayMessage("     Player2 score: ");
        interface->displayMessage(players[1]->getScore());
        interface->nextl();
        interface->displayMessage("Player 1 wins!");
    }
    else if(players[0]->getScore()<players[1]->getScore())
    {
        interface->displayMessage("Player1 score: ");
        interface->displayMessage(players[0]->getScore());
        interface->displayMessage("     Player2 score: ");
        interface->displayMessage(players[1]->getScore());
        interface->nextl();
        interface->displayMessage("Player 2 wins!");
    }
    else
    {
        interface->displayMessage("Player1 score: ");
        interface->displayMessage(players[0]->getScore());
        interface->displayMessage("     Player2 score: ");
        interface->displayMessage(players[1]->getScore());
        interface->nextl();
        interface->displayMessage("Tie!");
    }
    interface->nextl();
}

Interface *Game::useInterface()
{
    return interface;
}



