package com.example.hp_pc.mapit;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.pnikosis.materialishprogress.ProgressWheel;




public class SignUp extends Activity
             {
                 ProgressWheel progressWheel;
                 RelativeLayout relativeLayout;
                 LinearLayout signup,signupfields;

                 public void onBackPressed() {
                    Intent intent=new Intent(getApplicationContext(),LaunchScreen.class);
                     startActivity(intent);

                 }

             @Override
             protected void onCreate(Bundle savedInstanceState)
                            {
                                signup= (LinearLayout) findViewById(R.id.llsignup);

                                super.onCreate(savedInstanceState);

                                setContentView(R.layout.activity_sign_up);

                                final EditText signupname= (EditText) findViewById(R.id.signup_name);
                                final EditText signupemail= (EditText) findViewById(R.id.signup_email);
                                final EditText signuppass= (EditText) findViewById(R.id.signup_password);

                                final EditText repass= (EditText) findViewById(R.id.signup_repassword);



                            Button signup_btn= (Button) findViewById(R.id.signup_button);


                                   //Listener for sign up button
                                   signup_btn.setOnClickListener(new View.OnClickListener()
                                                                             {
                                                                                @Override
                                                                                public void onClick(View view)
                                                                                             {
                                   //Extracting values from edittexts
                                   String name = signupname.getText().toString();
                                   String email = signupemail.getText().toString();
                                   String pass = signuppass.getText().toString();
                                   String repassword = repass.getText().toString();
                                                

                                        if(pass.isEmpty()||name.isEmpty()||email.isEmpty()||repassword.isEmpty())
                                                       {

                                                       Toast.makeText(getApplicationContext(),"Fields are empty",Toast.LENGTH_LONG).show();
                                                       }

                                        else if (pass.equals(repassword))
                                                       {
                                                       repass.setBackgroundColor(Color.parseColor("#76FE9F"));

                                                       MainDbAdapter mainDbAdapter = new MainDbAdapter(getApplicationContext());
                                                       long a = mainDbAdapter.insertData(name, email, pass);
                                                        if (a > 0)
                                                                {

                                                                Toast.makeText(getApplicationContext(), "Registration Successful", Toast.LENGTH_LONG).show();
                                                                Intent intent=new Intent(getApplicationContext(),AfterSignUp.class);
                                                                startActivity(intent);
                                                                    finish();
                                                                }
                                                        else
                                                                {

                                                                Toast.makeText(getApplicationContext(), "Registration failed", Toast.LENGTH_LONG).show();
                                                                }


                                                       }//closing bracket for if

                                        else
                                                        {
                                                        repass.setBackgroundColor(Color.parseColor("#ff0000"));
                                                        Toast.makeText(getApplicationContext(),"Password did not Match",Toast.LENGTH_LONG).show();
                                                        }



                                                         }

                                                                                        });
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


