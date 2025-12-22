package com.yogs.loadervip;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Vibrator;
import android.os.IBinder;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.animation.ObjectAnimator;
import android.widget.Toast;



public class FloatLogo extends Service {

    private WindowManager mWindowManager;
    private View mFloatingView;
    private Context mContext;

    public FloatLogo() {
    }

    static {
        System.loadLibrary("Starkskillz");
    }

    public native void SettingMemory(int code, boolean value);

    public native void SettingValue(int code, boolean value);

    public native void SettingValueI(int code, int value);

    public native void WideView(int view);

    public native void Range(int range);

    public native void Recoil(int range);

    public native void Target(int target);

    public native void AimBy(int aimby);

    public native void AimingSpeed(int aimingspeed);

    public native void Smoothness(int smoothness);

    public native void TouchSize(int touchsize);

    public native void TouchPosX(int touchposx);

    public native void TouchPosY(int touchposy);

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    View espView, logoView;

    @SuppressLint("CutPasteId")
    @Override
    public void onCreate() {
        super.onCreate();
        this.mContext = this;
        createOver();
        logoView = mFloatingView.findViewById(R.id.relativeLayoutParent);
        espView = mFloatingView.findViewById(R.id.espView);
        espView.setAlpha(1f);
        Init();
    }

    @SuppressLint("InflateParams")
    void createOver() {
        mFloatingView = LayoutInflater.from(this).inflate(R.layout.float_logo, null);
        int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.RGBA_8888
        );

        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mFloatingView, params);

        TextView closeBtn = mFloatingView.findViewById(R.id.closeBtn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                espView.setVisibility(View.GONE);
                logoView.setVisibility(View.VISIBLE);
            }
        });

        final LinearLayout player = mFloatingView.findViewById(R.id.players);
        final LinearLayout memory = mFloatingView.findViewById(R.id.memoryMenu);
        final LinearLayout bg_playerBtn = mFloatingView.findViewById(R.id.bg_playerBtn);
        final LinearLayout bg_setting = mFloatingView.findViewById(R.id.bg_setting);
        //final LinearLayout track = mFloatingView.findViewById(R.id.selectorTrack);
        //final LinearLayout bg_trackBtn = mFloatingView.findViewById(R.id.bg_trackBtn);
        //final ImageView trackBtn = mFloatingView.findViewById(R.id.trackBtn);
        final ImageView playerBtn = mFloatingView.findViewById(R.id.playerBtn);
        final ImageView settingBtn = mFloatingView.findViewById(R.id.setBtn);

        final TextView textview_subx = mFloatingView.findViewById(R.id.textview_subx);

        final LinearLayout track = mFloatingView.findViewById(R.id.selectorTrack);
        final LinearLayout bg_trackBtn = mFloatingView.findViewById(R.id.bg_trackBtn);
        final ImageView trackBtn = mFloatingView.findViewById(R.id.trackBtn);

        playerBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                bg_setting.setBackgroundColor(Color.TRANSPARENT);
                bg_playerBtn.setBackgroundColor(Color.parseColor("#3C8C92AC"));
                playerBtn.setColorFilter(Color.parseColor("#ffffff"));
                settingBtn.setColorFilter(Color.TRANSPARENT);
                bg_trackBtn.setBackgroundColor(Color.TRANSPARENT);
                trackBtn.setColorFilter(Color.TRANSPARENT);
                track.setVisibility(View.GONE);
                player.setVisibility(View.VISIBLE);
                memory.setVisibility(View.GONE);
                textview_subx.setText("Display");
            }
        });

        trackBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                //bg_pickupBtn.setBackgroundColor(Color.TRANSPARENT);
                bg_playerBtn.setBackgroundColor(Color.TRANSPARENT);
                bg_setting.setBackgroundColor(Color.TRANSPARENT);
                bg_trackBtn.setBackgroundColor(Color.parseColor("#3C8C92AC"));
                playerBtn.setColorFilter(Color.TRANSPARENT);
                //pickupBtn.setColorFilter(Color.TRANSPARENT);
                trackBtn.setColorFilter(Color.parseColor("#ffffff"));
                settingBtn.setColorFilter(Color.TRANSPARENT);

                //modeitems2.setVisibility(View.GONE);
                //selectorItems.setVisibility(View.GONE);
                player.setVisibility(View.GONE);
                memory.setVisibility(View.GONE);
                track.setVisibility(View.VISIBLE);
                textview_subx.setText("Controls");
            }
        });

        settingBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                //bg_pickupBtn.setBackgroundColor(Color.TRANSPARENT);
                bg_playerBtn.setBackgroundColor(Color.TRANSPARENT);
                bg_setting.setBackgroundColor(Color.parseColor("#3C8C92AC"));
                //bg_trackBtn.setBackgroundColor(Color.TRANSPARENT);
                playerBtn.setColorFilter(Color.TRANSPARENT);
                //pickupBtn.setColorFilter(Color.TRANSPARENT);
                //trackBtn.setColorFilter(Color.TRANSPARENT);
                settingBtn.setColorFilter(Color.parseColor("#ffffff"));
                bg_trackBtn.setBackgroundColor(Color.TRANSPARENT);
                trackBtn.setColorFilter(Color.TRANSPARENT);
                track.setVisibility(View.GONE);
                //modeitems2.setVisibility(View.GONE);
                //selectorItems.setVisibility(View.GONE);
                player.setVisibility(View.GONE);
                memory.setVisibility(View.VISIBLE);
                //track.setVisibility(View.GONE);
                textview_subx.setText("Settings");
            }
        });

        //Floating window setting
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        final GestureDetector gestureDetector = new GestureDetector(this, new SingleTapConfirm());
        mFloatingView.findViewById(R.id.relativeLayoutParent).setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;
            private ObjectAnimator alphaAnimator;

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

                if (gestureDetector.onTouchEvent(event)) {
                    espView.setVisibility(View.VISIBLE);
                    logoView.setVisibility(View.GONE);
                    return true;
                } else {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            initialX = params.x;
                            initialY = params.y;
                            initialTouchX = event.getRawX();
                            initialTouchY = event.getRawY();
                            // Vibrate for 200 milliseconds
                            vibrator.vibrate(100);
                            // Set the alpha value to 0.5 (0.0 - fully transparent, 1.0 - fully opaque)
                            v.setAlpha(1f);
                            return true;
                        case MotionEvent.ACTION_MOVE:
                            params.x = initialX + (int) (event.getRawX() - initialTouchX);
                            params.y = initialY + (int) (event.getRawY() - initialTouchY);
                            mWindowManager.updateViewLayout(mFloatingView, params);
                            return true;
                        case MotionEvent.ACTION_UP:
                            // Gradually restore transparency to normal
                            alphaAnimator = ObjectAnimator.ofFloat(v, "alpha", 0.5f);
                            alphaAnimator.setDuration(500); // Adjust the duration as desired
                            alphaAnimator.start();
                            return true;
                    }
                    return false;
                }
            }
        });


        mFloatingView.findViewById(R.id.espView).setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);
                        mWindowManager.updateViewLayout(mFloatingView, params);
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatingView != null) {
            mWindowManager.removeView(mFloatingView);
        }
    }

    private void setValue(String key, boolean value) {
        SharedPreferences sp = getSharedPreferences("espValue", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean(key, value);
        ed.apply();
    }

    private boolean getConfig(String key) {
        SharedPreferences sp = getSharedPreferences("espValue", Context.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    void Init() {

        final Switch Bypass = mFloatingView.findViewById(R.id.bypasslogo);
        Bypass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Overlay.Shell(MainActivity.daemon64);
            }
        });

        final Switch EESP = mFloatingView.findViewById(R.id.drawesp);
        EESP.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    startService(new Intent(FloatLogo.this, Overlay.class));
                } else {
                    stopService(new Intent(FloatLogo.this, Overlay.class));
                }
            }
        });
        final CheckBox isEnemyWeapon = mFloatingView.findViewById(R.id.isEnemyWeapon);
        isEnemyWeapon.setChecked(getConfig((String) isEnemyWeapon.getText()));
        SettingValue(10, getConfig((String) isEnemyWeapon.getText()));
        isEnemyWeapon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(isEnemyWeapon.getText()), isEnemyWeapon.isChecked());
                SettingValue(10, isEnemyWeapon.isChecked());
            }
        });
        final CheckBox isGrenadeWarning = mFloatingView.findViewById(R.id.isGrenadeWarning);
        isGrenadeWarning.setChecked(getConfig((String) isGrenadeWarning.getText()));
        SettingValue(9, getConfig((String) isGrenadeWarning.getText()));
        isGrenadeWarning.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(isGrenadeWarning.getText()), isGrenadeWarning.isChecked());
                SettingValue(9, isGrenadeWarning.isChecked());
            }
        });
        final CheckBox isSkelton = mFloatingView.findViewById(R.id.isSkelton);
        isSkelton.setChecked(getConfig((String) isSkelton.getText()));
        SettingValue(8, getConfig((String) isSkelton.getText()));
        isSkelton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(isSkelton.getText()), isSkelton.isChecked());
                SettingValue(8, isSkelton.isChecked());
            }
        });
        final CheckBox isLine = mFloatingView.findViewById(R.id.isLine);
        isLine.setChecked(getConfig((String) isLine.getText()));
        SettingValue(6, getConfig((String) isLine.getText()));
        isLine.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(isLine.getText()), isLine.isChecked());
                SettingValue(6, isLine.isChecked());
            }
        });
        final CheckBox isBack = mFloatingView.findViewById(R.id.isBack);
        isBack.setChecked(getConfig((String) isBack.getText()));
        SettingValue(7, getConfig((String) isBack.getText()));
        isBack.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(isBack.getText()), isBack.isChecked());
                SettingValue(7, isBack.isChecked());
            }
        });
        final CheckBox isHealth = mFloatingView.findViewById(R.id.isHealth);
        isHealth.setChecked(getConfig((String) isHealth.getText()));
        SettingValue(4, getConfig((String) isHealth.getText()));
        isHealth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(isHealth.getText()), isHealth.isChecked());
                SettingValue(4, isHealth.isChecked());
            }
        });
        final CheckBox isNoBot = mFloatingView.findViewById(R.id.isNoBot);
        isNoBot.setChecked(getConfig((String) isNoBot.getText()));
        SettingValue(17, getConfig((String) isNoBot.getText()));
        isNoBot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(isNoBot.getText()), isNoBot.isChecked());
                SettingValue(17, isNoBot.isChecked());
            }
        });
        final CheckBox isName = mFloatingView.findViewById(R.id.isName);
        isName.setChecked(getConfig((String) isName.getText()));
        SettingValue(5, getConfig((String) isName.getText()));
        isName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(isName.getText()), isName.isChecked());
                SettingValue(5, isName.isChecked());
            }
        });
        final CheckBox isTeam = mFloatingView.findViewById(R.id.isTeam);
        isTeam.setChecked(getConfig((String) isTeam.getText()));
        SettingValue(16, getConfig((String) isTeam.getText()));
        isTeam.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(isTeam.getText()), isTeam.isChecked());
                SettingValue(16, getConfig((String) isTeam.getText()));
            }
        });
        final CheckBox isDist = mFloatingView.findViewById(R.id.isDist);
        isDist.setChecked(getConfig((String) isDist.getText()));
        SettingValue(3, getConfig((String) isDist.getText()));
        isDist.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(isDist.getText()), isDist.isChecked());
                SettingValue(3, isDist.isChecked());
            }
        });

        final SharedPreferences prefs = mContext.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        int savedIdd = prefs.getInt("radio_box_id", -1);
        if (savedIdd != -1) {
            RadioButton savedBtnn = mFloatingView.findViewById(savedIdd);
            if (savedBtnn != null) {
                savedBtnn.setChecked(true);
                SettingValueI(1, Integer.parseInt(savedBtnn.getTag().toString()));
            }
        }

        final RadioGroup radiooBox = mFloatingView.findViewById(R.id.radiooBox);
        radiooBox.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int chkdId = radiooBox.getCheckedRadioButtonId();
                RadioButton btn = mFloatingView.findViewById(chkdId);
                SettingValueI(1, Integer.parseInt(btn.getTag().toString()));

                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("radio_box_id", chkdId);
                editor.apply();
            }
        });

        int savedId = prefs.getInt("radio_line_id", -1);
        if (savedId != -1) {
            RadioButton savedBtn = mFloatingView.findViewById(savedId);
            if (savedBtn != null) {
                savedBtn.setChecked(true);
                SettingValueI(2, Integer.parseInt(savedBtn.getTag().toString()));
            }
        }

        final RadioGroup radiooLine = mFloatingView.findViewById(R.id.radiooLine);
        radiooLine.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int chkdId = radiooLine.getCheckedRadioButtonId();
                RadioButton btn = mFloatingView.findViewById(chkdId);

                // Pakai SettingValueI seperti biasa
                SettingValueI(2, Integer.parseInt(btn.getTag().toString()));

                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("radio_line_id", chkdId);
                editor.apply();
            }
        });

        final CheckBox smallCross = mFloatingView.findViewById(R.id.smallCross);
        smallCross.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SettingMemory(4, isChecked);
            }
        });

        final CheckBox hsbbd = mFloatingView.findViewById(R.id.rotationscreen);
        hsbbd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SettingValue(18, isChecked);
            }
        });

        final CheckBox hhsbbd = mFloatingView.findViewById(R.id.showtouch);
        hhsbbd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SettingValue(19, isChecked);
            }
        });

        final TextView rangs = mFloatingView.findViewById(R.id.textView5);
        final SeekBar range = mFloatingView.findViewById(R.id.range); // Dapatkan referensi SeekBar "range" di sini

        int savedFovValue = prefs.getInt("fov", -1); // Gunakan default value yang masuk akal, misal 50 jika -1 tidak cocok
        if (savedFovValue != -1) {
            range.setProgress(savedFovValue); // Atur progress SeekBar yang benar
            Range(savedFovValue);             // Panggil native method Anda dengan nilai yang dimuat
            rangs.setText("Fov Circle: " + savedFovValue); // Perbarui TextView
        }


        range.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Gunakan parameter 'progress' yang diberikan, lebih aman
                Range(progress);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("fov", progress);
                editor.apply();
                rangs.setText("Fov Circle: " + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Tidak ada perubahan di sini
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Tidak ada perubahan di sini
            }
        });

        final TextView recc = mFloatingView.findViewById(R.id.textView22);
        final SeekBar recoil = mFloatingView.findViewById(R.id.recoil);

        int savedIddd = prefs.getInt("recoil", -1);
        if (savedIddd != -1) {
            recoil.setProgress(savedIddd);
            Recoil(savedIddd);
            recc.setText("Aim Recoil: " + savedIddd);
        }

        recoil.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Recoil(recoil.getProgress());
                recc.setText("Aim Recoil: " + recoil.getProgress());
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("recoil", recoil.getProgress());
                editor.apply();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        final TextView speedtext = mFloatingView.findViewById(R.id.textSpeed);
        final SeekBar speed = mFloatingView.findViewById(R.id.speed);

        int speeds = prefs.getInt("speed", -1);
        if (speeds != -1) {
            speed.setProgress(speeds);
            AimingSpeed(speeds);
            speedtext.setText("Touch Speed: " + speeds);
        }

        speed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                AimingSpeed(speed.getProgress());
                speedtext.setText("Touch Speed: " + speed.getProgress());
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("speed", speed.getProgress());
                editor.apply();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        final TextView smoothtext = mFloatingView.findViewById(R.id.textSmooth);
        final SeekBar smooth = mFloatingView.findViewById(R.id.smooth);

        int smooths = prefs.getInt("smooth", -1);
        if (smooths != -1) {
            smooth.setProgress(smooths);
            Smoothness(smooths);
            smoothtext.setText("Aim smooth: " + smooths);
        }

        smooth.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Smoothness(smooth.getProgress());
                smoothtext.setText("Aim smooth: " + smooth.getProgress());
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("smooth", smooth.getProgress());
                editor.apply();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        final TextView touchsizetext = mFloatingView.findViewById(R.id.textTouchSize);
        final SeekBar touchsize = mFloatingView.findViewById(R.id.touchsize);

        int touchsizes = prefs.getInt("touchsize", -1);
        if (touchsizes != -1) {
            touchsize.setProgress(touchsizes);
            TouchSize(touchsizes);
            touchsizetext.setText("Touch Size: " + touchsizes);
        }

        touchsize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                TouchSize(touchsize.getProgress());
                touchsizetext.setText("Touch Size: " + touchsize.getProgress());
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("touchsize", touchsize.getProgress());
                editor.apply();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        final TextView posxtext = mFloatingView.findViewById(R.id.textPosX);
        final SeekBar posx = mFloatingView.findViewById(R.id.posx);

        int posxs = prefs.getInt("posx", -1);
        if (posxs != -1) {
            posx.setProgress(posxs);
            TouchPosX(posxs);
            posxtext.setText("Touch Pos X: " + posxs);
        }

        posx.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                TouchPosX(posx.getProgress());
                posxtext.setText("Touch Pos X: " + posx.getProgress());
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("posx", posx.getProgress());
                editor.apply();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        final TextView posytext = mFloatingView.findViewById(R.id.textPosY);
        final SeekBar posy = mFloatingView.findViewById(R.id.posy);

        int posys = prefs.getInt("posy", -1);
        if (posys != -1) {
            posy.setProgress(posys);
            TouchPosY(posys);
            posytext.setText("Touch Pos Y: " + posys);
        }

        posy.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                TouchPosY(posy.getProgress());
                posytext.setText("Touch Pos Y: " + posy.getProgress());
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("posy", posy.getProgress());
                editor.apply();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        final RadioGroup aimby = mFloatingView.findViewById(R.id.aimby);
        aimby.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int chkdId = aimby.getCheckedRadioButtonId();
                RadioButton btn = mFloatingView.findViewById(chkdId);
                AimBy(Integer.parseInt(btn.getTag().toString()));
            }
        });

        final RadioGroup aimbotmode = mFloatingView.findViewById(R.id.aimbotmode);
        aimbotmode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int chkdId = aimbotmode.getCheckedRadioButtonId();
                RadioButton btn = mFloatingView.findViewById(chkdId);
                Target(Integer.parseInt(btn.getTag().toString()));
            }
        });

        final Switch aimbot = mFloatingView.findViewById(R.id.aimbot);
        aimbot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    startService(new Intent(getApplicationContext(), FloatAim.class));
                } else {
                    SettingValue(11, false);
                    stopService(new Intent(getApplicationContext(), FloatAim.class));
                }
            }
        });

        final Switch aimknocked = mFloatingView.findViewById(R.id.aimknocked);
        aimknocked.setChecked(getConfig((String) aimknocked.getText()));
        SettingValue(13, getConfig((String) aimknocked.getText()));
        aimknocked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(aimknocked.getText()), aimknocked.isChecked());
                SettingValue(13, aimknocked.isChecked());
            }
        });
    }
}

class SingleTapConfirm extends GestureDetector.SimpleOnGestureListener {
    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        return true;
    }
}
