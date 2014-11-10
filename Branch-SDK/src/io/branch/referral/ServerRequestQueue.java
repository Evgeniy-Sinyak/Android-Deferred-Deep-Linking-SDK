package io.branch.referral;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class ServerRequestQueue {
	private static final String PREF_KEY = "BNCServerRequestQueue";
	private static ServerRequestQueue SharedInstance;	
	private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
	private List<ServerRequest> queue;

    public static ServerRequestQueue getInstance(Context c) {
    	if(SharedInstance == null) {
    		synchronized(ServerRequestQueue.class) {
    			if(SharedInstance == null) {
    				SharedInstance = new ServerRequestQueue(c);
    			}
    		}
    	}
    	return SharedInstance;
    }
    
    private ServerRequestQueue (Context c) {
    	sharedPref = c.getSharedPreferences("BNC_Server_Request_Queue", Context.MODE_PRIVATE);
		editor = sharedPref.edit();
		queue = retrieve();
		if (PrefHelper.LOG) Log.i(getClass().getSimpleName(), "Retrieved from persist: " + toString());
    }
    
    private void persist() {
    	new Thread(new Runnable() {
			@Override
			public void run() {
				LinkedList<ServerRequest> copyQueue = new LinkedList<ServerRequest>(queue);
				
				JSONArray jsonArr = new JSONArray();
				Iterator<ServerRequest> iter = copyQueue.iterator();
				while (iter.hasNext()) {
					jsonArr.put(iter.next().toJSON());
				}
				
				editor.putString(PREF_KEY, jsonArr.toString()).commit();
			}
		}).start();
    }
    
    private List<ServerRequest> retrieve() {
    	List<ServerRequest> result = Collections.synchronizedList(new LinkedList<ServerRequest>());
    	String jsonStr = sharedPref.getString(PREF_KEY, null);
    	
    	if (jsonStr != null) {
    		try {
    			JSONArray jsonArr = new JSONArray(jsonStr);
    			for (int i = 0; i < jsonArr.length(); i++) {
    				JSONObject json = jsonArr.getJSONObject(i);
    				ServerRequest req = ServerRequest.fromJSON(json);
    				result.add(req);
    			}
    		} catch (JSONException e) {
    		}
    	}
    	
    	return result;
    }
    
	public int getSize() {
		return queue.size();
	}
	
	public void enqueue(ServerRequest request) {
		if (request != null) {
			queue.add(request);
			persist();
		}
	}
    
	public ServerRequest dequeue() {
		ServerRequest req = null;
		try {
			req = queue.remove(0);
			persist();
		} catch (NoSuchElementException ex) {
		}
		return req;
	}

	public ServerRequest peek() {
		ServerRequest req = null;
		try {
			req = queue.get(0);
		} catch (NoSuchElementException ex) {
		}
		return req;
	}
	
	public ServerRequest peekAt(int index) {
		ServerRequest req = null;
		try {
			req = queue.get(index);
		} catch (NoSuchElementException ex) {
		}
		return req;
	}
	
	public void insert(ServerRequest request, int index) {
		try {
			queue.add(index, request);
			persist();
		} catch (IndexOutOfBoundsException ex) {
		}
	}
	
	public ServerRequest removeAt(int index) {
		ServerRequest req = null;
		try {
			req = queue.remove(index);
			persist();
		} catch (IndexOutOfBoundsException ex) {
		}
		return req;
	}

	public boolean containsInstallOrOpen() {
		synchronized(queue) {
			Iterator<ServerRequest> iter = queue.iterator();
			while (iter.hasNext()) {
				ServerRequest req = iter.next();
				if (req.getTag().equals(BranchRemoteInterface.REQ_TAG_REGISTER_INSTALL) || req.getTag().equals(BranchRemoteInterface.REQ_TAG_REGISTER_OPEN)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public void moveInstallOrOpenToFront(String tag, int networkCount) {
		synchronized(queue) {
			Iterator<ServerRequest> iter = queue.iterator();
			while (iter.hasNext()) {
				ServerRequest req = iter.next();
				if (req.getTag().equals(BranchRemoteInterface.REQ_TAG_REGISTER_INSTALL) || req.getTag().equals(BranchRemoteInterface.REQ_TAG_REGISTER_OPEN)) {
					iter.remove();
					break;
				}
			}
		}
	    
	    ServerRequest req = new ServerRequest(tag);
	    if (networkCount == 0) {
	    	insert(req, 0);
	    } else {
	    	insert(req, 1);
	    }
	}
	
	public String toString() {
		JSONArray jsonArr = new JSONArray();
		synchronized(queue) {
			Iterator<ServerRequest> iter = queue.iterator();
			while (iter.hasNext()) {
				jsonArr.put(iter.next().toJSON());
			}
		}
		return jsonArr.toString();
	}
}
