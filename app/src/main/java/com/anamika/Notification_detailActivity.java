package com.anamika;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

import Config.ConstValue;
import util.AnimateFirstDisplayListener;

public class Notification_detailActivity extends CommonAppCompatActivity {

    private TextView tv_date, tv_detail, tv_title;
    private ImageView iv_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_detail);

        File cacheDir = StorageUtils.getCacheDirectory(this);
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.slogooo)
                .showImageForEmptyUri(R.drawable.slogooo)
                .showImageOnFail(R.drawable.slogooo)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new SimpleBitmapDisplayer())
                .imageScaleType(ImageScaleType.NONE)
                .build();

        ImageLoaderConfiguration imgconfig = new ImageLoaderConfiguration.Builder(this)
                .build();
        ImageLoader.getInstance().init(imgconfig);
        ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

        Intent args = getIntent();

        String title = args.getStringExtra("title");
        String date = args.getStringExtra("date");
        String message = args.getStringExtra("desc");
        String image = args.getStringExtra("image_path");

        tv_title = (TextView) findViewById(R.id.tv_notific_title);
        tv_detail = (TextView) findViewById(R.id.tv_notific_detail);
        tv_date = (TextView) findViewById(R.id.tv_notific_date);
        iv_img = (ImageView) findViewById(R.id.iv_notific_img);

        tv_title.setText(title);
        tv_date.setText(date);
        tv_detail.setText(message);

        if (image == null) {
            iv_img.setVisibility(View.GONE);
        } else {
            iv_img.setVisibility(View.VISIBLE);

            ImageLoader.getInstance().displayImage(ConstValue.IMG_NOTIFICATION_URL + image, iv_img, options, animateFirstListener);
        }

    }
}
