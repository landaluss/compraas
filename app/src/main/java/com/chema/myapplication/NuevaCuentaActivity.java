package com.chema.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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


public class NuevaCuentaActivity extends AppCompatActivity {

    private EditText EtNombre , EtApellidos , EtCorreo , EtUsername , EtPassword;
    private Context mContext;
    private RequestQueue fRequestQueue;
    private SinglentonVolley volley;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_cuenta);

        mContext = this.getApplicationContext();
        volley = SinglentonVolley.getInstance(this);
        fRequestQueue = volley.getRequestQueue();

        Button btnNcuenta = (Button) findViewById(R.id.btnNcuenta);
        btnNcuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EtNombre = findViewById(R.id.EtNombre);
                EtApellidos = findViewById(R.id.EtAApellidos);
                EtCorreo = findViewById(R.id.EtCorreo);
                EtUsername = findViewById(R.id.EtCorreo);
                EtPassword = findViewById(R.id.EtPassword);

                if(EtNombre.getText().toString().isEmpty() || EtApellidos.getText().toString().isEmpty() || EtCorreo.getText().toString().isEmpty()
                        || EtUsername.getText().toString().isEmpty() || EtPassword.getText().toString().isEmpty()){
                            Toast.makeText(NuevaCuentaActivity.this, "Rellena todos los campos, son obligatorios", Toast.LENGTH_SHORT).show();
                } else {
                    if(Patterns.EMAIL_ADDRESS.matcher(EtCorreo.getText().toString()).matches() && EtCorreo.length() > 0){

                        JSONObject post = new JSONObject();
                        JSONObject usuario = new JSONObject();
                        try {
                            usuario.put("nombre", EtNombre.getText().toString());
                            usuario.put("apellidos", EtApellidos.getText().toString());
                            usuario.put("correo", EtCorreo.getText().toString());
                            usuario.put("username", EtUsername.getText().toString());
                            usuario.put("password", EtPassword.getText().toString());
                            post.put("usuario",usuario);
                            postRequestLogin(post);

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        Toast.makeText(NuevaCuentaActivity.this, "Registro terminado con éxito", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(NuevaCuentaActivity.this, "Dirección de correo no válida, revísala e intentalo de nuevo", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }

    private void postRequestLogin(JSONObject data) {
        fRequestQueue = volley.getRequestQueue();
        String url = "https://compras.informehoras.es/add_cuenta.php";

        JsonObjectRequest jsonRequestLogin=new JsonObjectRequest(Request.Method.POST, url, data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            if (Boolean.valueOf(response.getString("Autenticacion"))){

                                /*SharedPreferences.Editor editor = prefs.edit();
                                editor.putString("correo" , response.getString("correo"));
                                editor.putString("clave" , EtClave.getText().toString());
                                editor.putString("nombre_login" , EtNombre.getText().toString());
                                editor.putString("nombre" , response.getString("nombre"));
                                editor.putString("apellidos" , response.getString("apellidos"));
                                editor.putString("imei" , imei);
                                editor.putString("registrosHoy", response.getString("registros"));
                                editor.putString("horariosHoy", response.getString("horarios"));
                                editor.putBoolean("remember" , switchRemember.isChecked());
                                //editor.commit(); //sincrono
                                editor.apply();     //asincrono

                                Intent intent = new Intent(LoginActivity.this, PrincipalActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);*/

                                Toast.makeText(NuevaCuentaActivity.this, "Parece que todo se hizo bien", Toast.LENGTH_SHORT).show();
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
