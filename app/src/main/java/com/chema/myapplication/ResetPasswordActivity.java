package com.chema.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class ResetPasswordActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private EditText pass , rpass;
    private Button resetPassword;
    private String id;
    private Boolean rememberPrefs;

    private Context mContext;
    private RequestQueue fRequestQueue;
    private SinglentonVolley volley;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        mContext = this.getApplicationContext();
        volley = SinglentonVolley.getInstance(this);
        fRequestQueue = volley.getRequestQueue();

        pass = findViewById(R.id.EtResetPass);
        rpass = findViewById(R.id.EtRepeatResetPass);

        prefs = getSharedPreferences("Preferences" , Context.MODE_PRIVATE);
        id = prefs.getString("id" , null);

        //reset password action button
        resetPassword = findViewById(R.id.btnResetPassword);
        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(pass.getText().toString().isEmpty() || rpass.getText().toString().isEmpty()){
                    Toast.makeText(ResetPasswordActivity.this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                } else{

                    if(pass.getText().toString().equals(rpass.getText().toString())){

                        JSONObject post = new JSONObject();
                        JSONObject usuario = new JSONObject();

                        try {
                            usuario.put("id", id);
                            usuario.put("clave", pass.getText().toString());
                            post.put("usuario",usuario);
                            postRequestLogin(post);

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        Toast.makeText(ResetPasswordActivity.this, "Guardando contraseña, por favor espere...", Toast.LENGTH_SHORT).show();
                    } else{
                        Toast.makeText(ResetPasswordActivity.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });

    }

    private void postRequestLogin(JSONObject data) {
        fRequestQueue = volley.getRequestQueue();
        String url = "https://compras.informehoras.es/resetPassword.php";

        JsonObjectRequest jsonRequestLogin=new JsonObjectRequest(Request.Method.POST, url, data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            if (Boolean.valueOf(response.getString("Autenticacion"))){

                                SharedPreferences.Editor editor = prefs.edit();
                                editor.clear();
                                editor.commit();
                                editor.apply();

                                Toast.makeText(ResetPasswordActivity.this, response.getString("MSG"), Toast.LENGTH_LONG).show();

                                Intent login = new Intent(ResetPasswordActivity.this, MainActivity.class);
                                login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(login);

                            } else {
                                Toast.makeText(ResetPasswordActivity.this, response.getString("MSG"), Toast.LENGTH_SHORT).show();
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
}
