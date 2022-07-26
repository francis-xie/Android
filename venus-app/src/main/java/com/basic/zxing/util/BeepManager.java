package com.basic.zxing.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;
import com.emis.venus.R;
import com.basic.zxing.activity.CaptureActivity;

import java.io.Closeable;
import java.io.IOException;

/**
 * 管理嘟嘟声和振动 {@link CaptureActivity}.
 * Android下对于音频、视频的支持均需要使用到MediaPlayer，它主要用来控制Android下播放文件或流的类
 */
public final class BeepManager implements MediaPlayer.OnErrorListener, Closeable {

  private static final String TAG = BeepManager.class.getSimpleName();

  private static final float BEEP_VOLUME = 0.10f;
  private static final long VIBRATE_DURATION = 200L;

  private final Activity activity;
  private MediaPlayer mediaPlayer;
  private boolean playBeep;
  private boolean vibrate;

  public BeepManager(Activity activity) {
    this.activity = activity;
    this.mediaPlayer = null;
    updatePrefs();
  }

  public synchronized void updatePrefs() {
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
    playBeep = shouldBeep(prefs, activity);
    vibrate = prefs.getBoolean(PreferencesActivity.KEY_VIBRATE, false);
    if (playBeep && mediaPlayer == null) {
      // The volume on STREAM_SYSTEM is not adjustable, and users found it too loud,
      // so we now play on the music stream.
      activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
      mediaPlayer = buildMediaPlayer(activity);
    }
  }

  public synchronized void playBeepSoundAndVibrate() {
    if (playBeep && mediaPlayer != null) {
      mediaPlayer.start();
    }
    if (vibrate) {
      Vibrator vibrator = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
      vibrator.vibrate(VIBRATE_DURATION);
    }
  }

  private static boolean shouldBeep(SharedPreferences prefs, Context activity) {
    boolean shouldPlayBeep = prefs.getBoolean(PreferencesActivity.KEY_PLAY_BEEP, true);
    if (shouldPlayBeep) {
      // See if sound settings overrides this
      AudioManager audioService = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
      if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
        shouldPlayBeep = false;
      }
    }
    return shouldPlayBeep;
  }

  private MediaPlayer buildMediaPlayer(Context activity) {
    MediaPlayer mediaPlayer = new MediaPlayer();
    try (AssetFileDescriptor file = activity.getResources().openRawResourceFd(R.raw.beep)) {
      mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
      mediaPlayer.setOnErrorListener(this);
      mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
      mediaPlayer.setLooping(false);
      mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
      mediaPlayer.prepare();
      return mediaPlayer;
    } catch (IOException ioe) {
      Log.w(TAG, ioe);
      mediaPlayer.release();
      return null;
    }
  }

  @Override
  public synchronized boolean onError(MediaPlayer mp, int what, int extra) {
    if (what == MediaPlayer.MEDIA_ERROR_SERVER_DIED) {
      // we are finished, so put up an appropriate error toast if required and finish
      activity.finish();
    } else {
      // possibly media player error, so release and recreate
      close();
      updatePrefs();
    }
    return true;
  }

  @Override
  public synchronized void close() {
    if (mediaPlayer != null) {
      // 回收资源
      mediaPlayer.release();
      mediaPlayer = null;
    }
  }

}
