package com.zhengpu.aiui.utils.lyrics;


import com.zhengpu.aiui.utils.lyrics.model.LyricsInfo;

import java.nio.charset.Charset;

/**
 * 歌词文件生成器
 * 
 * @author zhangliangming
 * 
 */
public abstract class LyricsFileWriter {
	/**
	 * 默认编码
	 */
	protected Charset defaultCharset = Charset.forName("utf-8");

	/**
	 * 支持文件格式
	 * 
	 * @param ext
	 *            文件后缀名
	 * @return
	 */
	public abstract boolean isFileSupported(String ext);

	/**
	 * 获取支持的文件后缀名
	 * 
	 * @return
	 */
	public abstract String getSupportFileExt();

	/**
	 * 保存歌词文件
	 * 
	 * @param lyricsIfno
	 *            歌词数据
	 * @param lyricsFilePath
	 *            歌词文件路径
	 */
	public abstract boolean writer(LyricsInfo lyricsIfno, String lyricsFilePath)
			throws Exception;

	public void setDefaultCharset(Charset charset) {
		defaultCharset = charset;
	}

	public Charset getDefaultCharset() {
		return defaultCharset;
	}
}
