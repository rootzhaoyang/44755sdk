package com.example.progressbar;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 任务栏带进度条的下载
 * @author wanwan
 *
 */
public class MainActivity extends Activity implements OnClickListener {

	public static MainActivity instance = null;

	private static final int NOTIFICATION_ID = 0x12;
	private Notification notification = null;
	private NotificationManager manager = null;
	private int _progress = 0;
	private boolean isStop = false;
    private ProgressBar progressBar=null;
    private TextView tv2=null;
	@Override
	protected void onPause() {
		isStop = false;
		manager.cancel(NOTIFICATION_ID);

		super.onPause();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		instance = this;
		setContentView(R.layout.activity_main);
     progressBar=(ProgressBar)this.findViewById(R.id.progressBar0);
     tv2=(TextView)this.findViewById(R.id.textView2);

		Button btn = (Button) findViewById(R.id.button1);
		btn.setOnClickListener(this);
		notification = new Notification(R.drawable.ic_launcher, "带进条的提醒",
				System.currentTimeMillis());

		notification.contentView = new RemoteViews(getApplication()
				.getPackageName(), R.layout.notification);
		notification.contentView.setProgressBar(R.id.progressBar1, 100, 0,
				false);
		notification.contentView.setTextViewText(R.id.textView1, "进度"
				+ _progress + "%");

		notification.contentIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, MainActivity.class), 0);

		manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

	}

	public Handler handler = null;
	String url = "http://p.gdown.baidu.com/0ba9a401866d826527e632bd6efd501f937265c7150bae91dc11c89f878b90737e2b3f79054a3f8e587bcc631d146994392c78c1a31ac3cf98f35fac657a6fc12b8a7b424d5e2501055fc4483bfbdfb9e5014c1a65cb0c4ce21c898b267f43146ca6d5f9367fdec80066e82056cd897d5f7d4375161a1f26b483a68327577d28aa6b01d2b83ed8474cd446fd258e6eabc5a5279ec65218343dedebd1249c4711515b1102638113b8a4402ba3c03a729452651f73c5d0e6341b4e58e84b55185ac244b4d7d19410f7f32ea31a5cc6d31721aef32f6aaaf26b685165b32ebc531333520c6c2a16ced7e2632328669136552245c0cf16c33e94477917ff1b4715b9f98e7464c3568c98ded1a85553f43ef487e958b99b0a4d19";
//String url = "http://wdl.cache.ijinshan.com/wps/download/WPS.4047.19.552.exe";

	@Override
	public void onClick(View v) {
		Log.e("点击事件", "点击事件");
		isStop = true;
		manager.notify(NOTIFICATION_ID, notification);
		new Thread(new Runnable() {

			@Override
			public void run() {
				InputStream inputStream = new DownloadHelper()
						.getInputStreamFromUrl(url);
				FileUtils f = new FileUtils();
				try {
					f.createSDFile("myFile.exe");
				} catch (IOException e) {
					e.printStackTrace();
				}
				f.write2SDCARDFromInputSteam("", "myFile.exe", inputStream);

			}
		}).start();

	}

	{
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {

				Log.e("handle", "收到信息");
				notification.contentView.setProgressBar(R.id.progressBar1, 100,
						msg.arg1, false);
				notification.contentView.setTextViewText(R.id.textView1, "进度"
						+ msg.arg1 + "%");
				manager.notify(NOTIFICATION_ID, notification);
				
				progressBar.setProgress(msg.arg1);
				tv2.setText(msg.arg1+"%");

				if (msg.arg1 == 100) {
					Toast.makeText(MainActivity.this, "下载完毕", 1000).show();
				}
			}
		};
	}
}
