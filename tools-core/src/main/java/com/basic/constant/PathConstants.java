
package com.basic.constant;

import com.basic.tools.app.PathUtils;

import java.io.File;

/**
 * 文件路径常量
 *

 * @since 2020/6/6 1:51 AM
 */
public final class PathConstants {

    public static final String EXT_STORAGE_PATH = PathUtils.getExtStoragePath();

    public static final String EXT_STORAGE_DIR = EXT_STORAGE_PATH + File.separator;

    public static final String APP_EXT_STORAGE_PATH = EXT_STORAGE_DIR + "Android";

    public static final String EXT_DOWNLOADS_PATH = PathUtils.getExtDownloadsPath();

    public static final String EXT_PICTURES_PATH = PathUtils.getExtPicturesPath();

    public static final String EXT_DCIM_PATH = PathUtils.getExtDCIMPath();
}
