#include <iostream>
#include "Game.h"
#include <cstdlib>

using namespace std;

int main(int argc, char* argv[])
{
    srand(time(NULL));

    Game game(argc, argv[1], argv[2], argv[3]);
//    Game game("20", "random", "random");
    game.play(argc);

    return 0;
}
