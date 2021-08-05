package com.dumpit.ffff;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SlidingDrawer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;


public class Map extends Fragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener{
    MapView sView = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map , container, false);


        Button b1 = (Button) view.findViewById(R.id.m1);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getChildFragmentManager().beginTransaction().replace(R.id.s_map, new Map1()).commit();
            }
        });
        Button b2 = (Button) view.findViewById(R.id.m2);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getChildFragmentManager().beginTransaction().replace(R.id.s_map, new Map2()).commit();
            }
        });
        Button b3 = (Button) view.findViewById(R.id.m3);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getChildFragmentManager().beginTransaction().replace(R.id.s_map, new Map3()).commit();
            }
        });


        sView = view.findViewById(R.id.s_map);
        sView.onCreate(savedInstanceState);
        sView.getMapAsync(this);
        return view;
    }


    //이 메서드가 없으면 지도가 보이지 않음
    @Override
    public void onStart() {
        super.onStart();
        sView.onStart();
    }

    @Override
    public void onStop () {
        super.onStop();
        sView.onStop();

    }

    @Override
    public void onSaveInstanceState (@Nullable Bundle outState){
        super.onSaveInstanceState(outState);
        sView.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        sView.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sView.onLowMemory();
    }

    //맵뷰 설정
    @Override
    public void onMapReady(GoogleMap googleMap) {
        //마커찍기(위도,경도)
        String[] place = {"남대문약국","은나라약국", "아름다운달과별약국", "늘푸른약국", "한겨례약국", "정온누리약국",
                "경하프라자약국", "메디칼약국", "정문온누리약국", "대우약국", "정다운약국", "희망약국", "세계로약국", "연서메디칼약국",
                "연신내메디칼약국", "함께하는약국", "DMC역 4번출구약국", "선우약국" ,"신사프라자약국" ,"승윤약국" ,"참진약국" ,"메디팜대원약국",
                "오온누리약국" ,"왕 약국" ,"구세약국" ,"장수약국" ,"연도약국" ,"조은백화점약국", "보건약국", "조은약국" ,"애플약국" ,"은평오렌지약국",
                "가톨릭정문약국"};
        String[] address = {"서울특별시 은평구 통일로87길 19, 1층(갈현동)", " 서울특별시 은평구 갈현로 258, (갈현동)", " 서울특별시 은평구 통일로 855-8, (갈현동)",
                "서울특별시 은평구 통일로 871 (갈현동)", "  서울특별시 은평구 연서로 219, (갈현동)", "서울특별시 은평구 서오릉로 196, 106호(구산동)", "서울특별시 은평구 서오릉로 157, (구산동)",
                "서울특별시 은평구 서오릉로2길 17-5, (녹번동)", "서울특별시 은평구 진흥로 196, (녹번동)",
                "서울특별시 은평구 통일로 749, (대조동)", "서울특별시 은평구 진흥로 137, 1층 (대조동)", "서울특별시 은평구 서오릉로 138, (대조동)", "서울특별시 은평구 불광로 54(불광동)",
                " 서울특별시 은평구 연서로 253-9, 1층 (불광동)", "서울특별시 은평구 연서로 246, 104호 (불광동)", "서울특별시 은평구 수색로 264-1, (수색동)", " 서울특별시 은평구 수색로 175 1층 3호(수색동)",
                "서울특별시 은평구 은평로 56, (신사동)", "서울특별시 은평구 갈현로 16, (신사동)", "서울특별시 은평구 가좌로 325, (신사동)",
                " 서울특별시 은평구 역말로 40, (역촌동)", "서울특별시 은평구 진흥로1길 2, (역촌동)", "서울특별시 은평구 진흥로 97(역촌동)", "서울특별시 은평구 은평로 137, (응암동)",
                "서울특별시 은평구 응암로181 (응암동)", "서울특별시 은평구 응암로 248, (응암동)", "서울특별시 은평구 은평로 144, (응암동)", "서울특별시 은평구 응암로 201, (응암동)",
                "서울특별시 은평구 증산로9길 3, (증산동)", "서울특별시 은평구 진관 3로 70번지 821동 상가 103호 (진관동)", "서울특별시 은평구 진관2로 57-7, B106호 (진관동)",
                "서울특별시 은평구은평구 진관2로 29-21, 113,114호 (진관동, 드림스퀘어)", "서울특별시 은평구통일로 1031,1층 102호(진관동)"};
        double[] latitude = {37.560204, 37.6198729, 37.6190274, 40.2258676, 37.6183052, 37.4973224, 37.6119726,
                33.8856766, 35.2205537, 37.3212865, 37.6055716, 37.6555565, 35.1528806, 37.6199162, 37.6189996,
                37.4094716, 37.57754, 36.8356099, 37.5992466, 37.5961688, 35.1431403, 37.5943935, 47.3156626,
                37.4693429, 36.9919575, 40.759457, 37.6005465, 37.5899753, 37.2761265, 47.1780517, 37.5228498,
                37.6358587, 37.6434325};
        double[] longtitude = {126.9770969, 126.9160491, 126.9202237, -75.2548106, 126.9197238, 127.0551306, 126.9155854,
                -117.9943453, 126.8451217, 126.9535344, 127.064537, 127.0414833, 126.9173451, 126.9223901,
                126.9225846, 127.1259514, 126.90223, 127.1339676,126.9103243, 126.9089477, 129.0595121, 127.0337411,
                -122.3199544, 126.9386891, 127.1010183, -73.80381, 126.9237571, 126.9168915, 127.0169183, -122.4833193,
                127.0205872, 126.9194666, 126.9173565};

        for (int x = 0; x < latitude.length; x++) {
            LatLng loc = new LatLng(latitude[x], longtitude[x]);
            MarkerOptions marker = new MarkerOptions();
            marker.position(loc); //마커 위치
            marker.title(place[x]);
            marker.snippet(address[x]);

            BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.med);
            Bitmap b=bitmapdraw.getBitmap();
            Bitmap smallMarker = Bitmap.createScaledBitmap(b, 200, 200, false);
            marker.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));

            googleMap.addMarker(marker).showInfoWindow();
        }

        //서울 찍기(중심)
        LatLng SEOUL = new LatLng(37.56, 126.97);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(SEOUL);
        markerOptions.title("서울");
        markerOptions.snippet("한국의 수도");
        googleMap.addMarker(markerOptions);

        //인포윈도우 클릭
        googleMap.setOnInfoWindowClickListener(this);
        //맵뷰 카메라위치, 줌 설정 (서울)
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(SEOUL));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(10));
        googleMap.getUiSettings().setZoomControlsEnabled(true);
    }



    //인포윈도우 클릭 리스너
    @Override
    public void onInfoWindowClick (Marker marker){

    }


}