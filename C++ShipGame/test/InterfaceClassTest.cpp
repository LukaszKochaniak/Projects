#include <boost/test/unit_test.hpp>
#include <iostream>
#include "Computer.h"
#include "Ship.h"
#include "Game.h"
#include "PrimaryGrid.h"
#include "SecondaryGrid.h"
#include "Interface.h"
#include "GreedyStrategy.h"
#include "RandomStrategy.h"



BOOST_AUTO_TEST_SUITE(InterfaceClassTest)

BOOST_AUTO_TEST_CASE(PrintTest)
{

    PrimaryGrid primGrid1;
    SecondaryGrid secGrid1;
    vector <Ship*> ships1;
    GreedyStrategy strat1;
    RandomStrategy strat2;
    int argc=3;
    char *arg0 = "20";
    char *arg1 = "random";
    char *arg2 = "";
    Game game(argc, arg0, arg1, arg2);


    Computer computer2(primGrid1, secGrid1, ships1, &strat1, &game);
    Computer computer3(primGrid1, secGrid1, ships1, &strat2, &game);
    computer2.ArrangeShips();
    computer3.ArrangeShips();
    Interface interface;


    interface.printGrids(&computer3);
}

BOOST_AUTO_TEST_SUITE_END()
