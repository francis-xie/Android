package com.basic.floats.permission;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import com.basic.floats.permission.rom.HuaweiUtils;
import com.basic.floats.permission.rom.MeizuUtils;
import com.basic.floats.permission.rom.MiuiUtils;
import com.basic.floats.permission.rom.OppoUtils;
import com.basic.floats.permission.rom.QikuUtils;
import com.basic.floats.permission.rom.RomUtils;

import java.lang.reflect.Method;

/**
 * 悬浮窗权限申请
 */
public class FloatWindowPermission {
    private static final String TAG = "FloatWindowPermission";
    public static final int REQUEST_OVERLAY_PERMISSION_CODE = 10001;

    private static volatile FloatWindowPermission sInstance;

    private IPermissionApplyPrompter mIPermissionApplyPrompter;

    private FloatWindowPermission() {
        mIPermissionApplyPrompter = new DefaultPermissionApplyPrompter();
    }

    public static FloatWindowPermission getInstance() {
        if (sInstance == null) {
            synchronized (FloatWindowPermission.class) {
                if (sInstance == null) {
                    sInstance = new FloatWindowPermission();
                }
            }
        }
        return sInstance;
    }

    public Dialog applyFloatWindowPermission(Context context) {
        if (!checkPermission(context)) {
            return applyPermission(context);
        }
        return null;
    }

    private boolean checkPermission(Context context) {
        //6.0 版本之后由于 google 增加了对悬浮窗权限的管理，所以方式就统一了
        if (Build.VERSION.SDK_INT < 23) {
            if (RomUtils.checkIsMiuiRom()) {
                return miuiPermissionCheck(context);
            } else if (RomUtils.checkIsMeizuRom()) {
                return meizuPermissionCheck(context);
            } else if (RomUtils.checkIsHuaweiRom()) {
                return huaweiPermissionCheck(context);
            } else if (RomUtils.checkIs360Rom()) {
                return qikuPermissionCheck(context);
            } else if (RomUtils.checkIsOppoRom()) {
                return oppoROMPermissionCheck(context);
            }
        }
        return commonROMPermissionCheck(context);
    }

    private boolean huaweiPermissionCheck(Context context) {
        return HuaweiUtils.checkFloatWindowPermission(context);
    }

    private boolean miuiPermissionCheck(Context context) {
        return MiuiUtils.checkFloatWindowPermission(context);
    }

    private boolean meizuPermissionCheck(Context context) {
        return MeizuUtils.checkFloatWindowPermission(context);
    }

    private boolean qikuPermissionCheck(Context context) {
        return QikuUtils.checkFloatWindowPermission(context);
    }

    private boolean oppoROMPermissionCheck(Context context) {
        return OppoUtils.checkFloatWindowPermission(context);
    }

    private boolean commonROMPermissionCheck(Context context) {
        //最新发现魅族6.0的系统这种方式不好用，天杀的，只有你是奇葩，没办法，单独适配一下
        if (RomUtils.checkIsMeizuRom()) {
            return meizuPermissionCheck(context);
        } else {
            Boolean result = true;
            if (Build.VERSION.SDK_INT >= 23) {
                try {
                    Class clazz = Settings.class;
                    Method canDrawOverlays = clazz.getDeclaredMethod("canDrawOverlays", Context.class);
                    result = (Boolean) canDrawOverlays.invoke(null, context);
                } catch (Exception e) {
                    Log.e(TAG, Log.getStackTraceString(e));
                }
            }
            return result;
        }
    }

    private Dialog applyPermission(Context context) {
        if (Build.VERSION.SDK_INT < 23) {
            if (RomUtils.checkIsMiuiRom()) {
                return miuiROMPermissionApply(context);
            } else if (RomUtils.checkIsMeizuRom()) {
                return meizuROMPermissionApply(context);
            } else if (RomUtils.checkIsHuaweiRom()) {
                return huaweiROMPermissionApply(context);
            } else if (RomUtils.checkIs360Rom()) {
                return ROM360PermissionApply(context);
            } else if (RomUtils.checkIsOppoRom()) {
                return oppoROMPermissionApply(context);
            }
        } else {
            return commonROMPermissionApply(context);
        }
        return null;
    }

    private Dialog ROM360PermissionApply(final Context context) {
        return showConfirmDialog(context, new OnConfirmResult() {
            @Override
            public void confirmResult(boolean confirm) {
                if (confirm) {
                    QikuUtils.applyPermission(context);
                } else {
                    Log.e(TAG, "ROM:360, user manually refuse OVERLAY_PERMISSION");
                }
            }
        });
    }

