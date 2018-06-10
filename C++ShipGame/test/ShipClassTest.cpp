#include <boost/test/unit_test.hpp>
#include <iostream>
#include "OneFunnel.h"
#include "Ship.h"
#include "SecondaryGrid.h"

BOOST_AUTO_TEST_SUITE(ShipClassTest)

BOOST_AUTO_TEST_CASE(ConstructorTest)
{
    vector<int> row;
    vector<int> column;
    row.push_back(5);
    column.push_back(5);
    Ship ship(1, row, column);
    BOOST_REQUIRE_EQUAL(ship.getRow(0), 5);
}

BOOST_AUTO_TEST_CASE(GetRowTest)
{
    vector<int> row;
    vector<int> column;
    row.push_back(5);
    column.push_back(5);
    Ship ship(1, row, column);

    BOOST_REQUIRE_EQUAL(ship.getRow(0), 5);
}

BOOST_AUTO_TEST_CASE(getColumnTest)
{
    vector<int> row;
    vector<int> column;
    row.push_back(5);
    column.push_back(5);
    Ship ship(1, row, column);

    BOOST_REQUIRE_EQUAL(ship.getColumn(0), 5);
}

BOOST_AUTO_TEST_CASE(getSizeTest)
{
    vector<int> row;
    vector<int> column;
    row.push_back(5);
    column.push_back(5);
    Ship ship(1, row, column);
    BOOST_REQUIRE_EQUAL(ship.getSize(), 1);
}

BOOST_AUTO_TEST_CASE(isDestroyedTest)
{
    vector<int> row;
    vector<int> column;
    row.push_back(5);
    column.push_back(5);
    Ship ship(1, row, column);
    BOOST_REQUIRE_EQUAL(ship.isDestroyed(), false);
}

BOOST_AUTO_TEST_CASE(isMultifunnelTest)
{
    vector<int> row;
    vector<int> column;
    row.push_back(5);
    column.push_back(5);
    Ship ship(1, row, column);
    BOOST_REQUIRE_EQUAL(ship.isMultifunnel(), false);
}

BOOST_AUTO_TEST_CASE(canShootTest)
{
    vector<int> row;
    vector<int> column;
    row.push_back(5);
    column.push_back(5);
    Ship ship(1, row, column);
    SecondaryGrid secGrid;

    BOOST_REQUIRE_EQUAL(ship.canShoot(4, 4, secGrid), true);
}

BOOST_AUTO_TEST_CASE(shootTest)
{
    vector<int> row;
    vector<int> column;
    row.push_back(5);
    column.push_back(5);
    Ship ship(1, row, column);
    SecondaryGrid secGrid;


    BOOST_REQUIRE_EQUAL(ship.shoot(4, 4, secGrid), true);
}




BOOST_AUTO_TEST_SUITE_END()
