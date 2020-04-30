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
import android.widget.TextView;
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

public class EditActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private Bundle b;
    private ImageButton btneUpdate;
    private ImageButton logut;
    private ImageButton btnCuenta;

    private Context mContext;
    private RequestQueue fRequestQueue;
    private SinglentonVolley volley;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        prefs = getSharedPreferences("Preferences" , Context.MODE_PRIVATE);

        mContext = this.getApplicationContext();
        volley = SinglentonVolley.getInstance(this);
        fRequestQueue = volley.getRequestQueue();

        final TextView nombreCompra = findViewById(R.id.Etitulo);
        final TextView descripcion = findViewById(R.id.Ecompra);
        btneUpdate = findViewById(R.id.btnUpdate);

        Intent intent = getIntent();
        b = intent.getExtras();

        if(b!=null) {
            nombreCompra.setText(b.getString("NOM"));
            descripcion.setText(b.getString("DESC"));
        }

        //Toast.makeText(this, b.getString("ID"), Toast.LENGTH_SHORT).show();

        btneUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String compra = nombreCompra.getText().toString();
                String descompra = descripcion.getText().toString();

                if(compra.isEmpty() || descompra.isEmpty()){
                    Toast.makeText(EditActivity.this, "No se ha podido guardar, todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                } else{
                    JSONObject post = new JSONObject();
                    JSONObject usuario = new JSONObject();

                    try {
                        usuario.put("id", b.getString("ID"));
                        usuario.put("titulo", compra);
                        usuario.put("compra", descompra);
                        post.put("usuario",usuario);
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

                Intent intent = new Intent(EditActivity.this , MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        //details account button
        btnCuenta = findViewById(R.id.btnCuenta);
        btnCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent account = new Intent(EditActivity.this , DetaisAccountActivity.class);
                startActivity(account);
            }
        });

    }
    private void postRequestLogin(JSONObject data) {
        fRequestQueue = volley.getRequestQueue();
        String url = "https://compras.informehoras.es/update.php";

        JsonObjectRequest jsonRequestLogin=new JsonObjectRequest(Request.Method.POST, url, data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            if (Boolean.valueOf(response.getString("Autenticacion"))){
                                Toast.makeText(EditActivity.this, response.getString("MSG"), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(EditActivity.this , PrincipalActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            } else {
                                Toast.makeText(EditActivity.this, response.getString("MSG"), Toast.LENGTH_SHORT).show();
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
