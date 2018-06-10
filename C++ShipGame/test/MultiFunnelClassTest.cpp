#include <boost/test/unit_test.hpp>
#include <iostream>
#include "MultiFunnel.h"
#include "Ship.h"

BOOST_AUTO_TEST_SUITE(MultiFunnelClassTest)

BOOST_AUTO_TEST_CASE(ConstructorTest)
{
    vector<int> x, y;
    x.push_back(rand()%10);
    y.push_back(rand()%10);
    MultiFunnel *ship1 = new MultiFunnel(3, x, y);

    BOOST_REQUIRE_EQUAL(ship1->checkCooldown(), false);
}

BOOST_AUTO_TEST_CASE(CheckCooldownTest)
{
    vector<int> x, y;
    x.push_back(rand()%10);
    y.push_back(rand()%10);
    MultiFunnel *ship1 = new MultiFunnel(3, x, y);

    BOOST_REQUIRE_EQUAL(ship1->checkCooldown(), false);

}

BOOST_AUTO_TEST_CASE(CheckHealthTest)
{
    vector<int> x, y;
    x.push_back(rand()%10);
    y.push_back(rand()%10);
    MultiFunnel *ship1 = new MultiFunnel(3, x, y);

    BOOST_REQUIRE_EQUAL(ship1->checkHealth(), 3);
}

BOOST_AUTO_TEST_CASE(CanDoubleShootTest)
{
    vector<int> x, y;
    x.push_back(rand()%10);
    y.push_back(rand()%10);
    MultiFunnel *ship1 = new MultiFunnel(3, x, y);

    BOOST_REQUIRE_EQUAL(ship1->canDoubleShoot(), true);
}

BOOST_AUTO_TEST_CASE(GetHitTest)
{
    vector<int> x, y;
    x.push_back(rand()%10);
    y.push_back(rand()%10);
    MultiFunnel *ship1 = new MultiFunnel(3, x, y);
    ship1->getHit();

    BOOST_REQUIRE_EQUAL(ship1->checkHealth(), 2);
}


BOOST_AUTO_TEST_SUITE_END()
