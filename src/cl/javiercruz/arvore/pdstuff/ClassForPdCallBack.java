package cl.javiercruz.arvore.pdstuff;

public class ClassForPdCallBack {
	private PdListenerCallBack mainClass; //variavel cujo tipo é a interface PdListenerCallBack
    
    public ClassForPdCallBack (PdListenerCallBack mClass){
        mainClass = mClass; //inicializa  variavel.  Cria uma clase, neste caso de nome "mClass"
    }
    public void sendToCallBackMethod(String key, float val){
        mainClass.callWhenReceiveFloat(key, val);
    }
    public void sendToCallBackMethod(String key){
        mainClass.callWhenReceiveBang(key);
    }
    public void sendToCallBackMethod(String key, String symbol){
        mainClass.callWhenReceiveSymbol( key, symbol);
    }
    public void sendToCallBackMethod(String key, Object... args){
        mainClass.callWhenReceiveList( key, args);
    }
    public void sendToCallBackMethod(String key, String symbol, Object... args){
        mainClass.callWhenReceiveMessage( key, symbol, key);
    }
}
