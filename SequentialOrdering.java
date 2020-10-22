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
			"a","b","c","d","e","f","g","h","i","j","k"
		};
		
		String[][] dependencies = new String[][] {
			{"a","d"},{"f","b"},{"b","d"},{"f","a"},{"d","c"},{"k","a"},{"b","j"},{"j","g"},{"k","i"},{"b","a"},{"i","h"}
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
		System.out.println("Time for solve2: " + (endTime-startTime) + "ns");
	}		
	
}