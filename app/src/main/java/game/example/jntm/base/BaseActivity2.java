package game.example.jntm.base;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import game.example.jntm.view.biecaibaikuai.GridGameView;

public abstract class BaseActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutView());

        initView();
        initData();
        initEvent();
    }

    protected abstract View getLayoutView();

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void initEvent();

}
