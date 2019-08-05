
//Name: Lina Ghanim		email: linaghanim@brandeis.edu
import static org.junit.Assert.*;

import org.junit.Test;

public class PhoneBookTest {

	@Test
	public void insertTest() {
		// todo
		PhoneBook test = new PhoneBook();
		// create a tree
		// insert
		// assertequals(t.treestring(),"(what it should look like)")
		test.insert(new FullName("Charlie", "Smith"), new PhoneNumber("5555555555"));
		assertEquals(test.toString(), "(Smith, Charlie)");
		test.insert(new FullName("Abigail", "Smith"), new PhoneNumber("5555555556"));
		assertEquals(test.toString(), "((Smith, Charlie)Smith, Abigail)");
		test.insert(new FullName("Gordon", "Smith"), new PhoneNumber("5555555557"));
		assertEquals(test.toString(), "(Smith, Gordon(Smith, Charlie(Smith, Abigail)))");
		test.insert(new FullName("Dana", "Smith"), new PhoneNumber("5555555558"));
		assertEquals(test.toString(), "((Smith, Gordon)Smith, Dana(Smith, Charlie(Smith, Abigail)))");
		test.insert(new FullName("Bob", "Smith"), new PhoneNumber("5555555559"));
		assertEquals(test.toString(), "(((Smith, Gordon)Smith, Dana(Smith, Charlie))Smith, Bob(Smith, Abigail))");
		test.insert(new FullName("Frankie", "Smith"), new PhoneNumber("5555555560"));
		assertEquals(test.toString(),
				"((Smith, Gordon)Smith, Frankie((Smith, Dana(Smith, Charlie))Smith, Bob(Smith, Abigail)))");
		test.insert(new FullName("Harold", "Smith"), new PhoneNumber("5555555561"));
		assertEquals(test.toString(),
				"(Smith, Harold(Smith, Gordon(Smith, Frankie((Smith, Dana(Smith, Charlie))Smith, Bob(Smith, Abigail)))))");
		test.insert(new FullName("Engelbert", "Smith"), new PhoneNumber("5555555562"));
		assertEquals(test.toString(),
				"((Smith, Harold((Smith, Gordon)Smith, Frankie))Smith, Engelbert(Smith, Dana((Smith, Charlie)Smith, Bob(Smith, Abigail))))");

	}

	@Test
	public void deleteTest() {
		// todo
		PhoneBook test = new PhoneBook();
		// create a tree
		// insert
		// assertequals(t.treestring(),"(what it should look like)")
		test.insert(new FullName("Charlie", "Smith"), new PhoneNumber("5555555555"));
		assertEquals(test.toString(), "(Smith, Charlie)");
		test.insert(new FullName("Abigail", "Smith"), new PhoneNumber("5555555556"));
		assertEquals(test.toString(), "((Smith, Charlie)Smith, Abigail)");
		test.insert(new FullName("Gordon", "Smith"), new PhoneNumber("5555555557"));
		assertEquals(test.toString(), "(Smith, Gordon(Smith, Charlie(Smith, Abigail)))");
		test.insert(new FullName("Dana", "Smith"), new PhoneNumber("5555555558"));
		assertEquals(test.toString(), "((Smith, Gordon)Smith, Dana(Smith, Charlie(Smith, Abigail)))");
		test.insert(new FullName("Bob", "Smith"), new PhoneNumber("5555555559"));
		assertEquals(test.toString(), "(((Smith, Gordon)Smith, Dana(Smith, Charlie))Smith, Bob(Smith, Abigail))");
		test.insert(new FullName("Frankie", "Smith"), new PhoneNumber("5555555560"));
		assertEquals(test.toString(),
				"((Smith, Gordon)Smith, Frankie((Smith, Dana(Smith, Charlie))Smith, Bob(Smith, Abigail)))");
		test.insert(new FullName("Harold", "Smith"), new PhoneNumber("5555555561"));
		assertEquals(test.toString(),
				"(Smith, Harold(Smith, Gordon(Smith, Frankie((Smith, Dana(Smith, Charlie))Smith, Bob(Smith, Abigail)))))");
		test.insert(new FullName("Engelbert", "Smith"), new PhoneNumber("5555555562"));
		assertEquals(test.toString(),
				"((Smith, Harold((Smith, Gordon)Smith, Frankie))Smith, Engelbert(Smith, Dana((Smith, Charlie)Smith, Bob(Smith, Abigail))))");
		// search
		PhoneNumber p = test.search(new FullName("Abigail", "Smith"));
		assertEquals(test.toString(),
				"(((Smith, Harold((Smith, Gordon)Smith, Frankie))Smith, Engelbert((Smith, Dana(Smith, Charlie))Smith, Bob))Smith, Abigail)");
		boolean phone = p.equals(new PhoneNumber("5555555556"));
		assertEquals(phone, true);
		// delete
		test.delete(new FullName("Frankie", "Smith"));
		assertEquals(test.toString(),
				"((Smith, Harold)Smith, Gordon((Smith, Engelbert((Smith, Dana(Smith, Charlie))Smith, Bob))Smith, Abigail))");

	}

	@Test
	public void searchTest() {
		// todo
		PhoneBook test = new PhoneBook();
		// create a tree
		// insert
		// assertequals(t.treestring(),"(what it should look like)")
		test.insert(new FullName("Charlie", "Smith"), new PhoneNumber("5555555555"));
		assertEquals(test.toString(), "(Smith, Charlie)");
		test.insert(new FullName("Abigail", "Smith"), new PhoneNumber("5555555556"));
		assertEquals(test.toString(), "((Smith, Charlie)Smith, Abigail)");
		test.insert(new FullName("Gordon", "Smith"), new PhoneNumber("5555555557"));
		assertEquals(test.toString(), "(Smith, Gordon(Smith, Charlie(Smith, Abigail)))");
		test.insert(new FullName("Dana", "Smith"), new PhoneNumber("5555555558"));
		assertEquals(test.toString(), "((Smith, Gordon)Smith, Dana(Smith, Charlie(Smith, Abigail)))");
		test.insert(new FullName("Bob", "Smith"), new PhoneNumber("5555555559"));
		assertEquals(test.toString(), "(((Smith, Gordon)Smith, Dana(Smith, Charlie))Smith, Bob(Smith, Abigail))");
		test.insert(new FullName("Frankie", "Smith"), new PhoneNumber("5555555560"));
		assertEquals(test.toString(),
				"((Smith, Gordon)Smith, Frankie((Smith, Dana(Smith, Charlie))Smith, Bob(Smith, Abigail)))");
		test.insert(new FullName("Harold", "Smith"), new PhoneNumber("5555555561"));
		assertEquals(test.toString(),
				"(Smith, Harold(Smith, Gordon(Smith, Frankie((Smith, Dana(Smith, Charlie))Smith, Bob(Smith, Abigail)))))");
		test.insert(new FullName("Engelbert", "Smith"), new PhoneNumber("5555555562"));
		assertEquals(test.toString(),
				"((Smith, Harold((Smith, Gordon)Smith, Frankie))Smith, Engelbert(Smith, Dana((Smith, Charlie)Smith, Bob(Smith, Abigail))))");
		// search
		PhoneNumber p = test.search(new FullName("Abigail", "Smith"));
		assertEquals(test.toString(),
				"(((Smith, Harold((Smith, Gordon)Smith, Frankie))Smith, Engelbert((Smith, Dana(Smith, Charlie))Smith, Bob))Smith, Abigail)");
		boolean phone = p.equals(new PhoneNumber("5555555556"));
		assertEquals(phone, true);
	}

	@Test
	public void phonebookTest() {
		// todo
	}

}
