package com.onetimepad.solitaire;

import java.util.Random;

public class Deck2 {

	private Card[] deckOfCards;
	private int total;
	private Card prev;
	private Card head, tail;
	private final String[] SuitsInOrder = { "C", "D", "H", "S" };

	public Deck2(int numOfCardsPerSuit, int numOfSuits) {
		if (numOfCardsPerSuit < 1 || numOfCardsPerSuit > 13) {
			throw new IllegalArgumentException("the first input is not a number between 1 and 13 (both included)");
		}
		if (numOfSuits < 1 || numOfSuits > SuitsInOrder.length) {
			throw new IllegalArgumentException(
					"second input is not a number between 1 and the size of the class field suitsInOrder.");
		}
		total = numOfCardsPerSuit * numOfSuits + 2;
		deckOfCards = new Card[total];
		for (int i = 0, suit = 1; suit <= numOfSuits; suit++) {
			for (int rank = 1; rank <= numOfCardsPerSuit; rank++) {
				Card c = new PlayingCard(suit, rank);
				addCard(c);
				deckOfCards[i++] = c;
			}
		}
		addJoker();
	}

	private void addJoker() {
		Card c1 = new Joker("R");
		Card c2 = new Joker("B");
		addCard(c1);
		addCard(c2);
		deckOfCards[total - 2] = c1;
		deckOfCards[total - 1] = c2;
	}

	Deck2(Deck2 d) {
	}

	void generate() {
		int rjLocation = locateJoker("R");
		System.out.println(rjLocation);
		System.out.println(locateJoker("B"));
		Card rj = deckOfCards[rjLocation];
		System.out.println(rj);
		moveCard(rj, 1);
	}

	public static void main(String[] args) {
		Deck2 c = new Deck2(13, 2);
		c.printDeck();
		c.shuffle();
		c.printDeck();
//		c.generate();
//		c.printDeck();
	}

	private void printDeck() {
		for (int i = 0; i < total; i++) {
			if (deckOfCards != null)
				try {
					System.out.print("" + "(" + deckOfCards[i].prev + ")" + deckOfCards[i]
//							+ ":"
//							+ deckOfCards[i].getVal() 
							+ "(" + deckOfCards[i].next + ") ");
				} catch (Exception e) {
					e.printStackTrace();
				}
//			System.out.print(deckOfCards[i] + " ");
		}
		System.out.println(" ");
	}

	void addCard(Card c) {
		if (prev != null) {
			c.next = prev.next;
			prev.next = c;
		}
		c.prev = prev;
		c.next = c;
		prev = c;
	}

	void shuffle() {
		for (int i = total - 1; i > 1; i--) {
			int j = getRandom(i);
			Card a = deckOfCards[i];
			Card b = deckOfCards[j];
			Card c = a;
			Card d = b;

			a = b;
			a.prev = c.prev;
			a.prev.next = b;
			a.next = c.next;
			a.next.prev = b;

			b = c;
			b.prev = d.prev;
			b.prev.next = c;
			b.next = d.next;
			b.next.prev = c;

		}
		System.out.println("shuffled");
	}

	private int getRandom(int i) {
		Random r = new Random();
		int low = 0;
		int high = i;
		return r.nextInt(high - low) + low;
	}

	int locateJoker(String color) {
		for (int i = 0; i < total; i++) {
			if (deckOfCards[i] instanceof Joker
					&& ((Joker) deckOfCards[i]).JokerColor[((Joker) deckOfCards[i]).color - 1].equals(color)) {
				return i;
				// i+1
			}
		}
		return 0;
	}

	void moveCard(Card c, int p) {
		Card d = c, temp;
		while (p > 0) {
			c = c.next;
			p--;
		}
		temp = c;
		c = d;
		d = temp;
	}

	void tripleCut(Card firstCard, Card secondCard) {
	}

	void countCut() {
	}

	void generateNextKeyStreamValue() {
	}

	Card lookupCard() {
		return null;
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

		final String[] Suit = { "C", "D", "H", "S" };
		final String[] Rank = { "A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K" };

		private byte cardSuit;
		private byte cardRank;

		public PlayingCard(int suit, int rank) {
			cardRank = (byte) rank;
			cardSuit = (byte) suit;
			this.setVal(cardSuit + cardRank - 1);
			switch (cardSuit) {
			case 1:
				break;
			case 2:
				this.setVal(this.getVal() + 12);
				break;
			case 3:
				this.setVal(this.getVal() + 24);
				break;
			case 4:
				this.setVal(this.getVal() + 36);
				break;
			}
		}

		public int suit() {
			return (cardSuit);
		}

		public String suitStr() {
			return (Suit[cardSuit - 1]);
		}

		public int rank() {
			return (cardRank);
		}

		public String rankStr() {
			return (Rank[cardRank - 1]);
		}

		@Override
		public String toString() {
			return (Rank[cardRank - 1] + Suit[cardSuit - 1]);
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

		private final String[] JokerColor = { "R", "B" };

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
			return JokerColor[color - 1];
		}

		public String toString() {
			return JokerColor[this.color - 1] + "J";
		}

	}

}
