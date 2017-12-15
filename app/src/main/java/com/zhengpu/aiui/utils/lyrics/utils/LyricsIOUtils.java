package com.zhengpu.aiui.utils.lyrics.utils;


import com.zhengpu.aiui.utils.lyrics.LyricsFileReader;
import com.zhengpu.aiui.utils.lyrics.LyricsFileWriter;
import com.zhengpu.aiui.utils.lyrics.formats.hrc.HrcLyricsFileReader;
import com.zhengpu.aiui.utils.lyrics.formats.hrc.HrcLyricsFileWriter;
import com.zhengpu.aiui.utils.lyrics.formats.hrcs.HrcsLyricsFileReader;
import com.zhengpu.aiui.utils.lyrics.formats.hrcs.HrcsLyricsFileWriter;
import com.zhengpu.aiui.utils.lyrics.formats.hrcx.HrcxLyricsFileReader;
import com.zhengpu.aiui.utils.lyrics.formats.hrcx.HrcxLyricsFileWriter;
import com.zhengpu.aiui.utils.lyrics.formats.krc.KrcLyricsFileReader;
import com.zhengpu.aiui.utils.lyrics.formats.krc.KrcLyricsFileWriter;
import com.zhengpu.aiui.utils.lyrics.formats.ksc.KscLyricsFileReader;
import com.zhengpu.aiui.utils.lyrics.formats.ksc.KscLyricsFileWriter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 歌词io操作
 * @author zhangliangming
 * 
 */
public class LyricsIOUtils {
	private static ArrayList<LyricsFileReader> readers;
	private static ArrayList<LyricsFileWriter> writers;

	static {
		readers = new ArrayList<LyricsFileReader>();
		readers.add(new HrcsLyricsFileReader());
		readers.add(new HrcxLyricsFileReader());
		readers.add(new HrcLyricsFileReader());
		readers.add(new KscLyricsFileReader());
		readers.add(new KrcLyricsFileReader());

		//
		writers = new ArrayList<LyricsFileWriter>();
		writers.add(new HrcsLyricsFileWriter());
		writers.add(new HrcxLyricsFileWriter());
		writers.add(new HrcLyricsFileWriter());
		writers.add(new KscLyricsFileWriter());
		writers.add(new KrcLyricsFileWriter());
	}

	/**
	 * 获取支持的歌词文件格式
	 * 
	 * @return
	 */
	public static List<String> getSupportLyricsExts() {
		List<String> lrcExts = new ArrayList<String>();
		for (LyricsFileReader lyricsFileReader : readers) {
			lrcExts.add(lyricsFileReader.getSupportFileExt());
		}
		return lrcExts;
	}

	/**
	 * 获取歌词文件读取器
	 * 
	 * @param file
	 * @return
	 */
	public static LyricsFileReader getLyricsFileReader(File file) {
		return getLyricsFileReader(file.getName());
	}

	/**
	 * 获取歌词文件读取器
	 * 
	 * @param fileName
	 * @return
	 */
	public static LyricsFileReader getLyricsFileReader(String fileName) {
		String ext = FileUtils.getFileExt(fileName);
		for (LyricsFileReader lyricsFileReader : readers) {
			if (lyricsFileReader.isFileSupported(ext)) {
				return lyricsFileReader;
			}
		}
		return null;
	}

	/**
	 * 获取歌词保存器
	 * 
	 * @param file
	 * @return
	 */
	public static LyricsFileWriter getLyricsFileWriter(File file) {
		return getLyricsFileWriter(file.getName());
	}

	/**
	 * 获取歌词保存器
	 * 
	 * @param fileName
	 * @return
	 */
	public static LyricsFileWriter getLyricsFileWriter(String fileName) {
		String ext = FileUtils.getFileExt(fileName);
		for (LyricsFileWriter lyricsFileWriter : writers) {
			if (lyricsFileWriter.isFileSupported(ext)) {
				return lyricsFileWriter;
			}
		}
		return null;
	}
}
