package game.example.jntm.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import game.example.jntm.R;
import game.example.jntm.base.BaseActivity;
import game.example.jntm.view.chaojikunkun.CaoZuoGan;
import game.example.jntm.view.chaojikunkun.ChaoJiKunKunSFView;
import game.example.jntm.view.chaojikunkun.Constants;
import game.example.jntm.view.chaojikunkun.Kun;

public class ChaoJiKunKunActivity extends BaseActivity {

    private ChaoJiKunKunSFView chaoJiKunKunSFView;
    private Kun kun;
    private CaoZuoGan controller;
    private Button jump;



    @Override
    protected int getLayoutId() {
        return R.layout.activity_chao_ji_kun_kun;
    }

    @Override
    protected void initView() {
        chaoJiKunKunSFView = (ChaoJiKunKunSFView) findViewById(R.id.chaoJiKunKunSFView);
        controller = (CaoZuoGan) findViewById(R.id.controller);
        jump = (Button) findViewById(R.id.jump);

    }

    @Override
    protected void initData() {
        kun = chaoJiKunKunSFView.getKun();

    }

    @Override
    protected void initEvent() {
        controller.setListener(new CaoZuoGan.OnSliderListener() {
            @Override
            public void onLeft() {
                kun.moveLeft();
            }

            @Override
            public void onRight() {
                kun.moveRight();
            }

            @Override
            public void onStand() {
                kun.stand();
            }
        });

        jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kun.jump();
            }
        });
    }
}