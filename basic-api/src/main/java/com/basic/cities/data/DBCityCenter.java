package com.basic.cities.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.basic.cities.model.City;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.basic.cities.data.DBConfig.COLUMN_C_CODE;
import static com.basic.cities.data.DBConfig.COLUMN_C_NAME;
import static com.basic.cities.data.DBConfig.COLUMN_C_PINYIN;
import static com.basic.cities.data.DBConfig.COLUMN_C_PROVINCE;
import static com.basic.cities.data.DBConfig.DB_NAME;
import static com.basic.cities.data.DBConfig.TABLE_NAME;

/**
 * 城市中心信息存储
 */
public class DBCityCenter implements ICityCenter {

    private static final int BUFFER_SIZE = 1024;

    private String mDataBasePath;
    private Context mContext;

    public DBCityCenter(Context context) {
        mContext = context.getApplicationContext();
        mDataBasePath = File.separator + "data"
                + Environment.getDataDirectory().getAbsolutePath() + File.separator
                + context.getPackageName() + File.separator + "databases" + File.separator;
        copyDBFile();
    }

    private void copyDBFile() {
        File dir = new File(mDataBasePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        //创建新版本数据库
        File dbFile = new File(mDataBasePath + DB_NAME);
        if (!dbFile.exists()) {
            InputStream is;
            OutputStream os;
            try {
                is = mContext.getResources().getAssets().open(DB_NAME);
                os = new FileOutputStream(dbFile);
                byte[] buffer = new byte[BUFFER_SIZE];
                int length;
                while ((length = is.read(buffer, 0, buffer.length)) > 0) {
                    os.write(buffer, 0, length);
                }
                os.flush();
                os.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<City> getAllCities() {
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(mDataBasePath + DB_NAME, null);
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME, null);
        List<City> result = new ArrayList<>();
        City city;
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(COLUMN_C_NAME));
            String province = cursor.getString(cursor.getColumnIndex(COLUMN_C_PROVINCE));
            String pinyin = cursor.getString(cursor.getColumnIndex(COLUMN_C_PINYIN));
            String code = cursor.getString(cursor.getColumnIndex(COLUMN_C_CODE));
            city = new City(name, province, pinyin, code);
            result.add(city);
        }
        cursor.close();
        db.close();
        Collections.sort(result, new CityComparator());
        return result;
    }

    @Override
    public List<City> searchCity(final String keyword) {
        String sql = "select * from " + TABLE_NAME + " where "
                + COLUMN_C_NAME + " like ? " + "or "
                + COLUMN_C_PINYIN + " like ? ";
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(mDataBasePath + DB_NAME, null);
        Cursor cursor = db.rawQuery(sql, new String[]{"%" + keyword + "%", keyword + "%"});

        List<City> result = new ArrayList<>();
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(COLUMN_C_NAME));
            String province = cursor.getString(cursor.getColumnIndex(COLUMN_C_PROVINCE));
            String pinyin = cursor.getString(cursor.getColumnIndex(COLUMN_C_PINYIN));
            String code = cursor.getString(cursor.getColumnIndex(COLUMN_C_CODE));
            City city = new City(name, province, pinyin, code);
            result.add(city);
        }
        cursor.close();
        db.close();
        CityComparator comparator = new CityComparator();
        Collections.sort(result, comparator);
        return result;
    }

    /**
     * sort by a-z
     */
    private class CityComparator implements Comparator<City> {
        @Override
        public int compare(City lhs, City rhs) {
            String a = lhs.getPinyin().substring(0, 1);
            String b = rhs.getPinyin().substring(0, 1);
            return a.compareTo(b);
        }
    }
}
