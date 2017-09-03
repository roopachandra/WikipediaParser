import java.util.ArrayList;

/**
 * This class will use the URLGetter and connect to websites.
 * @author swapneel
 *
 */
public class URLTester {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		URLGetter url = new URLGetter("https://en.wikipedia.org/wiki/Portal:Academy_Award");
		
		url.printStatusCode();
		
		ArrayList<String> page = url.getContents();
		
		for (String line : page){
			System.out.println(line);
		}
		
		/* for (String line : page) {
			System.out.println(line);
		}
*/
	}

}