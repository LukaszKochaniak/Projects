#include <boost/test/unit_test.hpp>
#include <iostream>
#include <vector>
#include "GreedyStrategy.h"
#include "Ship.h"
#include "OneFunnel.h"
#include "MultiFunnel.h"


BOOST_AUTO_TEST_SUITE(GreedyStrategyTest)

BOOST_AUTO_TEST_CASE(ChooseShipTest)
{
    std::vector <Ship*> ships1;
    std::vector <int> x;
    std::vector <int> y;
    x.push_back(0);
    y.push_back(0);
    OneFunnel ship1(1, x, y);
    MultiFunnel ship2(2, x, y);
    MultiFunnel ship3(3, x, y);
    ships1.push_back(&ship1);
    ships1.push_back(&ship2);
    ships1.push_back(&ship3);

    GreedyStrategy strategy;
    Ship *temp=strategy.chooseShip(ships1);

    BOOST_REQUIRE_EQUAL(temp->getSize(), 3);

}


BOOST_AUTO_TEST_SUITE_END()
