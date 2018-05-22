package codeasylum.ua.paralaxscrollview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.NoSuchElementException;

import jp.wasabeef.picasso.transformations.BlurTransformation;

public class ParalaxBackground extends FrameLayout {

    private ViewPager viewPager;
    private int viewPagerId = -1;
    private HorizontalScrollView horizontalScrollView;
    private ImageView imageView;
    private String url;
    private int res;
    private boolean withBlur = false;
    private int blurRadius;
    private int placeholder_id;

    public ParalaxBackground(@NonNull Context context) {
        super(context);
    }

    public ParalaxBackground(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setAttributes(context, attrs);
    }

    public ParalaxBackground(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setAttributes(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ParalaxBackground(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setAttributes(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        setUpViews();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        initViews();

    }


    private void initViews() {
        if (viewPagerId != -1) {
            horizontalScrollView = (HorizontalScrollView) LayoutInflater.from(getContext()).inflate(R.layout.horizontal_scroll_item, null, false);
            imageView = horizontalScrollView.findViewById(R.id.background);
            this.addView(horizontalScrollView);
            viewPager = ((View) getParent()).findViewById(viewPagerId);
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    int x = (int) ((viewPager.getWidth() * position + positionOffsetPixels) * computeFactor());
                    horizontalScrollView.scrollTo(x, 0);
                }

                @Override
                public void onPageSelected(int position) {

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }

                private float computeFactor() {
                    return (imageView.getWidth() - viewPager.getWidth()) /
                            (float) (viewPager.getWidth() * (viewPager.getAdapter().getCount() - 1));
                }
            });
        } else throw new NoSuchElementException("ViewPager not added");
    }

    private void setAttributes(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ParalaxBackground);
        viewPagerId = typedArray.getResourceId(R.styleable.ParalaxBackground_viewPager, -1);
        typedArray.recycle();
    }


    private void setUpViews() {
        if (viewPager == null)
            return;

        if (viewPager.getAdapter() != null) {
            imageView.getLayoutParams().width = getWidth() * viewPager.getAdapter().getCount();
            if (res == -1) {
                if (withBlur) {
                    Picasso.with(getContext()).load(url).error(placeholder_id).placeholder(placeholder_id).transform(new BlurTransformation(getContext(), blurRadius)).into(imageView);
                } else {
                    Picasso.with(getContext()).load(url).error(placeholder_id).placeholder(placeholder_id).into(imageView);
                }
            } else {
                if (withBlur) {
                    Picasso.with(getContext()).load(res).error(placeholder_id).placeholder(placeholder_id).transform(new BlurTransformation(getContext(), blurRadius)).into(imageView);
                } else {
                    Picasso.with(getContext()).load(res).error(placeholder_id).placeholder(placeholder_id).into(imageView);
                }
            }
        }
    }




    public void setBackground(@DrawableRes int res, @DrawableRes int placeholder_id) {
        withBlur = false;
        this.res = res;
        url = null;
        this.placeholder_id = placeholder_id;
        updateParallax();
    }

    public void setBackground(String url, @DrawableRes int placeholder_id) {
        withBlur = false;
        res = -1;
        this.url = url;
        this.placeholder_id = placeholder_id;
        updateParallax();
    }

    public void setBlurBackground(@DrawableRes int res, @DrawableRes int placeholder_id, int blurRadius) {
        withBlur = true;
        this.blurRadius = blurRadius;
        this.res = res;
        url = null;
        this.placeholder_id = placeholder_id;
        updateParallax();
    }


    public void setBlurBackground(String url, @DrawableRes int placeholder_id, int blurRadius) {
        withBlur = true;
        this.blurRadius = blurRadius;
        res = -1;
        this.url = url;
        this.placeholder_id = placeholder_id;
        updateParallax();
    }


    public HorizontalScrollView getScrollView() {
        return horizontalScrollView;
    }

    public void updateParallax() {
        requestLayout();
    }
}