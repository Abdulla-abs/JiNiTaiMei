package game.example.jntm.ui;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import game.example.jntm.MainActivity;
import game.example.jntm.R;
import game.example.jntm.utils.SoundPoolUtil;
import game.example.jntm.view.saoji.Container;
import game.example.jntm.view.saoji.OnSaoJiListener;


public class SaoJiActivity extends AppCompatActivity {

    private Container container;
    private ToggleButton toggle;
    private TextView tip;



    private final SoundPoolUtil soundPoolUtil = SoundPoolUtil.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saoji);

        tip = (TextView) findViewById(R.id.tip);
        container = (Container) findViewById(R.id.container);
        toggle = (ToggleButton) findViewById(R.id.toggle);

        tip.setText("剩余雷数量"+container.getBoomCount());

        soundPoolUtil.loadR(this, MainActivity.SOUND_TAG[3],R.raw.mei);
        soundPoolUtil.loadR(this, MainActivity.SOUND_TAG[0],R.raw.ji);
        soundPoolUtil.loadR(this,MainActivity.SOUND_TAG[7],R.raw.jinishizaishitaimei);

        container.setListener(new OnSaoJiListener() {
            @Override
            public void onShow() {
                soundPoolUtil.play(MainActivity.SOUND_TAG[3]);
            }

            @Override
            public void onBoom() {
                soundPoolUtil.play(MainActivity.SOUND_TAG[0],5);
            }

            @Override
            public void onFlagChange() {
                tip.setText("剩余雷数量"+(container.getBoomCount()-container.getFlagBoomCount()));
            }

            @Override
            public void onFinish() {
                soundPoolUtil.play(MainActivity.SOUND_TAG[7]);
                tip.setText("基尼胎没");
                Snackbar.make(container,"基尼胎没",Snackbar.LENGTH_LONG).show();
            }
        });

        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                container.setFlagModel(toggle.isChecked());
            }
        });





    }
}