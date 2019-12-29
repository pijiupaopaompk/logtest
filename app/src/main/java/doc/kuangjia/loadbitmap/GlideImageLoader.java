package com.zkys.pad.launcher.loadbitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;
import android.support.annotation.UiThread;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.util.Util;

import java.io.File;
import java.io.FileOutputStream;


/**
 * Created by Grady on 2016/10/24.
 */

public class GlideImageLoader {

    /**
     * RecyclerView 滚动时，暂停加载
     */
//    @UiThread
//    public static void addScrollListener(RecyclerView view) {
//        GlideScrollListener listener = new GlideScrollListener();
//        view.addOnScrollListener(listener);
//    }

    @UiThread
    public static void pauseRequests(Context context) {
        if (Util.isOnMainThread()) {
            Glide.with(context).pauseRequests();
        }
    }

    @UiThread
    public static void resumeRequests(Context context) {
        if (Util.isOnMainThread()) {
            Glide.with(context).resumeRequests();
        }
    }


    public static void loadImage(String url, ImageView view, int defaultId) {
        if (Util.isOnMainThread()) {
            RequestOptions options = new RequestOptions();
            options.error(defaultId);
            options.placeholder(defaultId);
            options.diskCacheStrategy(DiskCacheStrategy.ALL);
            if (defaultId != -1) {
                Glide.with(view.getContext()).load(url).apply(options).into(view);
            } else {
                Glide.with(view.getContext()).load(url).into(view);
            }
        }
    }


    public static void loadImage(String url, ImageView view) {
        loadImage(url, view, -1);
    }


    public static void loadRoundImage(String url, ImageView view) {
        if (Util.isOnMainThread()) {
            RequestOptions options = new RequestOptions();
            options.transform(new GlideRoundTransform(view.getContext(), 6));
            Glide.with(view.getContext()).load(url).apply(options).into(view);
        }
    }

    public static void loadCircleImage(@DrawableRes int resId, ImageView view) {
        if (Util.isOnMainThread()) {
            RequestOptions options = new RequestOptions();
            options.transform(new GlideCircleTransform(view.getContext()));
            Glide.with(view.getContext()).load(resId).apply(options).into(view);
        }
    }
    public static void loadCircleImage(String url, ImageView view) {
        if (Util.isOnMainThread()) {
            RequestOptions options = new RequestOptions();
            options.transform(new GlideCircleTransform(view.getContext()));
            Glide.with(view.getContext()).load(url).apply(options).into(view);
        }
    }
    public static void loadCircleImage(String url, ImageView view ,@DrawableRes int resId) {
        if (Util.isOnMainThread()) {
            RequestOptions options = new RequestOptions();
            options.transform(new GlideCircleTransform(view.getContext()));
            options.placeholder(resId);
            Glide.with(view.getContext()).load(url).apply(options).into(view);
        }
    }
    /**
     * 加载网络大图
     *
     * @param url
     * @param view
     */
    public static void loadBigImage(String url, ImageView view, BitmapTransformation transformation) {
        if (Util.isOnMainThread()) {
            RequestOptions options = new RequestOptions();
            options.transform(transformation);
            Glide.with(view.getContext()).load(url).apply(options).into(view);//placeholder(R.drawable.default_big_image).
        }
    }

    /**
     * 加载网络小图
     *
     * @param url
     * @param view
     * @param transformation
     */
    public static void loadSmallImage(String url, ImageView view, BitmapTransformation transformation) {
        RequestOptions options = null;
        if (Util.isOnMainThread()) {
            if (transformation != null) {
                if (options == null) {
                    options = new RequestOptions();
                }
                options.transform(transformation);
                Glide.with(view.getContext()).load(url).apply(options).into(view);//placeholder(R.drawable.default_image).
            } else {
                Glide.with(view.getContext()).load(url).into(view);//placeholder(R.drawable.default_image).
            }
        }
    }

    /**
     * 加载头像
     *
     * @param url
     * @param view
     * @param drawable
     */
    public static void loadSmallImage(String url, ImageView view, int drawable) {
        RequestOptions options = new RequestOptions();
        options.placeholder(drawable);
        if (Util.isOnMainThread()) {
            options.transform(new GlideCircleTransform(view.getContext()));
            Glide.with(view.getContext()).load(url).apply(options).into(view);//placeholder(R.drawable.default_image).
        }
    }


