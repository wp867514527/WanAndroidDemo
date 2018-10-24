package com.aloe.zxlib.utils;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;

import com.aloe.zxlib.R;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author Aloe.Zheng
 * @time 2018/5/10 16:16
 * @email aloe200@163.com
 */
public class BeepManager implements MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, Closeable {
  private static final float BEEP_VOLUME = 0.10f;
  private static final long VIBRATE_DURATION = 200L;

  private final AppCompatActivity activity;
  private MediaPlayer mediaPlayer;
  private boolean playBeep;
  private boolean vibrate;

  public BeepManager(final AppCompatActivity activity) {
    this.activity = activity;
    this.mediaPlayer = null;
    updatePrefs();
  }

  private static boolean shouldBeep(final Context activity) {
    boolean shouldPlayBeep = true;
    AudioManager audioService = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
    if (audioService != null && audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
      shouldPlayBeep = false;
    }
    return shouldPlayBeep;
  }

  private synchronized void updatePrefs() {
    playBeep = shouldBeep(activity);
    vibrate = true;
    if (playBeep && mediaPlayer == null) {
      activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
      mediaPlayer = buildMediaPlayer(activity);
    }
  }

  public final synchronized void playBeepSoundAndVibrate() {
    if (playBeep && mediaPlayer != null) {
      mediaPlayer.start();
    }
    if (vibrate) {
      Vibrator vibrator = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
      if (vibrator != null) {
        vibrator.vibrate(VIBRATE_DURATION);
      }
    }
  }

  private MediaPlayer buildMediaPlayer(final Context activity) {
    MediaPlayer media = new MediaPlayer();
    media.setAudioStreamType(AudioManager.STREAM_MUSIC);
    media.setOnCompletionListener(this);
    media.setOnErrorListener(this);
    try {
      try (AssetFileDescriptor file = activity.getResources().openRawResourceFd(R.raw.beep)) {
        media.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
      }
      media.setVolume(BEEP_VOLUME, BEEP_VOLUME);
      media.prepare();
      return media;
    } catch (IOException e) {
      e.printStackTrace();
      media.release();
      return null;
    }
  }

  @Override
  public final void onCompletion(final MediaPlayer mp) {
    mp.seekTo(0);
  }

  @Override
  public final synchronized boolean onError(final MediaPlayer mp, final int what, final int extra) {
    if (what == MediaPlayer.MEDIA_ERROR_SERVER_DIED) {
      activity.finish();
    } else {
      mp.release();
      mediaPlayer = null;
      updatePrefs();
    }
    return true;
  }

  @Override
  public final synchronized void close() {
    if (mediaPlayer != null) {
      mediaPlayer.release();
      mediaPlayer = null;
    }
  }
}
