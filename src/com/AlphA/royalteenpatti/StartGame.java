package com.AlphA.royalteenpatti;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class StartGame extends Activity {

	int						id, time, position, gamerunning, points, midpoints, bidamount, turn, cun, s, s1, s2, s3, s4, s5;
	String					username, p1, p2, p3, p4, p5, c1, c2, c3, c4, c5;
	int[]					ac1, ac2, ac3, ac4, ac5;
	TextView				tn, tp, tp1, tp2, tp3, tp4, tp5, tn1, tn2, tn3, tn4, tn5, ts, trp1, trp2, trp3, trp4, trp5, tmidpoints;
	Extras					ext;
	ImageView				icrown, ip, ip1, ip2, ip3, ip4, ip5, ih1, ih2, ih3, ih4, ih5, ic1, ic2, ic3, ic4, ic5, ic6, ic7, ic8, ic9, ic10, ic11, ic12, ic13, ic14, ic15;
	static List<Integer>	al;
	ArrayList<Person>		p;
	int[]					status;
	AlertDialog.Builder		exitdiag, makebid;
	boolean					loading;
	JSONObject				js, tempjs;
	Button					bmakebid, bpack, bsee, bshow;
	Integer					temp;
	String					temps;
	boolean					go		= true;
	Integer[]				ci		= new Integer[3];
	String[]				cs		= new String[3];
	ImageView[]				card;
	int						playernu;
	int						active;
	int						turnset	= 0;
	RelativeLayout			rel;
	int						winner;

	private void ingameupdate() {
		// bid,seen
		Integer t = turn;
		if (t == position) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					Thread th = new Thread(new Runnable() {

						@Override
						public void run() {
							active = activestatus();
						}
					});
					th.start();
					try {
						th.join();
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					if (active < 3)
						bshow.setVisibility(View.VISIBLE);
					bmakebid.setVisibility(View.VISIBLE);
					bpack.setVisibility(View.VISIBLE);
					if (bsee != null)
						bsee.setVisibility(View.VISIBLE);
				}
			});
			int count = 0;
			int temp = 0;
			int s = 0;
			do {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				String result = ext.Get(ext.Post("ingameupdate.php", ext.Params(Integer.valueOf(id).toString())));
				try {
					JSONObject js = new JSONObject(result);
					temp = js.getInt("turn");
					s1 = js.getInt("s1");
					s2 = js.getInt("s2");
					s3 = js.getInt("s3");
					s4 = js.getInt("s4");
					s5 = js.getInt("s5");
					gamerunning = js.getInt("gamerunning");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				switch (position) {
					case 1:
						s = s1;
						break;
					case 2:
						s = s2;
						break;
					case 3:
						s = s3;
						break;
					case 4:
						s = s4;
						break;
					case 5:
						s = s5;
						break;
				}
				if ((s == 1 || s == 2) && gamerunning == 1) {
					if (temp != turn)
						break;
					else if (count == 20) {
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								if (gamerunning == 1) {
									Toast.makeText(getApplicationContext(), "You lost your turn due to inactivity", Toast.LENGTH_LONG).show();
								}
							}
						});
						Log.d("jc", "lost turn-gng to pack");
						pack();
						break;
					} else
						count++;
					Log.d("jc", "ingameupdate count-" + count);
				} else
					break;
			} while (true);
		}
	}

	private void switchturn() {
		// ext.Post("setp.php", ext.Params(id.toString(), turn.toString(), "1"));
		Log.d("jc", "in switchturn");
		int tempt;
		while (true) {
			tempt = ++turn;
			if (tempt >= 6) {
				tempt = 1;
				break;
			}
			int temp = 0;
			switch (tempt) {
				case 1:
					temp = s1;
					break;
				case 2:
					temp = s2;
					break;
				case 3:
					temp = s3;
					break;
				case 4:
					temp = s4;
					break;
				case 5:
					temp = s5;
					break;
			}
			if (temp == 1 || temp == 2)
				break;
			else
				continue;
		}
		ext.Post("setturn.php", ext.Params(Integer.valueOf(id).toString(), Integer.valueOf(tempt).toString()));
		Log.d("d", "switchturn-setturn");
	}

	public void bshow(View v) {
		bmakebid.setVisibility(View.INVISIBLE);
		bpack.setVisibility(View.INVISIBLE);
		try {
			bsee.setVisibility(View.INVISIBLE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		bshow.setVisibility(View.INVISIBLE);
		new Thread(new Runnable() {

			@Override
			public void run() {
				ext.Post("setgamerunning.php", ext.Params(Integer.valueOf(id).toString(), "3"));
				Log.d("jc", "bshow-setgamerunning =3");
			}
		}).start();
	}

	public void bpack(View v) {
		pack();
	}

	private void pack() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				ext.Post("setstatus.php", ext.Params(Integer.valueOf(id).toString(), Integer.valueOf(position).toString(), "3"));
				Log.d("jc", "pack-setstatus=3");
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						ccardsi(position);
						bpack.setVisibility(View.INVISIBLE);
						if (bsee != null)
							bsee.setVisibility(View.INVISIBLE);
						bmakebid.setVisibility(View.INVISIBLE);
						bshow.setVisibility(View.INVISIBLE);
					}
				});
				Log.d("jc", "switchturn from pack");
				switchturn();
			}
		}).start();
	}

	public void bsee(View v) {
		bsee.setVisibility(View.INVISIBLE);
		bsee = null;
		new Thread(new Runnable() {

			@Override
			public void run() {
				ext.Post("setstatus.php", ext.Params(Integer.valueOf(id).toString(), Integer.valueOf(position).toString(), "2"));
				Log.d("jc", "bsee-setstatus 2");
				showcards(position);
			}
		}).start();
	}

	private void showcards(int playerno) {
		switch (playerno) {
			case 1:
				temps = c1;
				break;
			case 2:
				temps = c2;
				break;
			case 3:
				temps = c3;
				break;
			case 4:
				temps = c4;
				break;
			case 5:
				temps = c5;
				break;
		}
		cs = temps.split("\\.");
		Log.d("jc","showcards-"+playerno+"-cards-"+cs[0]+","+cs[1]+","+cs[2]);
		playernu = playerno;
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				returncards(cs, playernu);
			}
		});
	}

	private void returncards(String[] cs, int playerno) {
		ImageView[] iv = new ImageView[3];
		switch (playerno) {
			case 1:
				iv[0] = ic1;
				iv[1] = ic2;
				iv[2] = ic3;
				break;
			case 2:
				iv[0] = ic4;
				iv[1] = ic5;
				iv[2] = ic6;
				break;
			case 3:
				iv[0] = ic7;
				iv[1] = ic8;
				iv[2] = ic9;
				break;
			case 4:
				iv[0] = ic10;
				iv[1] = ic11;
				iv[2] = ic12;
				break;
			case 5:
				iv[0] = ic13;
				iv[1] = ic14;
				iv[2] = ic15;
				break;
		}
		for (int i = 0; i < 3; i++) {
			InputStream ims;
			try {
				String tem = "c" + cs[i] + ".png";
				ims = getAssets().open(tem);
				Drawable d = Drawable.createFromStream(ims, null);
				iv[i].setImageDrawable(d);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void makebid(int i, Integer ambidding) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				bpack.setVisibility(View.INVISIBLE);
				try {
					bsee.setVisibility(View.INVISIBLE);
				} catch (Exception e) {
					e.printStackTrace();
				}
				bmakebid.setVisibility(View.INVISIBLE);
				bshow.setVisibility(View.INVISIBLE);
			}
		});
		switch (position) {
			case 1:
				temp = s1;
				break;
			case 2:
				temp = s2;
				break;
			case 3:
				temp = s3;
				break;
			case 4:
				temp = s4;
				break;
			case 5:
				temp = s5;
				break;
		}
		if (ambidding < 1000) {
			ambidding = ambidding * temp * i / 2;
		} else
			ambidding = 1000;
		if (points > ambidding) {
			points -= ambidding;
			midpoints += ambidding;
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					tp.setText(Integer.valueOf(points).toString());
					tmidpoints.setText(Integer.valueOf(midpoints).toString());
				}
			});
			Log.d("jc", "switchturn from makebid");
			switchturn();
			ext.Post("setbidamount.php", ext.Params(Integer.valueOf(id).toString(), Integer.valueOf(bidamount).toString(), Integer.valueOf(midpoints).toString()));
			ext.Post("setpoints.php", ext.Params(ext.iLineinFile(0), Integer.valueOf(points).toString()));
			Log.d("jc", "makebid-bid,points,turn");
		} else {
			Toast.makeText(getApplicationContext(), "You dont have points to bid", Toast.LENGTH_LONG).show();
			new Thread(new Runnable() {

				@Override
				public void run() {
					Log.d("jc", "switchturn from makebid");
					switchturn();
					ext.Post("deleteme.php", ext.Params(Integer.valueOf(id).toString(), Integer.valueOf(position).toString()));
					Log.d("jc", "makebid-deleteme");
				}
			}).start();
			finish();
		}
	}

	public void bmakebid(View v) {
		makebid.show();
	}

	/*
	 * private void initcards() { c1 = js.getString("c1"); c2 = js.getString("c2"); c3 = js.getString("c3"); c4 = js.getString("c4"); c5 = js.getString("c5"); for (int i = 1; i < 6; i++) { int j = 0; for (String retval :
	 * c.split("-", 3)) { ac[j] = retval; j++; } } }
	 */
	private void updatetomeonly() {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				tp.setVisibility(View.INVISIBLE);
				switch (position) {
					case 1:
						tp = (TextView) findViewById(R.id.tp1);
						break;
					case 2:
						tp = (TextView) findViewById(R.id.tp2);
						break;
					case 3:
						tp = (TextView) findViewById(R.id.tp3);
						break;
					case 4:
						tp = (TextView) findViewById(R.id.tp4);
						break;
					case 5:
						tp = (TextView) findViewById(R.id.tp5);
						break;
				}
				tp.setText(Integer.valueOf(points).toString());
				tp.setVisibility(View.VISIBLE);
			}
		});
	}

	private void updateonstatus() throws JSONException {
		// check if person is in game else invisible his things------------
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// invisible all cards
				// invisiblecards();
				for (int i = 1; i < 6; i++) {
					// fake variables
					ts = null;
					ip = null;
					tn = null;
					Integer temps = null;
					switch (i) {
						case 1:
							tn = (TextView) findViewById(R.id.tn1);
							ip = (ImageView) findViewById(R.id.ip1);
							ts = (TextView) findViewById(R.id.ts1);
							tn.setText(p1);
							ts.setVisibility(View.INVISIBLE);
							ip.setVisibility(View.INVISIBLE);
							tn.setVisibility(View.INVISIBLE);
							temps = s1;
							break;
						case 2:
							tn = (TextView) findViewById(R.id.tn2);
							ip = (ImageView) findViewById(R.id.ip2);
							ts = (TextView) findViewById(R.id.ts2);
							tn.setText(p2);
							ts.setVisibility(View.INVISIBLE);
							ip.setVisibility(View.INVISIBLE);
							tn.setVisibility(View.INVISIBLE);
							temps = s2;
							break;
						case 3:
							tn = (TextView) findViewById(R.id.tn3);
							ip = (ImageView) findViewById(R.id.ip3);
							ts = (TextView) findViewById(R.id.ts3);
							tn.setText(p3);
							ts.setVisibility(View.INVISIBLE);
							ip.setVisibility(View.INVISIBLE);
							tn.setVisibility(View.INVISIBLE);
							temps = s3;
							break;
						case 4:
							tn = (TextView) findViewById(R.id.tn4);
							ip = (ImageView) findViewById(R.id.ip4);
							ts = (TextView) findViewById(R.id.ts4);
							tn.setText(p4);
							ts.setVisibility(View.INVISIBLE);
							ip.setVisibility(View.INVISIBLE);
							tn.setVisibility(View.INVISIBLE);
							temps = s4;
							break;
						case 5:
							tn = (TextView) findViewById(R.id.tn5);
							ip = (ImageView) findViewById(R.id.ip5);
							ts = (TextView) findViewById(R.id.ts5);
							tn.setText(p5);
							ts.setVisibility(View.INVISIBLE);
							ip.setVisibility(View.INVISIBLE);
							tn.setVisibility(View.INVISIBLE);
							temps = s5;
							break;
					}
					temp = i;
					if (temps == 0) {
						// imageblur
						ip.setAlpha(0.4F);
					} else if (temps == 1) {
						// draw image
						ts.setText("blind");
						ccards(temp);
						ip.setAlpha(1.0F);
						ts.setVisibility(View.VISIBLE);
						ip.setVisibility(View.VISIBLE);
						tn.setVisibility(View.VISIBLE);
					} else if (temps == 2) {
						// draw image
						ts.setText("seen cards");
						ccards(temp);
						ip.setAlpha(1.0F);
						ts.setVisibility(View.VISIBLE);
						ip.setVisibility(View.VISIBLE);
						tn.setVisibility(View.VISIBLE);
					} else if (temps == 3) {
						// draw image
						ip.setAlpha(0.6F);
						ts.setText("pack");
						ts.setVisibility(View.VISIBLE);
						ip.setVisibility(View.VISIBLE);
						tn.setVisibility(View.VISIBLE);
						// imageblur
					}
				}
			}
		});
	}

	private void ccards(int i) {
		switch (i) {
			case 1:
				ic1.setVisibility(View.VISIBLE);
				ic2.setVisibility(View.VISIBLE);
				ic3.setVisibility(View.VISIBLE);
				break;
			case 2:
				ic4.setVisibility(View.VISIBLE);
				ic5.setVisibility(View.VISIBLE);
				ic6.setVisibility(View.VISIBLE);
				break;
			case 3:
				ic7.setVisibility(View.VISIBLE);
				ic8.setVisibility(View.VISIBLE);
				ic9.setVisibility(View.VISIBLE);
				break;
			case 4:
				ic10.setVisibility(View.VISIBLE);
				ic11.setVisibility(View.VISIBLE);
				ic12.setVisibility(View.VISIBLE);
				break;
			case 5:
				ic13.setVisibility(View.VISIBLE);
				ic14.setVisibility(View.VISIBLE);
				ic15.setVisibility(View.VISIBLE);
				break;
		}
	}

	private void ccardsi(int i) {
		switch (i) {
			case 1:
				ic1.setVisibility(View.INVISIBLE);
				ic2.setVisibility(View.INVISIBLE);
				ic3.setVisibility(View.INVISIBLE);
				break;
			case 2:
				ic4.setVisibility(View.INVISIBLE);
				ic5.setVisibility(View.INVISIBLE);
				ic6.setVisibility(View.INVISIBLE);
				break;
			case 3:
				ic7.setVisibility(View.INVISIBLE);
				ic8.setVisibility(View.INVISIBLE);
				ic9.setVisibility(View.INVISIBLE);
				break;
			case 4:
				ic10.setVisibility(View.INVISIBLE);
				ic11.setVisibility(View.INVISIBLE);
				ic12.setVisibility(View.INVISIBLE);
				break;
			case 5:
				ic13.setVisibility(View.INVISIBLE);
				ic14.setVisibility(View.INVISIBLE);
				ic15.setVisibility(View.INVISIBLE);
				break;
		}
	}

	private void invisiblecards() {
		ic1.setVisibility(View.INVISIBLE);
		ic2.setVisibility(View.INVISIBLE);
		ic3.setVisibility(View.INVISIBLE);
		ic4.setVisibility(View.INVISIBLE);
		ic5.setVisibility(View.INVISIBLE);
		ic6.setVisibility(View.INVISIBLE);
		ic7.setVisibility(View.INVISIBLE);
		ic8.setVisibility(View.INVISIBLE);
		ic9.setVisibility(View.INVISIBLE);
		ic10.setVisibility(View.INVISIBLE);
		ic11.setVisibility(View.INVISIBLE);
		ic12.setVisibility(View.INVISIBLE);
		ic13.setVisibility(View.INVISIBLE);
		ic14.setVisibility(View.INVISIBLE);
		ic15.setVisibility(View.INVISIBLE);
	}

	private void updateonturn() {
		// ,
		switch (turn) {
			case 1:
				ih1.setVisibility(View.VISIBLE);
				break;
			case 2:
				ih2.setVisibility(View.VISIBLE);
				break;
			case 3:
				ih3.setVisibility(View.VISIBLE);
				break;
			case 4:
				ih4.setVisibility(View.VISIBLE);
				break;
			case 5:
				ih5.setVisibility(View.VISIBLE);
				break;
		}
	}

	private void updatealways() {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				tmidpoints.setText(Integer.valueOf(midpoints).toString());
				AlphaAnimation alpha = new AlphaAnimation(0.4F, 1.0F); // change values as you want
				alpha.setDuration(1000); // Make animation instant
				alpha.setFillAfter(true); // Tell it to persist after the animation ends
				// And then on your imageview
				ImageView iv = null;
				switch (turn) {
					case 1:
						iv = (ImageView) findViewById(R.id.ip1);
						break;
					case 2:
						iv = (ImageView) findViewById(R.id.ip2);
						break;
					case 3:
						iv = (ImageView) findViewById(R.id.ip3);
						break;
					case 4:
						iv = (ImageView) findViewById(R.id.ip4);
						break;
					case 5:
						iv = (ImageView) findViewById(R.id.ip5);
						break;
				}
				iv.startAnimation(alpha);
			}
		});
	}

	private void update() throws JSONException {
		// tp,tn--only shows to me,depening on position
		updatetomeonly();
		// ts,cards--updates depending on status
		updateonstatus();
		// turn highlighting--depends on turn
		// -----------------------------------------------------------
		// updateonturn();
		// midpoints--always always
		updatealways();
	}

	private void updatedb() {
	}

	private void gettingdata() throws JSONException {
		time = js.getInt("time");
		points = js.getInt("points");
		gamerunning = js.getInt("gamerunning");
		bidamount = js.getInt("bidamount");
		midpoints = js.getInt("midpoints");
		turn = js.getInt("turn");
		s = js.getInt("s");
		s1 = js.getInt("s1");
		s2 = js.getInt("s2");
		s3 = js.getInt("s3");
		s4 = js.getInt("s4");
		s5 = js.getInt("s5");
		p1 = js.getString("p1");
		p2 = js.getString("p2");
		p3 = js.getString("p3");
		p4 = js.getString("p4");
		p5 = js.getString("p5");
		c1 = js.getString("c1");
		c2 = js.getString("c2");
		c3 = js.getString("c3");
		c4 = js.getString("c4");
		c5 = js.getString("c5");
		// cun = js.getInt("cun");
	}

	private void beforegameinits() throws JSONException {
		turnset = 0;
		username = ext.iLineinFile(0);
		ip1 = (ImageView) findViewById(R.id.ip1);
		ip2 = (ImageView) findViewById(R.id.ip2);
		ip3 = (ImageView) findViewById(R.id.ip3);
		ip4 = (ImageView) findViewById(R.id.ip4);
		ip5 = (ImageView) findViewById(R.id.ip5);
		ic1 = (ImageView) findViewById(R.id.ic1);
		ic2 = (ImageView) findViewById(R.id.ic2);
		ic3 = (ImageView) findViewById(R.id.ic3);
		ic4 = (ImageView) findViewById(R.id.ic4);
		ic5 = (ImageView) findViewById(R.id.ic5);
		ic6 = (ImageView) findViewById(R.id.ic6);
		ic7 = (ImageView) findViewById(R.id.ic7);
		ic8 = (ImageView) findViewById(R.id.ic8);
		ic9 = (ImageView) findViewById(R.id.ic9);
		ic10 = (ImageView) findViewById(R.id.ic10);
		ic11 = (ImageView) findViewById(R.id.ic11);
		ic12 = (ImageView) findViewById(R.id.ic12);
		ic13 = (ImageView) findViewById(R.id.ic13);
		ic14 = (ImageView) findViewById(R.id.ic14);
		ic15 = (ImageView) findViewById(R.id.ic15);
		ic1.setImageResource(R.drawable.ccard);
		ic2.setImageResource(R.drawable.ccard);
		ic3.setImageResource(R.drawable.ccard);
		ic4.setImageResource(R.drawable.ccard);
		ic5.setImageResource(R.drawable.ccard);
		ic6.setImageResource(R.drawable.ccard);
		ic7.setImageResource(R.drawable.ccard);
		ic8.setImageResource(R.drawable.ccard);
		ic9.setImageResource(R.drawable.ccard);
		ic10.setImageResource(R.drawable.ccard);
		ic11.setImageResource(R.drawable.ccard);
		ic12.setImageResource(R.drawable.ccard);
		ic13.setImageResource(R.drawable.ccard);
		ic14.setImageResource(R.drawable.ccard);
		ic15.setImageResource(R.drawable.ccard);
		tp1 = (TextView) findViewById(R.id.tp1);
		tp2 = (TextView) findViewById(R.id.tp2);
		tp3 = (TextView) findViewById(R.id.tp3);
		tp4 = (TextView) findViewById(R.id.tp4);
		tp5 = (TextView) findViewById(R.id.tp5);
		tn1 = (TextView) findViewById(R.id.tn1);
		tn2 = (TextView) findViewById(R.id.tn2);
		tn3 = (TextView) findViewById(R.id.tn3);
		tn4 = (TextView) findViewById(R.id.tn4);
		tn5 = (TextView) findViewById(R.id.tn5);
		trp1 = (TextView) findViewById(R.id.trp1);
		trp2 = (TextView) findViewById(R.id.trp2);
		trp3 = (TextView) findViewById(R.id.trp3);
		trp4 = (TextView) findViewById(R.id.trp4);
		trp5 = (TextView) findViewById(R.id.trp5);
		tmidpoints = (TextView) findViewById(R.id.tmidpoints);
		tmidpoints.setText("0");
		bmakebid = (Button) findViewById(R.id.bmakebid);
		bpack = (Button) findViewById(R.id.bpack);
		bsee = (Button) findViewById(R.id.bsee);
		bshow = (Button) findViewById(R.id.bshow);
		// make invisible
		ip1.setVisibility(View.INVISIBLE);
		ip2.setVisibility(View.INVISIBLE);
		ip3.setVisibility(View.INVISIBLE);
		ip4.setVisibility(View.INVISIBLE);
		ip5.setVisibility(View.INVISIBLE);
		ic1.setVisibility(View.INVISIBLE);
		ic2.setVisibility(View.INVISIBLE);
		ic3.setVisibility(View.INVISIBLE);
		ic4.setVisibility(View.INVISIBLE);
		ic5.setVisibility(View.INVISIBLE);
		ic6.setVisibility(View.INVISIBLE);
		ic7.setVisibility(View.INVISIBLE);
		ic8.setVisibility(View.INVISIBLE);
		ic9.setVisibility(View.INVISIBLE);
		ic10.setVisibility(View.INVISIBLE);
		ic11.setVisibility(View.INVISIBLE);
		ic12.setVisibility(View.INVISIBLE);
		ic13.setVisibility(View.INVISIBLE);
		ic14.setVisibility(View.INVISIBLE);
		ic15.setVisibility(View.INVISIBLE);
		tn1.setVisibility(View.INVISIBLE);
		tn2.setVisibility(View.INVISIBLE);
		tn3.setVisibility(View.INVISIBLE);
		tn4.setVisibility(View.INVISIBLE);
		tn5.setVisibility(View.INVISIBLE);
		tp1.setVisibility(View.INVISIBLE);
		tp2.setVisibility(View.INVISIBLE);
		tp3.setVisibility(View.INVISIBLE);
		tp4.setVisibility(View.INVISIBLE);
		tp5.setVisibility(View.INVISIBLE);
		trp1.setVisibility(View.INVISIBLE);
		trp2.setVisibility(View.INVISIBLE);
		trp3.setVisibility(View.INVISIBLE);
		trp4.setVisibility(View.INVISIBLE);
		trp5.setVisibility(View.INVISIBLE);
		bmakebid.setVisibility(View.INVISIBLE);
		bpack.setVisibility(View.INVISIBLE);
		bsee.setVisibility(View.INVISIBLE);
		bshow.setVisibility(View.INVISIBLE);
		switch (position) {
			case 1:
				tp = (TextView) findViewById(R.id.tp1);
				tp.setVisibility(View.INVISIBLE);
				break;
			case 2:
				tp = (TextView) findViewById(R.id.tp2);
				tp.setVisibility(View.INVISIBLE);
				break;
			case 3:
				tp = (TextView) findViewById(R.id.tp3);
				tp.setVisibility(View.INVISIBLE);
				break;
			case 4:
				tp = (TextView) findViewById(R.id.tp4);
				tp.setVisibility(View.INVISIBLE);
				break;
			case 5:
				tp = (TextView) findViewById(R.id.tp5);
				tp.setVisibility(View.INVISIBLE);
				break;
		}
		// tmidpoints.setVisibility(View.INVISIBLE);
		// --------------------recent points trp
		ac1 = new int[3];
		ac2 = new int[3];
		ac3 = new int[3];
		ac4 = new int[3];
		ac5 = new int[3];
		card = new ImageView[15];
	}

	private void gameupdate() throws JSONException, InterruptedException {
		while (true) {
			if (gamerunning == 4) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						StartMatch();
					}
				});
				break;
			}
			String result = ext.Get(ext.Post("getdetails.php", ext.Params(Integer.valueOf(id).toString(), ext.iLineinFile(0))));
			js = new JSONObject(result);
			if (js.getInt("success") == 1) {
				gettingdata();
			} else {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(getApplicationContext(), "Room has been closed", Toast.LENGTH_SHORT).show();
						finish();
					}
				});
				break;
			}
			update();
			int s = 0;
			switch (position) {
				case 1:
					s = s1;
					break;
				case 2:
					s = s2;
					break;
				case 3:
					s = s3;
					break;
				case 4:
					s = s4;
					break;
				case 5:
					s = s5;
					break;
			}
			if (gamerunning == 1 && (s == 1 || s == 2)) {
				ingameupdate();
			} else if (gamerunning == 3) {
				if (activestatus() < 2) {
					winner(lastplayer());
				} else {
					// showcards(position);
					while (true) {
						String sc = ext.Get(ext.Post("getwinner.php", ext.Params(Integer.valueOf(id).toString())));
						JSONObject js = null;
						js = new JSONObject(sc);
						int temp = js.getInt("winner");
						if (temp == 0)
							continue;
						String sca = ext.Get(ext.Post("last2players.php", ext.Params(Integer.valueOf(id).toString())));
						JSONObject jsa = null;
						jsa = new JSONObject(sca);
						int k = jsa.getInt("l1");
						int l = jsa.getInt("l2");
						Log.d("jc","before showcards call k,l-"+k+","+l);
						showcards(k);
						showcards(l);
						winner(temp);
						break;
					}
				}
				gamerunning = 4;
			}
			Thread.sleep(1000);
		}
	}
	
	private void ifp1() throws JSONException, InterruptedException {
		ext.Post("beforegameinits.php", ext.Params(Integer.valueOf(id).toString()));
		al = new ArrayList<Integer>(52);
		for (int i = 0; i < 52; i++) {
			al.add(i);
		}
		Collections.shuffle(al);
		p = new ArrayList<Person>();
		Integer i = 0;
		do {
			Person pe = new Person(al);
			pe.i = i;
			p.add(pe);
			ext.Post("setcards.php", ext.Params(Integer.valueOf(id).toString(), i.toString(), p.get(p.size() - 1).c[0].card.toString() + "." + p.get(p.size() - 1).c[1].card.toString() + "."
					+ p.get(p.size() - 1).c[2].card.toString()));
			i++;
		} while (i < 6);
		i = 0;
		ext.Post("makestatus.php", ext.Params(Integer.valueOf(id).toString()));
		// ext.Post("setgamerunning.php", ext.Params(id.toString(), "1"));
		Log.d("jc", "ifp1-makestatus,setgamerunning to 1");
		gamerunning = 1;
		while (true) {
			Thread.sleep(3000);
			/*
			 * if (midpoints >= 1000) { winner(comparecards()); } else if (activenoofplayers() > 1) { Integer temp = turn; // ..make another thread or handle in a better way than sleeping for 20 secs Thread.sleep(20000); if
			 * (temp == turn) { switchturn(); ext.Post("setstatus.php", ext.Params(id.toString(), position.toString(), "0")); ext.Post("setturn.php", ext.Params(id.toString(), turn.toString())); } }
			 */
			if (activestatus() < 2) {
				ext.Post("setgamerunning.php", ext.Params(Integer.valueOf(id).toString(), "3"));
				Log.d("jc", "ifp1-gamerunning 3");
				gamerunning = 3;
				break;
			}
			if (gamerunning == 3) {
				Log.d("jc", "ifp1,gamerunning=3, checking winner");
				String sc = ext.Get(ext.Post("last2players.php", ext.Params(Integer.valueOf(id).toString())));
				JSONObject js = null;
				js = new JSONObject(sc);
				int k = js.getInt("l1");
				int l = js.getInt("l2");
				int temp = compareonly(k, l);
				Log.d("jc", "winner - " + temp);
				ext.Post("setwinner.php", ext.Params(Integer.valueOf(id).toString(), Integer.valueOf(temp).toString()));
				break;
			}
		}
	}

	private void StartMatch() {
		Log.d("jc","-----------------------------------------------------------");
		loading = false;
		gamerunning = 0;
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					if (position == 1)
						ifp1();
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
		//wait for ifp1 to load
		//mean while make initial bidding,and some stuff
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		try {
			beforegameinits();
		} catch (JSONException e2) {
			e2.printStackTrace();
		}
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					gameupdate();
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	private int activestatus() {
		String result = ext.Get(ext.Post("activestatus.php", ext.Params(Integer.valueOf(id).toString())));
		JSONObject js;
		Integer active = null;
		try {
			js = new JSONObject(result);
			active = js.getInt("active");
		} catch (JSONException e) {
			e.printStackTrace();
		}/*
		 * if (active < 2) { winner(lastplayer()); gamerunning = 0; ext.Post("setgamerunning.php", ext.Params(id.toString(), "0")); }
		 */
		Log.d("jc", "activestatus-" + active);
		return active;
	}

	private void winner(int i) {
		winner = i;
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(getApplicationContext(), "Winner is " + winner, Toast.LENGTH_LONG).show();
				/*
				 * RelativeLayout layout = new RelativeLayout(StartGame.this); RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				 * relativeParams.addRule(RelativeLayout.CENTER_IN_PARENT); rel.addView(icrown, relativeParams);
				 */
				if (icrown != null)
					icrown.setVisibility(View.INVISIBLE);
				switch (winner) {
					case 1:
						icrown = (ImageView) findViewById(R.id.icrown1);
						break;
					case 2:
						icrown = (ImageView) findViewById(R.id.icrown2);
						break;
					case 3:
						icrown = (ImageView) findViewById(R.id.icrown3);
						break;
					case 4:
						icrown = (ImageView) findViewById(R.id.icrown4);
						break;
					case 5:
						icrown = (ImageView) findViewById(R.id.icrown5);
						break;
				}
				icrown.setVisibility(View.VISIBLE);
			}
		});
		if (position == i) {
			ext.Post("addpoints.php", ext.Params(ext.iLineinFile(0), Integer.valueOf(bidamount).toString()));
			Log.d("jc", "winner-add points-"+bidamount);
		}
	}

	private int lastplayer() {
		String result = ext.Get(ext.Post("lastplayer.php", ext.Params(Integer.valueOf(id).toString())));
		Log.d("jc", "lastplayer-" + result);
		try {
			JSONObject js = new JSONObject(result);
			int lastplayer = js.getInt("lastplayer");
			return lastplayer;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return 5;
	}

	private int compareonly(int i, int j) {
		ArrayList<Person> o = new ArrayList<Person>(5);
		o.add(p.get(i));
		o.add(p.get(j));
		Collections.sort(o, Person.pcom);
		i=o.get(0).i;
		o.removeAll(p);
		return i;
	}

	private int comparecards() {
		// Collections.sort(p, Person.pcom);
		Collections.sort(p, Person.pcom);
		System.out.println("winner-" + p.get(0).i + "--" + p.get(0).rank + ", reason " + p.get(0).reason);
		return p.get(0).i;
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*
		 * rel = (RelativeLayout) findViewById(R.id.rel); setContentView(rel);
		 */
		setContentView(R.layout.a_startgame);
	}

	@Override
	protected void onStart() {
		super.onStart();
		id = getIntent().getExtras().getInt("id");
		position = getIntent().getExtras().getInt("position");
		exitdiag = new AlertDialog.Builder(StartGame.this);
		exitdiag.setMessage("Do you want to Quit game?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int idk) {
				// stop updating
				new Thread() {

					public void run() {
						ext.Post("deleteme.php", ext.Params(Integer.valueOf(id).toString(), Integer.valueOf(position).toString()));
					}
				}.start();
				finish();
			}
		}).setNegativeButton("No", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int id) {
				// just sit silent
			}
		});
		makebid = new AlertDialog.Builder(StartGame.this);
		makebid.setMessage("Make Bid").setPositiveButton("Maximum", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int id) {
				new Thread() {

					public void run() {
						Log.d("jc","maximum clicked");
						makebid(2, bidamount);
					}
				}.start();
				bmakebid.setVisibility(View.INVISIBLE);
			}
		}).setNegativeButton("Minimum", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int id) {
				new Thread() {

					public void run() {
						Log.d("jc","minimum clicked");
						makebid(1, bidamount);
					}
				}.start();
				bmakebid.setVisibility(View.INVISIBLE);
			}
		});
		ext = new Extras(getFilesDir().getParent());
		StartMatch();
	}

	@Override
	protected void onStop() {
		super.onStop();
		new Thread() {

			public void run() {
				ext.Post("deleteme.php", ext.Params(Integer.valueOf(id).toString(), Integer.valueOf(position).toString()));
			}
		}.start();
		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (loading)
				return true;
			else
				exitdiag.show();
			return true;
		} else
			return super.onKeyDown(keyCode, event);
	}

	@Override
	public void finish() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				ext.Post("deleteme.php", ext.Params(Integer.valueOf(id).toString(), Integer.valueOf(position).toString()));
			}
		}).start();
		super.finish();
	}
}