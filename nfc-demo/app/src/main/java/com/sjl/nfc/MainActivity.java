package com.sjl.nfc;


import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.text.TextUtils;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends BaseActivity {

    private TextView mMsg;

    private NfcAdapter mNfcAdapter;
    private PendingIntent pi;
    @Override
    protected int getLayoutId() {
        return R.layout.main_activity;
    }

    @Override
    protected void initView() {
        mMsg= findViewById(R.id.tv_msg);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNfcAdapter == null){
            showMsg("设备不支持nfc");
            return;
        }
        pi = PendingIntent.getActivity(this, 0, new Intent(this, getClass())
                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
    }
    @Override
    protected void onResume() {
        super.onResume();
        //启动
        mNfcAdapter.enableForegroundDispatch(this, pi, null, null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mNfcAdapter != null){
            // 取消调度
            mNfcAdapter.disableForegroundDispatch(this);
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleNfcIntent(intent);

    }


    /**
     * 处理nfc标签
     *
     * @param intent
     */
    private void handleNfcIntent(Intent intent) {
        String action = intent.getAction();
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        showMsg("支持的Tag列表："+tag.toString());
        //卡片uid
        String uid= bytes2HexStr(tag.getId());
        showMsg("卡片uid:"+uid);
        //根据不同的Action进行获取不同的标签
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {

            try {
                processTag(tag);
            } catch (Exception e) {
               showMsg("通讯异常："+e.getMessage());
            }
        }
    }


    protected void processTag( Tag tag) {
        IsoDep isodep = IsoDep.get(tag);
        try {
            if (isodep != null){
                // 建立连接
                isodep.connect();
                //data是要发送的命令数据，自己组装
                byte[] data = new byte[20];
                // 发送命令
                byte[] response = isodep.transceive(data);
                // 解析响应数据
            }
        } catch (Exception e) {
            showMsg("通讯异常："+e.getMessage());
        }finally {
            if (isodep != null){
                // 关闭连接
                try {
                    isodep.close();
                } catch (IOException e) {

                }
            }
        }
    }

    private void showMsg(String msg) {
        String s = mMsg.getText().toString();
        if (TextUtils.isEmpty(s)){
            mMsg.setText(msg);
        }else {
            mMsg.setText(s+"\n"+msg);
        }

    }
    public static String bytes2HexStr(byte[] src) {
        StringBuilder builder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return "";
        }
        char[] buffer = new char[2];
        for (int i = 0; i < src.length; i++) {
            buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);
            buffer[1] = Character.forDigit(src[i] & 0x0F, 16);
            builder.append(buffer);
        }
        return builder.toString().toUpperCase();
    }


}
