package com.basic.code.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.LruCache;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.basic.web.core.AgentWeb;
import com.basic.web.core.client.DefaultWebClient;
import com.basic.image.PictureSelectionModel;
import com.basic.image.PictureSelector;
import com.basic.image.config.PictureConfig;
import com.basic.image.config.PictureMimeType;
import com.basic.face.FACE;
import com.basic.face.utils.DrawableUtils;
import com.basic.face.widget.dialog.materialdialog.MaterialDialog;
import com.basic.face.widget.imageview.preview.PreviewBuilder;
import com.basic.code.R;
import com.basic.code.activity.MainActivity;
import com.basic.code.base.webview.AgentWebActivity;
import com.basic.code.base.webview.MiddlewareWebViewClient;
import com.basic.code.fragment.components.imageview.preview.ImageViewInfo;
import com.basic.code.utils.update.CustomUpdateFailureListener;
import com.basic.renew.Update;
import com.basic.tools.Util;
import com.basic.tools.app.ActivityUtils;
import com.basic.tools.common.StringUtils;
import com.basic.tools.data.DateUtils;
import com.basic.tools.file.FileIOUtils;
import com.basic.tools.file.FileUtils;

import java.io.File;

import static com.basic.code.base.webview.AgentWebFragment.KEY_URL;

/**
 * @author XUE
 * @since 2019/4/1 11:25
 */
public final class Utils {

    public final static String UPDATE_URL = "https://gitee.com/zhiqiang/FACE/raw/master/jsonapi/update_api.json";

    private Utils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 初始化主题
     */
    public static void initTheme(Activity activity) {
        if (SettingSPUtils.getInstance().isUseCustomTheme()) {
            activity.setTheme(R.style.CustomAppTheme);
        } else {
            FACE.initTheme(activity);
        }
    }


    /**
     * 同步首页状态,未启动就启动
     */
    public static void syncMainPageStatus() {
        if (!Util.getActivityLifecycleHelper().isActivityExist(MainActivity.class)) {
            ActivityUtils.startActivity(MainActivity.class);
        }
    }

    /**
     * 请求浏览器
     *
     * @param url
     */
    public static void goWeb(Context context, final String url) {
        Intent intent = new Intent(context, AgentWebActivity.class);
        intent.putExtra(KEY_URL, url);
        context.startActivity(intent);
    }

    /**
     * 创建AgentWeb
     *
     * @param fragment
     * @param viewGroup
     * @param url
     * @return
     */
    public static AgentWeb createAgentWeb(Fragment fragment, ViewGroup viewGroup, String url) {
        return AgentWeb.with(fragment)
                .setAgentWebParent(viewGroup, -1, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
                .useDefaultIndicator(-1, 3)
                .useMiddlewareWebClient(new MiddlewareWebViewClient())
                .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)
                .createAgentWeb()
                .ready()
                .go(url);
    }

    /**
     * 进行版本更新检查
     *
     * @param context
     */
    public static void checkUpdate(Context context, boolean needErrorTip) {
        Update.newBuild(context).updateUrl(UPDATE_URL).update();
        Update.get().setOnUpdateFailureListener(new CustomUpdateFailureListener(needErrorTip));
    }

    //==========图片选择===========//

    /**
     * 获取图片选择的配置
     *
     * @param fragment
     * @return
     */
    public static PictureSelectionModel getPictureSelector(Fragment fragment) {
        return PictureSelector.create(fragment)
                .openGallery(PictureMimeType.ofImage())
                .theme(SettingSPUtils.getInstance().isUseCustomTheme() ? R.style.FACEPictureStyle_Custom : R.style.FACEPictureStyle)
                .maxSelectNum(8)
                .minSelectNum(1)
                .selectionMode(PictureConfig.MULTIPLE)
                .previewImage(true)
                .isCamera(true)
                .enableCrop(false)
                .compress(true)
                .previewEggs(true);
    }

    public static PictureSelectionModel getPictureSelector(Activity activity) {
        return PictureSelector.create(activity)
                .openGallery(PictureMimeType.ofImage())
                .theme(SettingSPUtils.getInstance().isUseCustomTheme() ? R.style.FACEPictureStyle_Custom : R.style.FACEPictureStyle)
                .maxSelectNum(8)
                .minSelectNum(1)
                .selectionMode(PictureConfig.MULTIPLE)
                .previewImage(true)
                .isCamera(true)
                .enableCrop(false)
                .compress(true)
                .previewEggs(true);
    }

    //==========图片预览===========//

    /**
     * 大图预览
     *
     * @param fragment
     * @param url      图片资源
     * @param view     小图加载控件
     */
    public static void previewPicture(Fragment fragment, String url, View view) {
        if (fragment == null || StringUtils.isEmpty(url)) {
            return;
        }
        Rect bounds = new Rect();
        if (view != null) {
            view.getGlobalVisibleRect(bounds);
        }
        PreviewBuilder.from(fragment)
                .setImgs(ImageViewInfo.newInstance(url, bounds))
                .setCurrentIndex(0)
                .setSingleFling(true)
                .setProgressColor(R.color.face_config_color_main_theme)
                .setType(PreviewBuilder.IndicatorType.Number)
                .start();
    }


