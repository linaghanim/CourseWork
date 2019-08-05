//Name: Lina Ghanim		email: linaghanim@brandeis.edu
public class PhoneNumber {
	private long number;

	public PhoneNumber(long number) {
		this.number = number;
	}

	public PhoneNumber(String digits) {
		long n = 0;
		for (int i = 0; i < digits.length(); i++) {
			switch (digits.charAt(i)) {
			case '0':
				n = n * 10;
				break;
			case '1':
				n = n * 10 + 1;
				break;
			case '2':
				n = n * 10 + 2;
				break;
			case '3':
				n = n * 10 + 3;
				break;
			case '4':
				n = n * 10 + 4;
				break;
			case '5':
				n = n * 10 + 5;
				break;
			case '6':
				n = n * 10 + 6;
				break;
			case '7':
				n = n * 10 + 7;
				break;
			case '8':
				n = n * 10 + 8;
				break;
			case '9':
				n = n * 10 + 9;
				break;
			default:
				break;
			}
		}
		this.number = n;
	}

	public String toString() {
		long areaCode = this.number / 10000000;
		long prefix = (this.number % 10000000) / 10000;
		long suffix = (this.number % 10000);
		return "(" + areaCode + ")" + prefix + "-" + suffix;
	}

	public boolean equals(PhoneNumber p) {
		return this.number == p.number;
	}
}
