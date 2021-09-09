package com.sjl.net;

import android.content.Intent;
import android.view.View;

import com.anthonycr.grant.PermissionsManager;
import com.anthonycr.grant.PermissionsResultAction;

public class MainActivity extends BaseActivity {



    @Override
    protected int getLayoutId() {
        return R.layout.main_activity;
    }


    protected void initView() {
        setContentView(R.layout.main_activity);
    }

    protected void initListener() {

    }



    protected void initData() {
        PermissionsManager.getInstance().requestAllManifestPermissionsIfNecessary(this, new PermissionsResultAction() {
            @Override
            public void onGranted() {

            }

            @Override
            public void onDenied(String permission) {

            }
        });
    }

    private void openActivity(Class clz) {
        startActivity(new Intent(this,clz));
    }

    public void downloadTest(View view) {
        openActivity(FileDownloadActivity.class);
    }



    public void uploadTest(View view) {
        openActivity(FileChunkUploadActivity.class);
    }


    public void downloadTest2(View view) {
        openActivity(MultiThreadDownloadActivity.class);
    }
}
