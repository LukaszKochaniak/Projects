#include <boost/test/unit_test.hpp>
#include <iostream>
#include "Grid.h"

BOOST_AUTO_TEST_SUITE(GridTest)

BOOST_AUTO_TEST_CASE(ConstructorTest)
{
    Grid grid;

    BOOST_REQUIRE_EQUAL(grid.getWidth(), 10);

}

BOOST_AUTO_TEST_CASE(CheckPositionTest)
{
    Grid grid;
    char test=grid.checkPosition(5, 5);

    BOOST_REQUIRE_EQUAL(test, '.');

}

BOOST_AUTO_TEST_CASE(MarkPositionTest)
{
    Grid grid;
    grid.markPosition(5, 5, 'S');
    char test=grid.checkPosition(5, 5);

    BOOST_REQUIRE_EQUAL(test, 'S');

}



BOOST_AUTO_TEST_SUITE_END()
