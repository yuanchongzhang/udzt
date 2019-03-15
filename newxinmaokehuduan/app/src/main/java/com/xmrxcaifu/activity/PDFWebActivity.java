package com.xmrxcaifu.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


import com.baidu.mobstat.StatService;
import com.github.barteksc.pdfviewer.PDFView;
import com.tencent.smtt.sdk.TbsReaderView;
import com.xmrxcaifu.R;
import com.xmrxcaifu.http.MyOkhttp;
import com.xmrxcaifu.http.callback.FileCallback;
import com.xmrxcaifu.http.request.BaseRequest;
import com.xmrxcaifu.pdffileservice.SuperFileView2;
import com.xmrxcaifu.statusbar.ImmersionBar;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

public class PDFWebActivity extends BaseActivty2 implements TbsReaderView.ReaderCallback {

    private static final String TAG = "PDFActivity";
    String pdfName;
    //    PDFView pdfView;
    @BindView(R.id.text_title)
    TextView textTitle;
    @BindView(R.id.title_view_new)
    View titleViewNew;
    @BindView(R.id.text_cancel)
    TextView textCancel;
    @BindView(R.id.text_name)
    TextView mTextName;
    @BindView(R.id.pdfView)
    PDFView mPdfView;
    private InputStream mIs;
    String filePath;


    private String getFilePath() {
        return filePath;
    }

    public void setFilePath(String fileUrl) {
        this.filePath = fileUrl;
    }

//    SuperFileView2 mSuperFileView;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf7);
        ButterKnife.bind(this);
        ImmersionBar.with(PDFWebActivity.this)
                .statusBarDarkFont(true, 0.2f)
                .init();
//        display();
//        showLoading();
//        pdfView = (PDFView) findViewById(R.id.pdfView);
        mTbsReaderView = new TbsReaderView(this, this);
//        mSuperFileView = (SuperFileView2) findViewById(R.id.mSuperFileView);
        getTitle(getIntent().getStringExtra("title"));
        String mobile = getIntent().getStringExtra("teleNum");
        String maskNumber = mobile.substring(0, 3) + "****" + mobile.substring(7, mobile.length());
        String name;
        name = null == getIntent().getStringExtra("name") ? "\n" : "\n" + getIntent().getStringExtra("name") + "\n";
        mTextName.setText(name + maskNumber + "\n");
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("PDF", "PDF");
        // 设置控件附加属性，用于无埋点对控件添加数据；控件绑定事件附加属性依赖于view.setTag(key, value)支持，使用key值：-96000；
        // 如果view已经绑定过此key值，则此设置不生效
        StatService.setAttributes(mTbsReaderView, hashMap);
//        https://xm-file.oss-cn-beijing.aliyuncs.com/file/iJDBXP556Z.pdf
        MyOkhttp.get(getIntent().getStringExtra("url"))
                .tag(this)
                .execute(new FileCallback(Environment.getExternalStorageDirectory() +
                        "/temp", "qcl.pdf") {
                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                    }

                    @Override
                    public void onSuccess(File file, Call call, Response response) {
                    }

                    @Override
                    public void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                        Log.d(progress + "", "333333333333333333");
                        super.downloadProgress(currentSize, totalSize, progress, networkSpeed);
                        pdfName = Environment.getExternalStorageDirectory() +
                                "/temp/qcl.pdf";
                        if (progress == 1.0) {
                            String path = pdfName;
                            setFilePath(path);
                            Log.d("3433434", pdfName);
                            mydismissloading();


                            //下载网络PDF到本地，在进行本地加载
                            mPdfView.fromFile(new File(getFilePath()))
                                    .enableSwipe(false) // allows to block changing pages using swipe
                                    .swipeHorizontal(false)
                                    .enableDoubletap(false)
                                    .defaultPage(0)
                                    .enableAnnotationRendering(true) // render annotations (such as comments, colors or forms)
                                    .password(null)
                                    .scrollHandle(null)
                                    .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                                    .load();

//                            mSuperFileView.displayFile(new File(getFilePath()));
//                            mSuperFileView.onStopDisplay();
                        } else {
//                            mLoadingDialog2.show();
                            myshowloading();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);

                    }
                });
/*
        mSuperFileView.setOnGetFilePathListener(new SuperFileView2.OnGetFilePathListener() {
            @Override
            public void onGetFilePath(SuperFileView2 mSuperFileView2) {
                getFilePathAndShowFile(mSuperFileView2);
            }
        });*/


    }

    @NonNull
    private void getTitle(String str) {
        if (str.endsWith(".pdf")) {
            getTitle(str.substring(0, str.length() - 4));
        } else {
            textTitle.setText(str);
            return;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
      /*  try {
            mIs.close();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
//        mSuperFileView.onStopDisplay();
//        mSuperFileView = null;

    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                //打开PDF


                /*  pdfView.fromStream(mIs)
                        .defaultPage(pageNumber)
                        .onPageChange(PDFActivity.this)
                        .enableAnnotationRendering(true)
                        .onLoad(PDFActivity.this)
                        .scrollHandle(new DefaultScrollHandle(PDFActivity.this))
                        .spacing(10) // in dp
                        .onPageError(PDFActivity.this)
                        .load();*/
            }
        }
    };

    private TbsReaderView mTbsReaderView;

    private void display() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
//                    URL url = new URL("https://xm-file.oss-cn-beijing.aliyuncs.com/file/iJDBXP556Z.pdf");
                    URL url = new URL(getIntent().getStringExtra("url"));
                    HttpURLConnection connection = (HttpURLConnection)
                            url.openConnection();
                    connection.setRequestMethod("GET");//试过POST 可能报错
                    connection.setDoInput(true);
                    connection.setConnectTimeout(10000);
                    connection.setReadTimeout(10000);
                    //实现连接
                    connection.connect();
                    Log.i(TAG, "connection.getResponseCode()=" + connection.getResponseCode());

                    if (connection.getResponseCode() == 200) {
                        mIs = connection.getInputStream();
                        //这里给过去就行了
                    }
                    Log.i(TAG, "run: 加载完了");
//                    mIs.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Message message = mHandler.obtainMessage();
                message.what = 1;
                mHandler.sendMessage(message);

            }
        }).start();


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @OnClick({R.id.text_title, R.id.title_view_new, R.id.text_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.text_title:
                break;
            case R.id.title_view_new:
                break;
            case R.id.text_cancel:
                finish();
//                mSuperFileView.onStopDisplay();
                break;
        }
    }


    private void displayFile() {
        Bundle bundle = new Bundle();
        bundle.putString("filePath", getLocalFile().getPath());
        bundle.putString("tempPath", Environment.getExternalStorageDirectory().getPath());
//        boolean result = mTbsReaderView.preOpen(parseFormat(pdfName), false);
//        if (result) {
//
//        }
        mTbsReaderView.openFile(bundle);
    }

    private File getLocalFile() {
        return new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), pdfName);
    }

    private String parseFormat(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    @Override
    public void onCallBackAction(Integer integer, Object o, Object o1) {

    }
}
