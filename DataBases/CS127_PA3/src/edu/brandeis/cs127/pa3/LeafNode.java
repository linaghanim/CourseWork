package edu.brandeis.cs127.pa3;

/**
 * LeafNodes of B+ trees
 */
public class LeafNode extends Node {

	/**
	 * Construct a LeafNode object and initialize it with the parameters.
	 * 
	 * @param d
	 *            the degree of the leafnode
	 * @param k
	 *            the first key value of the node
	 * @param n
	 *            the next node
	 * @param p
	 *            the previous node
	 */
	public LeafNode(int d, int k, Node n, Node p) {
		super(d, n, p);
		keys[1] = k;
		lastindex = 1;
	}

	public void outputForGraphviz() {

		// The name of a node will be its first key value
		// String name = "L" + String.valueOf(keys[1]);
		// name = BTree.nextNodeName();

		// Now, prepare the label string
		String label = "";
		for (int j = 0; j < lastindex; j++) {
			if (j > 0)
				label += "|";
			label += String.valueOf(keys[j + 1]);
		}
		// Write out this node
		BTree.writeOut(myname + " [shape=record, label=\"" + label + "\"];\n");
	}

	/**
	 * the minimum number of keys the leafnode should have.
	 */
	public int minkeys() {
		int min = 0;

		///////////////////
		// ADD CODE HERE //
		///////////////////
		min = (int) Math.ceil((this.degree - 1) / 2.0);
		//
		return min;
	}

	/**
	 * Check if this node can be combined with other into a new node without
	 * splitting. Return TRUE if this node and other can be combined.
	 * 
	 * @return true if this node can be combined with other; otherwise false.
	 */
	public boolean combinable(Node other) {
		// ADD CODE HERE
		if (this.lastindex + other.lastindex > this.degree - 1 || this.lastindex + other.lastindex < this.minkeys()) {
			return false;
		}
		//
		return true;
	}

	/**
	 * Combines contents of this node and its next sibling (nextsib) into a single
	 * node
	 */
	public void combine() {

		///////////////////
		// ADD CODE HERE //
		///////////////////
		if (this.combinable(this.getNext())) {
			int i = this.lastindex;
			i++;
			for (int j = 1; j <= this.getNext().lastindex; j++) {
				this.keys[i] = (this.getNext()).keys[j];
				i++;
				this.lastindex++;
			}
			if (this.getNext().getNext() != null) {
				this.getNext().getNext().setPrev(this);
			}
			this.setNext(this.getNext().getNext());

		}

		//
	}

	/**
	 * Redistributes keys and pointers in this node and its next sibling so that
	 * they have the same number of keys and pointers, or so that this node has one
	 * more key and one more pointer,.
	 * 
	 * @return int Returns key that must be inserted into parent node.
	 */
	public int redistribute() {
		int key = 0;

		///////////////////
		// ADD CODE HERE //
		///////////////////

		int[] A = new int[this.lastindex + this.getNext().lastindex + 1];
		for (int i = 0; i <= this.lastindex; i++) {
			A[i] = this.keys[i];
		}
		for (int i = 1; i <= this.next.lastindex; i++) {
			A[i + this.lastindex] = this.getNext().keys[i];
		}

		int splitlocation = (this.lastindex + this.getNext().lastindex + 1) / 2 + 1;
		if (this.lastindex == 0) {
			splitlocation--;
		}
		key = A[splitlocation];
		this.lastindex = -1;
		this.getNext().lastindex = 0;
		for (int i = 0; i < splitlocation; i++) {
			this.keys[i] = A[i];
			this.lastindex++;
		}
		for (int i = splitlocation; i < A.length; i++) {
			this.getNext().keys[i - splitlocation + 1] = A[i];
			this.getNext().lastindex++;
		}

		//
		return key;
	}

