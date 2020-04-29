package com.chema.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
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
import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperActivityToast;
import com.github.johnpersano.supertoasts.library.utils.PaletteUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private EditText EtNombre;
    private EditText EtPass;
    private Button login;
    private CheckBox recordar;
    private TextView password;
    private TextView ncuenta;
    private CheckBox switchremember;
    private Bundle b;
    private boolean rememberPrefs;
    private String id;

    private Context mContext;
    private RequestQueue fRequestQueue;
    private SinglentonVolley volley;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this.getApplicationContext();
        volley = SinglentonVolley.getInstance(this);
        fRequestQueue = volley.getRequestQueue();

        EtNombre = findViewById(R.id.EtNombre);
        EtPass = findViewById(R.id.EtPass);
        login = findViewById(R.id.btnLogin);

        recordar = findViewById(R.id.remember);
        password = findViewById(R.id.recuperar);
        ncuenta = findViewById(R.id.btnCuenta);
        switchremember = findViewById(R.id.remember);

        Intent intent = getIntent();
        b = intent.getExtras();

        if(b!=null) {
            EtNombre.setText(b.getString("NOM"));
            EtPass.setText(b.getString("PASS"));
        }

        prefs = getSharedPreferences("Preferences" , Context.MODE_PRIVATE);
        rememberPrefs = prefs.getBoolean("remember",
                false);

        if(rememberPrefs){
            EtNombre.setText(prefs.getString("nombre_login",""));
            EtPass.setText(prefs.getString("clave",""));
            switchremember.setChecked(true);
        }

        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forgot = new Intent(MainActivity.this , ForgotActivity.class);
                startActivity(forgot);
            }
        });

        ncuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nuevaCuenta = new Intent(MainActivity.this , NuevaCuentaActivity.class);
                startActivity(nuevaCuenta);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JSONObject post = new JSONObject();
                JSONObject usuario = new JSONObject();

                try {
                    usuario.put("nombre_login", EtNombre.getText().toString());
                    usuario.put("clave", EtPass.getText().toString());
                    post.put("usuario",usuario);
                    postRequestLogin(post);

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        });
    }

    private void postRequestLogin(JSONObject data) {
        fRequestQueue = volley.getRequestQueue();
        String url = "https://compras.informehoras.es/auth.php";

        JsonObjectRequest jsonRequestLogin=new JsonObjectRequest(Request.Method.POST, url, data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            if (Boolean.valueOf(response.getString("Autenticacion"))){

                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putString("correo" , response.getString("correo"));
                                editor.putString("clave" , EtPass.getText().toString());
                                editor.putString("nombre_login" , EtNombre.getText().toString());
                                editor.putString("nombre" , response.getString("nombre"));
                                editor.putString("apellidos" , response.getString("apellidos"));
                                editor.putString("id" , response.getString("id"));
                                editor.putBoolean("remember" , switchremember.isChecked());
                                editor.commit(); //sincrono
                                editor.apply();     //asincrono

                                Toast.makeText(MainActivity.this, response.getString("MSG"), Toast.LENGTH_SHORT).show();

                                Intent login = new Intent(MainActivity.this, PrincipalActivity.class);
                                login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(login);

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
        fRequestQueue.add(jsonRequestLogin);

    }
}
