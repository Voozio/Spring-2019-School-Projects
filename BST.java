package edu.csus.csc130.spring2019.assignment3;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;
/**
 * Modified by: Justin Voo
 *
 * 
 * Ordered Symbol Table implementation using Binary Search Tree
 */
public class BST<Key extends Comparable<Key>, Value> extends AbstractOrderedSymbolTable<Key, Value> {
	public class Node {
		Key key; // key
		Value val; // associated value
		Node left, right; // links to subtrees
		int n; // # nodes in subtree rooted here

		public Node(Key key, Value val, int n) {
			this.key = key;
			this.val = val;
			this.n = n;
		}
	}

	protected Node root; // root of BST

	// get the number of nodes from this tree
	public int size() {
		return size(root);
	}

	// get the number of nodes rooted at Node node
	private int size(Node node) {
		return (node == null ? 0 : node.n);
	}
	
	public Value get(Key key) {
		return get(root, key);
	}

	/**
	 * if the tree is empty, we have a search miss and return null; if the
	 * search key is equal to the key at the node, we have a search hit and
	 * return the value; Otherwise, we search in the appropriate subtree, moving
	 * left if the search key is smaller, right if it is larger.
	 */
	private Value get(Node node, Key key) {
		if (node == null)
			return null;

		int cmp = key.compareTo(node.key);
		if (cmp < 0)
			return get(node.left, key);
		else if (cmp > 0)
			return get(node.right, key);
		else
			return node.val;
	}

	public void put(Key key, Value val) {
		root = put(root, key, val);
	}

	/**
	 * 1) if the tree is empty, return a new node containing the key and value;
	 * 2) otherwise a) if the search key is equal to the key at the node, update
	 * the node with new value; b) if the search key is less than the key at the
	 * node, set the left link to the result of inserting the key into the left
	 * subtree; c) otherwise, set the right link to the result of inserting the
	 * key into the right subtree. update node.n value and return the node
	 */
	public Node put(Node node, Key key, Value val) {
		if (node == null)
			return new Node(key, val, 1);

		int cmp = key.compareTo(node.key);
		if (cmp < 0)
			node.left = put(node.left, key, val);
		else if (cmp > 0)
			node.right = put(node.right, key, val);
		else
			node.val = val;
		node.n = size(node.left) + size(node.right) + 1;

		return node;
	}

	@Override
	public Key min() {
		if (root == null) {
			return null;
		} else {
			return min(root).key;
		}
	}

	private Node min(Node node) {
		if (node.left == null) {
			return node;
		} else {
			return min(node.left);
		}
	}

	@Override
	public Key max() {
		if (root == null) {
			return null;
		} else {
			return max(root).key;
		}
	}

	private Node max(Node node) {
		if (node.right == null) {
			return node;
		} else {
			return max(node.right);
		}
	}

	@Override
	// find the greatest key less or equal to the given key
	public Key floor(Key key) {
		Node node = floor(root, key);
		if (node == null) {
			return null;
		} else {
			return node.key;
		}
	}

	private Node floor(Node node, Key key) {
		if (node == null)
			return null;

		int cmp = key.compareTo(node.key);
		if (cmp == 0) {
			return node;
		} else if (cmp < 0) {
			return floor(node.left, key);
		} else {
			Node t = floor(node.right, key);
			if (t != null)
				return t;
			else
				return node;
		}
	}

	@Override
	public Key ceiling(Key key) {
		if (key == null)
			throw new NullPointerException("argument to ceiling() is null");
		if (isEmpty())
			throw new NoSuchElementException("called ceiling() with empty symbol table");
		Node x = ceiling(root, key);
		if (x == null)
			return null;
		else
			return x.key;
	}

	private Node ceiling(Node x, Key key) {
		if (x == null)
			return null;
		int cmp = key.compareTo(x.key);
		if (cmp == 0)
			return x;
		if (cmp < 0) {
			Node t = ceiling(x.left, key);
			if (t != null)
				return t;
			else
				return x;
		}
		return ceiling(x.right, key);
	}

	@Override
	// return the kth (zero based) key
	public Key select(int k) {
		Node node = select(root, k);
		if (node == null) {
			return null;
		} else {
			return node.key;
		}
	}

