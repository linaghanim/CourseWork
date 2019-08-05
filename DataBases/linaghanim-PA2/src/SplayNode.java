//Name: Lina Ghanim		email: linaghanim@brandeis.edu
public class SplayNode<KeyType extends Comparable<KeyType>, DataType> {
	private DataType data;
	private KeyType key;
	private SplayNode<KeyType, DataType> parent;
	private SplayNode<KeyType, DataType> leftChild;
	private SplayNode<KeyType, DataType> rightChild;

	public SplayNode(KeyType key, DataType data) {
		this.data = data;
		this.key = key;
		this.leftChild = null;
		this.rightChild = null;
		this.parent = null;

	}

	public DataType getData() {
		// todo
		return this.data;
	}

	/**
	 * 
	 * @param v=the
	 *            root of the tree
	 * @param value=the
	 *            value searched for
	 * @return the node that has the same value. IF there is no value, returns last
	 *         node searched for O(log(n))
	 */
	public SplayNode<KeyType, DataType> bstSearch(SplayNode<KeyType, DataType> v, KeyType value) {
		if (v == null || v.key.compareTo(value) == 0) {
			return v;
		}
		if (v.key.compareTo(value) < 0) {
			if (v.leftChild == null)// returns the last node searched for
				return v;
			return bstSearch(v.leftChild, value);
		} else {
			if (v.rightChild == null)// returns the last node searched for
				return v;
			return bstSearch(v.rightChild, value);
		}
	}

	/**
	 * 
	 * @param value=
	 *            the value searched for
	 * @return the node with the value splayed to the top O(n)
	 */
	public SplayNode<KeyType, DataType> search(KeyType value) {
		// todo
		SplayNode<KeyType, DataType> s = bstSearch(this, value);
		if (s != null) {
			s.splay();// splays the node searched for
		}
		return s;
	}

	/**
	 * 
	 * @param key=
	 *            value searched for
	 * @return true if the tree contains the value O(log(n))
	 */
	public boolean contains(KeyType key) {
		// todo
		SplayNode<KeyType, DataType> s = bstSearch(this, key);// search for the node
		if (s != null) {
			if (s.key.equals(key)) {
				return true;
			} else {
				return false;
			}
		} else
			return false;
	}

	/**
	 * 
	 * @param newNode=
	 *            the node that has to be inserted
	 * @return the newnode inserted and splayed O(n)
	 */
	public SplayNode<KeyType, DataType> insert(SplayNode<KeyType, DataType> newNode) {
		// todo
		SplayNode<KeyType, DataType> v = null;
		SplayNode<KeyType, DataType> w = this;
		while (w != null) {
			v = w;
			if (w.key.compareTo(newNode.key) <= 0)// w last name after newNode last name
			{
				w = w.leftChild;
			} else {
				w = w.rightChild;
			}
		}
		newNode.parent = v;
		if (v != null) {
			if (v.key.compareTo(newNode.key) <= 0)// v last name after newNode last name
			{
				v.leftChild = newNode;
			} else {
				v.rightChild = newNode;
			}
		}
		newNode.splay();
		return newNode;
	}

	/**
	 * 
	 * @param oldVal=the
	 *            value that will be deleted
	 * @return the root of the new tree without the oldvalue O(n)
	 */
	public SplayNode<KeyType, DataType> delete(KeyType oldVal) {
		// todo
		SplayNode<KeyType, DataType> s = bstSearch(this, oldVal);// search for the node
		s.splay();// splays the node
		if (s == null)
			return null;
		if (s.leftChild == null && s.rightChild == null)
			return null;
		if (s.leftChild == null) {// no left tree
			s.rightChild.parent = null;
			return s.rightChild;
		}
		if (s.rightChild == null) {// no right tree
			s.leftChild.parent = null;
			return s.leftChild;
		}
		SplayNode<KeyType, DataType> righttree = s.rightChild;
		righttree.parent = null;
		s.rightChild = null;

		SplayNode<KeyType, DataType> lefttree = s.leftChild;
		lefttree.parent = null;
		s.leftChild = null;

		SplayNode<KeyType, DataType> max = findMax(lefttree);// max of the left subtree
		max.splay();
		max.rightChild = righttree;
		righttree.parent = max;
		return max;
	}

	/**
	 * 
	 * @param v=
	 *            the root of the tree
	 * @return the max value in the tree O(n)
	 */
	public SplayNode<KeyType, DataType> findMax(SplayNode<KeyType, DataType> v) {
		while (v.rightChild != null) {// most right
			v = v.rightChild;
		}
		return v;
	}

	/**
	 * O(n)
	 */
	private void splay() {
		// todo
		SplayNode<KeyType, DataType> grandparent;
		SplayNode<KeyType, DataType> v = this;
		while (v.parent != null) {
			if (this.parent.parent == null)// child of the root - zig
				rotate(this);
			else {
				grandparent = this.parent.parent;

				if (v.parent == grandparent.rightChild) {
					if (v == v.parent.leftChild) {// left of right - zig zag
						rotate(v);
						rotate(v);
					} else {// right of right - zig zig
						rotate(v.parent);
						rotate(v);
					}

				} else {
					if (v == v.parent.rightChild) {// right of left - zig zag
						rotate(v);
						rotate(v);
					} else {// left of left - zig zig
						rotate(v.parent);
						rotate(v);
					}
				}

			}
		}
	}

	/**
	 * 
	 * @param v=node
	 *            that will be rotated O(1)
	 */
	public void rotate(SplayNode<KeyType, DataType> v) {
		if (v == v.parent.rightChild) {
			leftRotation(v.parent);
		} else {
			rightRotation(v.parent);
		}
	}

	/**
	 * 
	 * @param v=node
	 *            that will be rotated to the right O(1)
	 */
	public void rightRotation(SplayNode<KeyType, DataType> v) {
		SplayNode<KeyType, DataType> y = v.leftChild;
		v.leftChild = y.rightChild;
		if (y.rightChild != null) {
			y.rightChild.parent = v;
		}
		y.parent = v.parent;
		if (v.parent != null) {
			if (v == v.parent.rightChild) {
				v.parent.rightChild = y;
			} else {
				v.parent.leftChild = y;
			}
		}
		y.rightChild = v;
		v.parent = y;
	}

	/**
	 * 
	 * @param v=node
	 *            that will be rotated to the left O(1)
	 */
	public void leftRotation(SplayNode<KeyType, DataType> v) {
		SplayNode<KeyType, DataType> y = v.rightChild;
		v.rightChild = y.leftChild;
		if (y.leftChild != null) {
			y.leftChild.parent = v;
		}
		y.parent = v.parent;
		if (v.parent != null) {
			if (v == v.parent.leftChild) {
				v.parent.leftChild = y;
			} else {
				v.parent.rightChild = y;
			}
		}
		y.leftChild = v;
		v.parent = y;
	}

	/**
	 * O(n)
	 */
	public String toString() {
		// todo

		return inOrder1(this);
	}

	public String inOrder1(SplayNode<KeyType, DataType> t) {
		if (t == null)
			return "";
		return inOrder1(t.leftChild) + "\n" + t.key + " : " + t.data + "\n" + inOrder1(t.rightChild);

	}

	/**
	 * 
	 * @return a string of the tree in a specific format O(n)
	 */
	public String treeString() {
		// todo
		return inOrder2(this);
	}

	public String inOrder2(SplayNode<KeyType, DataType> t) {
		if (t == null)
			return "";
		return "(" + inOrder2(t.leftChild) + t.key + inOrder2(t.rightChild) + ")";

	}

}
