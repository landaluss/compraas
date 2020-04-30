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
import android.widget.ImageButton;
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

public class ForgotActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private boolean rememberPrefs;
    private EditText EtCorreoForgot;
    private Button btnForgot;

    private Context mContext;
    private RequestQueue fRequestQueue;
    private SinglentonVolley volley;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);

        mContext = this.getApplicationContext();
        volley = SinglentonVolley.getInstance(this);
        fRequestQueue = volley.getRequestQueue();

        EtCorreoForgot= findViewById(R.id.EtCorreoForgot);
        btnForgot = findViewById(R.id.btnForgot);

        btnForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Patterns.EMAIL_ADDRESS.matcher(EtCorreoForgot.getText().toString()).matches() && EtCorreoForgot.length() > 0){

                    JSONObject post = new JSONObject();
                    JSONObject usuario = new JSONObject();

                    try {
                        usuario.put("correo", EtCorreoForgot.getText().toString());
                        post.put("usuario",usuario);
                        postRequestLogin(post);
                        Toast.makeText(ForgotActivity.this, "Estamos verificando su cuenta de correo, por favor espere...", Toast.LENGTH_LONG).show();

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                } else {

                    Toast.makeText(ForgotActivity.this, "Debe de poner una dirección de correo válida", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    private void postRequestLogin(JSONObject data) {
        fRequestQueue = volley.getRequestQueue();
        String url = "https://compras.informehoras.es/forgot.php";

        JsonObjectRequest jsonRequestLogin=new JsonObjectRequest(Request.Method.POST, url, data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            if (Boolean.valueOf(response.getString("Autenticacion"))){
                                prefs = getSharedPreferences("Preferences" , Context.MODE_PRIVATE);
                                rememberPrefs = prefs.getBoolean("remember",
                                        false);

                                if(rememberPrefs){
                                    SharedPreferences.Editor editor = prefs.edit();
                                    editor.remove("clave");
                                    editor.commit();
                                }
                                Toast.makeText(ForgotActivity.this, response.getString("MSG"), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ForgotActivity.this , MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            } else{
                                Toast.makeText(ForgotActivity.this, response.getString("MSG"), Toast.LENGTH_SHORT).show();
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
        jsonRequestLogin.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 5,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        fRequestQueue.add(jsonRequestLogin);

    }
}
