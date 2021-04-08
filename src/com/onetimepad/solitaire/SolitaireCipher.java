package com.onetimepad.solitaire;

import java.util.Arrays;

public class SolitaireCipher {

	Deck deck;

	public static void main(String[] args) {
		SolitaireCipher solitaireCipher = new SolitaireCipher();
		solitaireCipher.deck = new Deck(5, 2);
		String en=solitaireCipher.encrypt("Is that you, Bob?");
		System.out.println(en);
		String dn=solitaireCipher.decrypt(en);
		System.out.println(dn);
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
	
	//TODO
	public String decrypt(String message) {
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

//	last range[5, 2, 1, 3, 14, 15, 18, 4, 17, 12, 16, 11]
//
//
//			KEYS :: [3, 5, 17, 16, 5, 17, 14, 5, 16, 18, 4, 2]
//			TEXT :: ISTHATYOUBOB
//			FINAL:: LXKXFKMTKTSD

}
