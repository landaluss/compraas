package com.chema.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class ListaActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private ImageButton del;
    private ImageButton btnShare;
    private ImageButton btnedit;
    private ImageButton logut;
    private ImageButton btnCuenta;
    private Bundle b;

    private Context mContext;
    private RequestQueue fRequestQueue;
    private SinglentonVolley volley;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        prefs = getSharedPreferences("Preferences" , Context.MODE_PRIVATE);

        mContext = this.getApplicationContext();
        volley = SinglentonVolley.getInstance(this);
        fRequestQueue = volley.getRequestQueue();

        TextView nombreCompra = findViewById(R.id.tvNombreLista);
        TextView fecha = findViewById(R.id.tvFecha);
        TextView descripcion = findViewById(R.id.tvCompra);

        Intent intent = getIntent();
        b = intent.getExtras();

        if(b!=null) {
            nombreCompra.setText(b.getString("NOM"));
            fecha.setText(b.getString("FEC"));
            descripcion.setText(b.getString("DESC"));
        }

        del = (ImageButton) findViewById(R.id.btnDel);
        btnShare = (ImageButton) findViewById(R.id.btnShare);
        btnedit = findViewById(R.id.btnEdit);

        //Al hacer clic en borrar
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createCustomDialog().show();
            }
        });

        //Al hacer clic en whatsapp
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createCustomDialogwassap().show();
            }
        });

        //Al hacer click en editar
        btnedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent edit = new Intent(ListaActivity.this , EditActivity.class);
                edit.putExtra("ID",b.getString("ID"));
                edit.putExtra("NOM",b.getString("NOM"));
                edit.putExtra("DESC", b.getString("DESC"));
                startActivity(edit);
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

                Intent intent = new Intent(ListaActivity.this , MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        //details account button
        btnCuenta = findViewById(R.id.btnCuenta);
        btnCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent account = new Intent(ListaActivity.this , DetaisAccountActivity.class);
                startActivity(account);
            }
        });

        //Toast.makeText(ListaActivity.this, "ID: " + b.getString("ID"), Toast.LENGTH_SHORT).show();

    }

    public AlertDialog createCustomDialog(){
        final AlertDialog alertDialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(ListaActivity.this);
        LayoutInflater inflater = getLayoutInflater();

        View v = inflater.inflate(R.layout.dialog_signin, null);
        Button btnCancel = (Button) v.findViewById(R.id.AlertDialog_Negativo);
        Button btnTrue = (Button) v.findViewById(R.id.AlertDialog_Positivo);
        builder.setView(v);
        alertDialog = builder.create();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        btnTrue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JSONObject post = new JSONObject();
                JSONObject usuario = new JSONObject();

                try {
                    usuario.put("id", b.getString("ID"));
                    post.put("usuario",usuario);
                    postRequestLogin(post);

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                alertDialog.dismiss();
            }
        });

        return alertDialog;
    }

    public AlertDialog createCustomDialogwassap(){
        final AlertDialog alertDialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(ListaActivity.this);
        LayoutInflater inflater = getLayoutInflater();

        View v = inflater.inflate(R.layout.dialog_share, null);
        ImageButton btnCorreo = (ImageButton) v.findViewById(R.id.AlertDialog_correo);
        ImageButton btnWaassap = (ImageButton) v.findViewById(R.id.AlertDialog_wassap);
        builder.setView(v);
        alertDialog = builder.create();

        btnCorreo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentmail = new Intent(Intent.ACTION_SEND);
                intentmail.setType("text/plain");
                intentmail.putExtra(Intent.EXTRA_SUBJECT , "Lista de la compa: " + b.getString("NOM"));
                intentmail.putExtra(Intent.EXTRA_TEXT ,  b.getString("DESC"));
                startActivity(intentmail);

            }
        });

        btnWaassap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.setPackage("com.whatsapp");
                intent.putExtra(Intent.EXTRA_TEXT, b.getString("DESC"));

                try {
                    startActivity(intent);
                } catch (android.content.ActivityNotFoundException ex) {
                    ex.printStackTrace();

                    Toast.makeText(ListaActivity.this, "El dispositivo no tiene WHATSAPP instalado", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return alertDialog;
    }

    private void postRequestLogin(JSONObject data) {
        fRequestQueue = volley.getRequestQueue();
        String url = "https://compras.informehoras.es/deleteList.php";

        JsonObjectRequest jsonRequestLogin=new JsonObjectRequest(Request.Method.POST, url, data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            if (Boolean.valueOf(response.getString("Autenticacion"))){

                                Toast.makeText(ListaActivity.this, response.getString("MSG"), Toast.LENGTH_SHORT).show();

                                Intent login = new Intent(ListaActivity.this, PrincipalActivity.class);
                                login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(login);

                            } else {
                                Toast.makeText(ListaActivity.this, response.getString("MSG"), Toast.LENGTH_SHORT).show();
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
