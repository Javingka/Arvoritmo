package cl.javiercruz.arvore.multitouch;

public class ClassForMTCallBack {
	private MTListenerCallBack MTCBclass;
	
	ClassForMTCallBack(MTListenerCallBack mClass) {
		MTCBclass = mClass;
	}
	
	public void sendToCallBackMethod (int id, float x, float y) {
		MTCBclass.screenTouched(id, x, y);
	}
	public void sendToCallBackMethod (int id, float x, float y, float d, float ang) { 
		MTCBclass.screenTouchedDragged(id, x, y, d, ang);
	}
	public void sendToCallBackMethod (int id) {
		MTCBclass.screenTouchedReleased(id);
	}
	public void sendToCallBackMethod (float distBellisco) {
		MTCBclass.screenTouchedPinch(distBellisco);
	}
	
	
}
