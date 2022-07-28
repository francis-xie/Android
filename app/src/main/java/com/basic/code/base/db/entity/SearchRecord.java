
package com.basic.code.base.db.entity;

import androidx.annotation.NonNull;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 搜索记录
 */
@DatabaseTable(tableName = "search_record")
public class SearchRecord {

    @DatabaseField(generatedId = true)
    private long Id;
    /**
     * 搜索内容
     */
    @DatabaseField
    private String content;

    /**
     * 搜索时间
     */
    @DatabaseField
    private long time;


    public long getId() {
        return Id;
    }

    public SearchRecord setId(long id) {
        Id = id;
        return this;
    }

    public String getContent() {
        return content;
    }

    public SearchRecord setContent(String content) {
        this.content = content;
        return this;
    }

    public long getTime() {
        return time;
    }

    public SearchRecord setTime(long time) {
        this.time = time;
        return this;
    }

    @NonNull
    @Override
    public String toString() {
        return "QueryRecord{" +
                "Id=" + Id +
                ", content='" + content + '\'' +
                ", time=" + time +
                '}';
    }
}
