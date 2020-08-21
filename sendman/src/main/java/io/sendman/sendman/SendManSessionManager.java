package io.sendman.sendman;

import java.util.Date;
import java.util.UUID;

import io.sendman.sendman.models.SendManSession;

public class SendManSessionManager {

	private static final String TAG = SendManSessionManager.class.getSimpleName();
	private static final long MAX_SESSION_LENGTH_MS = 10 * 60 * 1000;
	private static SendManSessionManager instance = null;

	public synchronized static SendManSessionManager getInstance() {
		if (instance == null) {
			instance = new SendManSessionManager();
		}
		return instance;
	}

	public SendManSession getOrCreateSession() {
		SendManDatabase storage = new SendManDatabase(SendMan.getInstance().getApplicationContext());
		SendManSession session = storage.getLastSession();
		if (session == null || new Date().getTime() - session.getEnd() > MAX_SESSION_LENGTH_MS) {
			session = createNewSession();
		}

		session.setEnd(new Date().getTime());
		storage.setLastSession(session);

		return session;
	}

	/** --- Private logic --- */

	private SendManSession createNewSession() {
		long now = new Date().getTime();
		return new SendManSession(UUID.randomUUID(), now, now);
	}
}