	/**
	 * Insert val into this node at keys [i]. (Ignores ptr) Called when this node is
	 * not full.
	 * 
	 * @param val
	 *            the value to insert to current node
	 * @param ptr
	 *            not used now, use null when call this method
	 * @param i
	 *            the index where this value should be
	 */
	public void insertSimple(int val, Node ptr, int i) {

		///////////////////
		// ADD CODE HERE //
		///////////////////
		i++;
		for (int j = i; j <= this.lastindex; j++) {
			this.keys[j + 1] = this.keys[j];
		}
		this.keys[i] = val;
		this.lastindex++;
		//
	}

	/**
	 * Deletes keys [i] and ptrs [i] from this node, without performing any
	 * combination or redistribution afterwards. Does so by shifting all keys from
	 * index i+1 on one position to the left.
	 */
	public void deleteSimple(int i) {

		///////////////////
		// ADD CODE HERE //
		///////////////////
		this.ptrs[i] = null;
		for (int j = i; j < this.lastindex; j++) {
			this.keys[j] = keys[j + 1];
		}
		this.lastindex--;
	}

	/**
	 * Uses findKeyIndex, and if val is found, returns the reference with match set
	 * to true, otherwise returns the reference with match set to false.
	 * 
	 * @return a Reference object referring to this node.
	 */
	public Reference search(int val) {
		Reference ref = null;

		///////////////////
		// ADD CODE HERE //
		///////////////////
		int k = this.findKeyIndex(val);
		if (this.keys[k] == val)
			ref = new Reference(this, k, true);
		else
			ref = new Reference(this, k, false);

		//
		return ref;
	}

	/**
	 * Insert val into this, creating split and recursive insert into parent if
	 * necessary Note that ptr is ignored.
	 * 
	 * @param val
	 *            the value to insert
	 * @param ptr
	 *            (not used now, use null when calling this method)
	 */
	public void insert(int val, Node ptr) {

		///////////////////
		// ADD CODE HERE //
		///////////////////

		if (this.lastindex < this.degree - 1) {
			this.insertSimple(val, ptr, this.findKeyIndex(val));
		} else {

			if (val < this.keys[1]) {
				Node oldprev = this.getPrev();
				this.setPrev(new LeafNode(this.degree, 0, this, null));
				this.getPrev().lastindex = 0;
				this.getPrev().setPrev(oldprev);
				if (oldprev != null) {
					oldprev.setNext(this.getPrev());
				}

				int key = this.getPrev().redistribute();

				if (val >= key) {
					this.insert(val, ptr);
				} else {
					// this.getPrev().lastindex=0;
					this.getPrev().insert(val, ptr);
				}

				if (this.parentref == null)// make root
				{
					InternalNode root = new InternalNode(this.degree, this.getPrev(), key, this, null, null);

				} else {

					this.getPrev().parentref = this.parentref;
					this.parentref.getNode().ptrs[this.parentref.getNode().findKeyIndex(key)] = this.getPrev();
					((InternalNode) this.parentref.getNode()).insert(key, this.parentref.getNode());

				}
			}

			else {
				Node oldnext = this.getNext();
				this.setNext(new LeafNode(this.degree, 0, null, this));
				this.getNext().lastindex = 0;
				this.getNext().setNext(oldnext);
				if (oldnext != null) {
					oldnext.setPrev(this.getNext());
				}

				int key = this.redistribute();
				if (val >= key) {
					this.getNext().insert(val, ptr);
				} else {
					this.insert(val, ptr);
				}

				if (this.parentref == null)// make root
				{
					InternalNode root = new InternalNode(this.degree, this, key, this.getNext(), null, null);

				} else {

					this.getNext().parentref = this.parentref;
					((InternalNode) this.parentref.getNode()).insert(key, this.parentref.getNode());

				}

			}
		}
		//
	}

	/**
	 * Print to stdout the content of this node
	 */
	void printNode() {
		System.out.print("[");
		for (int i = 1; i < lastindex; i++)
			System.out.print(keys[i] + " ");
		System.out.print(keys[lastindex] + "]");
	}
}
