package com.AlphA.royalteenpatti;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainMenu extends Activity {

	AlertDialog.Builder	exitdiag;
	Button				bplaynow, bsubmit;
	TextView			terror;
	EditText			etusername, etemailid, etpassword;
	boolean				loading, fileexists, status = false;	// look for status
	String				statusstr	= "";
	Dialog				d;
	Extras				ext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_loading);
		loading = true;
	}

	public void playgame(View v) {
		if (!fileexists) {
			d.show();
		} else
			checkstatus();
	}

	public void points(View v) {
	}

	private void submit() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				String result = ext.Get(ext.Post("registration.php", ext.Params(etusername.getText().toString(), etemailid.getText().toString(), etpassword.getText().toString())));
				try {
					JSONObject js = new JSONObject(result);
					if (js.getInt("success") == 1) {
						String s = etusername.getText().toString() + "\n" + etemailid.getText().toString() + "\n" + etpassword.getText().toString();
						ext.writeStringAsFile(s);
						fileexists = true;
						ext.initDetails();
					} else {
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								terror.setVisibility(View.VISIBLE);
								d.show();
							}
						});
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).start();
		d.dismiss();
	}

	private void mainmenu() {
		bplaynow.setVisibility(View.VISIBLE);
		// bpoints.setVisibility(View.VISIBLE);
	}

	private void registDiag() {
		d = new Dialog(MainMenu.this);
		d.setContentView(R.layout.a_registration);
		d.setCanceledOnTouchOutside(false);
		d.setTitle("Register");
		bsubmit = (Button) d.findViewById(R.id.bsubmit);
		terror = (TextView) d.findViewById(R.id.terror);
		etusername = (EditText) d.findViewById(R.id.iusername);
		etemailid = (EditText) d.findViewById(R.id.iemail);
		etpassword = (EditText) d.findViewById(R.id.ipassword);
		bsubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				submit();
			}
		});
	}

	private void starting() {
		mainmenu();
		loading = false;
		if (fileexists)
			ext.initDetails();
		else {
			registDiag();
			d.show();
		}
	}

	private void checkstatus() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					String result = ext.Get(ext.Post("returnstatus.php", ext.Params(ext.iLineinFile(0))));
					JSONObject jsonobj = new JSONObject(result);
					if (jsonobj.getInt("status") == 1) {
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								Intent intent;
								intent = new Intent(MainMenu.this, FindMatch.class);
								startActivity(intent);
							}
						});
					} else if (jsonobj.getInt("status") == 0) {
						runOnUiThread(new Runnable() {

							public void run() {
								Toast.makeText(MainMenu.this, "waiting for account activation", Toast.LENGTH_LONG).show();
							}
						});
					} else {
						ext.deleteFile();
						finish();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	private void varinit() {
		ext = new Extras(getFilesDir().getParent());
		// refresh data i.e delete init file
		// ext.deleteFile();
		fileexists = ext.fileExist();
		setContentView(R.layout.a_mainmenu);
		bplaynow = (Button) findViewById(R.id.bplaynow);
		// bpoints = (Button) findViewById(R.id.bpoints);
		starting();
	}

	@Override
	protected void onStart() {
		super.onStart();
		setContentView(R.layout.a_loading);
		loading = true;
		exitdiag = new AlertDialog.Builder(MainMenu.this);
		exitdiag.setMessage("Do you want to Quit game?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int id) {
				finish();
			}
		}).setNegativeButton("No", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int id) {
				// just sit silent
			}
		});
		varinit();
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
}
