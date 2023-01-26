package game.example.jntm.view.tanchiji;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import game.example.jntm.R;


public class SnackView extends ViewGroup {

    //单个格子的宽度
    private int childViewWidth = 0;
    //单个格子的高度
    private int childViewHeight = 0;

    private Paint paint;
    //格子行数
    private int rowSize = 10;
    //格子列数
    private int columnSize = 10;
    //格子总数
    private final int childCount = rowSize * columnSize;

    //方向类型
    public static final int ACTION_LEFT = 0;
    public static final int ACTION_RIGHT = 1;
    public static final int ACTION_TOP = 2;
    public static final int ACTION_DOWN = 3;
    //当前方向，默认向下移动
    private int action = ACTION_DOWN;

    //初始蛇长
    public final static int snakeLong = 5;
    //所有方格
    private final List<GridView> allGrid = new ArrayList<>();
    //蛇
    private final LinkedList<Pointer> snake = new LinkedList<>();

    //运行当中？
    private boolean isRun = false;
    //定时器
    private Timer timer;

    //随机
    private final Random random = new Random();

    //当前的食物格子
    private GridView foodGrid;

    //当前蛇位置的上一次移动的蛇的最后位置
    private Pointer snackBeforeLastPointer;

    //蛇监听
    private SnakeListener snakeListener;
    private Handler handler;

    public static Bitmap j;
    public static Bitmap kun;

    public SnackView(Context context) {
        super(context);
        init();
    }

    public SnackView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SnackView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    //准备
    private void init() {
        j = BitmapFactory.decodeResource(getResources(), R.drawable.j);
        kun = BitmapFactory.decodeResource(getResources(),R.drawable.kun_avatar);

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);

//        final Matrix matrix = new Matrix();
//        matrix.postRotate(180);
//
//        j = Bitmap.createBitmap(j,0,0,j.getWidth(),j.getHeight(),matrix,true);
//        kun = Bitmap.createBitmap(kun,0,0,kun.getWidth(),kun.getHeight(),matrix,true);

        //初始化位置
        final Pointer pointer = new Pointer(rowSize, columnSize);
        //行格一半的位置（初始蛇的横坐标
        final int halfRow = rowSize / 2;
        //开始以全部格子大小开始遍历，将每个格子的Pointer进行赋值
        for (int i = 0; i < childCount; i++) {
            //创建格子
            final GridView childView = new GridView(getContext());
            //创建位置
            final Pointer localPointer = new Pointer(pointer);
            //将位置放进格子
            childView.setPointer(localPointer);
            //给个位置
            childView.setIndex(i);
            //在当前容器当中增加这个格子
            addView(childView);
            //记录下来这个格子
            allGrid.add(childView);
            //除却第一行，从第二行开始将行中间的格子作为蛇的身体，身体长度取决于snackLong（预先定义的蛇长
            if (i % halfRow == 0 && localPointer.getX() != 0 && snake.size() < snakeLong) {
                //默认蛇头应该是向下，因此此遍历翻转顺序（addFirst
                snake.addFirst(localPointer);
            }
            //位置移动至下一个位置
            pointer.moveToNextPosition();
        }
        //随机食物位置
        createFood();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //计算格子宽度
        childViewWidth = getMeasuredWidth() / rowSize;
        //计算格子长度
        childViewHeight = getMeasuredHeight() / columnSize;

