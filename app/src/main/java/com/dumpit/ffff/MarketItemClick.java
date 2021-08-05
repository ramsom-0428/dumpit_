package com.dumpit.ffff;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;

import static java.lang.System.exit;

/*
추후 구현사항:
아이템 설명..?
'찜하기' 기능
 */

public class MarketItemClick extends AppCompatActivity {
    TextView itemName;
    TextView itemPrice;
    TextView myPoint;
    ImageView itemImage;
    Button buybtn;
    TextView heart;
    CheckBox checkBox;
    String id;
    int point;
    boolean isExist;
    boolean canBuy;
    boolean isLove;

    FirebaseDatabase mDatabase;
    DatabaseReference mReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.market_item_click);

        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();

        // 사용자 id  받아오기
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        id = sharedPreferences.getString("id","");

        Intent intent = getIntent();
        String itemN = intent.getStringExtra("name");
        int itemP = intent.getIntExtra("price", 0);
        int itemI = intent.getIntExtra("image", 0);

        itemName = (TextView) findViewById(R.id.itemName);
        itemPrice = (TextView) findViewById(R.id.itemPrice);
        myPoint = (TextView) findViewById(R.id.myPoint);
        itemImage = (ImageView) findViewById(R.id.itemImage);
        buybtn = (Button) findViewById(R.id.buybtn);
        checkBox = (CheckBox) findViewById(R.id.checkBox);
        heart = (TextView) findViewById(R.id.heart);
        canBuy = true;

        itemName.setText(itemN);
        buybtn.setEnabled(false);

        // soldout resID: 2131165384
        // 품절이면 soldout 사진표시
        isExist = false;
        mReference.child("MarketItems").child(itemN).child("count").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if((int)dataSnapshot.getValue(Integer.class) <= 0) {
                    isExist = false;
                    itemImage.setImageResource(R.drawable.soldout);
                    itemPrice.setText("품절");
                    buybtn.setEnabled(false);
                } else {
                    isExist = true;
                    itemImage.setImageResource(itemI);
                    itemPrice.setText(itemP + "원");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //포인트 잔액 - 파이어베이스 연동
        mReference.child("users").child(id).child("Totalpoint").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                point = (int) dataSnapshot.getValue(Integer.class);
                if(point < itemP) canBuy = false;
                myPoint.setText(point+"");
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // 체크박스 표시해야 구매버튼 활성화
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkBox.isChecked() && isExist)
                    buybtn.setEnabled(true);
                else buybtn.setEnabled(false);
            }
        });

        //구매버튼 클릭
        buybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(canBuy) {
                    //날짜 및 시간 형식 지정
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String time = simpleDateFormat.format(System.currentTimeMillis());
                    // users - [id] - marketHistory - [시간] - [아이템 - 가격(원)] 형식으로 파베 저장
                    mReference.child("users").child(id).child("marketHistory").child(time).setValue(itemN + " - " + itemP + "원");
                    mReference.child("users").child(id).child("Totalpoint").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            point = (int) dataSnapshot.getValue(Integer.class);
                            int p = point - itemP;
                            Toast.makeText(getApplicationContext(), "결제완료! 잔액:" + p + "원", Toast.LENGTH_SHORT).show();
                            mReference.child("users").child(id).child("Totalpoint").setValue(p);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    mReference.child("MarketItems").child(itemN).child("count").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            int count = (int) dataSnapshot.getValue(Integer.class);
                            count--;
                            mReference.child("MarketItems").child(itemN).child("count").setValue(count);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    onBackPressed();
                }
                else
                    Toast.makeText(getApplicationContext(), "잔액이 부족합니다.", Toast.LENGTH_SHORT).show();
            }
        });
        // 찜하기 기능


    }
}
