package uk.org.sucu.tatupload;

import java.util.ArrayList;

import uk.org.sucu.tatupload.message.Text;
import android.app.Application;
import android.content.Context;

public class TatUploadApplication extends Application {
	
	private static boolean processingTexts = false;
	private static boolean confirmSplit = true;
	private static ArrayList<Text> messages = new ArrayList<Text>();
	private static String formName = "";
	private static final String uploadScript = "https://script.google.com/macros/s/AKfycbzfPd5U7tbyOmK8EERxB8LPn53CzLy_nzXzAu2jb2_fYC8V_aof/exec";
	
	public static boolean getProcessingTexts(){
		return processingTexts;
	}
	public static void setProcessingTexts(boolean b){
		processingTexts = b;
	}
	
	public static boolean getConfirmSplit(){
		return confirmSplit;
	}
	public static void setConfirmSplit(boolean b){
		confirmSplit = b;
	}
	
	public static ArrayList<Text> getMessageList(){
		return messages;
	}
	
	public static void setFormName(String newName){
		formName= newName;
	}
	public static String getFormName(){
		return formName;
	}
	
	public static String getScriptURL(){
		return uploadScript;
	}
	
}
