package me.nool.android.entity;

import android.text.format.DateUtils;

public class HomeEntry {
	private int id;
	private int threadId;
	private String threadName;
	private String iconURL;
	private long timestamp;
	private int contentId;
	private String contentText;
	private EntryStatus status;

	private HomeEntry(int id, int threadId, String threadName, String iconURL,
			int contentId, String contentText,long timestamp) {
		super();
		this.id = id;
		this.threadId = threadId;
		this.threadName = threadName;
		this.iconURL = iconURL;
		this.contentId = contentId;
		this.contentText = contentText;
		this.timestamp = timestamp;		
	}
	

	
	public HomeEntry(int id, int threadId, String threadName, String iconURL,
			int contentId, String contentText,long timestamp,
			int status) {
		this(id,threadId, threadName, iconURL, contentId, contentText,timestamp);
		this.status = EntryStatus.getStatus(status);
	}

	public HomeEntry(int id, int threadId, String threadName, String iconURL,
			int contentId, String contentText,long timestamp,
			EntryStatus status) {
		this(id, threadId, threadName, iconURL, contentId, contentText,timestamp);
		this.status = status;
	}


	
	public int getThreadId() {
		return threadId;
	}

	public void setThreadId(int threadId) {
		this.threadId = threadId;
	}

	public String getThreadName() {
		return threadName;
	}

	public void setThreadName(String threadName) {
		this.threadName = threadName;
	}

	public String getIconURL() {
		return iconURL;
	}

	public void setIconURL(String iconURL) {
		this.iconURL = iconURL;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	public String getTimestampAsString(){
		if(timestamp == 0){
			return "";
		}
		return (String) DateUtils.getRelativeTimeSpanString(timestamp, System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS);
	}

	public int getContentId() {
		return contentId;
	}

	public void setContentId(int contentId) {
		this.contentId = contentId;
	}

	public String getContentText() {
		return contentText;
	}

	public void setContentText(String contentText) {
		this.contentText = contentText;
	}

	public EntryStatus getStatus() {
		return status;
	}

	public void setStatus(EntryStatus status) {
		this.status = status;
	}

	public int getStatusAsInt() {
		return this.status.getInt();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	

}