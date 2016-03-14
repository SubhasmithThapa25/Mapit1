package com.example.hp_pc.mapit;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.pnikosis.materialishprogress.ProgressWheel;


public class SignIn extends Activity {

    public void onBackPressed() {
        Intent intent=new Intent(getApplicationContext(),LaunchScreen.class);
        startActivity(intent);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        final EditText loginname= (EditText) findViewById(R.id.Loginname);
        final EditText loginpass = (EditText) findViewById(R.id.Loginpassword);


        //listener for viewall button
        Button viewall= (Button) findViewById(R.id.viewall);
        viewall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MainDbAdapter mainDbAdapter = new MainDbAdapter(getApplicationContext());
                String data = mainDbAdapter.fetchData();
                Toast.makeText(getApplicationContext(), data, Toast.LENGTH_LONG).show();
            }
        });

        //Listener for Login Button
        Button login= (Button) findViewById(R.id.Login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=loginname.getText().toString();
                String pass=loginpass.getText().toString();
                if(email.isEmpty()||pass.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Enter Email or Password",Toast.LENGTH_SHORT).show();

                }
                else
                {
                    MainDbAdapter mainDbAdapter=new MainDbAdapter(getApplicationContext());

                    Cursor cursor=mainDbAdapter.Login(email,pass);
                    String Name=null;
                    String Email=null;
                    String Password=null;

                    while (cursor.moveToNext()) {
                        Name = cursor.getString(cursor.getColumnIndex(MainDbAdapter.DbAdapter.Name));
                        Email = cursor.getString(cursor.getColumnIndex(MainDbAdapter.DbAdapter.Email));
                        Password=cursor.getString(cursor.getColumnIndex(MainDbAdapter.DbAdapter.Password));
                        Intent intent=new Intent(getApplicationContext(),AfterSignUp.class);
                        startActivity(intent);
                        finish();
                    }
                    /*Intent intent=new Intent(getApplicationContext(),AfterSignUp.class);
                    startActivity(intent);
*/
                } Toast.makeText(getApplicationContext(),"Email or Password wrong",Toast.LENGTH_LONG).show();
            }
        });

        //Listener for Google Button


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
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
