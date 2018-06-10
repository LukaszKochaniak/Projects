#include <boost/test/unit_test.hpp>
#include <iostream>
#include "SecondaryGrid.h"

BOOST_AUTO_TEST_SUITE(SecondaryGridTest)

BOOST_AUTO_TEST_CASE(ConstructorTest)
{
    SecondaryGrid grid;

    BOOST_REQUIRE_EQUAL(grid.getWidth(), 10);
}

BOOST_AUTO_TEST_CASE(canBecomeTargetTest)
{
    SecondaryGrid grid;
    grid.markPosition(5, 5, 'X');

    BOOST_REQUIRE_EQUAL(grid.canBecomeTarget(5, 5), false);
}



BOOST_AUTO_TEST_SUITE_END()
