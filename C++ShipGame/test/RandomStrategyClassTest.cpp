#include <boost/test/unit_test.hpp>
#include <iostream>
#include <vector>
#include "RandomStrategy.h"
#include "Ship.h"
#include "OneFunnel.h"
#include "MultiFunnel.h"


BOOST_AUTO_TEST_SUITE(RandomStrategyTest)

BOOST_AUTO_TEST_CASE(ChooseShipTest)
{
    vector <Ship*> ships1;

    vector<int> x, y;
    x.push_back(5);
    y.push_back(5);
    OneFunnel *ship1 = new OneFunnel(1, x, y);
    ships1.push_back(ship1);

    RandomStrategy strategy;
    Ship *temp=strategy.chooseShip(ships1);

    BOOST_REQUIRE_EQUAL(temp->getSize(), 1);

}


BOOST_AUTO_TEST_SUITE_END()
