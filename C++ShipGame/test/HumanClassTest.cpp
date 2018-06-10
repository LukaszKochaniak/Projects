#include <boost/test/unit_test.hpp>
#include <iostream>
#include <vector>
#include "Human.h"
#include "Player.h"
#include "PrimaryGrid.h"
#include "SecondaryGrid.h"
#include "Interface.h"
#include "Game.h"


BOOST_AUTO_TEST_SUITE(HumanClassTest)

BOOST_AUTO_TEST_CASE(ConstructorTest)
{
    PrimaryGrid primGrid1;
    SecondaryGrid secGrid1;
    vector <Ship*> ships1;
    Interface interface;
    int argc=3;
    char arg0[] = "20";
    char arg1[] = "random";
    char arg2[] = "";
    Game game(argc, arg0, arg1, arg2);

    Human human1(primGrid1, secGrid1, ships1, &game);
//    human1.ArrangeShips();
//    interface.printGrids(*player1);
//    player1->ArrangeShips();
//    interface.printGrids(*player1);
//    cout<<human1.checkPositionPrim(1, 1)<<endl;

}


BOOST_AUTO_TEST_SUITE_END()
