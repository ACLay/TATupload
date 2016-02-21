package uk.org.sucu.tatupload2;

import uk.org.sucu.tatupload2.activity.MainActivity;
import uk.org.sucu.tatupload2.message.SmsList;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;

public class Notifications {

	public static final int notificationId = 1;
	
	public static void updateNotification(Context context){
		
		Settings settings = new Settings(context);
		
		boolean showingNotification = settings.getShowingNotification();
		
		if(!showingNotification){
			hideNotification(context);
			return;
		}
		
		boolean processingTexts = settings.getProcessingTexts();
		int queueSize = SmsList.getPendingList().getSize();

		if((!processingTexts) && queueSize == 0){
			hideNotification(context);
			return;
		}
		
		Notification n = buildNotification(context, processingTexts, queueSize);
		
		showNotification(context, n);
	}
	
	private static Notification buildNotification(Context context, boolean processingTexts, int queueSize){
		
		
		String title = context.getString(R.string.app_name);
		
		String text;
		if(processingTexts){
			text = context.getString(R.string.processing_texts);
		} else if(queueSize == 0){
			text = context.getString(R.string.no_queued_messages);
		} else if (queueSize == 1){
			text = context.getString(R.string.one_queued_message);
		} else {
			text = Integer.toString(queueSize) + context.getString(R.string._queued_messages);
		}
		
		int iconId = getResIdFromAttribute(context, R.attr.notification_icon);
		
		NotificationCompat.Builder builder =
				new NotificationCompat.Builder(context)
		.setSmallIcon(iconId)
		.setContentTitle(title)
		.setAutoCancel(false)
		.setOngoing(processingTexts)
		.setContentText(text)
		.setColor(Color.YELLOW);
		
		if(processingTexts){
			builder.setNumber(queueSize);
		}
		
		Intent resultIntent = new Intent(context,MainActivity.class);
		//prevent a new instance of the application being opened
		resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent resultPendingIntent =
				PendingIntent.getActivity(
						context,
						0,
						resultIntent,
						PendingIntent.FLAG_UPDATE_CURRENT);
		
		builder.setContentIntent(resultPendingIntent);
		
		return builder.build();
	}
	
	public static void displayNotification(Context context){
		boolean processingTexts = new Settings(context).getProcessingTexts();
		int queueSize = SmsList.getPendingList().getSize();
		
		Notification notification = buildNotification(context, processingTexts, queueSize);
		showNotification(context, notification);
	}
	
	private static void showNotification(Context context, Notification notification){
		NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		manager.notify(notificationId, notification);
	}
	
	public static void hideNotification(Context context){
		NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		manager.cancel(notificationId);
	}

	public static int getResIdFromAttribute(Context activity, int attr){
		TypedArray a = activity.getTheme().obtainStyledAttributes(new int[]{attr});
		int drawableId = a.getResourceId(0, 0);
		a.recycle();
		return drawableId;
	}
	
}
