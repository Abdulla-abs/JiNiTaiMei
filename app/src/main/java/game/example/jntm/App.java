package game.example.jntm;

import android.app.Application;

import game.example.jntm.utils.SoundPoolUtil;
import game.example.jntm.utils.Utils;
import game.example.jntm.view.chaojikunkun.Constants;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Utils.init(this);
        Constants.init();
    }
}
