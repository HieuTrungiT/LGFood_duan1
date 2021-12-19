package com.example.lgfood_duan1.Notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.lgfood_duan1.Activity.donHangUser_Activity;
import com.example.lgfood_duan1.Activity.trangChu_SanPham_Activity;
import com.example.lgfood_duan1.Model.model_hoaDon;
import com.example.lgfood_duan1.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class mServiceKhoHang extends Service {
    public static final String NOTIFICATION_CHANNEL = "channel_notification_khoHang";
    private static String CONTENT = "Cảm ơn quý khách đã tin tưởng LG FARM <3";
    private static String CONTENT_DANG_XU_LI = "Đơn hàng của quý khách đang được xử lý";
    private static String CONTENT_DA_XU_LI = "Đơn hàng đang được vẩn chuyển";

    private DatabaseReference dataRef;
    private FirebaseDatabase database;
    ArrayList<model_hoaDon> modelHoaDonArrayList;
    SharedPreferences sharedPreferences;
    int tinhTrangHoaDonIt;
    public mServiceKhoHang(){

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        modelHoaDonArrayList=new ArrayList<>();
        sharedPreferences=getSharedPreferences("USER_FILE",MODE_PRIVATE);
        database = FirebaseDatabase.getInstance("https://duan-lgfood1-default-rtdb.asia-southeast1.firebasedatabase.app/");
        dataRef=database.getReference("HoaDon").child(sharedPreferences.getString("IDDANHSACHDONHANG",""));
        NotificationManagerCompat notificationManager1 = NotificationManagerCompat.from(mServiceKhoHang.this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "studentChannel";
            String description = "Channel for student notifications";
            int inportance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL, name, inportance);
            channel.setDescription(description);


            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }

        Intent intent1=new Intent(mServiceKhoHang.this, donHangUser_Activity.class);
                intent1.setFlags(intent1.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                PendingIntent pendingIntent1=PendingIntent.getActivity(mServiceKhoHang.this,0,intent,0);
                PendingIntent pendingIntent1 = PendingIntent.getActivity(mServiceKhoHang.this, 1, intent1, PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Builder builder1= new NotificationCompat.Builder(mServiceKhoHang.this, NOTIFICATION_CHANNEL)
                        .setSmallIcon(R.drawable.heart)
                        .setContentTitle("Thông báo")
                        .setContentText("Chào mừng đến với LG FARM")
                        .setContentIntent(pendingIntent1)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                notificationManager1.notify(100, builder1.build());
        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    model_hoaDon hoaDon=dataSnapshot.getValue(model_hoaDon.class);
                        modelHoaDonArrayList.add(hoaDon);
                }
                for (int i=0;i<modelHoaDonArrayList.size();i++){
                    tinhTrangHoaDonIt=Integer.valueOf(modelHoaDonArrayList.get(i).getTinhTrangDonHang());

                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        dataRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded( DataSnapshot snapshot,  String previousChildName) {
//                Intent intent1=new Intent(mServiceKhoHang.this, donHangUser_Activity.class);
//                intent1.setFlags(intent1.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
////                PendingIntent pendingIntent1=PendingIntent.getActivity(mServiceKhoHang.this,0,intent,0);
//                PendingIntent pendingIntent1 = PendingIntent.getActivity(mServiceKhoHang.this, 1, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
//
//                NotificationCompat.Builder builder1= new NotificationCompat.Builder(mServiceKhoHang.this, NOTIFICATION_CHANNEL)
//                        .setSmallIcon(R.drawable.heart)
//                        .setContentTitle("Thông báo")
//                        .setContentText(CONTENT)
//                        .setContentIntent(pendingIntent1)
//                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo))
//                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//
//                notificationManager1.notify(100, builder1.build());
            }

            @Override
            public void onChildChanged(DataSnapshot snapshot, String previousChildName) {
//                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    model_hoaDon hoaDon=snapshot.getValue(model_hoaDon.class);
                    modelHoaDonArrayList.add(hoaDon);
//                }
                for (int i=0;i<modelHoaDonArrayList.size();i++){
                    tinhTrangHoaDonIt=Integer.valueOf(modelHoaDonArrayList.get(i).getTinhTrangDonHang());

                }
                if (tinhTrangHoaDonIt==0){
                    Intent intent1=new Intent(mServiceKhoHang.this, donHangUser_Activity.class);
                    intent1.setFlags(intent1.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    PendingIntent pendingIntent1=PendingIntent.getActivity(mServiceKhoHang.this,0,intent,0);

                    NotificationCompat.Builder builder1= new NotificationCompat.Builder(mServiceKhoHang.this, NOTIFICATION_CHANNEL)
                            .setSmallIcon(R.drawable.heart)
                            .setContentTitle("Thông báo")
                            .setContentText(CONTENT)
                            .setContentIntent(pendingIntent1)
                            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo))
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                    notificationManager1.notify(100, builder1.build());
                }else if (tinhTrangHoaDonIt==1){
                    Intent intent1=new Intent(mServiceKhoHang.this, donHangUser_Activity.class);
                    intent1.setFlags(intent1.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    PendingIntent pendingIntent1=PendingIntent.getActivity(mServiceKhoHang.this,0,intent,0);

                    NotificationCompat.Builder builder1= new NotificationCompat.Builder(mServiceKhoHang.this, NOTIFICATION_CHANNEL)
                            .setSmallIcon(R.drawable.heart)
                            .setContentTitle("Thông báo")
                            .setContentText(CONTENT_DANG_XU_LI)
                            .setContentIntent(pendingIntent1)
                            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo))
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                    notificationManager1.notify(100, builder1.build());
                }else if (tinhTrangHoaDonIt==2){
                    Intent intent1=new Intent(mServiceKhoHang.this, donHangUser_Activity.class);
                    intent1.setFlags(intent1.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    PendingIntent pendingIntent1=PendingIntent.getActivity(mServiceKhoHang.this,0,intent,0);

                    NotificationCompat.Builder builder1= new NotificationCompat.Builder(mServiceKhoHang.this, NOTIFICATION_CHANNEL)
                            .setSmallIcon(R.drawable.heart)
                            .setContentTitle("Thông báo")
                            .setContentText(CONTENT_DA_XU_LI)
                            .setContentIntent(pendingIntent1)
                            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo))
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                    notificationManager1.notify(100, builder1.build());
                }

            }

            @Override
            public void onChildRemoved( DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved( DataSnapshot snapshot, String previousChildName) {

            }

            @Override
            public void onCancelled( DatabaseError error) {

            }
        });

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
