package org.dgfoundation.amp.nireports.runtime;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import org.dgfoundation.amp.newreports.CalendarConverter;
import org.dgfoundation.amp.nireports.TranslatedDate;

/**
 * a wrapper for a {@link CalendarConverter} which caches the result of calling an underlying {@link CalendarConverter}.<br />
 * This is done because AMP's CalendarWorkers are very slow, while at the same time AMP's datasets usually contain multiple reusages of the same transaction date (DRC and Tanzania have a reuse ratio of around 30x)
 * @author Dolghier Constantin
 *
 */
public class CachingCalendarConverter implements CalendarConverter {

	protected final CalendarConverter inner;
	protected ConcurrentHashMap<Integer, TranslatedDate> cache = new ConcurrentHashMap<>();
	protected int calls;

	protected int nonCachedCalls;
	
	public CachingCalendarConverter(CalendarConverter inner) {
		this.inner = inner;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public TranslatedDate translate(Date date) {
		calls ++;
		int dayNumber = date.getYear() * 10000 + date.getMonth() * 100 + date.getDate(); // we like it safe: probably no month will ever have >100 days and no year will ever have > 100 months
		return cache.computeIfAbsent(dayNumber, z -> {
			nonCachedCalls ++;
			return inner.translate(date);
		});
	}

	@Override
	public boolean getIsFiscal() {
		return inner.getIsFiscal();
	}

	@Override
	public String getName() {
		return inner.getName();
	}

	@Override
	public Long getIdentifier() {
		return inner.getIdentifier();
	}

	public int getCalls() {
		return calls;
	}

	public int getNonCachedCalls() {
		return nonCachedCalls;
	}

	@Override
	public String toString() {
		return String.format("cached %s (named %s)", inner.toString(), inner.getName());
	}

}
