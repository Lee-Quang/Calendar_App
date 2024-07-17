package com.example.btl_ltdt_2;

import android.content.Context;
import android.content.Intent;

public class SenderClass {
    public static void sendDataToReceiver(Context context, String Title, String ChiTiet)
    {
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("Title", Title);
        intent.putExtra("ChiTiet",ChiTiet); // Gửi dữ liệu thông qua Intent
        context.sendBroadcast(intent);
    }
}
