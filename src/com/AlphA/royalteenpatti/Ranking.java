package com.AlphA.royalteenpatti;

public class Ranking {

	Person	p;
	int		rc0, rc1, rc2, sc0, sc1, sc2;
	int		rank;
	String	reason;

	public Ranking(Person p) {
		this.p = p;
		check();
	}

	private void check() {
		rc0 = p.c[0].rank;
		rc1 = p.c[1].rank;
		rc2 = p.c[2].rank;
		sc0 = p.c[0].suit;
		sc1 = p.c[1].suit;
		sc2 = p.c[2].suit;
		if (rc1 < rc2) {
			int temp = rc1;
			rc1 = rc2;
			rc2 = temp;
		}
		if (rc0 < rc1) {
			int temp = rc0;
			rc0 = rc1;
			rc1 = temp;
		}
		if (!checktrail())
			if (!checkstraight())
				if (!checknormalrun())
					if (!checkcolor())
						if (!checkpair())
							checkhigh();
	}

	private void checkhigh() {
		rank = 100;
		rank = rank + rc0 + rc1 + rc2;
		rank = rank + sc0 + sc1 + sc2;
		reason = "high";
	}

	private boolean checkpair() {
		if (++rc0 == rc1++ || rc1 == rc2) {
			rank = 200;
			rank = rank + rc0 + rc1 + rc2;
			rank = rank + sc0 + sc1 + sc2;
			reason = "pair";
			return true;
		}
		return false;
	}

	private boolean checkcolor() {
		if (sc0 == sc1 && sc1 == sc2) {
			rank = 300;
			rank += rc2;
			rank = rank + sc0 + sc1 + sc2;
			reason = "color";
			return true;
		}
		return false;
	}

	private boolean checknormalrun() {
		if (++rc0 == rc1++ && rc1 == rc2) {
			rank = 400;
			rc0--;
			rc1--;
			if (rc0 == 12 && rc1 == 11)
				rank = 0;
			else if (rc0 == 12 && rc1 == 2)
				rank += 12;
			else
				rank = rank + rc2 + 1;
			rank = rank + sc0 + sc1 + sc2;
			reason = "normalrun";
			return true;
		} else {
			rc0--;
			rc1--;
			return false;
		}
	}

	private boolean checkstraight() {
		if (sc0 == sc1 && sc1 == sc2) {
			if (++rc0 == rc1++ && rc1 == rc2) {
				rank = 500;
				rc0--;
				rc1--;
				if (rc0 == 12 && rc1 == 11)
					rank = 0;
				else if (rc0 == 12 && rc1 == 2)
					rank += 12;
				else
					rank = rank + rc2 + 1;
				rank = rank + sc0 + sc1 + sc2;
				reason = "straight";
				return true;
			} else {
				rc0--;
				rc1--;
				return false;
			}
		}
		return false;
	}

	private boolean checktrail() {
		if (rc0 == rc1 && rc1 == rc2) {
			rank = 600 + rc0;
			rank = rank + sc0 + sc1 + sc2;
			reason = "trail";
			return true;
		}
		return false;
	}
	/*
	 * private int compare(int a, int b) { final int a = compare(rc0, rc1); final int b = compare(rc1, rc2); final int c = compare(rc2, rc0); final int d = compare(sc0, sc1); final int e = compare(sc1, sc2); final int f =
	 * compare(sc2, sc0); if (a > b) return 1; else if (b > a) return -1; return 0; }
	 */
}
