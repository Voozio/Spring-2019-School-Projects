package edu.csus.csc130.spring2019.assignment4;
/**
 * Modified by: Justin Voo
 *
 * 
 * Double Hashing implementation
 */

public class DoubleHashingST<Key extends Comparable<Key>, Value> implements SymbolTable<Key, Value>{
	private int n; // number of key-value pairs in the table
	private int m = 16; // size of linear-probing table
	private Key[] keys; // the keys
	private Value[] vals; // the values

	public DoubleHashingST() {
		this(16);
	}
	
	@SuppressWarnings("unchecked")
	public DoubleHashingST(int capacity) {
		m = capacity;
		keys = (Key[]) new Comparable[m];
		vals = (Value[]) new Object[m];
	}	
	
	public Key getKeyByIndex(int i) {
		return keys[i];
	}

	private int h1(Key key) {
		return (key.hashCode() & 0x7fffffff);
	}
	
	private int h2(Key key) {
		return 1 + (key.hashCode() & 0x7fffffff) % (m-2);
	}
	
	/*
	 * Return (h1(key) + i*h2(key)) mod m
	 */
	private int hash(Key key, int i) {
		return (h1(key) + i*h2(key)) % m;
	}
	
	/* 
	 * Return the value associated with the key
	 */
	public Value get(Key key) {
		Value value = null;
		for(int x = 0; x < m; x++){
			if(keys[x] == key) {
				value = vals[x];
			}
		}
		return value;
	}
	
	/* 
	 * Insert key value pairs into the symbol table without resizing
	 */
	public void put(Key key, Value val) {
		for(int x = 0; x < m; x++) {
			if(x == hash(key, m)) {
				keys[x] = key;
				vals[x] = val;
			}
		}
		n++;
	}
	
	/* 
	 * Delete key value pairs from the symbol table without resizing
	 */
	public void delete(Key key) {
		for(int x = 0; x < m; x++) {
			if(x == hash(key, m)) {
				keys[x] = null;
				vals[x] = null;
			}
		}
		n--;
	}
	
	public boolean contains(Key key) {
		return (get(key) != null);
	}

	@Override
	public boolean isEmpty() {
		return n==0;
	}

	@Override
	public int size() {
		return n;
	}

	@Override
	public Iterable<Key> keys() {
		// unimplemented
		return null;
	}
	
}
