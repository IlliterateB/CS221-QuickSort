import java.util.Comparator;

/**
 * Class for sorting lists that implement the IndexedUnsortedList interface,
 * using ordering defined by class of objects in list or a Comparator.
 * As written uses Quicksort algorithm.
 *
 * @author CS221
 */
public class Sort
{	
	/**
	 * Returns a new list that implements the IndexedUnsortedList interface. 
	 * As configured, uses WrappedDLL. Must be changed if using 
	 * your own IUDoubleLinkedList class. 
	 * 
	 * @return a new list that implements the IndexedUnsortedList interface
	 */
	private static <T> IndexedUnsortedList<T> newList() 
	{
		return new IUDoubleLinkedList<T>(); // replaced with IUDoubleLinkedList for extra-credit
	}
	
	/**
	 * Sorts a list that implements the IndexedUnsortedList interface 
	 * using compareTo() method defined by class of objects in list.
	 * DO NOT MODIFY THIS METHOD
	 * 
	 * @param <T>
	 *            The class of elements in the list, must extend Comparable
	 * @param list
	 *            The list to be sorted, implements IndexedUnsortedList interface 
	 * @see IndexedUnsortedList 
	 */
	public static <T extends Comparable<T>> void sort(IndexedUnsortedList<T> list) 
	{
		quicksort(list);
	}

	/**
	 * Sorts a list that implements the IndexedUnsortedList interface 
	 * using given Comparator.
	 * DO NOT MODIFY THIS METHOD
	 * 
	 * @param <T>
	 *            The class of elements in the list
	 * @param list
	 *            The list to be sorted, implements IndexedUnsortedList interface 
	 * @param c
	 *            The Comparator used
	 * @see IndexedUnsortedList 
	 */
	public static <T> void sort(IndexedUnsortedList <T> list, Comparator<T> c) 
	{
		quicksort(list, c);
	}
	
	/**
	 * Quicksort algorithm to sort objects in a list 
	 * that implements the IndexedUnsortedList interface, 
	 * using compareTo() method defined by class of objects in list.
	 * DO NOT MODIFY THIS METHOD SIGNATURE
	 * 
	 * @param <T>
	 *            The class of elements in the list, must extend Comparable
	 * @param list
	 *            The list to be sorted, implements IndexedUnsortedList interface 
	 */
	private static <T extends Comparable<T>> void quicksort(IndexedUnsortedList<T> list)
	{
		
		// seed is if list size < 2
		if (list.size() < 2) {
			return;
		}

		// select pivot element - check 3 Els and choose middle
		// this would likely require many many conditional statements to select the middle value
		T pivot = list.first(); // he said its fine to use any element, even though first isn't the best
		list.removeFirst();
		

		IndexedUnsortedList<T> lt = newList(); // less than pivot
		IndexedUnsortedList<T> gt = newList(); // greater than pivot

		// loops through rest of list to compare to pivot
		while(!list.isEmpty()) {
			T currEl = list.removeFirst();

			if (currEl.compareTo(pivot) < 0) { // compareTo() is negative for less than, == 0 for equal, and > 0 for greater than pivot
				lt.add(currEl);
			}
			else {
				gt.add(currEl);
			}
		}

		// Recursively call quicksort
		quicksort(lt);
		quicksort(gt);


		// merge lists
		while (!lt.isEmpty()) { // merge less than list, which is already sorted itself
			list.add(lt.removeFirst());
		}

		list.add(pivot); // adds pivot back in after the smaller items but before all the larger ones

		while (!gt.isEmpty()) { // same with the greater than
			list.add(gt.removeFirst());
		}
	}
		
	/**
	 * Quicksort algorithm to sort objects in a list 
	 * that implements the IndexedUnsortedList interface,
	 * using the given Comparator.
	 * DO NOT MODIFY THIS METHOD SIGNATURE
	 * 
	 * @param <T>
	 *            The class of elements in the list
	 * @param list
	 *            The list to be sorted, implements IndexedUnsortedList interface 
	 * @param c
	 *            The Comparator used
	 */
	private static <T> void quicksort(IndexedUnsortedList<T> list, Comparator<T> c)
	{
		// This is basically the same, just has Comparator param in the recursive calls, and uses c.compare() rather than compareTo()

		// seed is if list size < 2
		if (list.size() < 2) {
			return;
		}

		// select pivot element e
		T pivot = list.first(); 
		list.removeFirst();
		

		IndexedUnsortedList<T> lt = newList();
		IndexedUnsortedList<T> gt = newList();

		// loop through and compare each El
		while(!list.isEmpty()) {
			T currEl = list.removeFirst();

			if (c.compare(currEl, pivot) < 0) {
				lt.add(currEl);
			}
			else {
				gt.add(currEl);
			}
		}

		// Recursively call quicksort with same comparator as first call, == c
		quicksort(lt, c);
		quicksort(gt, c);

		// merge lists
		while (!lt.isEmpty()) {
			list.add(lt.removeFirst());
		}

		list.add(pivot); // adds pivot back in after the smaller items but before all the larger ones

		while (!gt.isEmpty()) {
			list.add(gt.removeFirst());
		}

	}
	
}
