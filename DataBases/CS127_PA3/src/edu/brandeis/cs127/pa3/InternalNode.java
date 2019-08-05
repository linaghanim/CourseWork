package edu.brandeis.cs127.pa3;

/**
 * Internal Nodes of B+-Trees.
 * 
 * @author cs127b
 */
public class InternalNode extends Node {

	/**
	 * Construct an InternalNode object and initialize it with the parameters.
	 * 
	 * @param d
	 *            degree
	 * @param p0
	 *            the pointer at the left of the key
	 * @param k1
	 *            the key value
	 * @param p1
	 *            the pointer at the right of the key
	 * @param n
	 *            the next node
	 * @param p
	 *            the previous node
	 */
	public InternalNode(int d, Node p0, int k1, Node p1, Node n, Node p) {

		super(d, n, p);
		ptrs[0] = p0;
		keys[1] = k1;
		ptrs[1] = p1;
		lastindex = 1;

		if (p0 != null)
			p0.setParent(new Reference(this, 0, false));
		if (p1 != null)
			p1.setParent(new Reference(this, 1, false));
	}

	/**
	 * The minimal number of keys this node should have.
	 * 
	 * @return the minimal number of keys a leaf node should have.
	 */
	public int minkeys() {
		int min = 0;

		///////////////////
		// ADD CODE HERE //
		///////////////////
		min = (int) Math.ceil((this.degree) / 2.0);
		//
		return min;
	}

	/**
	 * Check if this node can be combined with other into a new node without
	 * splitting. Return TRUE if this node and other can be combined.
	 */
	public boolean combinable(Node other) {

		boolean combinable = true;

		///////////////////
		// ADD CODE HERE //
		///////////////////
		if (this.lastindex + other.lastindex > this.degree) {
			combinable = false;
		}
		//
		return combinable;
	}

