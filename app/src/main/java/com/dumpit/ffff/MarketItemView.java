package com.dumpit.ffff;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class MarketItemView extends LinearLayout {

    TextView textView;
    TextView textView2;
    ImageView imageView;

    // Generate > Constructor

    public MarketItemView(Context context) {
        super(context);

        init(context);
    }

    public MarketItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    // singer_item.xml을 inflation
    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.market_item, this, true);

        textView = (TextView) findViewById(R.id.textView);
        textView2 = (TextView) findViewById(R.id.textView2);
        imageView = (ImageView) findViewById(R.id.imageView);
    }

    public void setName(String name) {
        textView.setText(name);
    }

    public void setPrice(int price) {
        textView2.setText(price+"");
    }

    public void setImage(int resId) {
        imageView.setImageResource(resId);
    }

}