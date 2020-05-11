package cn.droid.game.demo;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import droid.game.common.toast.ToastEx;
import droid.game.core.bridge.DroidSdk;
import droid.game.core.keep.IEventReceiver;
import droid.game.core.parameter.GameParam;
import droid.game.core.result.Result;
import droid.game.open.source.otto.Subscribe;

public class GameActivity extends AppCompatActivity implements IEventReceiver {
    final int INIT_PAGE = 0;
    final int LOGIN_PAGE = 1;
    final int ROLE_PAGE = 2;
    final int GAME_PAGE = 3;
    final int PAY_PAGE = 4;
    final int VIDEO_PAGE = 5;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.act_game);

        DroidSdk.Game.registerSDKEventReceiver(new IEventReceiver() {
            @Subscribe
            public void onInitResult(Result.Init result){
                if (result.isSuccess()){
                    showPage(LOGIN_PAGE);
                }else{
                    ToastEx.longShow(result.getError());
                }
            }
        });

        DroidSdk.Activity.onCreate(this);

        showPage(INIT_PAGE);

        setupEvents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        DroidSdk.Activity.onResume(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        DroidSdk.Activity.onRestart(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        DroidSdk.Activity.onStart(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        DroidSdk.Activity.onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        DroidSdk.Activity.onStop(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DroidSdk.Activity.onDestroy(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        DroidSdk.Activity.onNewIntent(this,intent);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        DroidSdk.Activity.onConfigurationChanged(this,newConfig);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        DroidSdk.Activity.onActivityResult(this,requestCode,resultCode,data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        DroidSdk.Activity.onRequestPermissionsResult(this,requestCode,permissions,grantResults);
    }

    void showPage(int index){
        int[] ids = new int[]{R.id.initPage,R.id.loginPage,R.id.rolePage,R.id.gamePage,R.id.payPage,R.id.videoPage};
        for (int i = 0; i < ids.length - 1; i++){
            findViewById(ids[i]).setVisibility(i == index ? View.VISIBLE : View.GONE);
        }
    }

    void setupEvents(){
        setupInitPageEnents();
        setupLoginPageEnents();
    }

    void setupInitPageEnents() {
        findViewById(R.id.init).setOnClickListener((v) -> {
            GameParam gameParam = new GameParam();
            gameParam.setCanLogout(true);
            gameParam.setCanSwitchAccount(true);
            DroidSdk.Game.init(this,gameParam);
        });
    }

    void setupLoginPageEnents(){
        findViewById(R.id.login).setOnClickListener((v)-> {
            DroidSdk.Game.login(getActivity(),null);
            //new DialogTest(getActivity()).show();
        });
    }

    Activity getActivity(){
        return this;
    }
}
