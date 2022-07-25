package com.emis.venus.entity;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.HashMap;

public class SettingEntity {

  HashMap<String, Object> MapSetting;

  public SettingEntity(Context context) {
    SharedPreferences.Editor editor = (PreferenceManager.getDefaultSharedPreferences(context)).edit();

    // SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
    MapSetting = (HashMap<String, Object>) (PreferenceManager.getDefaultSharedPreferences(context)).getAll();

    // System.out.println("settings size():" + MapSetting.size());
    // for (HashMap.Entry<String, Object> entry : MapSetting.entrySet()) System.out.println("key:" + entry.getKey() + ", value:" + entry.getValue());
    if (MapSetting.size() == 0) {
      // 新安裝的程式沒有任何設定資料，寫入預設資料內容。
      editor.putString("USERID", "");
      editor.putString("PASSWORD", "");
      editor.putString("S_NO", "");
      editor.putString("D_NO", "");
      editor.putString("S_NAME", "");
      editor.putString("unino", "");
      editor.putString("addr", "");
      editor.putString("ID_NO", "");
      editor.putString("API_URL", "");
      editor.putString("MQTT_URL", "");
      editor.putString("P_NO_LEN", "13");
      editor.putString("P_VIRTUAL_NO", "NFPLU999");
      editor.putString("Day_Refund", "5");
      editor.putString("Time_Clear_S", "040000");
      editor.putString("Time_Clear_E", "060000");
      editor.putString("Price_select", "1");
      editor.putString("EINV_SELECT", "");
      editor.putString("MPAY_TIMEOUT", "120");
      editor.putString("taxRate", "0.05");
      editor.putString("mchType", "0");
      editor.putBoolean("EINV_ON", true);
      editor.putBoolean("DETAIL_ON", false);
      editor.putBoolean("ActiveNidin", false);
      editor.putString("ORG_DOMAIN", "");
      editor.putString("INV_SAFE", "100");
      editor.putString("PRINT_QTY", "1");
      editor.commit();

      MapSetting = (HashMap<String, Object>) (PreferenceManager.getDefaultSharedPreferences(context)).getAll();

      // System.out.println("settings size():" + MapSetting.size());
      // for (HashMap.Entry<String, Object> entry : MapSetting.entrySet()) System.out.println("key:" + entry.getKey() + ", value:" + entry.getValue());
    }
  }

  public void refreshSetting(Context context) {
    SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
    MapSetting = (HashMap<String, Object>) settings.getAll();
  }

  public String getStrValue(String key) {
    try {
      if (MapSetting.containsKey(key)) {
        return (String) MapSetting.get(key);
      } else {
        return "";
      }
    } catch (Exception e) {
      e.printStackTrace();
      return "";
    }
  }

  public Double getDobValue(String key) {
    try {
      if (MapSetting.containsKey(key)) {
        return (Double) MapSetting.get(key);
      } else {
        return 0.0;
      }
    } catch (Exception e) {
      return 0.0;
    }
  }

  public int getIntValue(String key) {
    try {
      if (MapSetting.containsKey(key)) {
        return (int) MapSetting.get(key);
      } else {
        return 0;
      }
    } catch (Exception e) {
      e.printStackTrace();
      return 0;
    }
  }

  public boolean getBooleanValue(String key) {
    try {
      if (MapSetting.containsKey(key)) {
        Object value = MapSetting.get(key);
        if (value instanceof Boolean) {
          return (boolean) value;
        } else if (value instanceof String) {
          switch ((String) value) {
            case "Y":
              return true;
            case "N":
              return false;
            case "true":
              return true;
            case "false":
              return false;
          }
        }
        return (boolean) MapSetting.get(key);
      } else {
        return false;
      }
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public boolean setStrValue(String key, String value, Context context) {
    boolean result = false;

    // SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);

    SharedPreferences.Editor editor = (PreferenceManager.getDefaultSharedPreferences(context)).edit();

    if (MapSetting.containsKey(key)) {
      MapSetting.put(key, value);

      editor.remove(key);
      editor.putString(key, value);
      editor.commit();

      result = true;
    } else {
      MapSetting.put(key, value);
      editor.putString(key, value);
      editor.commit();
      result = true;
    }
    return result;
  }

  public boolean setBooleanValue(String key, boolean value, Context context) {
    boolean result = false;

    if (MapSetting.containsKey(key)) {
      MapSetting.put(key, value);

      SharedPreferences.Editor editor = (PreferenceManager.getDefaultSharedPreferences(context)).edit();
      editor.remove(key);
      editor.putBoolean(key, value);
      editor.commit();

      result = true;
    }
    return result;
  }
}
