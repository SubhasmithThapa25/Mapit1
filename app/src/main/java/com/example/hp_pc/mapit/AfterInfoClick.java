package com.example.hp_pc.mapit;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


public class AfterInfoClick extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_info_click);
        final EditText title= (EditText) findViewById(R.id.editTexttitle);
        final EditText desc= (EditText) findViewById(R.id.editTextevent);
        final TextView title1= (TextView) findViewById(R.id.titletextview);
        final TextView desc1= (TextView) findViewById(R.id.textviewevent);

        Button done= (Button) findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title2=title.getText().toString();
                String desc2=desc.getText().toString();
                Intent intent=getIntent();
                String lat=intent.getStringExtra("lat");
                String lon=intent.getStringExtra("lon");
               MainDbAdapter mainDbAdapter=new MainDbAdapter(getApplicationContext());
               int res= mainDbAdapter.update_info(title2,desc2,lat,lon);
               if (res>0){
                   Toast.makeText(getApplicationContext(),"Update successful",Toast.LENGTH_SHORT).show();
                   startActivity(new Intent(getApplicationContext(),MapScreen.class));
               }
                else {
                   Toast.makeText(getApplicationContext(),"Update failed",Toast.LENGTH_SHORT).show();
               }
                title1.setText(title2);
                desc1.setText(desc2);

            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_after_info_click, menu);
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
