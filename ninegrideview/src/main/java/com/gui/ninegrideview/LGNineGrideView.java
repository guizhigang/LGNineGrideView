package com.gui.ninegrideview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by guizhigang on 16/7/13.
 */
public class LGNineGrideView extends ViewGroup {
    private final String TAG = "NineGrideView";

    private static final int DEFAULT_SPACING = 2;
    private static final int MAX_COUNT = 9;
    private int space = 0;
    private int grideWidth = 0;
    private int grideHeight = 0;
    private List<String> urls = new ArrayList<>();
    private Context context;
    private int rows;
    private int colums;

    public interface ImageCreator {
        ImageView createImageView(Context context);

        void loadImage(Context context, String url, ImageView imageView);
    }
    public interface OnItemClickListener{
        void onClickItem(int position,View view);
    }

    private ImageCreator imageCreator;
    private OnItemClickListener onItemClickListener;

    public LGNineGrideView(Context context) {
        super(context);
        initView(context, null);
    }

    public LGNineGrideView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public LGNineGrideView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LGNineGrideView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setImageCreator(ImageCreator imageCreator) {
        this.imageCreator = imageCreator;
    }

    private void initView(Context context, AttributeSet attrs) {
        this.context = context;
        int defaultSpace = DensityUtil.dp2px(context, DEFAULT_SPACING);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LGNineGrideView);
        space = (int) typedArray.getDimension(R.styleable.LGNineGrideView_space, defaultSpace);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        if (urls == null) {
            setVisibility(GONE);
            return;
        }
        int size = urls.size();
        int sugMinWidth = getSuggestedMinimumWidth();
        int minWidth = getPaddingLeft() + getPaddingRight() + sugMinWidth;
        int totalWidth = resolveSizeAndState(minWidth, widthMeasureSpec, 0);
        int availableWidth = totalWidth - getPaddingLeft() - getPaddingRight();
        if (size == 1) {
            grideWidth = availableWidth * 2 / 3;
            grideHeight = grideWidth;
        } else {
            grideWidth = (availableWidth - space * (colums - 1)) / 3;
            grideHeight = grideWidth;
        }
        int height = rows * grideHeight + (rows - 1) * space + getPaddingTop() + getPaddingBottom();
        setMeasuredDimension(totalWidth, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int size = urls.size();
        if (size == 0)
            return;
        int tmpWidth = grideWidth;
        int tmpHeight = grideHeight;

        int childSize = getChildCount();

        for (int i = 0; i < size; ++i) {
            final String url = urls.get(i);
            final int position = i;
            ImageView view = (ImageView) getChildAt(i);
            if (view == null) {
                if (imageCreator == null) {
                    imageCreator = DefaultImageCreator.getInstance();
                }
                view = imageCreator.createImageView(context);
                imageCreator.loadImage(context, url, view);
                addView(view);
                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(onItemClickListener != null){
                            onItemClickListener.onClickItem(position,v);
                        }
                    }
                });
            }
            view.setVisibility(VISIBLE);
            l = i % colums * (tmpWidth + space) + getPaddingLeft();
            t = i / colums * (tmpHeight + space) + getPaddingTop();
            r = l + tmpWidth;
            b = t + tmpHeight;
            view.layout(l, t, r, b);
        }

        if (size < childSize) {
            for (int i = size; i < childSize; ++i) {
                ImageView view = (ImageView) getChildAt(i);
                view.setVisibility(GONE);
            }
        }
    }

    private void initRowAndColum(int size) {
        rows = (size - 1) / 3 + 1;
        colums = (size - 1) % 3 + 1;
        if (size == 4) {
            rows = 2;
            colums = 2;
            return;
        } else {
            colums = 3;
        }
    }

    public void setImageDatas(List<String> urls) {
        if (urls == null || urls.isEmpty()) {
            setVisibility(GONE);
            return;
        }
        setVisibility(VISIBLE);
        if (this.urls == urls) {
            return;
        }
        this.urls.clear();
        this.urls.addAll(urls);
        initRowAndColum(urls.size());
        requestLayout();
    }

}
