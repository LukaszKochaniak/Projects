#include <iostream>
#include <vector>
#include "Player.h"
#include "Grid.h"
#include "PrimaryGrid.h"
#include "SecondaryGrid.h"
#include "Game.h"
#include "Interface.h"
using namespace std;

Player::Player(PrimaryGrid primGrid, SecondaryGrid secGrid, vector <Ship*> ships, Game *game)
{
    this->score=6; //=0
    this->primGrid=primGrid;
    this->secGrid=secGrid;
    this->shipsDestroyed=0; //=0
    this->ships=ships;
    this->game = game;
}

Player::Player()
{
    PrimaryGrid primGrid;
    SecondaryGrid secGrid;
    vector <Ship*> ships;
    Game *game;

    this->score=6; //=0
    this->primGrid=primGrid;
    this->secGrid=secGrid;
    this->shipsDestroyed=0; //=0
    this->ships=ships;
    this->game = game;
}

Player::~Player()
{
    for(int i=0; i<ships.size(); i++)
    {
        delete ships[i];
    }
    ships.clear();
}


char Player::checkPositionPrim(int x, int y)
{
    return primGrid.checkPosition(x, y);
}

char Player::checkPositionSec(int x, int y)
{
    return secGrid.checkPosition(x, y);
}

void Player::markPositionPrim(int x, int y, char newChar)
{
    primGrid.markPosition(x, y, newChar);
}

void Player::markPositionSec(int x, int y, char newChar)
{
    secGrid.markPosition(x, y, newChar);
}


int Player::getScore()
{
    return score;
}

bool Player::areAllShipsDestroyed()
{
    if(shipsDestroyed==3)
        return true;
    else
        return false;
}

bool Player::canDoubleShoot(Ship ship)
{
    if(ship.isMultifunnel())
    {
        if(ship.canDoubleShoot())
            return true;
        else
        {
            return false;
            game->useInterface()->displayMessage("You can't shoot twice (cooldown).");
            game->useInterface()->nextl();
        }
    }
    else
    {
        game->useInterface()->displayMessage("You can't shoot twice (cooldown).");
        game->useInterface()->nextl();
    }
}

void Player::decreasescore()
{
    score--;
}

char Player::getShot(int column, int row)
{
    for(int i=0; i<ships.size(); i++)
    {
        for(int j=0; j<ships[i]->getSize(); j++)
        {
            if(ships[i]->getColumn(j) == column && ships[i]->getRow(j) == row)
            {
                return ships[i]->getHit();
            }
        }
    }
    return 'x';
}

bool Player::hasTargets()
{
    for(int i=0; i<ships.size(); i++)
    {
        if(ships[i]->hasTargets()) return true;
    }

    return false;
}

Game *Player::getGame()
{
    return game;
}

void Player::changeCooldown(int s)
{
    for(int i=0; i<ships.size(); i++)
    {
        if(ships[i]->getSize()>1)
        {
            if(ships[i]->getSize()==s)
            {
                ships[i]->changeCooldown(s);
            }
            else
            {
                ships[i]->changeCooldown();
            }
        }
    }
}

Ship *Player::getShip(int i)
{
    return ships[i];
}

