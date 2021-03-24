package com.onetimepad.solitaire;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.onetimepad.solitaire.Deck2.Card;

public class Deck3 {

	private List<Card> deckOfCards;
	private int total;
	private Card prev;
	private Card head, tail;
	private final String[] SuitsInOrder = { "C", "D", "H", "S" };

	public Deck3(int numOfCardsPerSuit, int numOfSuits) {
		if (numOfCardsPerSuit < 1 || numOfCardsPerSuit > 13) {
			throw new IllegalArgumentException("the first input is not a number between 1 and 13 (both included)");
		}
		if (numOfSuits < 1 || numOfSuits > SuitsInOrder.length) {
			throw new IllegalArgumentException(
					"second input is not a number between 1 and the size of the class field suitsInOrder.");
		}
		total = numOfCardsPerSuit * numOfSuits + 2;
		deckOfCards = new ArrayList<>();
		for (int i = 0, suit = 1; suit <= numOfSuits; suit++) {
			for (int rank = 1; rank <= numOfCardsPerSuit; rank++) {
				Card c = new PlayingCard(suit, rank);
				addCard(c);
				deckOfCards.add(i++, c);
			}
		}
		addJoker();
	}

	void addCard(Card c) {
		if (prev != null) {
			prev.next = c;
			c.prev = prev;
		} else {
			head = c;
		}
		c.next = c;
		prev = c;
	}

	private void addJoker() {
		Card c1 = new Joker("R");
		Card c2 = new Joker("B");
		addCard(c1);
		addCard(c2);
		deckOfCards.add(c1);
		deckOfCards.add(c2);
		createCircularList(c2);
	}

	private void createCircularList(Card c2) {
		c2.next = head;
		head.prev = c2;
	}

	void shuffle() {
		for (int i = total - 1; i > 1; i--) {
			int j = 20;
			Card a = deckOfCards.get(i);
			Card b = deckOfCards.get(j);

			a.next.prev=b;
			a.prev.next=b;
			 printDeck();
			Card p1 = a.prev;
			Card n1 = a.next;
			Card p2 = b.prev;
			Card n2 = b.next;

	        b.prev = p1;
	        b.next = n1;

	        a.prev = p2;
	        a.next = n2;


	        printDeck();
	        if (b.next != null)
	            b.next.prev = b;
	        if (b.prev != null)
	            b.prev.next = b;
	        printDeck();
	        if (a.next != null)
	            a.next.prev = a;
	        if (a.prev != null)
	            a.prev.next = a;
	        System.out.println(a+" <> "+b);
	       printDeck();
		}
		System.out.println("shuffled");
	}

	private int getRandom(int i) {
		Random r = new Random();
		int low = 0;
		int high = i;
		return r.nextInt(high - low) + low;
	}

	public static void main(String[] args) {
		Deck3 c = new Deck3(13, 2);
		c.printDeck();
		c.shuffle();
		c.printDeck();
	}

	private void printDeck() {
		for (int i = 0; i < total; i++) {
			if (deckOfCards != null)
				try {
					System.out.print("" + "(" + deckOfCards.get(i).prev + ")" + deckOfCards.get(i)
//							+ ":"
//							+ deckOfCards.get(i).getVal() 
							+ "(" + deckOfCards.get(i).next + ") ");
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
		System.out.println(" ");
		
	}

	class Card {

		private int val;
		private Card next;
		private Card prev;

		Card() {
		}

		Card(int val) {
			this.val = val;
		}

		Card(int val, Card next, Card prev) {
			this.val = val;
			this.next = next;
			this.prev = prev;
		}

		public void setVal(int val) {
			this.val = val;
		}

		public int getVal() {
			return this.val;
		}

		public Card getNext() {
			return next;
		}

		public void setNext(Card next) {
			this.next = next;
		}

		public Card getPrev() {
			return prev;
		}

		public void setPrev(Card prev) {
			this.prev = prev;
		}

		public Card getCopy(Card c) {
			Card nc;
			if (c instanceof PlayingCard)
				nc = new PlayingCard(((PlayingCard) c).suit(), ((PlayingCard) c).rank());
			else
				nc = new Joker(((Joker) c).getColorStr());
			nc.setNext(c.getNext());
			nc.setPrev(c.getPrev());
			return nc;
		}

	}

	class PlayingCard extends Card {
		public static final int SPADE = 4;
		public static final int HEART = 3;
		public static final int DIAMOND = 2;
		public static final int CLUB = 1;

		final String[] Suit = { "*", "C", "D", "H", "S" };
		final String[] Rank = { "*", "A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K" };

		private byte cardSuit;
		private byte cardRank;

		public PlayingCard(int suit, int rank) {
			cardRank = (byte) rank;
			cardSuit = (byte) suit;
			this.setVal(cardSuit + cardRank);
			switch (cardSuit) {
			case 1:
				break;
			case 2:
				this.setVal(this.getVal() + 13);
				break;
			case 3:
				this.setVal(this.getVal() + 26);
				break;
			case 4:
				this.setVal(this.getVal() + 39);
				break;
			default:
				break;
			}
		}

		public int suit() {
			return (cardSuit);
		}

		public String suitStr() {
			return (Suit[cardSuit]);
		}

		public int rank() {
			return (cardRank);
		}

		public String rankStr() {
			return (Rank[cardRank]);
		}

		@Override
		public String toString() {
			return (Rank[cardRank] + Suit[cardSuit]);
		}

		@Override
		public boolean equals(Object x) {
			if (x instanceof PlayingCard)
				return (cardSuit == ((PlayingCard) x).cardSuit && cardRank == ((PlayingCard) x).cardRank);
			return false;
		}

		public int countValue() {
			return suit() + rank();
		}

	}

	class Joker extends Card {
		private byte color;
		public static final int BLACK = 2;
		public static final int RED = 1;

		private final String[] JokerColor = { "*", "R", "B" };

		Joker(String color) {
			if (!(color.equals("R") || color.equals("B"))) {
				throw new IllegalArgumentException("Joker color either R or B");
			}
			this.color = (byte) (color.equals("R") ? 1 : 2);
			this.setVal(total - 1);
		}

		public byte getColor() {
			return color;
		}

		public String getColorStr() {
			return JokerColor[color];
		}

		public String toString() {
			return JokerColor[this.color] + "J";
		}

		public int countValue() {
			return total - 1;
		}
	}

}
