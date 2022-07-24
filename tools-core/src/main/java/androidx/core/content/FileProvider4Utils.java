
package androidx.core.content;

import android.app.Application;

import androidx.annotation.Keep;

import com.basic.tools.Util;
import com.basic.tools.common.ObjectUtils;

/**
 * 提供FileProvider能力，并执行自动初始化
 *

 * @since 2020/6/5 11:26 PM
 */
@Keep
public final class FileProvider4Utils extends FileProvider {

    @Override
    public boolean onCreate() {
        if (Util.isAutoInit()) {
            Application application = ObjectUtils.cast(getContext(), Application.class);
            if (application != null) {
                Util.init(application.getApplicationContext());
            }
        }
        return super.onCreate();
    }
}
