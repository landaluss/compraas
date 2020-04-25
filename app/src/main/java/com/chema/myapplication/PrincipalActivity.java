package com.chema.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

public class PrincipalActivity extends AppCompatActivity {

    private ImageButton add;

    ListView lista;

    String[][] datos = {
            {"10/12/2019", "Christopher Nolan" , "As is talked about in this post, sometimes you want the ScrollView content to fill the screen. For example, if you had some buttons at the end of a readme. You want the buttons to always be at the end of the text and at bottom of the screen, even if the text doesn't scroll.\n" +
                    "\n" +
                    "If the content scrolls, everything is fine. However, if the content is smaller than the size of the screen, the buttons are not at the bottom.\n" +
                    "As is talked about in this post, sometimes you want the ScrollView content to fill the screen. For example, if you had some buttons at the end of a readme. You want the buttons to always be at the end of the text and at bottom of the screen, even if the text doesn't scroll.\n" +
                    "\n" +
                    "If the content scrolls, everything is fine. However, if the content is smaller than the size of the screen, the buttons are not at the bottom."},
            {"10/08/2019", "James Mangold" , "As is talked about in this post, sometimes you want the ScrollView content to fill the screen. For example, if you had some buttons at the end of a readme. You want the buttons to always be at the end of the text and at bottom of the screen, even if the text doesn't scroll.\n" +
                    "\n" +
                    "If the content scrolls, everything is fine. However, if the content is smaller than the size of the screen, the buttons are not at the bottom.\n" +
                    "As is talked about in this post, sometimes you want the ScrollView content to fill the screen. For example, if you had some buttons at the end of a readme. You want the buttons to always be at the end of the text and at bottom of the screen, even if the text doesn't scroll.\n" +
                    "\n" +
                    "If the content scrolls, everything is fine. However, if the content is smaller than the size of the screen, the buttons are not at the bottom."}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        lista = (ListView) findViewById(R.id.lvLista);

        lista.setAdapter(new Adaptador(this, datos));

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent visorDetalles = new Intent(view.getContext(), ListaActivity.class);
                visorDetalles.putExtra("FEC", datos[position][0]);
                visorDetalles.putExtra("NOM", datos[position][1]);
                visorDetalles.putExtra("DESC", datos[position][2]);
                startActivity(visorDetalles);
            }
        });

        //boton para crear una lista nueva
        add = (ImageButton) findViewById(R.id.btnAdd);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(PrincipalActivity.this, "asd", Toast.LENGTH_SHORT).show();
                Intent addIntent = new Intent(PrincipalActivity.this , AddList.class);
                startActivity(addIntent);
            }
        });

    }

}
