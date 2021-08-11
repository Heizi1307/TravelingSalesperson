This is the readme file for PA11.
file: big11.mtx
heuristic: cost = 3.3969307170000005, 2 milliseconds
mine: cost = 1.3566775349999998, 59 milliseconds
backtrack: cost = 1.3566775349999998, 47 milliseconds


HEURISTIC: Because this algorithms use the current local optimum to find the final solution, it always use the nearest city as next city, so sometimes the obtained solution might not be the optimal solution.
BACKTRACK: This algorithms go over all the cities combination, and find the best solution, the solution is the best solution but took a lot time to go over all the cities.
MINE: Use backtracking method and isPossible() to easy find out the possible combination and store them into a new list. In this way, code can imporve the speed its costs to run.