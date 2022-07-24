
package com.basic.code.widget.iconfont;

import android.graphics.Typeface;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import com.mikepenz.iconics.Iconics;
import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.iconics.typeface.ITypeface;
import com.basic.aop.annotation.MemoryCache;
import com.basic.code.R;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * FACE字体图标库，是使用 <a href="https://www.iconfont.cn/"> 平台自动生成的
 *

 * @since 2019-10-13 16:29
 */
public class FACEIconFont implements ITypeface {

    private static Map<String, Character> sChars;

    @NonNull
    @Override
    public String getAuthor() {
        return "zhiqiang";
    }

    @NonNull
    @Override
    public Map<String, Character> getCharacters() {
        if (sChars == null) {
            Map<String, Character> aChars = new HashMap<>();
            for (Icon v : Icon.values()) {
                aChars.put(v.name(), v.character);
            }
            sChars = aChars;
        }
        return sChars;
    }

    @NonNull
    @Override
    public String getDescription() {
        return "FACE字体图标库";
    }

    @NonNull
    @Override
    public String getFontName() {
        return "FACEIconFont";
    }

    @Override
    public int getIconCount() {
        return Icon.values().length;
    }

    @NonNull
    @Override
    public List<String> getIcons() {
        List<String> icons = new LinkedList<>();
        for (Icon value : Icon.values()) {
            icons.add(value.name());
        }
        return icons;
    }

    @NonNull
    @Override
    public String getLicense() {
        return "";
    }

    @NonNull
    @Override
    public String getLicenseUrl() {
        return "";
    }

    @NonNull
    @Override
    public String getMappingPrefix() {
        return "face";
    }

    @NonNull
    @Override
    public String getUrl() {
        return "https://github.com/zhiqiang/FACE";
    }

    @NonNull
    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @NonNull
    @Override
    public IIcon getIcon(@NonNull String key) {
        return Icon.valueOf(key);
    }

    private static ITypeface sITypeface;

    @Override
    public int getFontRes() {
        return R.font.iconfont;
    }

    @NotNull
    @Override
    public Typeface getRawTypeface() {
        return ResourcesCompat.getFont(Iconics.getApplicationContext(), getFontRes());
    }

    /**
     * 图标枚举
     */
    public enum Icon implements IIcon {

        face_file('\ue600'),
        face_chat('\ue601'),
        face_voice('\ue602'),
        face_delete('\ue603'),
        face_delete1('\ue613'),
        face_delete2('\ue630'),
        face_delete3('\ue658'),
        face_back('\ue609'),
        face_back1('\ue614'),
        face_add('\ue612'),
        face_add1('\ue615'),
        face_add2('\ue631'),
        face_reset('\ue616'),
        face_complete('\ue650'),
        face_complete1('\ue673'),
        face_collect('\ue77f'),
        face_emoj('\ue628');

        char character;

        Icon(char character) {
            this.character = character;
        }

        @Override
        public char getCharacter() {
            return character;
        }

        @Override
        public String getFormattedName() {
            return "{" + name() + "}";
        }

        @NonNull
        @Override
        public String getName() {
            return name();
        }

        @NonNull
        @Override
        public ITypeface getTypeface() {
            if (sITypeface == null) {
                sITypeface = new FACEIconFont();
            }
            return sITypeface;
        }

        public static Icon get(String name) {
            try {
                return Icon.valueOf("face_" + name);
            } catch (Exception e) {
            }
            return null;
        }

        public String getIconText() {
            return name().replace("face_", "");
        }

        @MemoryCache
        public static List<String> getAllIconTexts() {
            List<String> icons = new LinkedList<>();
            for (Icon value : Icon.values()) {
                icons.add(value.getIconText());
            }
            return icons;
        }


    }
}
