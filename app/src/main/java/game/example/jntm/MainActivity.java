package game.example.jntm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import game.example.jntm.ui.BieCaiBaiKuaiActivity;
import game.example.jntm.ui.ChaoJiKunKunActivity;
import game.example.jntm.ui.SaoJiActivity;
import game.example.jntm.ui.TanChiJiActivity;
import game.example.jntm.utils.SoundPoolUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button ji;
    private Button ni;
    private Button tai;
    private Button mei;
    private Button jinitaimei;
    private Button niganma;
    private Button lanqiu;
    private Button jinishizaishitaimei;
    private Button tanchiji;
    private Button saoji;
    private Button biecaijikuai;
    private Button caojikunkun;





    public static final String[] SOUND_TAG = new String[]{"ji","ni","tai","mei","jinitaimei","niganma","lanqiu","jinishizaishitaimei"};

    private final SoundPoolUtil soundPoolUtil = SoundPoolUtil.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ji = (Button) findViewById(R.id.ji);
        ji.setTag(0);
        ni = (Button) findViewById(R.id.ni);
        ni.setTag(1);
        tai = (Button) findViewById(R.id.tai);
        tai.setTag(2);
        mei = (Button) findViewById(R.id.mei);
        mei.setTag(3);
        jinitaimei = (Button) findViewById(R.id.jinitaimei);
        jinitaimei.setTag(4);
        niganma = (Button) findViewById(R.id.niganma);
        niganma.setTag(5);
        lanqiu = (Button) findViewById(R.id.lanqiu);
        lanqiu.setTag(6);
        jinishizaishitaimei = (Button) findViewById(R.id.jinishizaishitaimei);
        jinishizaishitaimei.setTag(7);
        tanchiji = (Button) findViewById(R.id.tanchiji);
        tanchiji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TanChiJiActivity.class));
            }
        });
        saoji = (Button) findViewById(R.id.saoji);
        saoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SaoJiActivity.class));
            }
        });
        biecaijikuai = (Button) findViewById(R.id.biecaijikuai);
        biecaijikuai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, BieCaiBaiKuaiActivity.class));
            }
        });
        caojikunkun = (Button) findViewById(R.id.caojikunkun);
        caojikunkun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ChaoJiKunKunActivity.class));
            }
        });


        soundPoolUtil.loadR(this,SOUND_TAG[0],R.raw.ji);
        soundPoolUtil.loadR(this,SOUND_TAG[1],R.raw.ni);
        soundPoolUtil.loadR(this,SOUND_TAG[2],R.raw.tai);
        soundPoolUtil.loadR(this,SOUND_TAG[3],R.raw.mei);
        soundPoolUtil.loadR(this,SOUND_TAG[4],R.raw.jinitaimei);
        soundPoolUtil.loadR(this,SOUND_TAG[5],R.raw.niganma);
        soundPoolUtil.loadR(this,SOUND_TAG[6],R.raw.lanqiu);
        soundPoolUtil.loadR(this,SOUND_TAG[7],R.raw.jinishizaishitaimei);


        ji.setOnClickListener(this);
        ni.setOnClickListener(this);
        tai.setOnClickListener(this);
        mei.setOnClickListener(this);
        jinitaimei.setOnClickListener(this);
        niganma.setOnClickListener(this);
        lanqiu.setOnClickListener(this);
        jinishizaishitaimei.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        soundPoolUtil.play(SOUND_TAG[((int)v.getTag())]);
    }
}