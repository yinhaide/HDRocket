package com.yhd.mediaplayer.app.frag;

import android.os.Handler;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;

import com.de.rocket.ue.frag.RoFragment;
import com.de.rocket.ue.injector.BindView;
import com.de.rocket.ue.injector.Event;
import com.yhd.mediaplayer.MediaPlayerHelper;
import com.yhd.mediaplayer.app.R;

/**
 * 双波浪曲线
 * Created by haide.yin(haide.yin@tcl.com) on 2019/6/6 16:12.
 */
public class Frag_mediaplayer extends RoFragment {

    private static final String TAG="MainActivity";
    private final static String URL="http://qiniucdn.wenshanhu.com/3043d83491eaef0823a7b7b2f6b07e56.mp4";

    @BindView(R.id.surfaceView)
    private SurfaceView surfaceView;

    @Override
    public int onInflateLayout() {
        return R.layout.frag_mediaplayer;
    }

    @Override
    public void initViewFinish(View inflateView) {
        MediaPlayerHelper
                .getInstance()
                .setSurfaceView(surfaceView)
                .setProgressInterval(1000)
                .setMediaPlayerHelperCallBack((state, mediaPlayerHelper, args) -> {

                    Log.v(TAG,"--"+state.toString());
                    if(state == MediaPlayerHelper.CallBackState.PROGRESS){
                        if(args.length > 0){
                            int percent=(int)args[0];
                            activity.runOnUiThread(() -> toast("进度:"+percent+"%"));
                        }
                    }else if(state== MediaPlayerHelper.CallBackState.COMPLETE){
                        MediaPlayerHelper.getInstance().playAsset(activity,"test.mp4");
                    }else if(state== MediaPlayerHelper.CallBackState.BUFFER_UPDATE){
                        if(args.length > 1){
                            int percent=(int)args[1];
                            activity.runOnUiThread(() -> toast("网络缓冲:"+percent+"%"));
                        }
                    }
        });
    }

    @Override
    public void onNexts(Object object) {
        new Handler().post(() -> MediaPlayerHelper.getInstance().playAsset(activity,"test.mp4"));
    }

    @Event(R.id.assetsMP3Button)
    private void playassetMP3(View view){
        MediaPlayerHelper.getInstance().playAsset(activity,"test.mp3");
    }

    @Event(R.id.assetsMP4Button)
    private void playAssetMP4(View view){
        MediaPlayerHelper.getInstance().playAsset(activity,"test.mp4");
    }

    @Event(R.id.urlButton)
    private void playNetMP4(View view){
        new Handler().post(() -> MediaPlayerHelper.getInstance().playUrl(URL));
    }

    @Event(R.id.stopButton)
    private void stop(View view){
        MediaPlayerHelper.getInstance().getMediaPlayer().pause();
    }

    @Event(R.id.startButton)
    private void start(View view){
        MediaPlayerHelper.getInstance().getMediaPlayer().start();
    }

    @Event(R.id.resetButton)
    private void reset(View view){
        MediaPlayerHelper.getInstance().playAsset(activity,"test.mp4");
    }

    public void onStop(){
        super.onStop();
        if(MediaPlayerHelper.getInstance().getMediaPlayer()!=null){
            MediaPlayerHelper.getInstance().getMediaPlayer().stop();
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        MediaPlayerHelper.getInstance().release();
    }
}
