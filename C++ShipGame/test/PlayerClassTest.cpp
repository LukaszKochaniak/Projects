#include <boost/test/unit_test.hpp>
#include <iostream>
#include <vector>
#include "PrimaryGrid.h"
#include "SecondaryGrid.h"
#include "Ship.h"
#include "MultiFunnel.h"
#include "Human.h"
#include "Game.h"

BOOST_AUTO_TEST_SUITE(PlayerClassTest)

BOOST_AUTO_TEST_CASE(ConstructorTest)
{
    PrimaryGrid primGrid1;
    SecondaryGrid secGrid1;
    vector <Ship*> ships1;
    int argc=3;
    char *arg0 = "20";
    char *arg1 = "random";
    char *arg2 = "";
    Game game(argc, arg0, arg1, arg2);

    Human player1(primGrid1, secGrid1, ships1, &game);

    BOOST_REQUIRE_EQUAL(player1.getScore(), 6);
}

BOOST_AUTO_TEST_CASE(GetScoreTest)
{
    PrimaryGrid primGrid1;
    SecondaryGrid secGrid1;
    vector <Ship*> ships1;
    int argc=3;
    char *arg0 = "20";
    char *arg1 = "random";
    char *arg2 = "";
    Game game(argc, arg0, arg1, arg2);

    Human player1(primGrid1, secGrid1, ships1, &game);

    BOOST_REQUIRE_EQUAL(player1.getScore(), 6);
}

BOOST_AUTO_TEST_CASE(AreAllShipsDestroyedTest)
{
    PrimaryGrid primGrid1;
    SecondaryGrid secGrid1;
    vector <Ship*> ships1;
    int argc=3;
    char *arg0 = "20";
    char *arg1 = "random";
    char *arg2 = "";
    Game game(argc, arg0, arg1, arg2);

    Human player1(primGrid1, secGrid1, ships1, &game);


    BOOST_REQUIRE_EQUAL(player1.areAllShipsDestroyed(), false);
}

BOOST_AUTO_TEST_CASE(canDoubleShootTest)
{
    PrimaryGrid primGrid1;
    SecondaryGrid secGrid1;
    vector <Ship*> ships1;
    int argc=3;
    char *arg0 = "20";
    char *arg1 = "random";
    char *arg2 = "";
    Game game(argc, arg0, arg1, arg2);

    vector<int> x, y;
    x.push_back(rand()%10);
    y.push_back(rand()%10);
    MultiFunnel *ship1 = new MultiFunnel(3, x, y);
    ships1.push_back(ship1);

    Player *player1;
    Human player(primGrid1, secGrid1, ships1, &game);
    player1=&player;

    Ship *temp=ships1[0];

    BOOST_REQUIRE_EQUAL(player1->canDoubleShoot(*temp), true);
}

BOOST_AUTO_TEST_CASE(DecreaseScoreTest)
{
    PrimaryGrid primGrid1;
    SecondaryGrid secGrid1;
    vector <Ship*> ships1;
    int argc=3;
    char *arg0 = "20";
    char *arg1 = "random";
    char *arg2 = "";
    Game game(argc, arg0, arg1, arg2);

    vector<int> x, y;
    x.push_back(rand()%10);
    y.push_back(rand()%10);
    OneFunnel *ship1 = new OneFunnel(1, x, y);
    ships1.push_back(ship1);

    Player *player1;
    Human player(primGrid1, secGrid1, ships1, &game);
    player1=&player;

    player1->decreasescore();

    BOOST_REQUIRE_EQUAL(player1->getScore(), 5);
}


BOOST_AUTO_TEST_SUITE_END()
