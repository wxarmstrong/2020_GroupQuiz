/*
William Armstrong
CS 3310
Cal Poly Pomona
Fall 2020
10/21/2020
*/

/*
COMPLEXITY ANALYSIS:
(Note: these complexities are based on the assumption that a valid solution exists)
--------------------
solve() / solve2():
Time Complexity  :
Key Operation    : HashMap lookup
Best case : If the dependencies are such that the initial project list is already a valid solution, then only V+E lookups occur.
Thus the time complexity is O(V+E). (linear)
Worst case: If the dependencies are such that every iteration over the project list only causes one new project to be added to the ongoing solution,
then O(V) iterations are required with O(V+E) key operations per iteration.
The time complexity is O(V^2 + E). (quadratic)
Average case: In O(V^2) (upper bound via worst case)

Memory Complexity: 
A HashMap is created containing a separate node for each project (vertex). Each dependency is listed twice, either as a parent->child or child->parent edge.
Thus the memory complexity is in O(V+2E) -> O(V+E)
--------------------
solve3():
Time Complexity  :
All cases: Each project is looked up in the HashMap once when it is moved from the queue to the solution, then every child of that project needs to be looked up
in order to remove the previous project from the parent lists.
Thus the time complexity is in O(V+E).

Memory Complexity:
A HashMap is created containing a separate node for each project (vertex). Each dependency is listed twice, either as a parent->child or child->parent edge.
A Queue is also created with max length equal to the number of projects.
Thus the memory complexity is in O(2V+2E) -> O(V+E)
--------------------
*/

import java.util.*;
import java.util.concurrent.TimeUnit;

class SequentialOrdering
{
	public static class Node {
		
		boolean placed = false;
		Set<String> parents;
		Set<String> children;
		
		public Node()
		{
			parents = new HashSet<String>();
			children = new HashSet<String>();
		}
	};
	
	public static String[] solve(String[] projects, String[][] dependencies)
	{
		HashMap<String, Node> table = new HashMap<String,Node>();
		
		for (int i = 0; i < projects.length; i++)
		{
			Node curNode = new Node();
			table.put(projects[i], curNode);
		}
		
		for (int i = 0; i < dependencies.length; i++)
		{
			String[] curDep = dependencies[i];
			String first = curDep[0];
			String second = curDep[1];
			Node curNode = table.get(second);
			curNode.parents.add(first);
		}
		
		// Debug to check that dependencies are listed correctly
		/*
		for (int i = 0; i < projects.length; i++)
		{
			Node curNode = table.get(projects[i]);
			System.out.print("Parents of " + projects[i] + ": ");
			for (String curParent : curNode.parents)
				System.out.print( curParent );
			System.out.println();
		}
		*/
		
		String[] solution = new String[projects.length];
		int pos = 0;
		
		while (pos < projects.length)
		{
			int lastPos = pos;
			
			for (int i = 0; i < projects.length; i++)
			{
				String curProject = projects[i];
				Node curNode = table.get(curProject);
				if (!curNode.placed)
				{
					boolean canPlace = true;
					for (String curParent : curNode.parents)
					{
						Node parentNode = table.get(curParent);
						if (!parentNode.placed)
						{
							canPlace = false;
							break;
						}
					}
					if (canPlace)
					{
						solution[pos] = curProject;
						curNode.placed = true;
						pos++;
					}					
				}
			}
			
			// If no projects were added in this iteration,
			//  that indicates a circular dependency, and
			//  thus no solution exists.
			if (pos == lastPos)
				throw new ArithmeticException("No solution");
		}
		
		return solution;
	}



