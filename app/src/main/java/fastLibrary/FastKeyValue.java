package fastLibrary;

//Custom class for return values
public class FastKeyValue {
	 public String key, value;
	 public FastKeyValue(String c, String v)
	 {
	     key = c; value = v;
	 }


	@Override
	public String toString() {
		return value.toString();
	}

}