    //==========拍照===========//

    public static final String JPEG = ".jpeg";

    /**
     * 处理拍照的回调
     *
     * @param data
     * @return
     */
    public static String handleOnPictureTaken(byte[] data) {
        return handleOnPictureTaken(data, JPEG);
    }

    /**
     * 处理拍照的回调
     *
     * @param data
     * @return
     */
    public static String handleOnPictureTaken(byte[] data, String fileSuffix) {
        String picPath = FileUtils.getDiskCacheDir() + "/images/" + DateUtils.getNowMills() + fileSuffix;
        boolean result = FileIOUtils.writeFileFromBytesByStream(picPath, data);
        return result ? picPath : "";
    }

    public static String getImageSavePath() {
        return FileUtils.getDiskCacheDir("images") + File.separator + DateUtils.getNowMills() + JPEG;
    }

    //==========截图===========//

    /**
     * 显示截图结果
     *
     * @param view
     */
    public static void showCaptureBitmap(View view) {
        final MaterialDialog dialog = new MaterialDialog.Builder(view.getContext())
                .customView(R.layout.dialog_drawable_utils_createfromview, true)
                .title("截图结果")
                .build();
        ImageView displayImageView = dialog.findViewById(R.id.createFromViewDisplay);
        Bitmap createFromViewBitmap = DrawableUtils.createBitmapFromView(view);
        displayImageView.setImageBitmap(createFromViewBitmap);

        displayImageView.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    /**
     * 显示截图结果
     */
    public static void showCaptureBitmap(Context context, Bitmap bitmap) {
        final MaterialDialog dialog = new MaterialDialog.Builder(context)
                .customView(R.layout.dialog_drawable_utils_createfromview, true)
                .title("截图结果")
                .build();
        ImageView displayImageView = dialog.findViewById(R.id.createFromViewDisplay);
        displayImageView.setImageBitmap(bitmap);

        displayImageView.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }


    /**
     * 截图RecyclerView
     *
     * @param recyclerView
     * @return
     */
    public static Bitmap getRecyclerViewScreenSpot(RecyclerView recyclerView) {
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        Bitmap bigBitmap = null;
        if (adapter != null) {
            int size = adapter.getItemCount();
            int height = 0;
            Paint paint = new Paint();
            int iHeight = 0;
            final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
            final int cacheSize = maxMemory / 8;
            LruCache<String, Bitmap> bitmapCache = new LruCache<>(cacheSize);
            for (int i = 0; i < size; i++) {
                RecyclerView.ViewHolder holder = adapter.createViewHolder(recyclerView, adapter.getItemViewType(i));
                adapter.onBindViewHolder(holder, i);
                holder.itemView.measure(
                        View.MeasureSpec.makeMeasureSpec(recyclerView.getWidth(), View.MeasureSpec.EXACTLY),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                holder.itemView.layout(0, 0, holder.itemView.getMeasuredWidth(),
                        holder.itemView.getMeasuredHeight());
                holder.itemView.setDrawingCacheEnabled(true);
                holder.itemView.buildDrawingCache();
                Bitmap drawingCache = holder.itemView.getDrawingCache();
                if (drawingCache != null) {
                    bitmapCache.put(String.valueOf(i), drawingCache);
                }
                height += holder.itemView.getMeasuredHeight();
            }
            // 这个地方容易出现OOM，关键是要看截取RecyclerView的展开的宽高
            bigBitmap = DrawableUtils.createBitmapSafely(recyclerView.getMeasuredWidth(), height, Bitmap.Config.ARGB_8888, 1);
            if (bigBitmap == null) {
                return null;
            }
            Canvas canvas = new Canvas(bigBitmap);
            Drawable background = recyclerView.getBackground();
            //先画RecyclerView的背景色
            if (background instanceof ColorDrawable) {
                ColorDrawable lColorDrawable = (ColorDrawable) background;
                int color = lColorDrawable.getColor();
                canvas.drawColor(color);
            }
            for (int i = 0; i < size; i++) {
                Bitmap bitmap = bitmapCache.get(String.valueOf(i));
                canvas.drawBitmap(bitmap, 0f, iHeight, paint);
                iHeight += bitmap.getHeight();
                bitmap.recycle();
            }
            canvas.setBitmap(null);
        }
        return bigBitmap;
    }


    public static FlexboxLayoutManager getFlexboxLayoutManager(Context context) {
        //设置布局管理器
        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(context);
        //flexDirection 属性决定主轴的方向（即项目的排列方向）。类似 LinearLayout 的 vertical 和 horizontal:
        // 主轴为水平方向，起点在左端。
        flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);
        //flexWrap 默认情况下 Flex 跟 LinearLayout 一样，都是不带换行排列的，但是flexWrap属性可以支持换行排列:
        // 按正常方向换行
        flexboxLayoutManager.setFlexWrap(FlexWrap.WRAP);
        //justifyContent 属性定义了项目在主轴上的对齐方式:
        // 交叉轴的起点对齐
        flexboxLayoutManager.setJustifyContent(JustifyContent.FLEX_START);
        return flexboxLayoutManager;
    }

}
