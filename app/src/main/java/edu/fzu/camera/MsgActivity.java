package edu.fzu.camera;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MsgActivity extends AppCompatActivity {

    Button button;
    EditText editText;
    Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = this;
        setContentView(R.layout.activity_msg);

        Intent intent = getIntent();
        String msg = intent.getStringExtra("msg");

        editText = findViewById(R.id.edit_message);
        editText.setText(msg);

        button = findViewById(R.id.btn_confirm);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = editText.getText().toString();
                Toast.makeText(ctx, "message: " + message, Toast.LENGTH_LONG).show();
                //TODO: 确认之后的动作
            }
        });
    }
}