	public static String[] solve2(String[] projects, String[][] dependencies)
	{
		HashMap<String, Node> table = new HashMap<String,Node>();
		
		for (int i = 0; i < projects.length; i++)
		{
			Node curNode = new Node();
			table.put(projects[i], curNode);
		}
		
		for (int i = 0; i < dependencies.length; i++)
		{
			String[] curDep = dependencies[i];
			String first = curDep[0];
			String second = curDep[1];
			Node curNode = table.get(second);
			curNode.parents.add(first);
			curNode = table.get(first);
			curNode.children.add(second);
		}
		
		// Debug to check that dependencies are listed correctly
		/*
		for (int i = 0; i < projects.length; i++)
		{
			Node curNode = table.get(projects[i]);
			System.out.print("Parents of " + projects[i] + ": ");
			for (String curParent : curNode.parents)
				System.out.print( curParent );
			System.out.println();
		}
		*/
		
		String[] solution = new String[projects.length];
		int pos = 0;
		
		while (pos < projects.length)
		{
			int lastPos = pos;
			
			for (int i = 0; i < projects.length; i++)
			{
				String curProject = projects[i];
				Node curNode = table.get(curProject);
				// Only place a new item in the list once all of its parents have been accounted for
				if (!curNode.placed && curNode.parents.size() == 0)
				{
					// Remove this item from the parents list of all of its children
					for (String curChild : curNode.children)
					{
						table.get(curChild).parents.remove(curProject);
					}
					solution[pos] = curProject;
					curNode.placed = true;
					pos++;
				}					
			}
		
			
			// If no projects were added in this iteration,
			//  that indicates a circular dependency, and
			//  thus no solution exists.
			if (pos == lastPos)
				throw new ArithmeticException("No solution");
		}
		
		return solution;
	}

	public static String[] solve3(String[] projects, String[][] dependencies)
	{
		HashMap<String, Node> table = new HashMap<String,Node>();
		
		for (String curProject : projects)
		{
			Node curNode = new Node();
			table.put(curProject, curNode);
		}
		
		for (String[] curDep : dependencies)
		{
			String first = curDep[0];
			String second = curDep[1];
			Node curNode = table.get(second);
			curNode.parents.add(first);
			curNode = table.get(first);
			curNode.children.add(second);
		}
		
		Queue<String> available = new LinkedList<String>();
		for (String curProject : projects)
		{
			Node curNode = table.get(curProject);
			if (curNode.parents.size() == 0)
				available.add(curProject);
		}
		
		String[] solution = new String[projects.length];
		int pos = 0;
		
		while (available.size() > 0)
		{
			String curProject = available.poll();
			solution[pos] = curProject;
			pos++;
			Node curNode = table.get(curProject);
			for (String curChild : curNode.children)
			{
				Node childNode = table.get(curChild);
				childNode.parents.remove(curProject);
				if (childNode.parents.size() == 0)
					available.add(curChild);
			}
		}
		
		if (pos < projects.length)
			throw new ArithmeticException("No solution");	
			
		return solution;
	}

	public static void printArray(String[] arr)
	{
		for (int i=0; i<arr.length; i++)
		{
			System.out.print(arr[i] + " ");
		}
		System.out.println();
	}
	
	public static void main(String[] args)  
	{
		String[] projects = new String[] {
			"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z","A","B","C","D"
		};
		
		String[][] dependencies = new String[][] {
		{"a","d"},{"f","b"},{"b","d"},{"f","a"},{"d","c"},{"k","a"},{"b","j"},{"j","g"},{"k","i"},{"b","a"},{"i","h"},{"d","m"},{"h","r"},{"g","p"},{"z","x"},{"x","b"},{"g","s"},{"s","k"},{"B","e"},{"b","A"},{"r","q"},{"q","t"},{"B","w"},{"C","f"},{"t","l"},{"h","n"},{"f","w"},{"a","B"},{"q","B"}
		};
		
		long startTime, endTime;
		
		startTime = System.nanoTime();
		String[] solution = solve(projects, dependencies);
		endTime = System.nanoTime();
		printArray(solution);
		System.out.println("Time for solve : " + (endTime-startTime) + "ns");
		
		startTime = System.nanoTime();
		String[] solution2 = solve2(projects, dependencies);
		endTime = System.nanoTime();
		printArray(solution2);
		System.out.println("Time for solve2: " + (endTime-startTime) + "ns");
		
		startTime = System.nanoTime();
		String[] solution3 = solve3(projects, dependencies);
		endTime = System.nanoTime();
		printArray(solution3);
		System.out.println("Time for solve3: " + (endTime-startTime) + "ns");
	}		
	
}
