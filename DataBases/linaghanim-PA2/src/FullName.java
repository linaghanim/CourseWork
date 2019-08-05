//Name: Lina Ghanim		email: linaghanim@brandeis.edu
public class FullName implements Comparable<FullName> {
	private String firstName;
	private String lastName;

	public FullName(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String toString() {
		return this.lastName + ", " + this.firstName;
	}

	public int compareTo(FullName f) {
		int retval = this.lastName.compareTo(f.lastName);
		if (retval == 0) {
			return this.firstName.compareTo(f.firstName);
		}
		return retval;
	}
}
