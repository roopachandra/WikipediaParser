import java.util.Scanner;
import java.util.*;
import java.util.regex.*;

public class main {
	
	/**
	 * Finds the number of spouses of each actor in the category
	 * and outputs those who have more than than cutoff number
	 * @param category
	 * @param cutOff
	 */
	public static void spouseNumber(String category, int cutOff){
		Map<String, Integer> spouses = new TreeMap<String, Integer>();
		URLGetter url = new URLGetter(getUrl(category));
		String template = "</a></small></th><td style=\"background:#(.*?)\"><b><span style=\"display:none;\" class=\"sortkey\">(.*?) !</span><span class=\"sorttext\"><a href=\"(.*?)\" title=\"(.*?)\">";
		Pattern p = Pattern.compile(template);
		Matcher m = p.matcher(url.getHTML());
		while(m.find()){
			boolean hasSpouse = false;
			URLGetter actorURL = new URLGetter("https://en.wikipedia.org" + m.group(3));
			String spouseTemplate = "<span class=\"nowrap\">Spouse(.*?)</span></th><td>(.*?)</td></tr>";
			Pattern p2 = Pattern.compile(spouseTemplate);
			Matcher m2 = p2.matcher(actorURL.getHTML());
			while(m2.find()){
				hasSpouse = true;
				if(m2.group(2).contains("<span itemscope=")){
					String [] parts = m2.group(2).split("<span itemscope=");
					int numSpouses = parts.length - 1;
					spouses.put(m.group(4), numSpouses);
					
				}
				else if(m2.group(2).contains("<a href=")){
					String [] parts = m2.group(2).split("<a href=");
					int numSpouses = parts.length;
					spouses.put(m.group(4), numSpouses);
					
				}
				else{
					String [] parts = m2.group(2).split("<br />");
					int numSpouses = parts.length;
					spouses.put(m.group(4), numSpouses);
				}
			}
			if(!hasSpouse){
				spouses.put(m.group(4), 0 );
			}
		}
		for(Map.Entry<String,Integer> entry : spouses.entrySet()) {
			  String actorName = entry.getKey();
			  Integer totalSpouses = entry.getValue();
			  if(totalSpouses >= cutOff){
				  System.out.println(actorName + " number of Spouses: " + totalSpouses);
			  }
			}
		
	}
	
	/**
	 * Finds the age of the actors when they won their oscar and
	 * outputs those who were older than the given age
	 * @param category
	 * @param age
	 */
	public static void ageAtWin(String category, int age){
		URLGetter url = new URLGetter(getUrl(category));
	
		String template = "<th scope=\"row\" rowspan=(.*?)title=\"(.*?) in film\">(.*?)<td style=\"background:#(.*?)\"><b><span style=\"display:none;\" class=\"sortkey\">(.*?) !</span><span class=\"sorttext\"><a href=\"(.*?)\" title=\"(.*?)\">";
		Pattern p = Pattern.compile(template);
		Matcher m = p.matcher(url.getHTML());
		System.out.println("Actors who won best leading actor after age: " + age);
		while(m.find()){
			URLGetter actorURL = new URLGetter("https://en.wikipedia.org" + m.group(6));
			String ageTemplate = "<span class=\"bday\">(\\d{4})-";
			Pattern p2 = Pattern.compile(ageTemplate);
			Matcher m2 = p2.matcher(actorURL.getHTML());
			while(m2.find()){
				int yearWon = Integer.parseInt(m.group(2));
				int yearBorn = Integer.parseInt(m2.group(1));
				if(yearWon - yearBorn > age){
					System.out.println(m.group(7));
				}				
			}
		}
	}
	
	/**
	 * Creates a treemap that maps directors to a linkedList of all
	 * the movies they directed. It then outputs those who have more
	 * nominates than the given number. 
	 * @param nominations
	 */
	public static void nominatedDirectors(int nominations){
		URLGetter url = new URLGetter(getUrl("Best Director"));
		Map<String, LinkedList<String>> directors= directorsTwoMovies();
		
		
		//Goes through and adds each movie a director is nominated for to a tree mamp
		String template = "<span style=\"display:none;\" class=\"sortkey\">(.*?) !</span><span class=\"sorttext\">(.*?)title=\"(.*?)\"(.*?)title=\"(.*?)\"";
		Pattern p = Pattern.compile(template);
		Matcher m = p.matcher(url.getHTML());
		while(m.find()){
			if(directors.containsKey(m.group(3))){
				LinkedList<String> currentMovies = directors.get(m.group(3));
				currentMovies.add(m.group(5));
				directors.put(m.group(3), currentMovies);
			}
			else{
				LinkedList<String> directedMovies = new LinkedList<String>();
				directedMovies.add(m.group(5));
				directors.put(m.group(3), directedMovies);
			}
			
		}
		for(Map.Entry<String,LinkedList<String>> entry : directors.entrySet()) {
			  String director = entry.getKey();
			  LinkedList<String> movies = entry.getValue();
			  if(movies.size() >= nominations){
				  System.out.println("Director: " + director);
				  System.out.println(" Movies Nominated for: " + movies.toString());
			  }

			}
	}
	
