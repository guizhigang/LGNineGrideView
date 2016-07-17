package com.gui.ninegrideview;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * Created by guizhigang on 16/7/14.
 */
public class MyImageLoader {

    private static MyImageLoader imageLoader;

    private MyImageLoader(Context context){
        init(context.getApplicationContext());
    }

    public static MyImageLoader getInstance(Context context){
        if(imageLoader == null){
            synchronized (MyImageLoader.class){
                if(imageLoader == null)
                    imageLoader = new MyImageLoader(context);
            }
        }
        return imageLoader;
    }

    private void init(Context context){
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.default_load)
                .showImageForEmptyUri(R.drawable.default_load)
                .showImageOnFail(R.drawable.default_load)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY)
                .build();

        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPoolSize(3);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024));
        config.memoryCacheSize(2 * 1024 * 1024);
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.defaultDisplayImageOptions(options);
        ImageLoader.getInstance().init(config.build());
        ImageLoader.getInstance().handleSlowNetwork(true);
    }

    public void loadImage(String url, ImageView imageView){
        ImageLoader.getInstance().displayImage(url,imageView);
    }
}
