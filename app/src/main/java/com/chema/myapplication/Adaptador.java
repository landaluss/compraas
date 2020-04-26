package com.chema.myapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Adaptador extends BaseAdapter implements Filterable {

    private Context context;
    private ArrayList<Entidad> listItems;
    private ArrayList<Entidad> listFiltered;

    public Adaptador(Context context, ArrayList<Entidad> listItems , ArrayList<Entidad> listFiltered) {
        this.context = context;
        this.listItems = listItems;
        this.listFiltered = listItems;
    }

    @Override
    public int getCount() {
        return listFiltered.size();
    }

    @Override
    public Object getItem(int position) {
        return listFiltered.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Entidad Item = (Entidad) getItem(position);

        convertView = LayoutInflater.from(context).inflate(R.layout.elemento_lista , null);
        TextView fecha = (TextView) convertView.findViewById(R.id.fecha);
        TextView nombreCompra = (TextView) convertView.findViewById(R.id.nombreCompra);

        fecha.setText(Item.getFecha());
        nombreCompra.setText(Item.getNombreCompra());

        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();

                if (constraint == null || constraint.length() == 0) {
                    //no constraint given, just return all the data. (no search)
                    results.count = listItems.size();
                    results.values = listItems;

                } else {//do the search
                    List<Entidad> resultsData = new ArrayList<>();
                    String searchStr = constraint.toString().toUpperCase();

                    for (Entidad s : listItems)
                        if (s.getNombreCompra().toUpperCase().contains(searchStr)) resultsData.add(s);
                    results.count = resultsData.size();
                    results.values = resultsData;
                }
                Log.v("log" , results.values.toString());
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                listFiltered = (ArrayList<Entidad>) results.values;
                notifyDataSetChanged();
            }
        };
    }

}
