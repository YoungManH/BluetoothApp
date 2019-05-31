package com.example.topbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Topbar extends RelativeLayout {
    private Button leftButton, rightButton;
    private TextView tvTitle;

    private int leftTextColor;
    private Drawable leftBackground;
    private String leftText;

    private int rightTextColor;
    private Drawable rightBackground;
    private String rightText;

    private float titleTextSize;
    private int titleTextColor;
    private String title;

    private LayoutParams leftParams, rightParams, titleParams;

    private topbarClickListener listener = null;

    public interface topbarClickListener {
        public void leftClick();

        public void rightClick();
    }

    public void setOnTopbarClickListener(topbarClickListener listener) {
        this.listener = listener;
    }

    public Topbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Topbar);


        leftTextColor = typedArray.getColor(R.styleable.Topbar_leftTextColor, 0);
        leftBackground = typedArray.getDrawable(R.styleable.Topbar_leftBackground);
        leftText = typedArray.getString(R.styleable.Topbar_leftText);

        rightTextColor = typedArray.getColor(R.styleable.Topbar_rightTextColor, 0);
        rightBackground = typedArray.getDrawable(R.styleable.Topbar_rightBackground);
        rightText = typedArray.getString(R.styleable.Topbar_rightText);

        titleTextSize = typedArray.getDimensionPixelSize(R.styleable.Topbar_titleTextSize, 0);
        titleTextColor = typedArray.getColor(R.styleable.Topbar_titleTextColor, 0);
        title = typedArray.getString(R.styleable.Topbar_title);

        //资源回收
        typedArray.recycle();

        leftButton = new Button(context);
        rightButton = new Button(context);
        tvTitle = new TextView(context);

        leftButton.setText(leftText);
        leftButton.setBackground(leftBackground);
        leftButton.setTextColor(leftTextColor);

        rightButton.setText(rightText);
        rightButton.setBackground(rightBackground);
        rightButton.setTextColor(rightTextColor);

        tvTitle.setText(title);
        tvTitle.setTextSize(titleTextSize);
        tvTitle.setTextColor(titleTextColor);
        tvTitle.setGravity(Gravity.CENTER);

        setBackgroundColor(0xFFF59563);

        leftParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        leftParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, TRUE);

        addView(leftButton, leftParams);

        rightParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, TRUE);

        addView(rightButton, rightParams);

        titleParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        titleParams.addRule(RelativeLayout.CENTER_IN_PARENT, TRUE);

        addView(tvTitle, titleParams);

        leftButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.leftClick();
            }
        });

        rightButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.rightClick();
            }
        });
    }
}
