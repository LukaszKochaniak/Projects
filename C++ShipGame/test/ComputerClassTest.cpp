#include <boost/test/unit_test.hpp>
#include <iostream>
#include <vector>
#include "PrimaryGrid.h"
#include "SecondaryGrid.h"
#include "Ship.h"
#include "Game.h"
#include "GameStrategy.h"
#include "OneFunnel.h"
#include "Computer.h"
#include "Interface.h"


BOOST_AUTO_TEST_SUITE(ComputerClassTest)

BOOST_AUTO_TEST_CASE(ConstructorTest)
{
    PrimaryGrid primGrid1;
    SecondaryGrid secGrid1;
    vector <Ship*> ships1;
    GameStrategy *strategy;
    char *arg0 = "20";
    char *arg1 = "random";
    char *arg2 = "";
    int argc=3;
    Game game(argc, arg0, arg1, arg2);

    Player *player1;
    Computer computer1(primGrid1, secGrid1, ships1, strategy, &game);
    player1=&computer1;

    BOOST_REQUIRE_EQUAL(computer1.getScore(), 6);
}


BOOST_AUTO_TEST_CASE(ShootTest)
{
    PrimaryGrid primGrid1;
    SecondaryGrid secGrid1;
    vector <Ship*> ships1;
    GameStrategy *strategy;
    char *arg0 = "20";
    char *arg1 = "random";
    char *arg2 = "";
    int argc=3;
    Game game(argc, arg0, arg1, arg2);

    vector<int> x, y;
    x.push_back(rand()%10);
    y.push_back(rand()%10);
    OneFunnel *ship1 = new OneFunnel(1, x, y);
    ships1.push_back(ship1);

    Player *player1;
    Computer computer1(primGrid1, secGrid1, ships1, strategy, &game);
    player1=&computer1;

    int coordinates[2];
    Ship *temp=ships1[0];
    player1->shoot(*temp, coordinates);
    bool check=false;

    if(coordinates[0]>=0 && coordinates[0]<=10)
        check = true;

    BOOST_REQUIRE_EQUAL(check, true);

}

BOOST_AUTO_TEST_CASE(ArrangeShipsTest)
{
    PrimaryGrid primGrid1;
    SecondaryGrid secGrid1;
    vector <Ship*> ships1;
    GameStrategy *strategy;
    Interface interface;
    char *arg0 = "20";
    char *arg1 = "random";
    char *arg2 = "";
    int argc=3;
    Game game(argc, arg0, arg1, arg2);

    Player *player1;
    Computer computer1(primGrid1, secGrid1, ships1, strategy, &game);
    computer1.ArrangeShips();
    interface.printGrids(&computer1);

    BOOST_REQUIRE_EQUAL(computer1.areAllShipsDestroyed(), false);

}


BOOST_AUTO_TEST_SUITE_END()
