package com.onetimepad.solitaire;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class Deck {

	private int total;
	private Card head, tail;
	private int[] range;
	private final String[] SuitsInOrder = { "C", "D", "H", "S" };

	public Deck(int numOfCardsPerSuit, int numOfSuits) {
		if (numOfCardsPerSuit < 1 || numOfCardsPerSuit > 13) {
			throw new IllegalArgumentException("the first input is not a number between 1 and 13 (both included)");
		}
		if (numOfSuits < 1 || numOfSuits > SuitsInOrder.length) {
			throw new IllegalArgumentException(
					"second input is not a number between 1 and the size of the class field suitsInOrder.");
		}
		total = numOfCardsPerSuit * numOfSuits + 2;
		range = new int[total];
		for (int i = 0, suit = 1; suit <= numOfSuits; suit++) {
			for (int rank = 1; rank <= numOfCardsPerSuit; rank++, i++) {
				Card c = new PlayingCard(suit, rank);
				addCard(c);
				range[i] = c.getVal();
			}
		}
		addJoker();
		range[total - 2] = total - 1;
		range[total - 1] = total;
	}

	private void addCard(Card c) {
		if (this.head == null) {
			this.head = c;
			this.tail = this.head;
		} else {
			Card newNode = c;
			newNode.setNext(this.head);
			newNode.setPrev(this.tail);
			this.head.setPrev(newNode);
			this.tail.setNext(newNode);
			this.tail = newNode;
		}
	}

	public void printDeck() {
		if (this.head != null) {
			Card currentNode = this.head;
			int i = 1;
			while (currentNode != null) {
				System.out.print("" + "(" + currentNode.prev + ")" + currentNode + ":" + currentNode.getVal() + "("
						+ currentNode.next + ") ");
				if (currentNode.getNext() == this.head) {
					break;
				}
				currentNode = currentNode.getNext();
				i++;
			}
			System.out.println("END \n");
			if (i != total)
				System.out.println(
						"<<<<<<<<<<<<<<<<<<<<<<<...............ALERT...................<<<<<<<<<<<<<<<<<<<<<<<");
		} else {
			System.out.println("Linked list is empty.");
		}
	}

	private void addJoker() {
		Card c1 = new Joker("R");
		Card c2 = new Joker("B");
		addCard(c1);
		addCard(c2);

	}

	void shuffle() {
//		this.range = IntStream.rangeClosed(1, total).toArray();
		System.out.println(Arrays.toString(range));
		for (int i = total - 1; i > 1; i--) {
			int j = getRandom(i);
			swap(i, j);
		}
		createDeck(range);
		System.out.println(Arrays.toString(range));
	}

	private void swap(int i, int j) {
		int t = range[i];
		range[i] = range[j];
		range[j] = t;
	}

	private void createDeck(int[] range) {
		this.head = null;
		for (int i = 0; i < range.length; i++) {
			addCard(getCardByValue(range[i]));
		}
	}

	private int getRandom(int i) {
		int low = 0;
		int high = i;
		return ThreadLocalRandom.current().nextInt(low, high + 1);
	}

	private Card getCardByValue(int val) {
		if (val == total - 1 || val == total)
			return val == total ? new Joker("B") : new Joker("R");
		if (val < 14) {
			return new PlayingCard(1, val);
		} else if (val < 27) {
			return new PlayingCard(2, val - 13);
		} else if (val < 40) {
			return new PlayingCard(3, val - 26);
		} else {
			return new PlayingCard(4, val - 39);
		}
	}

	private Card locateJoker(String color) {
		Card curr = this.head;
		int i = 1;
		do {
			if (curr.getVal() == total - 1 || curr.getVal() == total) {
				Joker j = (Joker) getCardByValue(((Joker) curr).countValue());
				if (j.getColorStr().equals(color)) {
					System.out.print(color + " found " + j + " at " + i + " \n");
					return curr;
				}
			}
			curr = curr.next;
			i++;
		} while (curr != head);
		return null;
	}

	private void moveCard(Card c, int p) {
		Card curr = c;
		c.prev.next = c.next;
		c.next.prev = c.prev;
		while (p > 0) {
			curr = curr.next;
			p--;
			if (c == head) {
				head = head.next;
			}
			if (c == tail) {
				tail = tail.prev;
			}
		}
		c.prev = curr;
		c.next = curr.next;
		curr.next.prev = c;
		curr.next = c;
	}

	private void tripleCut() {
//		(AC)10C:10(7C) (10C)7C:7(9C) (7C)9C:9(8C) (9C)8C:8(BJ) (8C)BJ:11(2C) (BJ)2C:2(6C)
//		(2C)6C:6(4C) (6C)4C:4(5C) (4C)5C:5(RJ) (5C)RJ:11(3C) (RJ)3C:3(AC) (3C)AC:1(10C) END 
		moveIfHeadIsJoker();
		Card firstJoker = null;
		Card secondJoker = null;
		Card temp2 = null;
		Card current = head.next;
		Card temp1 = null;
		System.out.println("HEAD:" + head);
		System.out.println("TAIL:" + tail);
		// Point the Joker A and B--------------------------------
		while (current != head) {
			if (current.getVal() == total - 1 || current.getVal() == total) {
				firstJoker = current;
				break;
			} else {
				current = current.next;
			}
		}
		System.out.println("FIRST JOKER:" + firstJoker);
		current = firstJoker.next;
		while (current != head) {
			if (current.getVal() == total - 1 || current.getVal() == total) {
				secondJoker = current;
				break;
			} else {
				current = current.next;
			}
		}
		System.out.println("SECOND JOKER:" + secondJoker);
		current = head;
		while (current.next != firstJoker) {
			current = current.next;
		}
		System.out.println("CURRENT:" + current);
		if (secondJoker == tail || secondJoker.next == head) {
			head = firstJoker;
			tail = current;
		} else {
			temp1 = secondJoker.next;
			System.out.println("TEMP1:" + temp1);
			secondJoker.next = null;
			firstJoker.prev = null;
			tail.next = null;
			head.prev = null;
			current.next = null;
			temp1.prev = null;

			secondJoker.next = head;
			head.prev = secondJoker;

			tail.next = firstJoker;
			firstJoker.prev = tail;

			head = temp1;
			tail = current;

			head.prev = tail;
			tail.next = head;

		}
	}

	private void moveIfHeadIsJoker() {
		if (head instanceof Joker) {
			moveCard(head, 1);
		}

	}

	private void countCut() {
		System.out.println("tail card value > " + tail.getVal());
		int value = tail.getVal();
		Card t = tail;
		// remove tail card
		tail = tail.prev;
		head.prev = tail;
		tail.next = head;

		while (value > 0) {
			head = head.next;
			tail = tail.next;
			value--;
		}
		// add tail card
		tail.next = t;
		t.prev = tail;
		t.next = head;
		head.prev = t;
	}

	private Card lookUpCard() {
		int value = head.getVal();
		System.out.println("head > " + head);
		Card curr = head;
		while (value > 0) {
			curr = curr.next;
			value--;
		}
		System.out.println("lookup card > " + curr);
		if (curr.getVal() == total - 1 || curr.getVal() == total) {
			return null;
		} else {
			return curr;
		}
	}

	public int generateNextKeystreamValue() {
		System.out.print("initial: ");
		printDeck();
		shuffle();
		System.out.print("shuffle: ");
		printDeck();
		moveCard(locateJoker("R"), 1);
		printDeck();
		moveCard(locateJoker("B"), 2);
		printDeck();
		tripleCut();
		System.out.print("triple cut: ");
		printDeck();
		System.out.print("count cut: ");
		countCut();
		printDeck();
		Card card = lookUpCard();
		if (card == null) {
			return generateNextKeystreamValue();
		} else {
			System.out.println("=========" + card.getVal() + "=========");
			return card.getVal();
		}
	}

	public static void main(String[] args) {
		Deck deck = new Deck(5, 2);
		int[] range = { 9, 2, 12, 1, 5, 11, 8, 6, 10, 7, 3, 4 };
		deck.createDeck(range);
//		deck.printDeck();
//		deck.moveCard(deck.locateJoker("R"), 1);
//		deck.printDeck();
//		deck.moveCard(deck.locateJoker("B"), 2);
//		deck.printDeck();
		deck.tripleCut();
		deck.printDeck();
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
			return color == 1 ? total - 1 : total;
		}

	}

}