    /**
     * 加载本地文件路径的图片
     *
     * @param view       ImageView
     * @param filePath   文件路径   string
     * @param defaultRId 默认图片资源
     */
    public static void loadBitmapByLocal(ImageView view, String filePath, @DrawableRes int defaultRId) {
        if (TextUtils.isEmpty(filePath)) {
            return;
        }
        RequestOptions options = new RequestOptions();
        options.placeholder(defaultRId);
        File file = new File(filePath);
        Glide.with(view.getContext()).load(file).apply(options).into(view);
    }


    /**
     * 加载本地文件路径的图片
     *
     * @param view     ImageView
     * @param filePath 文件路径    string
     */
    public static boolean loadBitmapByLocal(ImageView view, String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return false;
        }
        File file = new File(filePath);
        if (file.exists()) {
            Glide.with(view.getContext()).load(file).into(view);
            return true;
        }
        return false;
    }


    /**
     * PS:  运行在 backThread
     * 根据网络图片的链接，获取本地图片文件的路径 string
     *
     * @param context 上下文对象
     * @param url     网络路径
     * @return String
     */
    public static String getFilePathByUrl(Context context, String url) {
        try {
            File file = Glide.with(context).downloadOnly().load(url).submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
            if (file != null) {
                return file.getAbsolutePath();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 加载本地资源图片
     *
     * @param drawable
     * @param view
     * @param transformation
     */
    public static void loadLocalImage(int drawable, ImageView view, BitmapTransformation transformation) {
        if (Util.isOnMainThread()) {
            if (transformation != null) {
                RequestOptions options = null;
                if (options == null) {
                    options = new RequestOptions();
                }
                options.transform(transformation);
                Glide.with(view.getContext()).asBitmap().load(drawable).apply(options).into(view);
            } else {
                Glide.with(view.getContext()).asBitmap().load(drawable).into(view);
            }
        }
    }

    /**
     * 以Glide目录保存
     *
     * @param context
     * @param bitmap
     * @return
     */
    public static String saveBitmap(Context context, Bitmap bitmap) {
        File cacheDir = Glide.getPhotoCacheDir(context);
        File fileLocal = new File(cacheDir, "media" + System.currentTimeMillis());
        String filePath = isSaveBitmap(fileLocal, bitmap) ? fileLocal.getAbsolutePath() : "";
        return filePath;
    }


    public static boolean isSaveBitmap(File filePath, Bitmap bitmap) {
        createFile(filePath.getAbsolutePath());
        try {
            FileOutputStream fos = new FileOutputStream(filePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }


    /**
     * 指定默认图片加载
     *
     * @param url
     * @param view
     */
    public static void loadLocalImage(String url, ImageView view) {
        if (Util.isOnMainThread()) {
            Glide.with(view.getContext()).load(url).into(view);
        }
    }

    /**
     * 加载本地图片
     *
     * @param url
     * @param view
     * @param transformation
     */
    public static void loadLocalImage(String url, ImageView view, BitmapTransformation transformation) {
        if (Util.isOnMainThread()) {
            if (transformation != null) {
                RequestOptions options = null;
                if (options == null) {
                    options = new RequestOptions();
                }
                options.transform(transformation);
                Glide.with(view.getContext()).load(url).apply(options).into(view);
            } else {
                Glide.with(view.getContext()).load(url).into(view);
            }
        }
    }

    /**
     * 指定默认图片加载
     *
     * @param url
     * @param defaultId
     * @param view
     */
    public static void loadLocalImage(String url, int defaultId, ImageView view) {
        if (Util.isOnMainThread()) {
            RequestOptions options = null;
            if (options == null) {
                options = new RequestOptions();
            }
            options.placeholder(defaultId);
            Glide.with(view.getContext()).load(url).apply(options).into(view);
        }
    }


    /**
     * 指定默认图片加载
     *
     * @param defaultId
     * @param view
     */
    public static void loadLocalImage(int rId, int defaultId, ImageView view) {
        if (Util.isOnMainThread()) {
            RequestOptions options = null;
            if (options == null) {
                options = new RequestOptions();
            }
            options.placeholder(defaultId);
            Glide.with(view.getContext()).load(rId).apply(options).into(view);
        }
    }


    public static void createFile(String filePath) {
        if (TextUtils.isEmpty(filePath))
            return;
        File file = new File(filePath);
        if (file.exists()) {
            return;
        }
        File parenFile = file.getParentFile();
        if (parenFile != null) {
            boolean bool = parenFile.mkdirs();
            Log.e("msg", "boolean: " + bool);
        }
    }

}