	/**
	 * Combines contents of this node and its next sibling (next) into a single
	 * node,
	 */
	public void combine() {

		///////////////////
		// ADD CODE HERE //
		///////////////////
		if (this.combinable(this.getNext())) {
			int i = this.lastindex;
			i++;
			for (int j = 0; j < this.getNext().lastindex; j++) {
				this.keys[i] = (this.getNext()).keys[j];
				this.ptrs[i] = this.getNext().ptrs[j];
				i++;
			}
			if (this.getNext().ptrs[0] != null) {
				this.keys[this.lastindex + 1] = this.getNext().ptrs[0].keys[1];
				this.ptrs[this.lastindex + 1] = this.getNext().ptrs[0];
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
	 * more key and one more pointer. Returns the key that must be inserted into
	 * parent node.
	 * 
	 * @return the value to be inserted to the parent node
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
		this.getNext().ptrs[0] = this.ptrs[splitlocation];
		this.getNext().ptrs[0].parentref = new Reference(this.getNext(), 0, false);
		this.ptrs[splitlocation] = null;
		this.getNext().ptrs[1] = this.getNext().ptrs[0].getNext();
		this.getNext().ptrs[1].parentref = new Reference(this.getNext(), 1, false);

		this.getNext().ptrs[0].setNext(this.getNext().ptrs[1]);
		this.getNext().ptrs[1].setPrev(this.getNext().ptrs[0]);

		for (int i = splitlocation + 1; i < A.length; i++) {
			this.getNext().keys[i - splitlocation + 1] = A[i];
			this.getNext().ptrs[i - splitlocation + 1] = this.ptrs[i];
			this.getNext().ptrs[i - splitlocation + 1].parentref = new Reference(this.getNext(), i - splitlocation + 1,
					false);
			this.ptrs[i] = null;
			this.getNext().lastindex++;
		}

		//
		return key;
	}

	/**
	 * Inserts (val, ptr) pair into this node at keys [i] and ptrs [i]. Called when
	 * this node is not full. Differs from {@link LeafNode} routine in that updates
	 * parent references of all ptrs from index i+1 on.
	 * 
	 * @param val
	 *            the value to insert
	 * @param ptr
	 *            the pointer to insert
	 * @param i
	 *            the position to insert the value and pointer
	 */
	public void insertSimple(int val, Node ptr, int i) {

		////////////////////
		// ADD CODE HERE //
		////////////////////
		if (this.lastindex == 0) {
			this.keys[1] = val;
			this.lastindex++;
			return;
		}
		i++;
		for (int j = i; j <= this.lastindex; j++) {
			this.keys[j + 1] = this.keys[j];
		}
		for (int j = i; j <= this.lastindex; j++) {
			this.ptrs[j + 1] = this.ptrs[j];
		}
		this.keys[i] = val;
		// this.ptrs[i-1]=this.ptrs[i].getPrev();
		this.ptrs[i] = this.ptrs[i - 1].getNext();

		this.lastindex++;
	}

	/**
	 * Deletes keys [i] and ptrs [i] from this node, without performing any
	 * combination or redistribution afterwards. Does so by shifting all keys and
	 * pointers from index i+1 on one position to the left. Differs from
	 * {@link LeafNode} routine in that updates parent references of all ptrs from
	 * index i+1 on.
	 * 
	 * @param i
	 *            the index of the key to delete
	 */
	public void deleteSimple(int i) {

		///////////////////
		// ADD CODE HERE //
		///////////////////
		this.ptrs[i] = null;
		for (int j = i; j < this.lastindex; j++) {
			this.keys[j] = keys[j + 1];
			this.ptrs[j] = this.ptrs[j + 1];
		}
		this.ptrs[this.lastindex] = null;
		this.lastindex--;
	}

	/**
	 * Uses findPtrInex and calles itself recursively until find the value or pind
	 * the position where the value should be.
	 * 
	 * @return the referenene pointing to a leaf node.
	 */
	public Reference search(int val) {
		Reference ref = null;

		///////////////////
		// ADD CODE HERE //
		///////////////////

		int i = this.findPtrIndex(val);

		ref = this.ptrs[i].search(val);
		//
		return ref;
	}

	/**
	 * Insert (val, ptr) into this node. Uses insertSimple, redistribute etc. Insert
	 * into parent recursively if necessary
	 * 
	 * @param val
	 *            the value to insert
	 * @param ptr
	 *            the pointer to insert
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
					this.getPrev().insert(val, this.getPrev());
				}

				if (this.parentref == null)// make root
				{
					InternalNode root = new InternalNode(this.degree, this.getPrev(), key, this, null, null);

				} else {

					this.getPrev().parentref = this.parentref;
					((InternalNode) this.parentref.getNode()).insert(key, this.parentref.getNode());

				}

			} else {
				Node oldnext = this.getNext();

				this.setNext(new InternalNode(this.degree, null, 0, null, null, this));
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

				if (this.parentref == null)// root
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

	public void outputForGraphviz() {

		// The name of a node will be its first key value
		// String name = "I" + String.valueOf(keys[1]);
		// name = BTree.nextNodeName();

		// Now, prepare the label string
		String label = "";
		for (int j = 0; j <= lastindex; j++) {
			if (j > 0)
				label += "|";
			label += "<p" + ptrs[j].myname + ">";
			if (j != lastindex)
				label += "|" + String.valueOf(keys[j + 1]);
			// Write out any link now
			BTree.writeOut(myname + ":p" + ptrs[j].myname + " -> " + ptrs[j].myname + "\n");
			// Tell your child to output itself
			ptrs[j].outputForGraphviz();
		}
		// Write out this node
		BTree.writeOut(myname + " [shape=record, label=\"" + label + "\"];\n");
	}

	/**
	 * Print out the content of this node
	 */
	void printNode() {

		int j;
		System.out.print("[");
		for (j = 0; j <= lastindex; j++) {

			if (j == 0)
				System.out.print(" * ");
			else
				System.out.print(keys[j] + " * ");

			if (j == lastindex)
				System.out.print("]");
		}
	}
}
