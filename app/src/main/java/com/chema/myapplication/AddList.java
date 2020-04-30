package com.chema.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class AddList extends AppCompatActivity {

    private SharedPreferences prefs;
    private EditText titulo;
    private EditText compra;
    private ImageButton save;
    private String id;
    private ImageButton logut;
    private ImageButton btnCuenta;

    private Context mContext;
    private RequestQueue fRequestQueue;
    private SinglentonVolley volley;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_list);

        prefs = getSharedPreferences("Preferences" , Context.MODE_PRIVATE);
        id = prefs.getString("id", null);

        mContext = this.getApplicationContext();
        volley = SinglentonVolley.getInstance(this);
        fRequestQueue = volley.getRequestQueue();

        titulo = (EditText) findViewById(R.id.Etitulo);
        compra = (EditText) findViewById(R.id.Ecompra);
        save = (ImageButton) findViewById(R.id.btnAdd);

        //boton de guardar
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(titulo.getText().toString().isEmpty() || compra.getText().toString().isEmpty()){

                    Toast.makeText(AddList.this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();

                } else {

                    JSONObject post = new JSONObject();
                    JSONObject usuario = new JSONObject();

                    try {
                        usuario.put("titulo", titulo.getText().toString());
                        usuario.put("compra", compra.getText().toString());
                        usuario.put("id", id);
                        post.put("usuario", usuario);
                        postRequestLogin(post);

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }
        });

        //log aou button
        logut = findViewById(R.id.btnLogOut);
        logut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("sesion" , false);
                editor.commit(); //sincrono
                editor.apply();     //asincrono

                Intent intent = new Intent(AddList.this , MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        //details account button
        btnCuenta = findViewById(R.id.btnCuenta);
        btnCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent account = new Intent(AddList.this , DetaisAccountActivity.class);
                startActivity(account);
            }
        });

    }

    private void postRequestLogin(JSONObject data) {
        fRequestQueue = volley.getRequestQueue();
        String url = "https://compras.informehoras.es/add_list.php";

        JsonObjectRequest jsonRequestLogin = new JsonObjectRequest(Request.Method.POST, url, data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            if (Boolean.valueOf(response.getString("Autenticacion"))) {

                                Toast.makeText(AddList.this, response.getString("MSG"), Toast.LENGTH_SHORT).show();

                                Intent login = new Intent(AddList.this, PrincipalActivity.class);
                                login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(login);

                            } else{
                                Toast.makeText(AddList.this, response.getString("MSG"), Toast.LENGTH_SHORT).show();
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
                } else if (error instanceof NoConnectionError) {
                    //Toast.makeText(mContext,mContext.getString(R.string.error_network_timeout),Toast.LENGTH_LONG).show();
                    Toast.makeText(mContext, "No connection...", Toast.LENGTH_LONG).show();

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
        fRequestQueue.add(jsonRequestLogin);
    }
}
