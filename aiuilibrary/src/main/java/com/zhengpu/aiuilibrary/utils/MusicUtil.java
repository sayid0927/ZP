package com.zhengpu.aiuilibrary.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.provider.MediaStore.Audio.Media;

import com.orhanobut.logger.Logger;
import com.zhengpu.aiuilibrary.iflytekbean.MusicBean;

import java.util.ArrayList;
import java.util.List;


public class MusicUtil {

    private static ContentResolver contentResolver;

    /**
     * 获取SD卡所有的音乐
     *
     * @param context
     * @return
     */
    public static List<MusicBean> getMusic(Context context) {
        List<MusicBean> olist = null;
        int index = 0;
        contentResolver = context.getContentResolver();
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            olist = new ArrayList<>();
            Cursor cursor = contentResolver.query(Media.EXTERNAL_CONTENT_URI, null, null, null, Media.DEFAULT_SORT_ORDER);
            while (cursor.moveToNext()) {

                String name = cursor.getString(cursor.getColumnIndex(Media.TITLE));
                String author = cursor.getString(cursor.getColumnIndex(Media.ARTIST));
                String path = cursor.getString(cursor.getColumnIndex(Media.DATA));
                MusicBean music = new MusicBean(index++, name, author, path);
                olist.add(music);

            }
        }
        return olist;
    }

    /**
     * 获取SD卡获取到的第一首歌曲
     *
     * @param context
     * @return
     */
    public static MusicBean getFirstMusic(Context context) {
        MusicBean music = new MusicBean();
        contentResolver = context.getContentResolver();
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Cursor cursor = contentResolver.query(Media.EXTERNAL_CONTENT_URI, null, null, null, Media.DEFAULT_SORT_ORDER);
            Logger.e("getFirstMusic" + cursor);
            if (cursor.moveToFirst()) {
                String name = cursor.getString(cursor.getColumnIndex(Media.TITLE));
                String author = cursor.getString(cursor.getColumnIndex(Media.ARTIST));
                String path = cursor.getString(cursor.getColumnIndex(Media.DATA));
                music.setId(0);
                music.setName(name);
                music.setAuthor(author);
                music.setPath(path);
            }
        }
        Logger.e("getFirstMusic" + music.toString());
        return music;
    }
}
