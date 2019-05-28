package com.AlphA.royalteenpatti;

import java.util.Comparator;
import java.util.List;

public class Person {

	int									i	= 0;
	static List<Integer>				al;
	Card[]								c;
	int									rank;
	String								reason;
	public static Comparator<Person>	pcom;

	public Person(List<Integer> al) {
		c = new Card[3];
		this.al = al;
		for (int i = 0; i < 3; i++) {
			c[i] = new Card(al);
		}
		Ranking r = new Ranking(this);
		rank = r.rank;
		reason = r.reason;
		pcom = new Comparator<Person>() {

			public int compare(Person p1, Person p2) {
				int sp1 = p1.rank;
				int sp2 = p2.rank;
				return sp2 - sp1;
			}
		};
	}
}
