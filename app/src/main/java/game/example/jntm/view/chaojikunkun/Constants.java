package game.example.jntm.view.chaojikunkun;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import game.example.jntm.R;
import game.example.jntm.utils.ScreenUtils;
import game.example.jntm.utils.Utils;

public class Constants {

    public static Bitmap kun_s = BitmapFactory.decodeResource(Utils.getContext().getResources(), R.drawable.kun_avatar);
    public static Bitmap kun_l = BitmapFactory.decodeResource(Utils.getContext().getResources(), R.drawable.j);
    public static Bitmap wall = BitmapFactory.decodeResource(Utils.getContext().getResources(), R.drawable.wall);

    private static final Matrix kunMatrix = new Matrix();
    private static final Matrix wallMatrix = new Matrix();
    private static final Matrix jMatrix = new Matrix();

    public static void init() {
        kunMatrix.postScale(0.07f,0.07f);
        kun_s = Bitmap.createBitmap(kun_s,0,0,kun_s.getWidth(),kun_s.getHeight(),kunMatrix,true);
        jMatrix.postScale(((float)kun_s.getWidth())/kun_l.getWidth(),((float)kun_s.getHeight())/kun_l.getHeight());
        kun_l = Bitmap.createBitmap(kun_l,0,0,kun_l.getWidth(),kun_l.getHeight(),jMatrix,true);
        int scWidth = ScreenUtils.getScreenWidth();
        int scHeight = ScreenUtils.getScreenHeight();
        final float wallWidth = scWidth / 18.0f;
        wallMatrix.postScale(wallWidth/wall.getWidth(),wallWidth/wall.getHeight());
        wall = Bitmap.createBitmap(wall,0,0,wall.getWidth(),wall.getHeight(),wallMatrix,true);

        kun.put(KUN_STATUE_S, kun_s);
        kun.put(KUN_STATUE_L, kun_l);
        kun.put(KUN_STATUE_R, kun_l);
        kun.put(KUN_STATUE_U, kun_s);
        kun.put(KUN_STATUE_F, kun_s);

        List<ZhangAi> zhangAis_1 = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 18; j++) {
                zhangAis_1.add(new ZhangAi(ZA_WALL,j*wallWidth,scHeight-(i*wallWidth+wallWidth)));
            }
        }
        for (int i = 0; i < 3; i++) {
            zhangAis_1.add(new ZhangAi(ZA_WALL,scWidth/2+i*wallWidth,scHeight-(2*wallWidth+wallWidth)));
        }
        for (int i = 0; i < 5; i++) {
            zhangAis_1.add(new ZhangAi(ZA_WALL,scWidth/2.0f+i*wallWidth,scHeight/3.0f));
        }

        G_ZA.put(1,zhangAis_1);
    }

    public static int KUN_STATUE_S = 0;
    public static int KUN_STATUE_L = 1;
    public static int KUN_STATUE_R = 2;
    public static int KUN_STATUE_U = 3;
    public static int KUN_STATUE_F = 4;

    public static int ZA_WALL = 5;

    public static HashMap<Integer, Bitmap> kun = new HashMap<>();
    public static HashMap<Integer,List<ZhangAi>> G_ZA = new HashMap<>();

}