    private Dialog huaweiROMPermissionApply(final Context context) {
        return showConfirmDialog(context, new OnConfirmResult() {
            @Override
            public void confirmResult(boolean confirm) {
                if (confirm) {
                    HuaweiUtils.applyPermission(context);
                } else {
                    Log.e(TAG, "ROM:huawei, user manually refuse OVERLAY_PERMISSION");
                }
            }
        });
    }

    private Dialog meizuROMPermissionApply(final Context context) {
        return showConfirmDialog(context, new OnConfirmResult() {
            @Override
            public void confirmResult(boolean confirm) {
                if (confirm) {
                    MeizuUtils.applyPermission(context);
                } else {
                    Log.e(TAG, "ROM:meizu, user manually refuse OVERLAY_PERMISSION");
                }
            }
        });
    }

    private Dialog miuiROMPermissionApply(final Context context) {
        return showConfirmDialog(context, new OnConfirmResult() {
            @Override
            public void confirmResult(boolean confirm) {
                if (confirm) {
                    MiuiUtils.applyMiuiPermission(context);
                } else {
                    Log.e(TAG, "ROM:miui, user manually refuse OVERLAY_PERMISSION");
                }
            }
        });
    }

    private Dialog oppoROMPermissionApply(final Context context) {
        return showConfirmDialog(context, new OnConfirmResult() {
            @Override
            public void confirmResult(boolean confirm) {
                if (confirm) {
                    OppoUtils.applyOppoPermission(context);
                } else {
                    Log.e(TAG, "ROM:miui, user manually refuse OVERLAY_PERMISSION");
                }
            }
        });
    }

    /**
     * 通用 rom 权限申请
     */
    private Dialog commonROMPermissionApply(final Context context) {
        //这里也一样，魅族系统需要单独适配
        if (RomUtils.checkIsMeizuRom()) {
            return meizuROMPermissionApply(context);
        } else {
            if (Build.VERSION.SDK_INT >= 23) {
                return showConfirmDialog(context, new OnConfirmResult() {
                    @Override
                    public void confirmResult(boolean confirm) {
                        if (confirm) {
                            try {
                                commonROMPermissionApplyInternal(context);
                            } catch (Exception e) {
                                Log.e(TAG, Log.getStackTraceString(e));
                            }
                        } else {
                            Log.d(TAG, "user manually refuse OVERLAY_PERMISSION");
                            //需要做统计效果
                        }
                    }
                });
            }
        }
        return null;
    }

    public static void commonROMPermissionApplyInternal(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(activity)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + activity.getPackageName()));
                activity.startActivityForResult(intent, REQUEST_OVERLAY_PERMISSION_CODE);
            }
        }
    }

    public static void commonROMPermissionApplyInternal(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(context)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + context.getPackageName()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        }
    }

    private Dialog showConfirmDialog(Context context, OnConfirmResult result) {
        return showConfirmDialog(context, "您的手机没有授予悬浮窗权限，请开启后再试", result);
    }

    /**
     * 设置悬浮权限申请提示
     *
     * @param iPermissionApplyPrompter
     * @return
     */
    public FloatWindowPermission setIPermissionApplyPrompter(@NonNull IPermissionApplyPrompter iPermissionApplyPrompter) {
        mIPermissionApplyPrompter = iPermissionApplyPrompter;
        return this;
    }

    private Dialog showConfirmDialog(Context context, String message, final OnConfirmResult result) {
        return mIPermissionApplyPrompter.showPermissionApplyDialog(context, message, result);
    }

    public interface OnConfirmResult {
        /**
         * @param confirm 是否确认申请悬浮权限
         */
        void confirmResult(boolean confirm);
    }


    /**
     * 默认悬浮权限申请提示
     */
    public class DefaultPermissionApplyPrompter implements IPermissionApplyPrompter {

        @Override
        public Dialog showPermissionApplyDialog(Context context, String message, final OnConfirmResult result) {
            Dialog dialog = new AlertDialog.Builder(context).setCancelable(true).setTitle("")
                    .setMessage(message)
                    .setPositiveButton("现在去开启",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    result.confirmResult(true);
                                    dialog.dismiss();
                                }
                            }).setNegativeButton("暂不开启",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    result.confirmResult(false);
                                    dialog.dismiss();
                                }
                            }).create();
            dialog.show();
            return dialog;
        }
    }

}
