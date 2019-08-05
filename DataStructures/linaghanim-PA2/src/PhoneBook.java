//Name: Lina Ghanim		email: linaghanim@brandeis.edu
public class PhoneBook {
	private SplayNode<FullName, PhoneNumber> root;

	public PhoneBook() {
	}

	/**
	 * 
	 * @param name=
	 *            the name of the person inserted
	 * @param num=the
	 *            number of the person inserted O(n)
	 */
	public void insert(FullName name, PhoneNumber num) {
		// todo
		SplayNode<FullName, PhoneNumber> newNode = new SplayNode<FullName, PhoneNumber>(name, num);
		if (this.root == null) {
			this.root = newNode;
		} else {
			SplayNode<FullName, PhoneNumber> r = this.root.insert(newNode);
			this.root = r;
		}
	}

	/**
	 * 
	 * @param name=the
	 *            name of the person to delete O(n)
	 */
	public void delete(FullName name) {
		// todo
		this.root = root.delete(name);
	}

	/**
	 * 
	 * @param name=the
	 *            name of the person searched for
	 * @return the number of the person searched for
	 */
	public PhoneNumber search(FullName name) {
		// todo
		SplayNode<FullName, PhoneNumber> s = root.search(name);
		if (s == null)
			return null;
		this.root = s;
		return s.getData();
	}

	public String toString() {
		// todo
		return root.treeString();
	}
}
