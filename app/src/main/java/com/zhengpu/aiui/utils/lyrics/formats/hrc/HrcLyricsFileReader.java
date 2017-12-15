package com.zhengpu.aiui.utils.lyrics.formats.hrc;

import android.util.Base64;

import com.zhengpu.aiui.utils.lyrics.LyricsFileReader;
import com.zhengpu.aiui.utils.lyrics.model.LyricsInfo;
import com.zhengpu.aiui.utils.lyrics.model.LyricsLineInfo;
import com.zhengpu.aiui.utils.lyrics.model.LyricsTag;
import com.zhengpu.aiui.utils.lyrics.model.TranslateLrcLineInfo;
import com.zhengpu.aiui.utils.lyrics.model.TranslateLyricsInfo;
import com.zhengpu.aiui.utils.lyrics.model.TransliterationLyricsInfo;
import com.zhengpu.aiui.utils.lyrics.utils.CharUtils;
import com.zhengpu.aiui.utils.lyrics.utils.StringCompressUtils;
import com.zhengpu.aiui.utils.lyrics.utils.StringUtils;
import com.zhengpu.aiui.utils.lyrics.utils.TimeUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * hrc歌词解析器
 *
 * @author zhangliangming
 */
public class HrcLyricsFileReader extends LyricsFileReader {
    /**
     * 歌曲名 字符串
     */
    private final static String LEGAL_SONGNAME_PREFIX = "haplayer.songName";
    /**
     * 歌手名 字符串
     */
    private final static String LEGAL_SINGERNAME_PREFIX = "haplayer.singer";
    /**
     * 时间补偿值 字符串
     */
    private final static String LEGAL_OFFSET_PREFIX = "haplayer.offset";
    /**
     * 歌词Tag
     */
    private final static String LEGAL_TAG_PREFIX = "haplayer.tag";
    /**
     * 歌词 字符串
     */
    private final static String LEGAL_LYRICS_LINE_PREFIX = "haplayer.lrc";

    /**
     * 额外歌词
     */
    private final static String LEGAL_EXTRA_LYRICS_PREFIX = "haplayer.extra.lrc";

    public HrcLyricsFileReader() {

    }

    @Override
    public LyricsInfo readFile(File file) throws Exception {
        if (file != null) {
            return readInputStream(new FileInputStream(file));
        }
        return null;
    }

    @Override
    public LyricsInfo readLrcText(String base64FileContentString,
                                  File saveLrcFile) throws Exception {
        byte[] fileContent = Base64.decode(base64FileContentString, Base64.NO_WRAP);

        if (saveLrcFile != null) {
            // 生成歌词文件
            FileOutputStream os = new FileOutputStream(saveLrcFile);
            os.write(fileContent);
            os.close();
        }

        return readInputStream(new ByteArrayInputStream(fileContent));
    }

    @Override
    public LyricsInfo readLrcText(byte[] base64ByteArray, File saveLrcFile)
            throws Exception {
        if (saveLrcFile != null) {
            // 生成歌词文件
            FileOutputStream os = new FileOutputStream(saveLrcFile);
            os.write(base64ByteArray);
            os.close();
        }

        return readInputStream(new ByteArrayInputStream(base64ByteArray));
    }

