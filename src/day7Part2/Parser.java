package day7Part2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Parser {
	
	private String file;
	public ArrayList<Hand> hands = new ArrayList<Hand>();
	public ArrayList<Hand> allSortedHands;
	public ArrayList<ArrayList<Hand>> allHands = new ArrayList<ArrayList<Hand>>();

	int type = 0;
	char[] label = {'A', 'K', 'Q', 'T','9','8','7','6','5','4','3','2', 'J'};

	public Parser(String file) {
		this.file = file;
	}
	
	public void parseFile() throws FileNotFoundException {
		
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String cards;
			int bid;

			for (String line : br.lines().toList()) { // text.split("\n")) {//
				String[] input = line.trim().split(" ");
			
				cards = input[0];
				bid = Integer.parseInt(input[1]);
				hands.add(new Hand (cards, bid));
			}
		} catch (FileNotFoundException e) {
			throw e;
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
	}

	public void findResultPart1() {	//i.o.: find Type of all hands and set Type
		for (int i = 0; i< hands.size(); i++) {
			findType(hands.get(i));
			
			findSortString(hands.get(i));
		}
	
		//sort (stärkste/ höchste nach hinten)
		allSortedHands = new ArrayList<Hand>(sortMyHands(hands));
		
		//totalWinnings bestimmen (Summe aus bid*rank über alle Hands)
		findWinnings (allSortedHands);
		System.out.println("Result day7Part2: " + determineTotalWinnings(allSortedHands) );
	}

	private void findWinnings(ArrayList<Hand> allSortedHands) {
		int winnings;
		for (int i = 0;i<allSortedHands.size(); i++) {
			winnings = allSortedHands.get(i).getBid()*(i+ 1);
			
			allSortedHands.get(i).setWinnings(winnings);
		}	
	}

	private void findSortString(Hand hand) {//i.O.
		char[] data = hand.getCards().toCharArray();
		String sortString = new String(String.copyValueOf(data));
		
		sortString= sortString.replace('T', 'B');
		sortString= sortString.replace('Q', 'D');
		sortString= sortString.replace('K', 'E');
		sortString= sortString.replace('A', 'F');
		sortString= sortString.replace('J', '0'); //in contrast to the Puzzle day7Part1 
				
		hand.setSortString(sortString);
	}

	private ArrayList<Hand> sortMyHands (ArrayList<Hand> h) {
		h.sort(null);
		for (Hand hand: h) {
			System.out.println("SortMyHands: Type: " + hand.getType() + ", Cards: " + hand.getCards());
		}
		return h;
	}
	
	private int determineTotalWinnings(ArrayList<Hand> allSortedHands) {
		int sum=0;
		for (int i =0; i<allSortedHands.size(); i++) {
			sum += allSortedHands.get(i).getWinnings(); 
		}
		return sum;
	}

	private void findType(Hand h) { //i.o.
		int[] count = new int[label.length]; 
		for (int i = 0; i <label.length; i++) {
			count[i] = 0;
		}
		count = countLabels(h.getCards(),count);
		
		//findType
		h.setType(determineType(count));
	}

	private int determineType(int[] ct) {	// i.o. //Unterschied zu Part1
		//in case of having one or more 'J's in this Hand:
		if (ct[12] >0 && ct[12]<5) {return determineType(ct, ct[12]);}
	
		//in case of having no 'J' in this Hand
		for (int  i = 0; i<ct.length; i++) {
			if (ct[i] == 5) return 6;	
			if (ct[i] == 4) return 5;
			if (ct[i] == 3) {	//looking for a FullHouse
				for (int j = 0; j<ct.length; j++) {
					if (ct[j] == 2) {
						return 4;
					} 
				}
				return 3;
			}
			if (ct[i] == 2) { // looking for TwoPair
								//or looking for FullHouse!
				for (int j = 0; j<ct.length; j++) {
					if ((i!=j) && ct[j] == 2) {
						return 2;	//TwoPair
					} 
					if (ct[j] == 3) {
						return 4;	//FullHouse
					}
				}
				return 1;
			}
		}
		return 0;	
	}

	private int determineType(int[] ct, int jokers) {// io
//		5ofAKind,	4OfAKind,	FullHouse,	3OfAKind,TwoPair,	OnePair, 	HighCard
		//	6			,5			,4			,3		,2			,1			,0 (default)
		for (int  i = 0; i<ct.length-1; i++) {
			if (ct[i] == 4) {	//4 identical cards
				return 6; //--> 5OfAkind, Type6 
			}
			if (ct[i] == 3) {	
				if (jokers==2) {
					return 6; //--> 5OfAkind
				} else return 5; // in case of 1 'J' --> 4OfAkind
			}	
			if (ct[i] == 2) {
				if (jokers==3) {
					return 6; // --> 5OfAkind
				} else if (jokers==2) {
					return 5; // --> 4OfAkind
				} else  {//jokers == 1
				//looking for another pair, then --> make Fullhouse
					for(int j = 0; j<ct.length-1; j++) {
						if (i!=j &&ct[j] == 2) {
							return 4; //make FullHouse
						}
					} //if no other 2 were found
					return 3;  //make 3OfAkind
				}
			}
		} 	//remaining cases: only single cards and jokers: 
		if (jokers ==4)return 6;
		if (jokers == 3) return 5; //make 4ofakind
		if (jokers == 2) return 3; //make 3 of a Kind
		return 1;
	}

	private int[] countLabels(String cards, int[] ct) { //i.o.
		for (int i = 0; i< cards.length(); i++) {	
			for (int j = 0; j<label.length; j++) {
				if (cards.charAt(i) == label[j]) {
					ct[j]++;
					continue;
				}
			}
		}	
		return ct;
	}
}