        //遍历所有格子，将长宽设置给格子
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final GridView child = (GridView) getChildAt(i);
            final LayoutParams layoutParams = child.getLayoutParams();
            layoutParams.height = childViewHeight;
            layoutParams.width = childViewWidth;
            child.setLayoutParams(layoutParams);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //遍历所有格子，设置格子位置
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            //拿到格子
            final GridView child = (GridView) getChildAt(i);
            //拿到格子坐标
            final Pointer pointer = child.getPointer();
            //格子左坐标是 格子位置*位置
            int left = pointer.getX() * childViewWidth;
            //格子右坐标是 格子左位置+格子宽度
            int right = left + childViewWidth;
            //同上
            int top = pointer.getY() * childViewHeight;
            int bottom = top + childViewHeight;
            //布局设置
            child.layout(left, top, right, bottom);
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        for (int i = 0; i < columnSize+1; i++) {
            canvas.drawLine(0, i * childViewHeight, getWidth(), i * childViewHeight, paint);
        }
        for (int i = 0; i < rowSize+1; i++) {
            canvas.drawLine(i * childViewWidth, 0, i * childViewWidth, getHeight(), paint);
        }
    }

    /**
     * 启动
     */
    public void run() {
        timer = new Timer();
        //定时任务，1s后开始，每秒执行4次
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //运行标记位
                if (!isRun) {
                    isRun = true;
                }
                //当前动作（将蛇移动至下一个位置，其实就是计算出蛇头位置并移除最后的尾部
                switch (action) {
                    //计算蛇头位置并增加到队列第一位，移除最后一位，记录此次移除位置
                    case ACTION_LEFT://左移
                        //左移即蛇头位置x坐标-1，y轴保持不变。其他同此
                        snake.addFirst(getPointerOfPointerAt(snake.getFirst().getX() - 1, snake.getFirst().getY()));
                        snackBeforeLastPointer = snake.getLast();
                        snake.removeLast();
                        break;
                    case ACTION_RIGHT:
                        snake.addFirst(getPointerOfPointerAt(snake.getFirst().getX() + 1, snake.getFirst().getY()));
                        snackBeforeLastPointer = snake.getLast();
                        snake.removeLast();
                        break;
                    case ACTION_TOP:
                        snake.addFirst(getPointerOfPointerAt(snake.getFirst().getX(), snake.getFirst().getY() - 1));
                        snackBeforeLastPointer = snake.getLast();
                        snake.removeLast();
                        break;
                    case ACTION_DOWN:
                        snake.addFirst(getPointerOfPointerAt(snake.getFirst().getX(), snake.getFirst().getY() + 1));
                        snackBeforeLastPointer = snake.getLast();
                        snake.removeLast();
                        break;
                }

                //判断蛇头是否在蛇身上
                final Pointer snakeHead = snake.getFirst();
                for (int i = 1; i < snake.size(); i++) {
                    if (snakeHead.equals(snake.get(i))) {
                        //游戏结束
                        timer.cancel();
                        if (snakeListener != null && handler != null) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    snakeListener.onSnakeDie(snake);
                                }
                            });
                        }
                    }
                }
                //冒泡判断 蛇身是否在蛇身上
