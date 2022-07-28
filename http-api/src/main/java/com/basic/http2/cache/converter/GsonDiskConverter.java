package com.basic.http2.cache.converter;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.basic.http2.logs.HttpLog;
import com.basic.http2.utils.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;

/**
 * Gson磁盘序列化器
 */
public class GsonDiskConverter implements IDiskConverter {
    private Gson mGson;

    public GsonDiskConverter() {
        mGson = new Gson();
    }

    public GsonDiskConverter(Gson gson) {
        mGson = gson;
    }

    @Override
    public <T> T load(InputStream source, Type type) {
        T value = null;
        try {
            if (mGson == null) {
                mGson = new Gson();
            }
            value = mGson.fromJson(new InputStreamReader(source), type);
        } catch (JsonIOException e) {
            HttpLog.e(e);
        } catch (JsonSyntaxException e) {
            HttpLog.e(e);
        } finally {
            Utils.closeIO(source);
        }
        return value;
    }

    @Override
    public boolean writer(OutputStream sink, Object data) {
        try {
            String json = mGson.toJson(data);
            byte[] bytes = json.getBytes();
            sink.write(bytes, 0, bytes.length);
            sink.flush();
            return true;
        } catch (JsonIOException e) {
            HttpLog.e(e);
        } catch (JsonSyntaxException e) {
            HttpLog.e(e);
        } catch (IOException e) {
            HttpLog.e(e);
        } finally {
            Utils.closeIO(sink);
        }
        return false;
    }

}

