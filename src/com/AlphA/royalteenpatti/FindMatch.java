package com.AlphA.royalteenpatti;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class FindMatch extends Activity {

	TextView	tresult, ttime;
	Extras		ext;
	Integer		id, time, t;
	Integer		position;
	Integer		active		= 0;
	int			starting	= 0;
	Thread		th;
	ImageView	ip1, ip2, ip3, ip4, ip5, ip;
	String		p;
	boolean intentset=true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_findmatch);
	}

	@Override
	protected void onStart() {
		super.onStart();
		tresult = (TextView) findViewById(R.id.tresult);
		tresult.setText("");
		ext = new Extras();
		th = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					update();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void update() throws Exception {
		while (true) {
			if (starting == 1)
				break;
			String s = ext.Get(ext.Post("getps.php", ext.Params(id.toString())));
			JSONObject js = new JSONObject(s);
			for (int i = 1; i < 6; i++) {
				switch (i) {
					case 1:
						p = js.getString("p1");
						break;
					case 2:
						p = js.getString("p2");
						break;
					case 3:
						p = js.getString("p3");
						break;
					case 4:
						p = js.getString("p4");
						break;
					case 5:
						p = js.getString("p5");
						break;
				}
				if (!p.equals("0")) {
					switch (i) {
						case 1:
							ip = ip1;
							break;
						case 2:
							ip = ip2;
							break;
						case 3:
							ip = ip3;
							break;
						case 4:
							ip = ip4;
							break;
						case 5:
							ip = ip5;
							break;
					}
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							ip.setVisibility(View.VISIBLE);
						}
					});
				}else if(p.equals("0")) {
					switch (i) {
						case 1:
							ip = ip1;
							break;
						case 2:
							ip = ip2;
							break;
						case 3:
							ip = ip3;
							break;
						case 4:
							ip = ip4;
							break;
						case 5:
							ip = ip5;
							break;
					}
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							ip.setVisibility(View.INVISIBLE);
						}
					});
				}
			}
			Thread.sleep(500);
		}
	}

	public void searchroom(View v) {
		intentset=true;
		new Thread(new Runnable() {

			public void run() {
				String result = ext.Get(ext.Post("findmatches.php", ext.Params(ext.iLineinFile(0))));
				int success = 0;
				JSONObject jsresult;
				try {
					jsresult = new JSONObject(result);
					success = jsresult.getInt("success");
					id = jsresult.getInt("id");
					position = jsresult.getInt("position");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				if (success == 1) {
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							tresult.setText("room found");
							// set the image
							setContentView(R.layout.a_waiting);
							ip1 = (ImageView) findViewById(R.id.ipwr1);
							ip2 = (ImageView) findViewById(R.id.ipwr2);
							ip3 = (ImageView) findViewById(R.id.ipwr3);
							ip4 = (ImageView) findViewById(R.id.ipwr4);
							ip5 = (ImageView) findViewById(R.id.ipwr5);
							ip1.setVisibility(View.INVISIBLE);
							ip2.setVisibility(View.INVISIBLE);
							ip3.setVisibility(View.INVISIBLE);
							ip4.setVisibility(View.INVISIBLE);
							ip5.setVisibility(View.INVISIBLE);
							ttime = (TextView) findViewById(R.id.ttime);
							starting = 0;
							th.start();
						}
					});
					new Thread(new Runnable() {

						@Override
						public void run() {
							while (true) {
								String s = ext.Get(ext.Post("gettime.php", ext.Params(id.toString())));
								JSONObject js;
								int success = 0;
								try {
									js = new JSONObject(s);
									success = js.getInt("success");
									time = js.getInt("time");
								} catch (JSONException e) {
									e.printStackTrace();
								}
								if (success == 1 && time > 1) {
									runOnUiThread(new Runnable() {

										@Override
										public void run() {
											ttime.setText(time.toString());
										}
									});
								} else if (success == 0) {
									runOnUiThread(new Runnable() {

										@Override
										public void run() {
											finish();
										}
									});
								} else {
									runOnUiThread(new Runnable() {

										@Override
										public void run() {
											if(intentset) {
												intentset=false;
												Intent intent;
												intent = new Intent(FindMatch.this, StartGame.class);
												intent.putExtra("id", id);
												intent.putExtra("position", position);
												startActivity(intent);
												finish();
											}
											starting = 1;
										}
									});
								}
								if (starting == 1)
									break;
							}
						}
					}).start();
				} else {
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							Toast.makeText(getApplicationContext(), "No room found", Toast.LENGTH_SHORT).show();
						}
					});
				}
			}
		}).start();
	}

	@Override
	protected void onStop() {
		super.onStop();
		/*new Thread() {

			public void run() {
				try {
					ext.Post("deleteme.php", ext.Params(id.toString(), position.toString()));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();*/
		finish();
	}

	public void createroom(View v) {
		intentset=true;
		t = 5;
		new Thread(new Runnable() {

			public void run() {
				String result = ext.Get(ext.Post("creatematch.php", ext.Params(ext.iLineinFile(0))));
				int success = 0;
				JSONObject jsresult;
				try {
					jsresult = new JSONObject(result);
					success = jsresult.getInt("success");
					id = jsresult.getInt("id");
					position = 1;
				} catch (JSONException e) {
					e.printStackTrace();
				}
				if (success == 1) {
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							tresult.setText("room created");
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							// set the image
							setContentView(R.layout.a_waiting);
							ip1 = (ImageView) findViewById(R.id.ipwr1);
							ip2 = (ImageView) findViewById(R.id.ipwr2);
							ip3 = (ImageView) findViewById(R.id.ipwr3);
							ip4 = (ImageView) findViewById(R.id.ipwr4);
							ip5 = (ImageView) findViewById(R.id.ipwr5);
							ip1.setVisibility(View.INVISIBLE);
							ip2.setVisibility(View.INVISIBLE);
							ip3.setVisibility(View.INVISIBLE);
							ip4.setVisibility(View.INVISIBLE);
							ip5.setVisibility(View.INVISIBLE);
							ttime = (TextView) findViewById(R.id.ttime);
							starting = 0;
							th.start();
						}
					});
					// set image get position
					// before start check atleast one player is there at regular intervals or when host press start
					new Thread(new Runnable() {

						@Override
						public void run() {
							t = 10;
							while (true) {
								runOnUiThread(new Runnable() {

									@Override
									public void run() {
										ttime.setText(t.toString());
									}
								});
								if (t > 0) {
									ext.Post("settime.php", ext.Params(id.toString(), t.toString()));
									t--;
								} else {
									String s = ext.Get(ext.Post("activeplayers.php", ext.Params(id.toString())));
									JSONObject js;
									try {
										js = new JSONObject(s);
										active = js.getInt("active");
									} catch (JSONException e) {
										e.printStackTrace();
									}
									runOnUiThread(new Runnable() {

										@Override
										public void run() {
											if (active < 2) {
												Toast.makeText(getApplicationContext(), "Room was empty", Toast.LENGTH_SHORT).show();
												finish();
												new Thread(new Runnable() {
													
													@Override
													public void run() {
														ext.Post("deleteme.php", ext.Params(id.toString(), position.toString()));
													}
												}).start();
											} else if(active>1&&intentset){
												intentset=false;
												Intent intent;
												intent = new Intent(FindMatch.this, StartGame.class);
												intent.putExtra("id", id);
												intent.putExtra("position", position);
												startActivity(intent);
												finish();
											}
											starting = 1;
										}
									});
								}
								if (starting == 1)
									break;
								try {
									Thread.sleep(800);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						}
					}).start();
				}
			}
		}).start();
	}

	@Override
	protected void onPause() {
		super.onPause();
		finish();
	}
}