	/**
	 * Helper function for nominatedDirectors that checks for directors
	 * who were nominated for two different movies the same year and adds the 
	 * second movie to the treemap
	 * @return
	 */
	public static Map<String, LinkedList<String>> directorsTwoMovies(){

		Map<String, LinkedList<String>> directors = new TreeMap<String, LinkedList<String>>();
		URLGetter url = new URLGetter(getUrl("Best Director"));
		
		//checks for directors that were nominated for two movies in the same year
		String preTemplate = "</tr><tr><td rowspan=\"(\\d)\"><span style=\"display:none;\" class=\"sortkey\">(.*?) !</span><span class=\"sorttext\">(.*?)title=\"(.*?)\">(.*?)</a></span></td><td><i><a href=\"(.*?)\" title=\"(.*?)\">(.*?)</a></i></td></tr><tr><td><i><a href=\"(.*?)\" title=\"(.*?)\">(.*?)</a></i></td></tr><tr>";
		Pattern p0 = Pattern.compile(preTemplate);
		Matcher m0 = p0.matcher(url.getHTML());
		while(m0.find()){
			if(directors.containsKey(m0.group(4))){
				LinkedList<String> currentMovies = directors.get(m0.group(4));
				currentMovies.add(m0.group(11));
				directors.put(m0.group(4), currentMovies);
			}
			else{
				LinkedList<String> directedMovies = new LinkedList<String>();
				directedMovies.add(m0.group(11));
				directors.put(m0.group(4), directedMovies);
			}
		}
		return directors;

	}
	
	/**
	 * A function that finds the highest grossing film that won within a given category
	 * @param category - the academy film category you want to parse
	 */
	public static void topGrossingMovie(String category){
		System.out.println("Calculating:");
		URLGetter url = new URLGetter(getUrl(category));
		//String template = "<tr style=\"background:#FAEB86\"><td><i><b><a href=\"(.*?)\" title=\"(.*?)\">";
		String template = "<tr style=\"background:#FAEB86\"><td><i><a href=\"(.*?)\"(.*?) title=\"(.*?)\">";
		Pattern p = Pattern.compile(template);
		Matcher m = p.matcher(url.getHTML());
		String topGrossingMovie ="";
		double boxOffice = 0;
		while(m.find()){
			URLGetter movieUrl = new URLGetter("https://en.wikipedia.org" + m.group(1));
			String template2 = ">Box office</th><td style=\"line-height:1.3em;\">\\$(\\d+)+([.][0-9]+?)(.*?)(\\w+)illion<sup id";
			
			Pattern p2 = Pattern.compile(template2);
			Matcher m2 = p2.matcher(movieUrl.getHTML());
			while(m2.find()){
				Double currentValue = Double.parseDouble(m2.group(1) + m2.group(2));
				if(m2.group(4).equals("m")){
					currentValue = currentValue * 1000000;
				}
				if(m2.group(4).equals("b")){
					currentValue = currentValue * 1000000000;
				}
				if(currentValue > boxOffice){
					boxOffice = currentValue;
					topGrossingMovie = m.group(3);
				}
			}	
		}
		System.out.println("Top Grossing Movie: " + topGrossingMovie);
		System.out.println("Grossed About: $" + boxOffice );
	}
	
	/**
	 * Finds all the Best Leading Actors who were nominated
	 * who played a certain role.
	 * @param roleName
	 */
	public static void roleBestActor(String roleName){
		URLGetter url = new URLGetter(getUrl("Best Leading Actor")); 
		String template ="class=\"sortkey\">(.*?) !</span><span class=\"sorttext\"><a href=(.*?)title=\"(.*?)\"(.*?)</a>(.*?)</tr><tr>";
		Pattern p = Pattern.compile(template);
		Matcher m = p.matcher(url.getHTML());
		System.out.println("Actors who played a " + roleName + ":" );
		while(m.find()){
			String[] parts = m.group(5).split("class=\"sorttext\">");
			int lastPos = parts.length - 1;
			if(parts[lastPos].contains(roleName + " ")){
				System.out.println(m.group(3));
			}
			
		}
	}
	
	/**
	 * Finds the writers that were nominated for a given movie
	 * and prints them out
	 * @param movieTitle
	 */
	public static void originalScreenplay(String movieTitle){
		URLGetter url = new URLGetter(getUrl("Best Original Screenplay"));
		String template ="<span style=\"display:none;\"(.*?)title=\""+ movieTitle +"(.*?)\">";
		Pattern p = Pattern.compile(template);
		Matcher m = p.matcher(url.getHTML());
		System.out.println("Writers:");
		while(m.find()){
			String [] parts = m.group(0).split("</tr><tr>");
			int lastSpot = parts.length - 1;
			String template2 = "<span style=\"display:none;\" class=\"sortkey\">(.*?)<a href=(.*?)title=\"(.*?)\"";
			Pattern p2 = Pattern.compile(template2);
			Matcher m2 = p2.matcher(parts[lastSpot]);
			while(m2.find()){
				System.out.println(m2.group(3));
			}
		}
		
	}
	
