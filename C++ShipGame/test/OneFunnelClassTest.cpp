#include <boost/test/unit_test.hpp>
#include <iostream>
#include "OneFunnel.h"
#include "Ship.h"

BOOST_AUTO_TEST_SUITE(OneFunnelClassTest)

BOOST_AUTO_TEST_CASE(ConstructorTest)
{
    vector<int> x, y;
    x.push_back(rand()%10);
    y.push_back(rand()%10);
    OneFunnel *ship1 = new OneFunnel(1, x, y);

    BOOST_REQUIRE_EQUAL(ship1->isDestroyed(), false);
}

BOOST_AUTO_TEST_CASE(CanDoubleShootTest)
{
    vector<int> x, y;
    x.push_back(rand()%10);
    y.push_back(rand()%10);
    OneFunnel *ship1 = new OneFunnel(1, x, y);

    BOOST_REQUIRE_EQUAL(ship1->canDoubleShoot(), false);
}

BOOST_AUTO_TEST_CASE(GetHitTest)
{
    vector<int> x, y;
    x.push_back(rand()%10);
    y.push_back(rand()%10);
    OneFunnel *ship1 = new OneFunnel(1, x, y);
    ship1->getHit();

    BOOST_REQUIRE_EQUAL(ship1->isDestroyed(), true);
}

BOOST_AUTO_TEST_SUITE_END()
