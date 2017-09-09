import java.io.*;
import java.util.*;
// Assumptions and limitations:
// 1) there will never be a target (node/edge) 0
//
// 2) all inputs will be as described in the
// exercise, e.g.
// 5 4
// 3 2 1 5
// 2 2 5 3
// 4 1 3
// 5 1 1  
// and so assumes all input ints are separated by a single whitespace character 
// so split(" ") will return a meaningful array for each line
//
// 3) I am not at all sure that this code satisfies this requirement in the exercise:
// "To avoid ambiguity in the output, ​
// in cases where multiple tasks have all dependencies satisfied, the task
// with the lowest numeric value should be printed."
// as I was running short on time.

class Graph
{
    // FAIL is syntactic sugar for exit on failure    
    private static int FAIL = 1;
    private int NodeCount;   
    private LinkedList<Integer>[] adj; 

    // Constructor
    public Graph(int count)
    {
        NodeCount = count;
        adj = new LinkedList[count+1];
        // create the adjacency lists
        for (int i = 0; i < adj.length; i++)
          adj[i] = new LinkedList<Integer>();
    }

   
    public void addEdge(int n, int e)
    {
        // System.out.format("Adding edge: %d -> %d\n", n,e);
        adj[n].add(e);
    }
    
    public static void main(String args[])
    {
        
        int taskCount = 0, ruleCount = 0;
        
        Scanner s = new Scanner(System.in);
        String[] headers = s.nextLine().split(" ");
        try {
            taskCount = Integer.parseInt(headers[0]);
            ruleCount = Integer.parseInt(headers[1]);
        
        } catch (NumberFormatException nfe) {
            errOut("Bad header line in input: could not parse task/rule counts");
        }

        if (taskCount < 1 || taskCount > 100) {
            errOut(String.format("task count out of range: %d\n", taskCount));
        }

        if (ruleCount < 1 || ruleCount > 100) {
            errOut(String.format("rule count out of range: %d\n", ruleCount));
        }
        //System.out.format("taskCount = %d", taskCount);
        Graph dag = new Graph(taskCount);

        Queue<Integer> edges; 
        while(s.hasNextLine()) {
            // depends will be something like {3, 2, 1, 5}
            String[] depends = s.nextLine().split(" ");
            edges = parseDeps(depends);
            addEdges(edges, dag);
        }
        s.close(); 

        Stack<Integer> results = dag.tSort();
        // 
        //make sure we print the evaluation order, not the dependency order
        StringBuilder op = new StringBuilder();
        
        // Iterator<Integer> res = results.iterator();
        // while (res.hasNext()) {
        //     //System.out.format("%d ", res.next());
        //     op.append(res.next());
        //     op.append(" ");
        // }
        // System.out.println(op.toString());

        Integer[] resArr = results.toArray(new Integer[results.size()]);
        for (int x : resArr) {
            op.append(String.format("%d ", x));
        }
        System.out.println(op.toString());
    }

// addEdges assumes that the edges queue contains some ints and
// will not contain
// any nulls, e.g. there is no reason to check the value
// returned by poll() nor any reason to check queue size at start
    private static void addEdges(Queue<Integer> edges, Graph g) {
      Integer e1 = 0, e2 = 0;
      // head of queue is rule target
      e1 = edges.poll();
      while (edges.size() > 0) {
        e2 = edges.poll();
        g.addEdge(e1, e2);
      }
    }

private static Queue<Integer> parseDeps(String[] deps) {
    Queue<Integer> q = new LinkedList<Integer>();
    Integer dint = 0;
    // on empty input, just bail out
    if (deps.length == 0) {
        return q;
    }
    for (String d : deps){
        try {
            dint = Integer.parseInt(d);
        } catch (NumberFormatException nfe) {
            continue;
        }
        q.add(dint);
    } 
    return q;
}


private void sortHelper(int node, boolean checked[], Stack<Integer> stack) {
  // Mark  current node 
  checked[node] = true;
  Integer i = 0;

  // Recur for all the vertices adjacent to this
  // vertex
  Iterator<Integer> it = adj[node].iterator();
  while (it.hasNext()) {
    i = it.next();
    if (!checked[i]) {
      sortHelper(i, checked, stack);
    }
  }

  // Push current vertex to stack which stores result
  stack.push(new Integer(node));
}

// The function to do Topological Sort. It uses
// recursive topologicalSortUtil()
public Stack<Integer> tSort() {
  Stack<Integer> stack = new Stack<Integer>();

  // no nodes have been checked/visited yet
  boolean visited[] = new boolean[NodeCount+1];
  for (int i = 0; i <= NodeCount; i++) {
    visited[i] = false;
  }

// Iterate over the nodes and call  the  helper  to save
// tSort results 
  for (int i = 1; i <= NodeCount; i++) {
    if (!visited[i] ) {
      sortHelper(i, visited, stack);
    }
  }
  return stack;
}
  private static void errOut(String msg) {
    System.err.print(msg);
    System.exit(FAIL);
  }
}
