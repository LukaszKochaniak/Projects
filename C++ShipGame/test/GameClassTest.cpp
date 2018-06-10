#include <boost/test/unit_test.hpp>
#include <iostream>
#include "Game.h"

BOOST_AUTO_TEST_SUITE(GameClassTest)

BOOST_AUTO_TEST_CASE(ConstructorTest)
{
    int argc=3;
    char  arg0[] = "20";
    char  arg1[] = "random";
    char  arg2[] = "";
    char* argv[] = { &arg0[0], &arg1[0], &arg2[0], NULL };

    Game game(argc,arg0, arg1, arg2);
    game.RoundCounter();

    BOOST_REQUIRE_EQUAL(game.getRounds(), 19);
}

/*
BOOST_AUTO_TEST_CASE(PlayTest)
{
    cout<<"Test start"<<endl;
    int argc=3;
    char  arg0[] = "9";
    char  arg1[] = "random";
    char  arg2[] = "";
    char* argv[] = { &arg0[0], &arg1[0], &arg2[0], NULL };

    cout<<endl<<endl<<"Game start"<<endl<<endl;
    Game game(argc,arg0, arg1, arg2);
    game.play(4);
    cout<<"Test end"<<endl;
}
*/
BOOST_AUTO_TEST_CASE(CheckModeTest)
{
    int argc=3;
    char  arg0[] = "20";
    char  arg1[] = "random";
    char  arg2[] = "";
    char* argv[] = { &arg0[0], &arg1[0], &arg2[0], NULL };
    Game game(argc,arg0, arg1, arg2);
    game.checkMode(arg1);

    BOOST_REQUIRE_EQUAL(game.checkMode(arg1), true);

}

BOOST_AUTO_TEST_CASE(CheckRoundsTest)
{
    int argc=3;
    char  arg0[] = "20";
    char  arg1[] = "greedy";
    char  arg2[] = "";
    char* argv[] = { &arg0[0], &arg1[0], &arg2[0], NULL };

    Game game(argc,arg0, arg1, arg2);

    BOOST_REQUIRE_EQUAL(game.CheckRounds(arg0), 20);

}

BOOST_AUTO_TEST_CASE(CorrectArgTest)
{
    int argc=3;
    char  arg0[] = "20";
    char  arg1[] = "random";
    char  arg2[] = "";
    char* argv[] = { &arg0[0], &arg1[0], &arg2[0], NULL };

    Game game(argc,arg0, arg1, arg2);
    game.CorrectArgNumber(argc);
    game.RoundCounter();

    BOOST_REQUIRE_EQUAL(game.getRounds(), 19);
}

BOOST_AUTO_TEST_CASE(IfGameEndsTest)
{
    int argc=3;
    char  arg0[] = "1";
    char  arg1[] = "random";
    char  arg2[] = "";
    char* argv[] = { &arg0[0], &arg1[0], &arg2[0], NULL };

    Game game(argc,arg0, arg1, arg2);
    game.RoundCounter();

    BOOST_REQUIRE_EQUAL(game.IfGameEnds(), true);
}


BOOST_AUTO_TEST_CASE(EndGameTest)
{

}



BOOST_AUTO_TEST_SUITE_END()
