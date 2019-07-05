## Index of the README
- [Team Adrenaline](#team-adrenaline)
- [Adrenaline board game](#adrenaline-board-game)
  - [Description of the game](#description-of-the-game)
  - [How to play](#how-to-play)
- [Run the game](#run-the-game)
- [Feautures](#features)
  - [Basic](#basic)
  - [Additional functionalities](#additional-functionalities)
  - [Extra functionalities](#extra-functionalities)
- [External libraries used](#external-libraries-used)
- [Test coverage](#test-coverage)



# Team Adrenaline
This project is part of the course Software Engineering (2018-2019) of the Politecnico di Milano.
We are Team Adrenaline (group-37) and the developers are:

- [Reylander Roland](https://github.com/rockplayer007): roland.gandini@mail.polimi.it
- [Tarasco Antonio](https://github.com/tarascoant): antonio.tarasco@mail.polimi.it
- [Xu Yijie](https://github.com/yijie0110):  yijie.xu@mail.polimi.it


## Adrenaline board game

<img src="https://www.boardgamequest.com/wp-content/uploads/2017/01/Adrenaline-Header.jpg" height="350"></img>

### Description of the game
In the future, war has left the world in complete destruction and split the people into factions. The factions have decided to stop the endless war and settle their dispute in the arena. A new virtual bloodsport was created. The Adrenaline tournament. Every faction has a champion, every champion has a chance to fight and the chance to win. Will you take the chance of becoming the next champion of the Adrenaline tournament?

Play a first-person shooter on your gaming table. Grab some ammo, grab a gun, and start shooting. Build up an arsenal for a killer turn. Combat resolution is quick and diceless. And if you get shot, you get faster!

> A Game for 3 to 5 players that can be played in about 30-60 minutes


### How to play

The rules of the game can be found [here](https://czechgames.com/en/adrenaline/).

## Run the game

The game requires [Java 8](https://www.java.com/it/download/) or later. To play the game you first need to run the server on one machine like this:

```sh
$ java -jar server.jar
```
You can change the `configuration.txt` file that is in the same folder as the `server.jar` if you want to personalize the game. **Note** that in case the file is missing or corrupted, then the default options will be considered.

Next you can run a user interface like this:
```sh
$ java -jar client.jar
```
You will be asked if you like to play with a cool `command line interface` (CLI) or an amazing `graphical user inteface` (GUI). In addition it is possible to run a BOT that will randomly play with higher probabilities to attack.
**Note** that a `data.txt` file will be created where a unique id will be stored. If you delete this file it will not be possible for you to reconnect to the game in case your connection falls. Also you can not swap from CLI to GUI or vice versa.

## Features

### Basic
- [x] Complete rules
- [x] Socket
- [x] RMI
- [x] CLI
- [x] GUI


### Additional functionalities
- [x] Multiple matches
- [ ] Persistance
- [ ] Domination or Turret mode
- [ ] Terminator

### Extra functionalities

- [x] Run a BOT that plays automatically 
- [x] Start match on one client and continue on another
- [x] Set number of minimum and maximum players indipendently of the rules
- [x] Set number of skulls and points as you wish, in case you want to speed up the game
- [x] Configure all possible timers of the game, to have a speedy game or a strategy play
- [x] Change RMI and Socket ports how you prefere


## External libraries used

The external libraries we used to implement some game's features are linked below.

|      Library  |Link							 |Use                |
|---------------|--------------------------------|-------------------|
|GSON			|https://github.com/google/gson  | We used this library to easily class objects from server to client |
|Jansi    |https://github.com/fusesource/jansi  | Useful to have a colorful play experience on CLI |

## Test coverage

Based on Intellij's line coverage we reached 71.2% on the model and 53% on the controller. Since the methods of the controller are stricly connected to the network, it was not possible to test all the methods completly. To do this we tested on a LAN as well as locally.
In addition we run multiple bots that played the game fluently.

|      Element  |Class, %							 |Method, %                |Line, %             |
|---------------|--------------------------------|-------------------|--------------------|
|controller			|100% (8/8)  | 70% (55/78) |  54% (384/708)  |
|model			|87% (63/72)  | 74% (214/287) |  76% (1097/1427)  |

![Bots testing](https://github.com/rockplayer007/ing-sw-2019-37/blob/master/demo_pics/CLI_bots.png)

