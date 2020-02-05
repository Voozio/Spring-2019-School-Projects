package edu.csus.csc130.spring2019.assignment2;

/**
 * Modified by: Justin Voo
 *
 */
public class ThreeWayMerge {
	
	/**
	 * Merge three sorted arrays with these ranges [lo..mid1], [mid1+1..mid2], [mid2+1..hi] into one sorted array.
	 * Array a has the original input and final sorted input.  Array aux is the auxiliary array.
	 */
	public static void Merge(Integer[] a, int lo, int mid1, int mid2, int hi, Integer[] aux) {
		int l = lo, m = mid1+1, n = mid2+1; 
		// Copy a[lo..hi] to aux[lo..hi]
		for(int k = lo; k <= hi; k++)
		{
			aux[k] = a[k];
		}
		
		//Merge first two arrays back to a[lo..mid2]
		for(int k = lo; k <= mid2; k++)
		{
			if(l > mid1) {
				a[k] = aux[m++];
			} else if (m > mid2) {
				a[k] = aux[l++];
			} else if (aux[m].compareTo(aux[l]) < 0) {
				a[k] = aux[m++];
			} else {
				a[k] = aux[l++];
			}
		}

		// Reset l
		l = lo;
		// Copy a[lo..mid2] to aux[lo..mid2]
		for(int x = lo; x <= mid2; x++)
		{
			aux[x] = a[x];
		}
		
		// Merge combined array and third array back to a[lo..hi]
		for(int x = lo; x <= hi; x++)
		{
			if(l > mid2) {
				a[x] = aux[n++];
			} else if (n > hi) {
				a[x] = aux[l++];
			} else if (aux[n].compareTo(aux[l]) < 0) {
				a[x] = aux[n++];
			} else {
				a[x] = aux[l++];
			}
		}
	}

}
