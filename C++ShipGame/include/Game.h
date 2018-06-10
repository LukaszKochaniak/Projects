#ifndef _GAME_H_
#define _GAME_H_

#include <iostream>
#include <string>
#include <vector>

class Player;
class GameStrategy;
class Interface;

using namespace std;

class Game
{
private:
    int rounds;
    vector<Player *> players;
    Interface* interface;
    bool mode;

public:
    Game(int argc, char *arg1, char *arg2, char *arg3);
    ~Game();
    int CheckRounds(char *rounds);
    void RoundCounter();
    void CorrectArgNumber(int argc);
    bool IfGameEnds();
    bool checkMode(char *arg);
    GameStrategy* checkStrategy(char* arg);
    void play(int argc);
    void EndGame();
    int getRounds()
    {
        return rounds;
    };
    Interface *useInterface();
};



#endif // _GAME_H_

