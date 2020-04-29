package com.chema.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.chema.myapplication.Clases.SinglentonVolley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class PrincipalActivity extends AppCompatActivity {

    private ImageButton add;
    private ImageButton logut;
    private EditText search;
    private SharedPreferences prefs;
    private String id;

    private ListView lista;
    private Adaptador adaptador;
    private Entidad entidad;

    private Context mContext;
    private RequestQueue fRequestQueue;
    private SinglentonVolley volley;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        prefs = getSharedPreferences("Preferences" , Context.MODE_PRIVATE);
        id = prefs.getString("id", null);

        mContext = this.getApplicationContext();
        volley = SinglentonVolley.getInstance(this);
        fRequestQueue = volley.getRequestQueue();

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

        logut = findViewById(R.id.btnLogOut);
        logut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PrincipalActivity.this , MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        JSONObject post = new JSONObject();
        JSONObject usuario = new JSONObject();

        try {
            usuario.put("idUser", id);
            post.put("usuario",usuario);
            postRequestLogin(post);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void postRequestLogin(JSONObject data) {
        fRequestQueue = volley.getRequestQueue();
        String url = "https://compras.informehoras.es/list.php";

        JsonObjectRequest jsonRequestLogin=new JsonObjectRequest(Request.Method.POST, url, data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            if (Boolean.valueOf(response.getString("Autenticacion"))){

                                JSONArray r = response.getJSONArray("listas");

                                ArrayList<Entidad> listItems1 = new ArrayList<>();

                                for (int i = 0; i <= r.length(); i++) {
                                    JSONObject listas = r.getJSONObject(i);
                                    listItems1.add(new Entidad(listas.getString("fecha") , listas.getString("titulo") ,
                                            listas.getString("compra")));
                                    /*Toast.makeText(PrincipalActivity.this, "fecha: " + listas.getString("fecha") +
                                            " / titulo: " + listas.getString("titulo") +
                                            " / compra: " + listas.getString("compra"), Toast.LENGTH_SHORT).show();*/

                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                // Handle error
                Log.v("RESPUESTAERROR", error.toString());

                if (error instanceof TimeoutError) {
                    //Toast.makeText(mContext,mContext.getString(R.string.error_network_timeout),Toast.LENGTH_LONG).show();
                    Toast.makeText(mContext, "Timeout error...", Toast.LENGTH_LONG).show();
                }else if(error instanceof NoConnectionError){
                    //Toast.makeText(mContext,mContext.getString(R.string.error_network_timeout),Toast.LENGTH_LONG).show();
                    Toast.makeText(mContext, "No connection...", Toast.LENGTH_LONG).show();

                } else if (error instanceof AuthFailureError) {
                    try {
                        Toast.makeText(mContext, "Login incorrecto...", Toast.LENGTH_LONG).show();
                        Log.v("RESPUESTAERRORATH", new String(error.networkResponse.data, "UTF-8"));
                        onStart();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    //TODO
                } else if (error instanceof ServerError) {
                    //TODO
                    Log.v("RESPUESTAERROR.ServerE", ".");
                } else if (error instanceof NetworkError) {
                    Log.v("RESPUESTAERROR.NetworkE", ".");
                    //TODO
                } else if (error instanceof ParseError) {
                    //TODO
                    Log.v("RESPUESTAERROR", "ParseError");
                }
            }
        });
        jsonRequestLogin.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 5,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        fRequestQueue.add(jsonRequestLogin);

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