//                for (int i = 0; i < snake.size(); i++) {
//                    for (int j = 1; j < snake.size() - i; j++) {
//                        if (i != j && snake.get(i).equals(snake.get(j))) {
//                            游戏结束
//                            timer.cancel();
//                        }
//                    }
//                }
                //判断当前蛇头是否覆盖了食物位置，如果覆盖，蛇长+1，增加的位置是上面记录的移除位置。完成后随机出下一个食物位置
                if (snakeHead.equals(foodGrid.getPointer())) {
                    if (snackBeforeLastPointer != null) {
                        snake.addLast(snackBeforeLastPointer);
                        if (snakeListener != null && handler != null) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    snakeListener.onSnakeLongAdd(snake, snake.size());
                                }
                            });
                        }
                        createFood();
                    }
                }

                //遍历所有格子，判断这个格子是不是蛇身，以标记位重绘格子颜色
                for (GridView gridView : allGrid) {
                    if (gridView.getPointer().equals(snake.getFirst())) {
                        gridView.setSnackHead(true);
                    } else if (snake.contains(gridView.getPointer())) {
                        gridView.setSnackBody(true);
                    } else {
                        gridView.setNone();
                    }
                    post(new Runnable() {
                        @Override
                        public void run() {
                            gridView.invalidate();
                        }
                    });
                }
            }
        }, 1000, 1000 / 4);
    }

    private Pointer getPointerOfPointerAt(Pointer pointer) {
        int index = 0;
        for (int i = 0; i < allGrid.size(); i++) {
            if (allGrid.get(i).getPointer().equals(pointer)) {
                index = i;
                break;
            }
        }
        return allGrid.get(index).getPointer();
    }

    /**
     * 通过x，y拿到格子的位置，返回的pointer是原生位置，而不是new的pointer
     *
     * @param x 横坐标
     * @param y 纵坐标
     * @return 格子的位置
     */
    private Pointer getPointerOfPointerAt(int x, int y) {
        //新建一个位置方便判断
        Pointer pointer = new Pointer(rowSize, columnSize, x, y);
        //如果x右移后位置超过右边界（说明x超出长度，重设0
        if (x == rowSize) {
            pointer.setX(0);
            pointer = new Pointer(pointer);
        }
        //如果y下移位置超过下边界（说明y超出长度，重设0
        if (y == columnSize) {
            pointer.setY(0);
            pointer = new Pointer(pointer);
        }
        //如果x左移后位置超出左边界，设置为右边界
        if (x < 0) {
            pointer.setX(rowSize - 1);
            pointer = new Pointer(pointer);
        }
        if (y < 0) {
            pointer.setY(columnSize - 1);
            pointer = new Pointer(pointer);
        }

        int index = 0;
        //遍历所有格子，拿到对应的位置的格子，返回这个格子
        for (int i = 0; i < allGrid.size(); i++) {
            if (allGrid.get(i).getPointer().equals(pointer)) {
                index = i;
                break;
            }
        }
        return allGrid.get(index).getPointer();
    }

    /**
     * 通过x,y获得格子位置
     *
     * @param x 横坐标
     * @param y 纵坐标
     * @return 对应位置格子
     */
    private GridView getChildViewOfPointer(int x, int y) {
        Pointer pointer = new Pointer(rowSize, columnSize, x, y);
        for (int i = 0; i < allGrid.size(); i++) {
            if (allGrid.get(i).getPointer().equals(pointer)) {
                return allGrid.get(i);
            }
        }
        return null;
    }

    /**
     * 生成食物
     */
    private void createFood() {
        //随机x，y
        final int x = random.nextInt(rowSize);
        final int y = random.nextInt(columnSize);
        //创建这个坐标
        final Pointer randomPointer = new Pointer(rowSize, columnSize, x, y);
        //如果当前食物格子为空，直接拿对应位置
        if (foodGrid == null) {
            foodGrid = getChildViewOfPointer(x, y);
        } else if (!foodGrid.getPointer().equals(randomPointer)) {//如果这次不是随机出来一模一样的位置
            foodGrid = getChildViewOfPointer(x, y);
        } else {//食物不为空且随机出来一模一样的位置，重新拿一遍
            createFood();
        }

        assert foodGrid != null;
        //如果食物位置就是蛇身，重新拿一遍
        if (snake.contains(foodGrid.getPointer())) {
            createFood();
        }

        foodGrid.setFood(true);
        post(new Runnable() {
            @Override
            public void run() {
                foodGrid.invalidate();
            }
        });
    }

    public boolean isRun() {
        return isRun;
    }

    public void stop() {
        timer.cancel();
        isRun = false;
    }

    /**
     * 设置当前方向（不能原地掉头，即如果往下走，就不能直接往上
     *
     * @param action 方向
     */
    public void setAction(int action) {
        if ((this.action == ACTION_TOP && action == ACTION_DOWN) || (this.action == ACTION_DOWN && action == ACTION_TOP)
                || (this.action == ACTION_LEFT && action == ACTION_RIGHT) || (this.action == ACTION_RIGHT && action == ACTION_LEFT)) {
            return;
        }

        this.action = action;
    }

    public void setSnakeListener(SnakeListener snakeListener, Handler handler) {
        this.snakeListener = snakeListener;
        this.handler = handler;
    }
}
