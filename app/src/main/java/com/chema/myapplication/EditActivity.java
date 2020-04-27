package com.chema.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class EditActivity extends AppCompatActivity {

    private Bundle b;
    private ImageButton btneUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        final TextView nombreCompra = findViewById(R.id.Etitulo);
        final TextView descripcion = findViewById(R.id.Ecompra);
        btneUpdate = findViewById(R.id.btnUpdate);

        Intent intent = getIntent();
        b = intent.getExtras();

        if(b!=null) {
            nombreCompra.setText(b.getString("NOM"));
            descripcion.setText(b.getString("DESC"));
        }

        btneUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String compra = nombreCompra.getText().toString();
                String descompra = descripcion.getText().toString();

                if(compra.isEmpty() || descompra.isEmpty()){
                    Toast.makeText(EditActivity.this, "No se ha podido guardar, todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(EditActivity.this, "Guardado", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}
