package com.example.vineet.myapplicationone.Tab_Fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.vineet.myapplicationone.Adapters.OneWeekAdapter;
import com.example.vineet.myapplicationone.Adapters.RecycleAdapter;
import com.example.vineet.myapplicationone.Bob_order_onclick;
import com.example.vineet.myapplicationone.Models.ListItems;
import com.example.vineet.myapplicationone.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Vineet on 10/6/2016.
 */

public class LastWeek_Fragment extends Fragment {

    ArrayList<ListItems> listitemto;
    OneWeekAdapter recycleAdapter;
    RecyclerView recyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View frag_view = inflater.inflate(R.layout.bob_allorders_fragment,null);
        listitemto = new ArrayList<ListItems>();

        Fango_last asyncTask = new Fango_last();
        asyncTask.execute("http://52.66.140.142:8080/fango_live/admin/order/get/all");
        recyclerView = (RecyclerView) frag_view.findViewById(R.id.bob_recycle_view);
        final LinearLayoutManager lastlayoutmanager = new LinearLayoutManager(getContext());
        lastlayoutmanager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(lastlayoutmanager);

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Intent intent = new Intent(v.getContext(),Bob_order_onclick.class);
                intent.putExtra("key", listitemto.get(position).getFango_name());
                intent.putExtra("key2",listitemto.get(position).getFango_order_id());
                intent.putExtra("key3",listitemto.get(position).getFango_total_amount());
                intent.putExtra("key4",listitemto.get(position).getFango_no_of_items());
                intent.putExtra("key5",listitemto.get(position).getFango_city());
                startActivity(intent);
            }
        });

        return frag_view;

    }


    public class Fango_last extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                String finaljson = buffer.toString();
                JSONArray jArray = new JSONArray(finaljson);
                if (jArray == null) {

                }
                for (int i = 0; i < jArray.length(); i++)
                {
                    ListItems listItems = new ListItems();
                    JSONObject jObject = jArray.getJSONObject(i);
                    listItems.setFango_name(jObject.getString("name"));
                    listItems.setFango_location(jObject.getString("address"));
                    listItems.setFango_order_id(jObject.getString("order_id"));
                    listItems.setFango_fango_id(jObject.getString("order_code"));
                    listItems.setFango_city(jObject.getString("city"));
                    listItems.setFango_no_of_items(jObject.getString("no_of_items"));
                    listItems.setFango_total_amount(jObject.getString("total_amount"));

                    Calendar currentDate = Calendar.getInstance();
                    currentDate.add(Calendar.DAY_OF_WEEK,-8);
//                    Log.d("Test","Date current :"+currentDate.getTime());

                    Calendar orderDate = Calendar.getInstance();
                    orderDate.setTimeInMillis(Long.parseLong(jObject.getString("order_time")));
//                    Log.d("Test","Date order :"+currentDate.getTime());

                    if(currentDate.getTimeInMillis() <= orderDate.getTimeInMillis())
                    {
                        listitemto.add(listItems);
//                        Log.d("Test","Date added date ---------- :"+orderDate.getTime());
                        Date sel = orderDate.getTime();
                        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
                        String time = format.format(orderDate.getTime());
                        listItems.setFango_time(String.valueOf(time));
                    }
                    /*Calendar currentDate = Calendar.getInstance();
//                    SimpleDateFormat dateForma = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date formattedDat = currentDate.getTime();


//                    Date newd = newDate.getTime();
                    Log.d("Current ","Date is ::::"+formattedDat);


                    Calendar orderDate = Calendar.getInstance();
                    orderDate.setTimeInMillis(Long.parseLong(jObject.getString("order_time")));

                  //  SimpleDateFormat da = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = (Date) orderDate.getTime();
                    currentDate.add(Calendar.DAY_OF_WEEK,-7);
                  //  Date newa = currentDate.getTime();
//                    Collections.sort(listitemto);
                   // Calendar calendar = Calendar.getInstance();
                    orderDate.setTime(date);


                    Log.d("day of week","is "+newa);

                    listItems.setFango_time(String.valueOf(orderDate.getTime()));



                    if (formattedDat.getTime()>=newa.getTime())

                   {

//                        Log.d("Current time ","is :"+formattedDat);
//                        Log.d("Earlier time ","is :"+orderDate.getTime());
//                        Log.d("dates ", "are"+listItems);
//                        Calendar cat = GregorianCalendar.getInstance();
//                        cat.setTimeInMillis(Long.parseLong(jObject.getString("order_time")));
//                        Date dae = (Date) cat.getTime();
//                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                        String time = format.format(dae);
//                        listItems.setFango_time(time);
                       listitemto.add(listItems);


//                        Log.d("loop ", " is : "+time);

                    }
//                    Log.d("Current ","Date is :"+time);
*/



                    Log.d("list ", ":" + listItems);

                }

            } catch (Exception e) {
                e.printStackTrace();
//                Toast.makeText(getActivity(),"UnAble to fetch data, check Connection", Toast.LENGTH_LONG).show();
                Log.d("Error is ", ": " + e);
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(),"Server Offline , Try After Some Time", Toast.LENGTH_LONG).show();
                    Log.d("Another url error", " :" + e);
                }

            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean s) {
            Log.d("Misttlistttt", "isssss :" + listitemto.size());
                Log.d("Misttlistttt", "isssss :" + listitemto.size());
                recycleAdapter = new OneWeekAdapter(listitemto);
                recyclerView.setAdapter(recycleAdapter);

//            recycleAdapter.notifyDataSetChanged();

        }
    }


}
