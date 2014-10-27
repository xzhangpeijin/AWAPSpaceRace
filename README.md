AWAPSpaceRace
=============

Algorithms with a purpose 2014 competition

####Results

Code earned 6th place overall in competition

####Movement strategy

Algorithm is greedy and optimizes total score at each move. All valid moves are sorted based on a metric. Points scored by the piece dominate the metric, and if any pieces are tied in score, then the piece with the greatest distance away from the player origin is chosen. Effectively this strategy is greedily choosing the piece that will give the most points while and also expand the bounding box of player territory the most.

####Prerequisites

Requires:
* Python 2.7
* Pip
* Maven
* Java 1.7

####Running

######Setup:
```
pip install --upgrade -r requirements.txt
mvn clean install
mvn package
```

######Running:
```
sh run.sh -t <teamID>
```
