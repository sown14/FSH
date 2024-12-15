package com.example.shoe;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FCMService extends FirebaseMessagingService {


    static String token;
    public static void getDeviceToken (){
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if(task.isSuccessful()){
                    token = task.getResult();
                    Log.d("FCM_TOKEN", token);
                }
            }
        });
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Kiểm tra nếu thông báo chứa dữ liệu
        if (remoteMessage.getData().size() > 0) {
            String message = remoteMessage.getData().get("message");

            // Hiển thị thông báo
            sendNotification(message);
        }

        // Kiểm tra nếu có notification payload (nếu gửi từ FCM Console)
        if (remoteMessage.getNotification() != null) {
            String messageBody = remoteMessage.getNotification().getBody();


            // Hiển thị thông báo
            sendNotification(messageBody);
        }
    }

    private void sendNotification(String messageBody) {
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Tạo kênh thông báo (chỉ cần làm 1 lần với Android 8.0+)
        String channelId = "default_channel";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Thông báo",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationManager.createNotificationChannel(channel);
        }

        // Tạo thông báo
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_notifications_black_24dp) // Biểu tượng thông báo
                        .setContentTitle("Thông báo mới") // Tiêu đề
                        .setContentText(messageBody) // Nội dung thông báo
                        .setPriority(NotificationCompat.PRIORITY_HIGH) // Độ ưu tiên cao
                        .setAutoCancel(true); // Tự động hủy khi nhấn

        // Hiển thị thông báo
        notificationManager.notify((int) System.currentTimeMillis(), notificationBuilder.build());
    }

}