	/**
	 * Returns Node containing kth (zero based) key Let t be the number of nodes
	 * in the left subtree of node If t > k, look for the node of rank k in the
	 * left subtree if t < k, look for the node of rank k-t-1 in the right
	 * subtree if t = k, return the node
	 */
	private Node select(Node node, int k) {
		if (node == null)
			return null;

		int t = size(node.left);
		if (t > k)
			return select(node.left, k);
		else if (t < k)
			return select(node.right, k - t - 1);
		else
			return node;
	}

	@Override
	public Iterable<Key> keys(Key lo, Key hi) {
		Queue<Key> queue = new LinkedList<Key>();
		keys(root, queue, lo, hi);
		return queue;
	}

	private void keys(Node node, Queue<Key> queue, Key lo, Key hi) {
		if (node == null)
			return;

		int cmplo = lo.compareTo(node.key);
		int cmphi = hi.compareTo(node.key);
		if (cmplo < 0)
			// if lo < node.key, visit left subtree
			keys(node.left, queue, lo, hi);
		if (cmplo <= 0 && cmphi >= 0)
			// if node.key within range, add to the queue
			queue.add(node.key);
		if (cmphi > 0)
			// if hi > node.key, visit right subtree
			keys(node.right, queue, lo, hi);
	}

	@Override
	public void delete(Key key) {
		root = delete(root, key);
	}

	private Node delete(Node node, Key key) {
		if (node == null)
			return null;

		int cmp = key.compareTo(node.key);
		if (cmp < 0) {
			node.left = delete(node.left, key);
		} else if (cmp > 0) {
			node.right = delete(node.right, key);
		} else {
			if (node.right == null) {
				return node.left;
			} else if (node.left == null) {
				return node.right;
			} else {
				Node curNode = node;
				// curNode's successor
				node = min(curNode.right);
				node.right = deleteMin(curNode.right);
				node.left = curNode.left;
			}
		}
		node.n = size(node.left) + size(node.right) + 1;
		return node;
	}

	public void deleteMin() {
		if (root != null) {
			root = deleteMin(root);
		}
	}

	/**
	 * @param node root of the sub-tree
	 * @return root of the sub-tree with node of minimum key removed
	 */
	public Node deleteMin(Node node) {
		if (node.left == null)
			return node.right;
		node.left = deleteMin(node.left);
		node.n = size(node.left) + size(node.right) + 1;
		return node;
	}

	public void deleteMax() {
		if (root != null) {
			root = deleteMax(root);
		}
	}

	private Node deleteMax(Node node) {
		if (node.right == null) return node.left;
		node.right = deleteMax(node.right);
		node.n = size(node.left) + size(node.right) + 1;
		return node;
	}

	public void printInorder() {
		printInorder(root);
	}

	private void printInorder(Node node) {
		if (node == null) return;
		printInorder(node.left);
		System.out.format("(key=%s, n=%d), ", node.key, node.n);
		//System.out.print(node.key + ", ");
		printInorder(node.right);
	}

	public void printPreorder() {
		printPreorder(root);
	}

	private void printPreorder(Node node) {
		if (node == null) return;
		//System.out.print(node.key + ", ");
		System.out.format("(key=%s, n=%d), ", node.key, node.n);
		printPreorder(node.left);
		printPreorder(node.right);
	}

	public void printPostorder() {
		printPostorder(root);
	}

	private void printPostorder(Node node) {
		if (node == null) return;
		printPostorder(node.left);
		printPostorder(node.right);
		System.out.print(node.key + ", ");
	}

	@Override
	public int rank(Key key) {
		// Not implemented yet
		return -1;
	}
	
	/**
	 * Return key associated with predecessor
	 */
	public Key getPredecessor(Key key) {
		Node prevNode = root;
		Node node = root;

		while(node != null)
		{
			int cmp = key.compareTo(node.key);
			if (cmp < 0) {
				prevNode = node;
				node = node.left;
			}
			else if (cmp > 0) {
				prevNode = node;
				node = node.right;
			}
			else 
				return prevNode.key;
		}
		return null;
	}
}
