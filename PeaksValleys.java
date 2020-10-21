import java.util.*;
import java.util.Random;
import java.util.concurrent.TimeUnit;

class PeaksValleys
{ 

	public static final int[] SIZES = {10,100,1000,10000};
	public static int ITERATIONS = 100;
	public static int INT_MINIMUM = -100;
	public static int INT_MAXIMUM = 100;

	public static void sort(int[] arr)
	{
		for (int i=0; i<arr.length; i++)
		{
			int idx;
			if (i%2==0)
				idx = findMax(arr, i);
			else
				idx = findMin(arr, i);
			int temp = arr[i];
			arr[i] = arr[idx];
			arr[idx] = temp;
		}
	}
	
	public static int findMax(int[] arr, int start)
	{
		int maxVal = arr[start];
		int maxIdx = start;
		for (int i = start+1; i < arr.length; i++)
		{
			if (arr[i] > maxIdx)
			{
				maxIdx = i;
				maxVal = arr[i];
			}
		}
		return maxIdx;
	}
	
	public static int findMin(int[] arr, int start)
	{
		int minVal = arr[start];
		int minIdx = start;
		for (int i = start+1; i < arr.length; i++)
		{
			if (arr[i] > minIdx)
			{
				minIdx = i;
				minVal = arr[i];
			}
		}
		return minIdx;
	}

	public static void sort2(int[] arr)
	{
		Arrays.sort(arr);
		for (int i=0; i<arr.length-1; i++)
		{
			if ( (i%2==0 && arr[i] < arr[i+1]) || (i%2==1 && arr[i] > arr[i+1]) )
			{
				int temp = arr[i];
				arr[i] = arr[i+1];
				arr[i+1] = temp;
			}
		}
	}

	public static void printArray(int[] arr)
	{
		for (int i=0; i<arr.length; i++)
		{
			System.out.print(arr[i] + " ");
		}
		System.out.println();
	}

    public static void main(String[] args)  
    { 
		Random rand = new Random();
		for (int i=0; i<SIZES.length; i++)
		{
			int curSize = SIZES[i];
			System.out.println("Now solving for input size n = " + curSize);
			long totalTime_sort1 = 0;
			long totalTime_sort2 = 0;
			for (int j=0; j<ITERATIONS; j++)
			{
				int[] arr1 = new int[curSize];
				int[] arr2 = new int[curSize];
				for (int k=0; k<curSize; k++)
				{
					arr1[k] = rand.nextInt(201) - 100;
					arr2[k] = arr1[k];
				}

				long startTime, endTime;
				
				startTime = System.nanoTime();
				sort(arr1);
				endTime = System.nanoTime();
				totalTime_sort1 += (endTime - startTime);
				
				startTime = System.nanoTime();
				sort2(arr2);
				endTime = System.nanoTime();
				totalTime_sort2 += (endTime - startTime);	
			}
			
			System.out.println("Sort1: " + (totalTime_sort1/ITERATIONS) + "ns");
			System.out.println("Sort2: " + (totalTime_sort2/ITERATIONS) + "ns");
		}
    } 
	
} 