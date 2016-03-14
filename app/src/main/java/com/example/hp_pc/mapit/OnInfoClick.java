package com.example.hp_pc.mapit;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class OnInfoClick extends Activity {
    String tile;
    String desc;
    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_info_click);
        TextView textView= (TextView) findViewById(R.id.textView3);
        TextView textView1= (TextView) findViewById(R.id.textView4);


        Button delButton= (Button) findViewById(R.id.button);
        Intent intent=getIntent();
        String lat=intent.getStringExtra("lat1");
        String lon=intent.getStringExtra("lon1");

        MainDbAdapter mainDbAdapter=new MainDbAdapter(getApplicationContext());
       Cursor cursor= mainDbAdapter.fetchSelectiveLocationInfo(lat, lon);

        if (cursor.moveToNext()){
            tile=cursor.getString(cursor.getColumnIndex(MainDbAdapter.DbAdapter.tile));
            desc=cursor.getString(cursor.getColumnIndex(MainDbAdapter.DbAdapter.desc));
        }

        textView.setText(tile);
        textView1.setText(desc);


        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=getIntent();
                String lat=intent.getStringExtra("lat1");
                String lon=intent.getStringExtra("lon1");
                MainDbAdapter mainDbAdapter=new MainDbAdapter(getApplicationContext());
                int r=mainDbAdapter.delete(lat,lon);
                if (r>0){
                    Toast.makeText(getApplicationContext(),"deleted",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),MapScreen.class));
                }

                else {

                    Toast.makeText(getApplicationContext(),"not deleted",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_on_info_click, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
