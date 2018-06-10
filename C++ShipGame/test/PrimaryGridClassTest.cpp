#include <boost/test/unit_test.hpp>
#include <iostream>
#include "PrimaryGrid.h"
#include "OneFunnel.h"
//////////////////////
BOOST_AUTO_TEST_SUITE(PrimaryGridTest)

BOOST_AUTO_TEST_CASE(ConstructorTest)
{
    PrimaryGrid grid;

    BOOST_REQUIRE_EQUAL(grid.getWidth(), 10);
}

BOOST_AUTO_TEST_CASE(CheckPositionTest)
{
    PrimaryGrid grid;
    char test=grid.checkPosition(5, 5);

    BOOST_REQUIRE_EQUAL(test, '.');
}


BOOST_AUTO_TEST_CASE(CanPutShipDownTest)
{
    PrimaryGrid grid;
    bool test = true;
    vector<int> x, y;
    x.push_back(5);
    y.push_back(5);
    OneFunnel *ship1 = new OneFunnel(1, x, y);
    grid.markPosition(5, 5, 'S');
    test=grid.canPutShipDown(5, 5);

    BOOST_REQUIRE_EQUAL(test, false);
}

BOOST_AUTO_TEST_CASE(PutShipDownTest)
{
    PrimaryGrid grid;
    bool test = true;
    vector<int> x, y;
    x.push_back(5);
    y.push_back(5);
    OneFunnel *ship1 = new OneFunnel(1, x, y);
    grid.putShipDown(5, 5);

    BOOST_REQUIRE_EQUAL(grid.checkPosition(5, 5), 'S');
}



BOOST_AUTO_TEST_SUITE_END()
