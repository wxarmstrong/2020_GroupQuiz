import java.util.*;

class SequentialOrdering
{
	public static class Node {
		
		boolean placed = false;
		Set<String> parents;
		
		public Node()
		{
			parents = new HashSet<String>();
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
			
			// If no new nodes were added in this iteration,
			//  that indicates a circular dependency, and
			//  thus no solution exists.
			if (pos == lastPos)
				throw new ArithmeticException("No solution");
		}
		
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
			"a","b","c","d","e","f"
		};
		
		String[][] dependencies = new String[][] {
			{"a","d"},{"f","b"},{"b","d"},{"f","a"},{"d","c"}
		};
		
		String[] solution = solve(projects, dependencies);
		printArray(solution);
	}		
	
}