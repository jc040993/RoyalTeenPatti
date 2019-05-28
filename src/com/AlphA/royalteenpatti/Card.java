package com.AlphA.royalteenpatti;

import java.util.List;

public class Card {

	Integer				card;
	int				rank;
	int				suit;
	List<Integer>	al;

	public Card(List<Integer> al) {
		this.al = al;
		make(al.get(0));
		al.remove(0);
	}

	public void make(int i) {
		card = i;
		rank = i % 13;
		suit = i / 13;
		//System.out.println("card-" + i + ",rank-" + rank + ",suit" + suit);
	}
}
