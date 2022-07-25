package com.emis.venus.util;

import com.emis.venus.common.emisKeeper;
import com.emis.venus.entity.Entity;
import com.emis.venus.util.log4j.LogKit;
import com.emis.venus.util.log4j.emisLogger;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class emisPropUtil {

  /**
   * 重新加载
   */
  public static void reload() {
  }

  /**
   * 将Entity转成HashMap（所有的资料）
   *
   * @return
   */
  public static HashMap getPropMapfromEntity() {
    HashMap emisPropMap = new HashMap();
    Entity emisProp = null;
    try {
      emisProp = new Entity("EMISPROP");
      try {
        emisProp.load(null);
      } catch (Exception e) {
        LogKit.error(e, e);
      }
      emisProp.first();
      do {
        emisPropMap.put(emisProp.getString("NAME"), emisProp.getString("VALUE"));
      } while (emisProp.next());
      emisProp.cleanData();
      emisProp = null;
    } catch (Exception e) {
    } finally {
      //SaleUtil.cleanEntity(emisProp);
    }
    return emisPropMap;
  }

  /**
   * 将Entity转成HashMap（指定类型VAR_KIND 的数据）
   *
   * @param sKind
   * @return
   */
  public static HashMap getPropMapfromEntity(String sKind) {
    HashMap emisPropMap = new HashMap();
    Entity emisProp = new Entity("EMISPROP");
    try {
      emisProp.loadBySql("select * from EMISPROP where VAR_KIND='" + sKind + "'");
    } catch (Exception e) {
      LogKit.error(e, e);
    }
    emisProp.first();
    do {
      emisPropMap.put(emisProp.getString("VAR_NAME"), emisProp.getString("VAR_VALUE"));
    } while (emisProp.next());
    emisProp.cleanData();
    emisProp = null;
    return emisPropMap;
  }

  /**
   * 将Entity转成HashMap（指定Entity的资料）
   *
   * @param emisProp
   * @return
   */
  public static HashMap getPropMapfromEntity(Entity emisProp) {
    HashMap emisPropMap = new HashMap();
    emisProp.first();
    String VAR_NAME = null;
    String VAR_KIND = null;
    do {
      VAR_NAME = emisProp.getString("VAR_NAME");
      VAR_KIND = emisProp.getString("VAR_KIND");
      emisPropMap.put(VAR_NAME, VAR_KIND);
    } while (emisProp.next());
    return emisPropMap;
  }

  /**
   * 将HashMap中的值更新到Entity中，并且返回Entity
   *
   * @param map
   * @return
   */
  public static Entity getPropEntityFromHashMap(HashMap map) {
    Entity emisProp = new Entity("EMISPROP");
    try {
      emisProp.load(null);
    } catch (Exception e) {
      LogKit.error(e, e);
    }
    emisProp.first();
    String VAR_NAME = null;
    Iterator it = null;
    Map.Entry entry = null;
    boolean isExist = false;// 是否存在HashMap中
    do {
      isExist = false;
      VAR_NAME = emisProp.getString("VAR_NAME");
      if (map != null && !map.isEmpty()) {
        it = map.entrySet().iterator();
        while (it.hasNext()) {
          entry = (Map.Entry) it.next();
          if (VAR_NAME.equals(entry.getKey())) {
            emisProp.setString("VAR_VALUE", emisUtil.parseString(entry.getValue()));
            map.remove(entry.getKey());
            isExist = true;
            break;
          }
        }
      }
      // 将不存在HashMap的数据清除，减少update Entity的笔数
      if (!isExist) {
        boolean isFirstClean = emisProp.getM_currRow() == 0;
        try {
          emisProp.cleanCurRow();
          //2014/07/24 viva modify 处理emisprop表第一个和第二个参数无法保存到的问题
          if (emisProp.isFirst() && isFirstClean) {
            emisProp.moveBeforeFirst();
          }
        } catch (Exception e) {
          LogKit.error(e);
        }
      }
    } while (emisProp.next());

    return emisProp;
  }

  /**
   * 获取EmisProp中的参数
   *
   * @param key
   * @return
   */
  public static Object getEmisProp(String key) {
    if (emisKeeper.getInstance().getEmisPropMap() != null) {
      return emisKeeper.getInstance().getEmisPropMap().get(key);
    }
    return null;
  }

  public static Object getEmisProp(String key, Object def) {
    Object ret = getEmisProp(key);
    return ret == null ? def : ret;
  }

  /**
   * 设置EmisProp中的参数
   *
   * @param key
   * @param obj
   */
  @SuppressWarnings("unchecked")
  public static void putEmisProp(String key, Object obj) {
    if (emisKeeper.getInstance().getEmisPropMap() != null) {
      emisKeeper.getInstance().getEmisPropMap().put(key, obj);
    }
  }

  /**
   * 将修改后的资料更新到emisKeeper中（含单据BillEmisKeeper）
   */
  public static void reloadEmisPropMap() {
    // 将修改后的资料更新到emisKeeper中
    HashMap emisPropMap = emisPropUtil.getPropMapfromEntity();
    emisKeeper.getInstance().setEmisPropMap(emisPropMap);
  }

  /**
   * @param toPath 文件移动到的目錄 目錄文件不存在則創建
   * @param file   要copy的文件
   * @return boolean 文件是否復制成功
   * @throws Exception
   */
  public static boolean moveFile(String toPath, File file) throws Exception {
    if (file == null)
      return false;
    File toDir = new File(toPath);

    if (!toDir.exists() || !toDir.isDirectory()) {
      toDir.mkdirs();
    }

    if (toDir.isDirectory()) {
      if (file.canRead()) {
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        BufferedWriter out = new BufferedWriter(new FileWriter(toPath + file.getName()));
        String aline;
        while ((aline = in.readLine()) != null) {
          out.write(aline.trim());
          out.flush();
        }
        in.close();
        out.close();
      } else {
        return false;
      }
    } else {
      return false;
    }
    return true;
  }
}
