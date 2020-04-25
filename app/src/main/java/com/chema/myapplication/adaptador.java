package com.chema.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

class Adaptador extends BaseAdapter {

    private static LayoutInflater inflater = null;

    Context contexto;
    String[][] datos;

    public Adaptador(Context conexto, String[][] datos)
    {
        this.contexto = conexto;
        this.datos = datos;

        inflater = (LayoutInflater)conexto.getSystemService(conexto.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {

        final View vista = inflater.inflate(R.layout.elemento_lista, null);

        TextView fecha = (TextView) vista.findViewById(R.id.fecha);
        TextView nombreCompra = (TextView) vista.findViewById(R.id.nombreCompra);

        fecha.setText(datos[i][0]);
        nombreCompra.setText(datos[i][1]);

        return vista;
    }

    @Override
    public int getCount() {
        return datos.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}
