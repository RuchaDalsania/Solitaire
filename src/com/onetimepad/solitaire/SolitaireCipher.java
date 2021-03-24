package com.onetimepad.solitaire;

import java.util.Arrays;

public class SolitaireCipher {

	Deck deck;

	public static void main(String[] args) {
		SolitaireCipher solitaireCipher = new SolitaireCipher();
		solitaireCipher.deck = new Deck(5, 2);
		System.out.println(solitaireCipher.encrypt("Is that you, Bob?"));
	}

	public String encrypt(String message) {
		StringBuffer msg = new StringBuffer();
		for (int i = 0; i < message.length(); i++) {
			if (!Character.isLetter(message.charAt(i)))
				continue;
			else {
				char ch = Character.toUpperCase(message.charAt(i));
				msg.append(ch);
			}
		}
		
		int[] keys = getKeystream(msg.length());
		StringBuffer encryptedText = new StringBuffer();
		for (int i = 0; i < msg.length(); i++) {
			char ch = Character.toUpperCase(msg.charAt(i));
			int key = keys[i];
			int code = ch + key;
			if (code > 90)
				code -= 26;
			ch = (char) (code);
			encryptedText.append(ch);
		}
		System.out.println("TEXT :: "+msg.toString());
		System.out.println("FINAL:: "+encryptedText.toString());
		return encryptedText.toString();
	}

	private int[] getKeystream(int size) {
		int[] keys = new int[size];
		for (int i = 0; i < size; i++) {
			keys[i] = deck.generateNextKeystreamValue();
		}
		System.out.println("KEYS :: "+Arrays.toString(keys));
		return keys;
	}

}