    @Override
    public LyricsInfo readInputStream(InputStream in) throws Exception {
        LyricsInfo lyricsIfno = new LyricsInfo();
        lyricsIfno.setLyricsFileExt(getSupportFileExt());
        if (in != null) {
            // 获取歌词文件里面的所有内容，并对文本内容进行解压
            String lyricsTextStr = StringCompressUtils.decompress(in,
                    getDefaultCharset());
            // System.out.println(lyricsTextStr);
            String[] lyricsTexts = lyricsTextStr.split("\n");
            // 这里面key为该行歌词的开始时间，方便后面排序
            SortedMap<Integer, LyricsLineInfo> lyricsLineInfosTemp = new TreeMap<Integer, LyricsLineInfo>();
            Map<String, Object> lyricsTags = new HashMap<String, Object>();
            for (int i = 0; i < lyricsTexts.length; i++) {
                try {
                    // 解析歌词
                    parserLineInfos(lyricsIfno, lyricsLineInfosTemp,
                            lyricsTags, lyricsTexts[i]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            in.close();
            // 重新封装
            TreeMap<Integer, LyricsLineInfo> lyricsLineInfos = new TreeMap<Integer, LyricsLineInfo>();
            int index = 0;
            Iterator<Integer> it = lyricsLineInfosTemp.keySet().iterator();
            while (it.hasNext()) {
                lyricsLineInfos
                        .put(index++, lyricsLineInfosTemp.get(it.next()));
            }
            // 设置歌词的标签类
            lyricsIfno.setLyricsTags(lyricsTags);
            //
            lyricsIfno.setLyricsLineInfoTreeMap(lyricsLineInfos);
        }

        return lyricsIfno;
    }

    /**
     * 解析每行的歌词
     *
     * @param lyricsLineInfos
     * @param lyricsTags
     * @param lineInfo
     */
    private void parserLineInfos(LyricsInfo lyricsIfno,
                                 SortedMap<Integer, LyricsLineInfo> lyricsLineInfos,
                                 Map<String, Object> lyricsTags, String lineInfo) {
        if (lineInfo.startsWith(LEGAL_SONGNAME_PREFIX)) {
            String temp[] = lineInfo.split("\'")[1].split(":");

            lyricsTags
                    .put(LyricsTag.TAG_TITLE, temp.length == 1 ? "" : temp[1]);

        } else if (lineInfo.startsWith(LEGAL_SINGERNAME_PREFIX)) {
            String temp[] = lineInfo.split("\'")[1].split(":");

            lyricsTags.put(LyricsTag.TAG_ARTIST, temp.length == 1 ? ""
                    : temp[1]);

        } else if (lineInfo.startsWith(LEGAL_OFFSET_PREFIX)) {
            String temp[] = lineInfo.split("\'")[1].split(":");

            lyricsTags.put(LyricsTag.TAG_OFFSET, temp.length == 1 ? ""
                    : temp[1]);

        } else if (lineInfo.startsWith(LEGAL_TAG_PREFIX)) {
            // 自定义标签
            String temp[] = lineInfo.split("\'")[1].split(":");
            lyricsTags.put(temp[0], temp.length == 1 ? "" : temp[1]);

        } else if (lineInfo.startsWith(LEGAL_EXTRA_LYRICS_PREFIX)) {
            int left = LEGAL_EXTRA_LYRICS_PREFIX.length() + 1;
            int right = lineInfo.length();

            // 解析翻译歌词
            // 获取json base64字符串
            String translateJsonBase64String = lineInfo.substring(left + 1,
                    right - 3);
            if (!translateJsonBase64String.equals("")) {
                try {
                    //
                    String translateJsonString = new String(
                            Base64.decode(translateJsonBase64String, Base64.NO_WRAP));
                    parserOtherLrc(lyricsIfno, translateJsonString);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        } else if (lineInfo.startsWith(LEGAL_LYRICS_LINE_PREFIX)) {
            int left = LEGAL_LYRICS_LINE_PREFIX.length() + 1;
            int right = lineInfo.length();

            String[] lineComments = lineInfo.substring(left + 1, right - 3)
                    .split("'\\s*,\\s*'", -1);
            // 歌词
            String lineLyricsStr = lineComments[1];

            List<String> lineLyricsList = getLyricsWords(lineLyricsStr);

            // 歌词分隔
            String[] lyricsWords = lineLyricsList
                    .toArray(new String[lineLyricsList.size()]);

            // 获取当行歌词
            String lineLyrics = getLineLyrics(lineLyricsStr);

            // 时间标签
            String timeText = lineComments[0];
            int timeLeft = timeText.indexOf('<');
            int timeRight = timeText.length();
            timeText = timeText.substring(timeLeft + 1, timeRight - 1);
            String[] timeTexts = timeText.split("><");

            // 每个歌词的时间标签
            String wordsDisIntervalText = lineComments[2];
            String[] wordsDisIntervalTexts = wordsDisIntervalText.split(",");

            parserLineInfos(lyricsLineInfos, lyricsWords, lineLyrics,
                    timeTexts, wordsDisIntervalTexts);
        }

    }

    /**
     * 解析翻译和音译歌词
     *
     * @param lyricsIfno
     * @param translateJsonString
     */
    private void parserOtherLrc(LyricsInfo lyricsIfno,
                                String translateJsonString) throws Exception {

        try {

            JSONObject resultObj = new JSONObject(translateJsonString);
            JSONArray contentArrayObj = resultObj.getJSONArray("content");
            for (int i = 0; i < contentArrayObj.length(); i++) {
                JSONObject dataObj = contentArrayObj.getJSONObject(i);
                JSONArray lyricContentArrayObj = dataObj
                        .getJSONArray("lyricContent");
                int type = dataObj.getInt("lyricType");
                if (type == 1) {
                    // 解析翻译歌词
                    if (lyricsIfno.getTranslateLyricsInfo() == null)
                        parserTranslateLrc(lyricsIfno, lyricContentArrayObj);

                } else if (type == 0) {
                    // 解析音译歌词
                    if (lyricsIfno.getTransliterationLyricsInfo() == null)
                        parserTransliterationLrc(lyricsIfno,
                                lyricContentArrayObj);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析音译歌词
     *
     * @param lyricsIfno
     * @param lyricContentArrayObj
     */
    private void parserTransliterationLrc(LyricsInfo lyricsIfno,
                                          JSONArray lyricContentArrayObj) throws Exception {

        // 音译歌词集合
        TransliterationLyricsInfo transliterationLyricsInfo = new TransliterationLyricsInfo();
        List<LyricsLineInfo> transliterationLrcLineInfos = new ArrayList<LyricsLineInfo>();
        // 获取歌词内容
        for (int j = 0; j < lyricContentArrayObj.length(); j++) {
            JSONArray lrcDataArrayObj = lyricContentArrayObj.getJSONArray(j);
            // 音译行歌词
            LyricsLineInfo transliterationLrcLineInfo = new LyricsLineInfo();
            String[] lyricsWords = new String[lrcDataArrayObj.length()];
            String lineLyrics = "";
            for (int k = 0; k < lrcDataArrayObj.length(); k++) {
                if (k == lrcDataArrayObj.length() - 1) {
                    lyricsWords[k] = lrcDataArrayObj.getString(k).trim();
                    lineLyrics += lrcDataArrayObj.getString(k).trim();
                } else {
                    lyricsWords[k] = lrcDataArrayObj.getString(k).trim() + " ";
                    lineLyrics += lrcDataArrayObj.getString(k).trim() + " ";
                }
            }
            transliterationLrcLineInfo.setLineLyrics(lineLyrics);
            transliterationLrcLineInfo.setLyricsWords(lyricsWords);

            transliterationLrcLineInfos.add(transliterationLrcLineInfo);
        }
        // 添加音译歌词
        if (transliterationLrcLineInfos.size() > 0) {
            transliterationLyricsInfo
                    .setTransliterationLrcLineInfos(transliterationLrcLineInfos);
            lyricsIfno.setTransliterationLyricsInfo(transliterationLyricsInfo);
        }
    }

    /**
     * 解析翻译歌词
     *
     * @param lyricsIfno
     * @param lyricContentArrayObj
     */
    private void parserTranslateLrc(LyricsInfo lyricsIfno,
                                    JSONArray lyricContentArrayObj) throws Exception {

        // 翻译歌词集合
        TranslateLyricsInfo translateLyricsInfo = new TranslateLyricsInfo();
        List<TranslateLrcLineInfo> translateLrcLineInfos = new ArrayList<TranslateLrcLineInfo>();

        // 获取歌词内容
        for (int j = 0; j < lyricContentArrayObj.length(); j++) {
            JSONArray lrcDataArrayObj = lyricContentArrayObj.getJSONArray(j);
            String lrcComtext = lrcDataArrayObj.getString(0);

            // 翻译行歌词
            TranslateLrcLineInfo translateLrcLineInfo = new TranslateLrcLineInfo();
            translateLrcLineInfo.setLineLyrics(lrcComtext);

            translateLrcLineInfos.add(translateLrcLineInfo);
        }
        // 添加翻译歌词
        if (translateLrcLineInfos.size() > 0) {
            translateLyricsInfo.setTranslateLrcLineInfos(translateLrcLineInfos);
            lyricsIfno.setTranslateLyricsInfo(translateLyricsInfo);
        }
    }

    /**
     * 解析每行歌词的数据
     *
     * @param lyricsLineInfos
     * @param lyricsWords           歌词
     * @param lineLyrics            该行歌词
     * @param timeTexts             时间文本
     * @param wordsDisIntervalTexts
     */
    private void parserLineInfos(
            SortedMap<Integer, LyricsLineInfo> lyricsLineInfos,
            String[] lyricsWords, String lineLyrics, String[] timeTexts,
            String[] wordsDisIntervalTexts) {
        if (timeTexts.length == wordsDisIntervalTexts.length) {
            for (int i = 0; i < wordsDisIntervalTexts.length; i++) {

                LyricsLineInfo lyricsLineInfo = new LyricsLineInfo();

                // 每一行的开始时间和结束时间
                String timeTextStr = timeTexts[i];
                String[] timeTextCom = timeTextStr.split(",");

                String startTimeStr = timeTextCom[0];
                int startTime = TimeUtils.parseInteger(startTimeStr);

                String endTimeStr = timeTextCom[1];
                int endTime = TimeUtils.parseInteger(endTimeStr);

                lyricsLineInfo.setEndTime(endTime);
                lyricsLineInfo.setStartTime(startTime);

                //
                lyricsLineInfo.setLineLyrics(lineLyrics);
                lyricsLineInfo.setLyricsWords(lyricsWords);

                // 每一行歌词的每个时间
                String wordsDisIntervalStr = wordsDisIntervalTexts[i];
                List<String> wordsDisIntervalList = getWordsDisIntervalList(wordsDisIntervalStr);
                int wordsDisInterval[] = getWordsDisIntervalList(wordsDisIntervalList);
                lyricsLineInfo.setWordsDisInterval(wordsDisInterval);
                //
                lyricsLineInfos.put(startTime, lyricsLineInfo);
            }
        }
    }

    /**
     * 获取每个歌词的时间
     *
     * @param wordsDisIntervalList
     * @return
     */
    private int[] getWordsDisIntervalList(List<String> wordsDisIntervalList) {
        int wordsDisInterval[] = new int[wordsDisIntervalList.size()];
        for (int i = 0; i < wordsDisIntervalList.size(); i++) {
            String wordDisIntervalStr = wordsDisIntervalList.get(i);
            if (StringUtils.isNumeric(wordDisIntervalStr)) {
                wordsDisInterval[i] = Integer.parseInt(wordDisIntervalStr);
            }
        }
        return wordsDisInterval;
    }

    /**
     * 获取每个歌词的时间
     *
     * @param wordsDisIntervalString
     * @return
     */
    private List<String> getWordsDisIntervalList(String wordsDisIntervalString) {
        List<String> wordsDisIntervalList = new ArrayList<String>();
        String temp = "";
        for (int i = 0; i < wordsDisIntervalString.length(); i++) {
            char c = wordsDisIntervalString.charAt(i);
            switch (c) {
                case ':':
                    wordsDisIntervalList.add(temp);
                    temp = "";
                    break;
                default:
                    // 判断是否是数字
                    if (Character.isDigit(c)) {
                        temp += String.valueOf(wordsDisIntervalString.charAt(i));
                    }
                    break;
            }
        }
        if (!temp.equals("")) {
            wordsDisIntervalList.add(temp);
        }
        return wordsDisIntervalList;
    }

    /**
     * 分隔每个歌词
     *
     * @param lineLyricsStr
     * @return
     */
    private List<String> getLyricsWords(String lineLyricsStr) {
        List<String> lineLyricsList = new ArrayList<String>();
        String temp = "";
        boolean isEnter = false;
        for (int i = 0; i < lineLyricsStr.length(); i++) {
            char c = lineLyricsStr.charAt(i);
            if (CharUtils.isChinese(c) || CharUtils.isHangulSyllables(c)
                    || CharUtils.isHiragana(c)
                    || (!CharUtils.isWord(c) && c != '[' && c != ']')) {
                if (isEnter) {
                    temp += String.valueOf(lineLyricsStr.charAt(i));
                } else {
                    lineLyricsList.add(String.valueOf(lineLyricsStr.charAt(i)));
                }
            } else if (c == '[') {
                isEnter = true;
            } else if (c == ']') {
                isEnter = false;
                lineLyricsList.add(temp);
                temp = "";
            } else {
                temp += String.valueOf(lineLyricsStr.charAt(i));
            }
        }
        return lineLyricsList;
    }

    /**
     * 获取当前行歌词，去掉中括号
     *
     * @param lineLyricsStr
     * @return
     */
    private String getLineLyrics(String lineLyricsStr) {
        String temp = "";
        for (int i = 0; i < lineLyricsStr.length(); i++) {
            switch (lineLyricsStr.charAt(i)) {
                case '[':
                    break;
                case ']':
                    break;
                default:
                    temp += String.valueOf(lineLyricsStr.charAt(i));
                    break;
            }
        }
        return temp;
    }

    @Override
    public boolean isFileSupported(String ext) {
        return ext.equalsIgnoreCase("hrc");
    }

    @Override
    public String getSupportFileExt() {
        return "hrc";
    }
}