	/**
	 * Returns the winners from a given category for the whole decade
	 * @param category
	 * @param year
	 */
	public static void winnersYear(String category, String year){
		URLGetter url = new URLGetter(getUrl(category));
		//URLGetter url = new URLGetter("https://en.wikipedia.org/wiki/Academy_Award_for_Best_Documentary_Feature");
		int stopYear = 10 + Integer.parseInt(year); //added 10 to find year that should not be parsed
		String template = "title=\""+year+" in film\">"+year+"</a></b>(.*?)title=\""+stopYear+" in film\">"+stopYear+"</a></b>";
		Pattern p = Pattern.compile(template);
		Matcher m = p.matcher(url.getHTML());
		while(m.find()){
			String template2 = "<i><b><a href=\"(.*?)title=\"(.*?)\">(.*?)</a></b></i>";
			Pattern p2 = Pattern.compile(template2);
			Matcher m2 = p2.matcher(m.group(0));
			while(m2.find()){
				System.out.println("Winner: " + m2.group(3));
			}
		}
		
	}
	
	/**
	 * Helper function that returns a URL given the category
	 * Prevents hardcoding of URLS
	 * @param category
	 * @return
	 */
	public static String getUrl(String category){
		URLGetter url = new URLGetter("https://en.wikipedia.org/wiki/Portal:Academy_Award");
		String template = "Current Awards for Merit(.*)<a href=\"(.*?)\" title=\"Academy Award for(.*?)\">" + category;
		
		Pattern p = Pattern.compile(template);
		Matcher m = p.matcher(url.getHTML());
		while(m.find()){
			String[] parts = m.group(2).split("class");
			String[]urls = parts[0].split("\"");
			return "https://en.wikipedia.org" + urls[0];
		}
		return "";
	}
	/**
	 * Find the studio who has the most nominations for best animated feature
	 */
	public static void bestFeature(){
		int highestNom = 0;
		String studio = "";
		URLGetter url = new URLGetter(getUrl("Best Animated Feature"));
		String template = "<td style=\"text-align:center;\"><a href=\"/wiki/[a-zA-Z]+(.*?)\" title=\"(.*?)\">(.*?)</a></td>(.*?)<td style=\"text-align:center;\"(.*?)>(\\d*)</td><td style=\"text-align:left";
		Pattern p = Pattern.compile(template);
		Matcher m = p.matcher(url.getHTML());
		int currentNom = 0;
		while (m.find()) {
			if(m.group(6).equals("")){
				currentNom = 0;
			}
			else{
				currentNom = Integer.parseInt(m.group(6));
			}
			if(currentNom > highestNom){
				  studio = m.group(2);
				  highestNom = currentNom;
			  }
			}
		System.out.println("Studio with most nominations is: " + studio + " which had " + highestNom + " nominations");

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Scanner reader = new Scanner(System.in);

		
		System.out.println("Enter the number of the question you want entered (1-8)");
		int n = reader.nextInt();
		reader.nextLine();
		
		if (n == 1) {
			bestFeature();
		}
		else if(n == 2) {
			System.out.println("Enter the name of the movie (Divorce Italian Style):");
			String movieTitle = reader.nextLine();
			originalScreenplay(movieTitle);
		} 
		else if(n == 3) {
			System.out.println("Enter the category you want to search (Best Documentary Feature): ");
			String category = reader.nextLine();
			System.out.println("Enter the decade (1980): ");
			String decade = reader.nextLine();
			winnersYear(category, decade);
		} 
		
		else if(n == 4) {
			System.out.println("Enter the role name you want to search (King): ");
			String roleName = reader.nextLine();
			roleBestActor(roleName);
		}
		
		else if(n == 5) {
			System.out.println("Enter the number of nominations you want to see (4): ");
			int nomNum = reader.nextInt();
			nominatedDirectors(nomNum);
			reader.nextLine();
		}
		
		else if(n == 6) {
			System.out.println("Please enter the category you want to search for top grossing(Best Picture): ");
			String category = reader.nextLine();
			topGrossingMovie(category);
		}
		
		else if(n == 7) {
			System.out.println("Enter category you want to search(Best Leading Actor): ");
			String category = reader.nextLine();
			System.out.println("Enter an age(45):");
			int age = reader.nextInt();
			reader.nextLine();
			ageAtWin(category, age);
		}
		else if(n == 8){
			System.out.println("Enter category you want to see number of spouses for(Best Leading Actor): ");
			String category = reader.nextLine();
			System.out.println("Enter the cutoff number of spouses (3): ");
			int cutOff = reader.nextInt();
			reader.nextLine();
			spouseNumber(category, cutOff);
		}
		
		else {
			System.out.println("Not a valid choice");
		}
		
		
		


	}

}
