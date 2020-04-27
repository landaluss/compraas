package com.chema.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class PrincipalActivity extends AppCompatActivity {

    private ImageButton add;
    private EditText search;

    private ListView lista;
    private Adaptador adaptador;
    private Entidad entidad;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        search = findViewById(R.id.EtNombre);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adaptador.getFilter().filter(s);

                if(s.length()==0){
                   //esconder teclado
                    InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(PrincipalActivity.this.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(search.getWindowToken(), 0);
                } else {
                    InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(PrincipalActivity.this.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(search.getWindowToken(), 1);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        lista = findViewById(R.id.lvLista);
        final ArrayList<Entidad> gItems = GetArrayItems();
        adaptador = new Adaptador(this , gItems);
        lista.setAdapter(adaptador);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent visorDetalles = new Intent(view.getContext(), ListaActivity.class);
                visorDetalles.putExtra("FEC", gItems.get(position).getFecha());
                visorDetalles.putExtra("NOM", gItems.get(position).getNombreCompra());
                visorDetalles.putExtra("DESC", gItems.get(position).getCompra());
                startActivity(visorDetalles);
            }
        });

        //boton para crear una lista nueva
        add = findViewById(R.id.btnAdd);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(PrincipalActivity.this, "asd", Toast.LENGTH_SHORT).show();
                Intent addIntent = new Intent(PrincipalActivity.this , AddList.class);
                startActivity(addIntent);
            }
        });

    }

    private ArrayList<Entidad> GetArrayItems(){
        ArrayList<Entidad> listItems = new ArrayList<>();
        listItems.add(new Entidad("10/03/2015" , "Compra de enero" , "If the content scrolls, everything is fine. However, if the content is smaller than the size of the screen, the buttons are not at the bottom."));
        listItems.add(new Entidad("10/03/2016" , "Compra de febrero" , "If the content scrolls, everything is fine. However, if the content is smaller than the size of the screen, the buttons are not at the bottom."));
        listItems.add(new Entidad("10/03/2017" , "Compra de marzo" , "If the content scrolls, everything is fine. However, if the content is smaller than the size of the screen, the buttons are not at the bottom."));
        listItems.add(new Entidad("10/03/2018" , "Compra de abril" , "If the content scrolls, everything is fine. However, if the content is smaller than the size of the screen, the buttons are not at the bottom."));

        return listItems;
    }

}
