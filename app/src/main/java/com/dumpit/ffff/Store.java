package com.dumpit.ffff;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/*
추후 구현해야할 것:
searchView로 아이템 검색기능 구현
 */

public class Store extends Fragment{
    ViewGroup viewGroup;
    MarketAdapter adapter;
    GridView gridView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.store,container,false);

        // 어댑터 안에 데이터 담기
        adapter = new MarketAdapter();
        gridView = (GridView) viewGroup.findViewById(R.id.gridView);

        FirebaseDatabase mDatabase;
        DatabaseReference mReference;
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();

        //mReference.child("users").child("121212_naver").child("point").setValue(30000);
        adapter.addItem(new MarketItem("BHC 치즈볼", 5000, R.drawable.m1));
        adapter.addItem(new MarketItem("하늘보리", 1500, R.drawable.m2));
        adapter.addItem(new MarketItem("양키 캔들", 30000, R.drawable.m3));
        adapter.addItem(new MarketItem("바세린 50ml", 3500, R.drawable.m4));
        adapter.addItem(new MarketItem("문화상품권 5000원", 5100, R.drawable.m5));
        adapter.addItem(new MarketItem("빼빼로 오리지널", 1400, R.drawable.m6));
        adapter.addItem(new MarketItem("가나 초콜릿", 1200, R.drawable.m7));
        adapter.addItem(new MarketItem("스타벅스 아이스 아메리카노 tall", 4100, R.drawable.m8));
        // 리스트 뷰에 어댑터 설정
        gridView.setAdapter(adapter);

        // 이벤트 처리 리스너 설정
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MarketItem item = (MarketItem) adapter.getItem(position);
                Intent intent = new Intent(getActivity(), MarketItemClick.class);
                intent.putExtra("image", item.getResId());
                intent.putExtra("name", item.getName());
                intent.putExtra("price", item.getPrice());
                startActivity(intent);
            }
        });

        return viewGroup;
    }
}

class MarketAdapter extends BaseAdapter {
    ArrayList<MarketItem> items = new ArrayList<MarketItem>();


    // Generate > implement methods
    @Override
    public int getCount() {
        return items.size();
    }

    public void addItem(MarketItem item) {
        items.add(item);
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 뷰 객체 재사용
        MarketItemView view = null;
        if (convertView == null) {
            view = new MarketItemView(parent.getContext());
            // 윗줄에서 자꾸 오류남.. 원래는 view = new SingerItemView(getApplicationContext()); 였음
        } else {
            view = (MarketItemView) convertView;
        }

        MarketItem item = items.get(position);

        view.setName(item.getName());
        view.setPrice(item.getPrice());
        view.setImage(item.getResId());


        return view;
    }
}

