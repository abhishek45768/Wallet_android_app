package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Options extends AppCompatActivity implements View.OnClickListener {
    Button b1,b2,b3,b4;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        b1=findViewById(R.id.button5);
        b2=findViewById(R.id.button6);
        b3=findViewById(R.id.button7);
        b4=findViewById(R.id.button8);
        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
        b4.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent inta= getIntent();
        String ab=inta.getStringExtra("username");
        if(v==b1)
        {
            Intent in=new Intent(Options.this,Withdraw.class);
            in.putExtra("abValue", ab);
            startActivity(in);

        }
        if(v==b2)
        {
            Intent in=new Intent(Options.this,Deposit.class);
            in.putExtra("abValue", ab);
            startActivity(in);
        }
        if(v==b3)
        {
            Intent in=new Intent(Options.this,Pin_change.class);
            in.putExtra("abValue", ab);
            startActivity(in);
        }
        if(v==b4)
        {
            Intent in=new Intent(Options.this,Balance.class);
            in.putExtra("abValue", ab);
            startActivity(in);
        }
    }



}