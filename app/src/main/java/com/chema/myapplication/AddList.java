package com.chema.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class AddList extends AppCompatActivity {

    private EditText titulo;
    private EditText compra;
    private ImageButton save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_list);

        titulo = (EditText) findViewById(R.id.Etitulo);
        compra = (EditText) findViewById(R.id.Ecompra);
        save = (ImageButton) findViewById(R.id.btnAdd);

        //boton de guardar
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String tituloguardar = titulo.getText().toString();
               String compraguardar = compra.getText().toString();

                Toast.makeText(AddList.this, "Titulo: " + tituloguardar + " / Compra: " + compraguardar, Toast.LENGTH_SHORT).show();
            }
        });

    }

}
