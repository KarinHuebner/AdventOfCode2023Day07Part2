package day7Part2;
/**
 * Problem Descripition Part1: (LINK)
 * 
 * in contrast to Part1, here in Part2 the 
 * Card-Label 'J' is interpreted as a 'Joker'
 * 
 */
public class Main {

	public static void main(String[] args) {
		try {
			String file = "input07.txt";
			//String file = "test07.2.txt";
			Parser p = new Parser (file);
			p.parseFile();
			p.findResultPart1();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
