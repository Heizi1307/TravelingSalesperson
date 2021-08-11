/*
 * Name: Longxin Li
 * Date: 4/28/2021
 * Filename: PA11Main.java
 * Course: CSC 210
 * 
 * Class to perform different algorithms to solve the traveling salesman problem.
 */

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class PA11Main {
    private static String file_name;
    private static String command;

    public static void main(String[] args) throws Exception {
        file_name = args[0];
        command = args[1];
        DGraph graph = buildGraph(file_name);
        Trip trip;
        if (command.equals("HEURISTIC")) {
            trip = HEURISTIC(graph);
            System.out.println(trip.toString(graph));
        }
        if (command.equals("BACKTRACK")) {
            trip = BACKTRACK(graph);
            System.out.println(trip.toString(graph));
        }
        if (command.equals("MINE")) {
            trip = MINE(graph);
            System.out.println(trip.toString(graph));
        }
        if (command.equals("TIME")) {
            TIME(graph);
        }
    }

    public static DGraph buildGraph(String filePath) throws IOException {
        boolean flag = true;
        String[] temp;
        String[] str;
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(filePath), "UTF-8"));
        String line = reader.readLine();
        while (flag) {
            line = reader.readLine();
            if (line.startsWith("%")) {
                continue;
            }
            flag = false;
        }
        str = line.split(" ");
        int rows = (Integer.valueOf(str[0].trim())).intValue();
        DGraph graph = new DGraph(rows);
        while ((line = reader.readLine()) != null) {
            temp = line.split(" +");
            int node1 = (Integer.valueOf(temp[0].trim())).intValue();
            int node2 = (Integer.valueOf(temp[1].trim())).intValue();
            double weight = (Double.valueOf(temp[2].trim())).doubleValue();
            graph.addEdge(node1, node2, weight);
        }
        reader.close();
        return graph;
    }

    public static Trip HEURISTIC(DGraph graph) {
        Trip myTrip = new Trip(graph.getNumNodes());
        int currentCity = 1;
        int closestCity;
        double closestDistance;
        int i = 2;
        myTrip.chooseNextCity(currentCity);
        while (i <= graph.getNumNodes()) {
            closestCity = 0;
            closestDistance = Double.MAX_VALUE;
            List<Integer> neighbors = graph.getNeighbors(currentCity);
            for (Integer neighborCity : neighbors) {
                if (myTrip.isCityAvailable(neighborCity)) {
                    double tempDistance = graph.getWeight(currentCity,
                            neighborCity);
                    if (tempDistance < closestDistance) {
                        closestCity = neighborCity;
                        closestDistance = tempDistance;
                    }
                }
            }
            myTrip.chooseNextCity(closestCity);
            currentCity = closestCity;
            i++;
        }
        return myTrip;

    }

    public static Trip BACKTRACK(DGraph graph) {
        Trip myTrip = new Trip(graph.getNumNodes());
        Trip mineTrip = new Trip(graph.getNumNodes());
        int currentCity = 1;
        myTrip.chooseNextCity(currentCity);
        backtrackHelper(graph, myTrip, mineTrip);
        return mineTrip;
    }

    public static void backtrackHelper(DGraph graph, Trip currentTrip,
            Trip minTrip) {
        if (currentTrip.isPossible(graph)) {
            if (currentTrip.tripCost(graph) < minTrip.tripCost(graph)) {
                minTrip.copyOtherIntoSelf(currentTrip);
            }
            return;
        }
        if (currentTrip.tripCost(graph) < minTrip.tripCost(graph)) {
            for (Integer city : currentTrip.citiesLeft()) {
                currentTrip.chooseNextCity(city);
                backtrackHelper(graph, currentTrip, minTrip);
                currentTrip.unchooseLastCity();
            }
        }
    }

    public static Trip MINE(DGraph graph) {
        Trip myTrip = new Trip(graph.getNumNodes());
        Trip mineTrip = new Trip(graph.getNumNodes());
        int currentCity = 1;
        ArrayList<Integer> possibleList = new ArrayList<>();
        myTrip.chooseNextCity(currentCity);
        possibleList.add(currentCity);
        MineHelper(graph, myTrip, mineTrip, possibleList);
        return mineTrip;
    }

    public static void MineHelper(DGraph graph,
            Trip currentTrip,
            Trip minTrip, ArrayList<Integer> possibleList) {
        if (currentTrip.citiesLeft().size() == 0
                && graph.getWeight(possibleList.get(0),
                        possibleList.get(graph.getNumNodes() - 1)) != -1) {
            if (currentTrip.tripCost(graph) < minTrip.tripCost(graph)) {
                minTrip.copyOtherIntoSelf(currentTrip);
            }
            return;
        }
        if (currentTrip.tripCost(graph) < minTrip.tripCost(graph)) {
            for (Integer city : currentTrip.citiesLeft()) {
                currentTrip.chooseNextCity(city);
                possibleList.add(city);
                MineHelper(graph, currentTrip, minTrip,
                        possibleList);
                currentTrip.unchooseLastCity();
                possibleList.remove(city);
            }
        }
    }

    public static void TIME(DGraph graph) {
        HeuristicWay(graph);
        MineWay(graph);
        BacktrackWay(graph);
    }

    public static void HeuristicWay(DGraph graph) {
        Trip trip;
        long startTime = System.nanoTime();
        trip = HEURISTIC(graph);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1000000;
        System.out.println("heuristic: cost = " + trip.tripCost(graph) + ", "
                + duration + " milliseconds");
    }

    public static void MineWay(DGraph graph) {
        Trip trip;
        long startTime = System.nanoTime();
        trip = MINE(graph);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1000000;
        System.out.println("mine: cost = " + trip.tripCost(graph) + ", "
                + duration + " milliseconds");
    }

    public static void BacktrackWay(DGraph graph) {
        Trip trip;
        long startTime = System.nanoTime();
        trip = BACKTRACK(graph);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1000000;
        System.out.println("backtrack: cost = " + trip.tripCost(graph) + ", "
                + duration + " milliseconds");
    }
}
