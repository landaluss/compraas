package com.chema.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperActivityToast;
import com.github.johnpersano.supertoasts.library.utils.PaletteUtils;

public class MainActivity extends AppCompatActivity {

    private EditText EtNombre;
    private EditText EtPass;
    private Button login;
    private CheckBox recordar;
    private TextView password;
    private TextView ncuenta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EtNombre = findViewById(R.id.EtNombre);
        EtPass = findViewById(R.id.EtPass);
        login = findViewById(R.id.btnLogin);

        recordar = findViewById(R.id.remember);
        password = findViewById(R.id.recuperar);
        ncuenta = findViewById(R.id.btnCuenta);

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

                String user = EtNombre.getText().toString();
                String pass = EtPass.getText().toString();

                if(user.equals("familia") && pass.equals("charavia")){

                    SuperActivityToast.create(MainActivity.this, new Style(), Style.TYPE_BUTTON)
                            .setProgressBarColor(Color.WHITE)
                            .setText("Acceso Correcto. Por favor espere...")
                            .setDuration(Style.DURATION_VERY_LONG)
                            .setFrame(Style.FRAME_LOLLIPOP)
                            .setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_GREEN))
                            .setAnimations(Style.ANIMATIONS_POP).show();

                    Intent intent = new Intent(MainActivity.this , PrincipalActivity.class);
                    startActivity(intent);

                } else {

                    SuperActivityToast.create(MainActivity.this, new Style(), Style.TYPE_BUTTON)
                            .setProgressBarColor(Color.WHITE)
                            .setText("Usuario o contraseña incorrectos. Intentelo de nuevo")
                            .setDuration(Style.DURATION_LONG)
                            .setFrame(Style.FRAME_LOLLIPOP)
                            .setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_GREY))
                            .setAnimations(Style.ANIMATIONS_POP).show();
                }

                //Toast.makeText(MainActivity.this, "Nombre: " + user + " / Pass: " + pass, Toast.LENGTH_SHORT).show();

            }
        });
    }
}
