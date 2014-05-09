package com.example.template.multitouch;

public interface MTListenerCallBack {
	public void screenTouched(int id, float x, float y);
	public void screenTouchedReleased(int id);
	public void screenTouchedDragged(int id, float x, float y, float dist, float ang);
}
