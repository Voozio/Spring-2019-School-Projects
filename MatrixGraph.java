package edu.csus.csc130.spring2019.assignment5;

import java.util.ArrayList;
import java.util.List;

/**
 * Modified by: Justin Voo
 *
 */
public class MatrixGraph {
	private int V = 0; // number of vertices
	private int E; // number of edges
	private boolean[][] matrix;
	
	//create a V-vertex graph with no edges
	public MatrixGraph(int V)  {
		this.V = V; 
		this.E = 0;
		this.matrix = new boolean[V][V];
	}
	
	//return number of vertices
	public int V() {
		return V;
	}
	
	//return number of edges
	public int E() {
		return E;
	}
	
	//add edge v-w to this graph
	public void addEdge(int v, int w) {
		matrix[v][w] = true;
		matrix[w][v] = true;
		E++;
	}
	
	public boolean hasEdge(int v, int w) {
		return matrix[v][w];
	}
	
	// Hint: A list can be used and returned
	@SuppressWarnings("unchecked")
	public Iterable<Integer> adj(int v)  {
		List<Integer>[] adj;
		adj = new ArrayList[V];
		for (int u = 0; u < V(); u++) {
			adj[u] = new ArrayList<Integer>();
		}
		for (int x = 0; x < V(); x++) {
			if (matrix[v][x] == true)
				adj[v].add(x);
		}
		return adj[v];
	}
	
	@SuppressWarnings("unused")
	public int degree(int v) {
		int degree = 0;
		for (int w : adj(v)) {
			degree++;
		}
		return degree;
	}
	
	public int maxDegree() {
		int max = 0;
		for (int x = 0; x < V(); x++) {
			for (int y = 0; y < V(); y++) {
				int degree = degree(x);
				if (degree > max)
					max = degree;
			}
		}
		return max;
	}

	public int avgDegree() {
		int sum = 0,num = 0;
		for (int x = 0; x < V(); x++) {
			for (int y = 0; y < V(); y++) {
				int degree = degree(x);
				sum += degree;
				num++;
			}
		}
		return sum/num;
	}

}
