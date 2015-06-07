package com.example.kudotoshiya.mapapp.Fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.kudotoshiya.mapapp.BusHolder;
import com.example.kudotoshiya.mapapp.Adapter.CustomAdapter;
import com.example.kudotoshiya.mapapp.GoogleGeoCodeResponse;
import com.example.kudotoshiya.mapapp.Item;
import com.example.kudotoshiya.mapapp.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StoreSearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StoreSearchFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private GoogleMap mMap;
    private RequestQueue mQueue;

    private List<Item> list;

    private ListView listView;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     */
    // TODO: Rename and change types and number of parameters
    public static StoreSearchFragment newInstance(String param1, String param2) {
        StoreSearchFragment fragment = new StoreSearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public StoreSearchFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.store_search_layout,container,false);

        listView = (ListView)view.findViewById(R.id.list_item);


        String url = getString(R.string.kimono_api);

        //指定のHTMLをスクレイピングするAPI
        mQueue = Volley.newRequestQueue(getActivity());
        mQueue.add(new JsonObjectRequest(Request.Method.GET, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // JSONObjectのパース、List、Viewへの追加等
                        list = new ArrayList<Item>();

                        try {
                            JSONObject object = response.getJSONObject("results");
                            JSONArray collections = object.getJSONArray("collection1");

                            for (int i = 0; i < collections.length(); i++) {
                                Item item = new Item();
                                JSONObject collection = collections.getJSONObject(i);
                                String address = collection.getString("property1");
                                item.setAddress(address);
                                list.add(item);
                            }

                            CustomAdapter adapter = new CustomAdapter(getActivity(), 0, list);
                            listView.setAdapter(adapter);

                        } catch (Exception e) {

                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // エラー処理 error.networkResponseで確認
                        // エラー表示など
                    }
                }));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Item item = (Item)parent.getItemAtPosition(position);

                // 以下と同処理
                // ListView lv = (ListView) findViewById(R.id.list_item)
                // ListAdapter adapter = lv.getAdapter();
                // Item item = (Item)adapter.getItem(position);
                goLocation(item.getAddress());
            }
        });

        return view;
    }

    public void goLocation(String address){

        String url = getString(R.string.komeda_address_url_1)+address+getString(R.string.komeda_address_url_2);

        //googleジオコーディングAPI
        mQueue = Volley.newRequestQueue(getActivity());
        mQueue.add(new JsonObjectRequest(Request.Method.GET, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // JSONObjectのパース、List、Viewへの追加等
                        Gson gson = new Gson();
                        try {
                            GoogleGeoCodeResponse result = gson.fromJson(response.toString(),
                                    GoogleGeoCodeResponse.class);
                            BusHolder.get().post(result);

                        } catch (Exception e) {

                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // エラー処理 error.networkResponseで確認
                        // エラー表示など
                    }
                }));

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}
