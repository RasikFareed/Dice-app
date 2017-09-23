package com.example.rasik.dice;

import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity  implements View.OnTouchListener{
    private double mCurrAngle = 0;
    private double mPrevAngle = 0;
        ImageView dice_picture;
        Random rng=new Random();
        SoundPool dice_sound;
        int sound_id;
        Handler handler;
        boolean rolling = false;
        Timer timer=new Timer();

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            InitSound();
            dice_picture = (ImageView) findViewById(R.id.dice_picture);
           dice_picture.setOnClickListener(new HandleClick());
            handler=new Handler(callback);
           // dice_picture.setOnTouchListener(this);


        }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        final float xc = dice_picture.getWidth() / 2;
        final float yc = dice_picture.getHeight() / 2;

        final float x = motionEvent.getX();
        final float y = motionEvent.getY();

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                dice_picture.clearAnimation();
                mCurrAngle = Math.toDegrees(Math.atan2(x - xc, yc - y));
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                mPrevAngle = mCurrAngle;
                mCurrAngle = Math.toDegrees(Math.atan2(x - xc, yc - y));
                animate(mPrevAngle, mCurrAngle, 0);
                dice_picture.setOnClickListener(new HandleClick());
                System.out.println(mCurrAngle);
                break;
            }
            case MotionEvent.ACTION_UP : {
                mPrevAngle = mCurrAngle = 0;
                break;
            }
        }

        return true;
    }
    private void animate(double fromDegrees, double toDegrees, long durationMillis) {
        final RotateAnimation rotate = new RotateAnimation((float) fromDegrees, (float) toDegrees,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(durationMillis);
        rotate.setFillEnabled(true);
        rotate.setFillAfter(true);
        dice_picture.startAnimation(rotate);
        System.out.println(mCurrAngle);
    }
    private class HandleClick implements View.OnClickListener {
            public void onClick(View arg0) {
                if (!rolling) {
                    rolling = true;
                    dice_picture.setImageResource(R.drawable.dice3d160);
                    dice_sound.play(sound_id, 1.0f, 1.0f, 0, 0, 1.0f);
                    timer.schedule(new Roll(), 400);
                }
            }
        }

        void InitSound() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                AudioAttributes aa = new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build();


                dice_sound= new SoundPool.Builder().setAudioAttributes(aa).build();

            } else {

                dice_sound=PreLollipopSoundPool.NewSoundPool();
            }

            sound_id=dice_sound.load(this,R.raw.shake_dice,1);
        }


        class Roll extends TimerTask {
            public void run() {
                handler.sendEmptyMessage(0);
            }
        }


        Handler.Callback callback = new Handler.Callback() {
            public boolean handleMessage(Message msg) {

                switch(rng.nextInt(6)+1) {
                    case 1:
                        dice_picture.setImageResource(R.drawable.one);
                        break;
                    case 2:
                        dice_picture.setImageResource(R.drawable.two);
                        break;
                    case 3:
                        dice_picture.setImageResource(R.drawable.three);
                        break;
                    case 4:
                        dice_picture.setImageResource(R.drawable.four);
                        break;
                    case 5:
                        dice_picture.setImageResource(R.drawable.five);
                        break;
                    case 6:
                        dice_picture.setImageResource(R.drawable.six);
                        break;
                    default:
                }
                rolling=false;
                return true;
            }
        };


        protected void onPause() {
            super.onPause();
            dice_sound.pause(sound_id);
        }
        protected void onDestroy() {
            super.onDestroy();
            timer.cancel();
        }



}

