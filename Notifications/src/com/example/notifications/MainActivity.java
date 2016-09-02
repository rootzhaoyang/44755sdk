package com.example.notifications;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable.Callback;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
@ContentView(R.layout.down)
public class MainActivity extends Activity {
	  @ViewInject(R.id.button)
	    Button button;
	    @ViewInject(R.id.progressBar)
	    ProgressBar progressBar;
	    private String Path = Environment.getExternalStorageDirectory().getAbsolutePath() + "demo.apk";
	    private String url = "http://p.gdown.baidu.com/0ba9a401866d826527e632bd6efd501f937265c7150bae91dc11c89f878b90737e2b3f79054a3f8e587bcc631d146994392c78c1a31ac3cf98f35fac657a6fc12b8a7b424d5e2501055fc4483bfbdfb9e5014c1a65cb0c4ce21c898b267f43146ca6d5f9367fdec80066e82056cd897d5f7d4375161a1f26b483a68327577d28aa6b01d2b83ed8474cd446fd258e6eabc5a5279ec65218343dedebd1249c4711515b1102638113b8a4402ba3c03a729452651f73c5d0e6341b4e58e84b55185ac244b4d7d19410f7f32ea31a5cc6d31721aef32f6aaaf26b685165b32ebc531333520c6c2a16ced7e2632328669136552245c0cf16c33e94477917ff1b4715b9f98e7464c3568c98ded1a85553f43ef487e958b99b0a4d19";

	    private ProgressDialog pDialog;
	    private String nowVersion;
	    private ProgressDialog progressDialog;

	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);

	        NotificationUtil(this);

	        button.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View arg0) {
	                showNotification(1);
	              setDownLoad(url);
	            }
	        });
	    }


	    /**
	     * 下载包
	     *
	     * @param downloadurl 下载的url
	     */
	    @SuppressLint("SdCardPath")
	    protected void setDownLoad(String downloadurl) {
	        RequestParams params = new RequestParams(downloadurl);
	        params.setAutoRename(true);//断点下载
	        params.setSaveFilePath("/mnt/sdcard/demo.apk");
	        x.http().get(params, new Callback.ProgressCallback<File>() {
	            @Override
	            public void onCancelled(CancelledException arg0) {
	            }

	            @Override
	            public void onError(Throwable arg0, boolean arg1) {
	                if (progressDialog != null && progressDialog.isShowing()) {
	                    progressDialog.dismiss();
	                }
	                System.out.println("提示更新失败");
	            }

	            @Override
	            public void onFinished() {
	            }

	            @Override
	            public void onSuccess(File arg0) {
	                if (progressDialog != null && progressDialog.isShowing()) {
	                    progressDialog.dismiss();
	                }
	                Intent intent = new Intent(Intent.ACTION_VIEW);
	                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	                intent.setDataAndType(Uri.fromFile(new File(Environment
	                                .getExternalStorageDirectory(), "demo.apk")),
	                        "application/vnd.android.package-archive");
	                startActivity(intent);
	            }

	            @Override
	            public void onLoading(long arg0, long arg1, boolean arg2) {
	                progressDialog.setMax((int) arg0);
	                progressDialog.setProgress((int) arg1);
	                double pro=100.00*arg1/arg0;
	                System.out.println(arg0+"-----"+arg1+"------------"+arg2+"-----------"+pro);
	                updateProgress(1,pro);
	            }

	            @Override
	            public void onStarted() {
	                System.out.println("开始下载");
	                progressDialog = new ProgressDialog(DownloadActivity.this);
	                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);//设置为水平进行条
	                progressDialog.setMessage("正在下载中...");
	                progressDialog.setProgress(0);
	                progressDialog.show();
	            }
	            @Override
	            public void onWaiting() {
	            }
	        });
	    }




	    private Context mContext;
	    // NotificationManager ： 是状态栏通知的管理类，负责发通知、清楚通知等。
	    private NotificationManager manager;
	    // 定义Map来保存Notification对象
	    private Map<Integer, Notification> map = null;

	    public void NotificationUtil(Context context) {
	        this.mContext = context;
	        // NotificationManager 是一个系统Service，必须通过 getSystemService()方法来获取。
	        manager = (NotificationManager) mContext
	                .getSystemService(Context.NOTIFICATION_SERVICE);
	        map = new HashMap<Integer, Notification>();
	    }

	    public void showNotification(int notificationId) {
	        // 判断对应id的Notification是否已经显示， 以免同一个Notification出现多次
	        if (!map.containsKey(notificationId)) {
	            // 创建通知对象
	            Notification notification = new Notification();
	            // 设置通知栏滚动显示文字
	            notification.tickerText = "开始下载xx文件";
	            // 设置显示时间
	            notification.when = System.currentTimeMillis();
	            // 设置通知显示的图标
	            notification.icon = R.drawable.ic_launcher;
	            // 设置通知的特性: 通知被点击后，自动消失
	            notification.flags = Notification.FLAG_AUTO_CANCEL;
	            // 设置点击通知栏操作
	            Intent in = new Intent(mContext, MainActivity.class);// 点击跳转到指定页面
	            PendingIntent pIntent = PendingIntent.getActivity(mContext, 0, in,0);
	            notification.contentIntent = pIntent;
	            // 设置通知的显示视图
	            RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.notification_apk_updata);
	            // 设置暂停按钮的点击事件
	            Intent pause = new Intent(mContext, MainActivity.class);// 设置跳转到对应界面
	            PendingIntent pauseIn = PendingIntent.getActivity(mContext, 0, in,
	                    0);
	            // 这里可以通过Bundle等传递参数
	            remoteViews.setOnClickPendingIntent(R.id.button, pauseIn);
	            /********** 简单分隔 **************************/
	            // 设置取消按钮的点击事件
	            Intent stop = new Intent(mContext, MainActivity.class);// 设置跳转到对应界面
	            PendingIntent stopIn = PendingIntent
	                    .getActivity(mContext, 0, in, 0);
	            // 这里可以通过Bundle等传递参数
	            remoteViews.setOnClickPendingIntent(R.id.cancel, stopIn);
	            // 设置通知的显示视图
	            notification.contentView = remoteViews;
	            // 发出通知
	            manager.notify(notificationId, notification);
	            map.put(notificationId, notification);// 存入Map中
	        }
	    }

	    /**
	     * 取消通知操作
	     *
	     * @description：
	     * @author ldm
	     * @date 2016-5-3 上午9:32:47
	     */
	    public void cancel(int notificationId) {
	        manager.cancel(notificationId);
	        map.remove(notificationId);
	    }

	    public void updateProgress(int notificationId, double progress) {
	        Notification notify = map.get(notificationId);
	        if (null != notify) {
	            // 修改进度条
	            notify.contentView.setProgressBar(R.id.content_view_progress, 100, (int)progress, false);
	            notify.contentView.setTextViewText(R.id.content_view_text1,progress+"%");
	            manager.notify(notificationId, notify);
	        }
	    }

	}
