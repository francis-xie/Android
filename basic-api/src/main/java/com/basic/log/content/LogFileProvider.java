package com.basic.log.content;

import androidx.core.content.FileProvider;

/**
 * 继承FileProvider是为了防止manifest中Provider定义重复
 */
public class LogFileProvider extends FileProvider {

}
