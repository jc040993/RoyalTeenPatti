package com.AlphA.royalteenpatti;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_main);
        //let thread sleep for some time to make blackscreen visble for some time
        Intent i=new Intent(MainActivity.this, MainMenu.class);
		startActivity(i);
    }
    @Override
    protected void onPause() {
    	super.onPause();
    	finish();
    }
}