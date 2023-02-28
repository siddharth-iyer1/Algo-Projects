/*
 * Name: Jon Snow
 * EID: GoT0001
 */

// Implement your algorithms here
// Methods may be added to this file, but don't remove anything
// Include this file in your final submission

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class Program2 {

    /**
     * findMinimumLength()
     *
     * @param problem - contains the regions of the graph.
     * @return The sum of all the edges of the MST.
     * @function Should track the edges in the MST using region.mst_neighbors and region.mst_weights
     * This function will not modify the mst_lists when run Gradescope if called in calculateDiameter()
     */
    public int findMinimumLength(Problem problem) {

        problem.getRegions().get(0).setMinDist(0);
        Queue<Region> heap = new PriorityQueue<>(problem.getRegions());


        ArrayList<Integer> visited = new ArrayList<Integer>();
        ArrayList<Integer> visitedNames = new ArrayList<Integer>();
        for (int i = 0; i < problem.getRegions().size(); i++) {
            visited.add(0); // Unvisited
        }
        int sum = 0;

        // Find initial minimum cost edge
        int minEdge = Integer.MAX_VALUE;
        Region minEdgev1 = problem.getRegions().get(0);
        Region minEdgev2 = problem.getRegions().get(0);

        for (int i = 0; i < problem.getRegions().size(); i++) {   // For each region
            int index = problem.getRegions().get(i).getName();  // The index in question
            for (int j = 0; j < problem.getRegions().get(i).getWeights().size(); j++) {     // For each neighbor
                if (problem.getRegions().get(i).getWeights().get(j) < minEdge) {
                    // Update initial minimum cost edge
                    minEdge = problem.getRegions().get(i).getWeights().get(j);
                    minEdgev1 = problem.getRegions().get(i);
                    minEdgev2 = problem.getRegions().get(i).getNeighbors().get(j);
                }
            }
        }

        // Now add the starting vertices

        sum += minEdge;
        visited.set(minEdgev1.getName(), 1);
        visitedNames.add(minEdgev1.getName());
        visited.set(minEdgev2.getName(), 1);
        visitedNames.add(minEdgev2.getName());

        minEdgev1.setMST_NeighborAndWeight(minEdgev2, minEdge);
        minEdgev2.setMST_NeighborAndWeight(minEdgev1, minEdge);

        while (visitedNames.size() != (problem.getRegions().size())) {
            // Check neighbors
            int currMinEdge = Integer.MAX_VALUE;
            Region originRegion = problem.getRegions().get(0);
            Region newRegion = problem.getRegions().get(0);

            for (int i = 0; i < visitedNames.size(); i++) {   // For every visited region
                int index = visitedNames.get(i);    // The region in question
                for (int j = 0; j < problem.getRegions().get(index).getNeighbors().size(); j++) {     // For each neighbor
                    if (visited.get(problem.getRegions().get(index).getNeighbors().get(j).getName()) == 0) {     // unvisited
                        if (problem.getRegions().get(index).getWeights().get(j) < currMinEdge) {
                            currMinEdge = problem.getRegions().get(index).getWeights().get(j);
                            originRegion = problem.getRegions().get(index);
                            newRegion = problem.getRegions().get(index).getNeighbors().get(j);
                        }
                    }
                }
            }

            visitedNames.add(newRegion.getName());
            visited.set(newRegion.getName(), 1);

            originRegion.setMST_NeighborAndWeight(newRegion, currMinEdge);
            newRegion.setMST_NeighborAndWeight(originRegion, currMinEdge);

            sum += currMinEdge;

        }

        return sum;
    }


    /* calculateDiameter(Problem problem)
     *
     * @param problem  - contains the regions of the problem. Each region has an adjacency list
     * defined by mst_neighbors and mst_weights, which defines the provided MST.
     *
     */
    public int calculateDiameter(Problem problem) {
        int length = findMinimumLength(problem);

        ArrayList<Integer> visited = new ArrayList<Integer>();
        ArrayList<Integer> distances = new ArrayList<Integer>();
        for(int i = 0; i < problem.getRegions().size(); i++){
            visited.add(0);
            distances.add(0);
        }

        ArrayList<Integer> path = new ArrayList<Integer>();

        for(int i = 0; i < problem.getRegions().get(0).getMST_Neighbors().size(); i++){
            DFS(problem, problem.getRegions().get(0).getMST_Neighbors().get(i).getName(),visited, path,distances);
        }

        int maxVertex = 0;
        int maxDistance = 0;
        for(int i = 0; i < distances.size(); i++){  // Find end vertex with max distance
            if(distances.get(i) > maxDistance){
                maxVertex = i;
                maxDistance = distances.get(i);
            }
        }

        ArrayList<Integer> diameter = new ArrayList<Integer>(); // For distances param, this will only have one value
        for(int i = 0; i < problem.getRegions().size(); i++){
            diameter.add(0);
        }

        DFS(problem, maxVertex, visited, path, diameter);

        int max = 0;
        for(int i = 0; i < diameter.size(); i++){
            if(diameter.get(i) > max) {
                max = diameter.get(i);
            }
        }

        return max - 1;
    }
    public void DFS(Problem problem, int current, ArrayList<Integer> visited, ArrayList<Integer> path, ArrayList<Integer> distances){
        visited.set(current, 1);
        path.add(current);

        // If all adjacent vertices are visited, end of path
        boolean flag = true;
        for(int i = 0; i < problem.getRegions().get(current).getMST_Neighbors().size(); i++){
            if (visited.get(problem.getRegions().get(current).getMST_Neighbors().get(i).getName()) == 0) {
                flag = false;
                break;
            }
        }
        if(flag){   // End of path
            distances.set(current, path.size());
        }

        for(int i = 0; i < problem.getRegions().get(current).getMST_Neighbors().size(); i++){
            if (visited.get(problem.getRegions().get(current).getMST_Neighbors().get(i).getName()) == 0) {
                DFS(problem, problem.getRegions().get(current).getMST_Neighbors().get(i).getName(), visited,
                        path, distances);
            }
        }

        visited.set(current, 0);
        path.remove(path.size() - 1);
    }
}