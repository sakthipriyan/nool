package me.nool.android.entity;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum EntryStatus {
	COMPLETED(0), OVERDUE(1), UPCOMING(2), SKIPPED(3);

	private int status;

	private static final Map<Integer, EntryStatus> lookup = new HashMap<Integer, EntryStatus>();

	static {
		for (EntryStatus s : EnumSet.allOf(EntryStatus.class))
			lookup.put(s.getInt(), s);
	}

	EntryStatus(int status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return super.toString();
	}

	public int getInt() {
		return this.status;
	}

	public static EntryStatus getStatus(int status) {
		return lookup.get(status);
	}
}
