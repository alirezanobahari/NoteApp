package com.individual.noteapp.application;

import android.content.Context;
import android.content.res.Configuration;

import com.individual.noteapp.models.Note;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.orm.SugarApp;
import com.orm.SugarContext;

/**
 * Created by Blackout on 1/25/2017.
 */

public class AppController extends SugarApp {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        SugarContext.init(getApplicationContext());
        try {
            Note.findById(Note.class, (long) 1);
        } catch (Exception e) {}

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getContext())
                .memoryCacheExtraOptions(480, 800)
                .diskCacheExtraOptions(480, 800, null)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100)
                .writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(config);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public static Context getContext() {
        return mContext;
    }




}
