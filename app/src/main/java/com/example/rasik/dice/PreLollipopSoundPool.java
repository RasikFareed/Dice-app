package com.example.rasik.dice;

import android.media.AudioManager;
import android.media.SoundPool;

/**
 * Created by rasik on 23/9/17.
 */
final class PreLollipopSoundPool {
    @SuppressWarnings("deprecation")
    public static SoundPool NewSoundPool() {
        return new SoundPool(1, AudioManager.STREAM_MUSIC,0);
    }
}
