package com.example.qhsj.okhttpdownloadapk;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import com.example.qhsj.okhttpdownloadapk.pojo.AppVersion;
import com.example.qhsj.okhttpdownloadapk.service.DownloadingService;
import com.example.qhsj.okhttpdownloadapk.utils.MyUtil;

public class MainActivity extends AppCompatActivity {

    private AppVersion checkResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkUpdate();
    }

    /*
     * 调用服务器的接口，检查是否需要有更新
     */
    private void checkUpdate() {
        // 接口这里就不调用了

        // 直接需要更新,模拟服务器返回的数据
        checkResult = new AppVersion();

        // 检查是否是 wifi 环境
        if (MyUtil.isWifiConnect(MainActivity.this)) {
            // 直接下载
            showDownloadDialog(checkResult);
        } else {
            // 提示不是wifi，要不要下载
            showConfirmDialog();
        }


    }

    /*
     * 下载 apk 的弹窗
     */
    private void showDownloadDialog(final AppVersion checkResult) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("update apk");
        builder.setMessage(checkResult.updateType);
        builder.setCancelable(false);
        builder.setPositiveButton("下载安装", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startDownloadService(checkResult);
            }
        });
        builder.setNegativeButton("取消", null);
        builder.create().show();
    }

    /*
     * 非WiFi网络环境 的弹窗
     */
    private void showConfirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("警告");
        builder.setMessage("当前非WiFi网络环境，下载更新将会消耗流量，确定更新吗？");
        builder.setCancelable(false);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showDownloadDialog(checkResult);
            }
        });
        builder.setNegativeButton("取消", null);
        builder.create().show();
    }

    private void startDownloadService(AppVersion result) {
        Intent intent = new Intent(MainActivity.this, DownloadingService.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("APP_VERSION", result);
//        bundle.putSerializable(UpdateConfig.TAG, updateConfig);
        intent.putExtras(bundle);
        MainActivity.this.startService(intent);
    }
}
