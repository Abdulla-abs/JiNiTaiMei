package game.example.jntm.view.saoji;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.ArraySet;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Stream;

/**
 * 扫鸡自定义view容器
 */
public class Container extends ViewGroup implements View.OnClickListener {

    //单个格子的宽度
    private int childViewWidth = 0;
    //单个格子的高度
    private int childViewHeight = 0;

    private Paint paint;

    private boolean flagModel = false;

    //格子行数
    private final int rowSize = 10;
    //格子列数
    private final int columnSize = 10;
    //格子总数
    private final int childCount = rowSize * columnSize;

    //所有方格
    private final List<Grid> allGrid = new ArrayList<>();
    //雷方块
    private final List<Grid> allBoom = new ArrayList<>();

    //随机
    private final Random random = new Random();

    //炸弹数量
    private final int boomCount = 15;

    //回调监听器
    private OnSaoJiListener listener;

    public Container(Context context) {
        this(context, null);
    }

    public Container(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Container(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        //初始化位置
        final Pointer pointer = new Pointer(rowSize, columnSize);

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);

        //初始化格子
        for (int i = 0; i < childCount; i++) {
            final Grid grid = new Grid(getContext());
            //每个格子的点击事件
            grid.setOnClickListener(v -> {
                if (flagModel) {
                    if (grid.getPointer().isShow()) return;
                    grid.toggleFlag();
                    listener.onFlagChange();
                } else {
                    if (grid.isShowFlag()) {
                        Toast.makeText(getContext(), "当前格子已经被标记，取消标记后即可翻开", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (grid.getPointer().isShow()) return;
                    //如果格子是炸弹，直接结算
                    if (grid.isBoom()) {
                        //显示游戏结束
                        Toast.makeText(getContext(), "game over", Toast.LENGTH_SHORT).show();
                        //格子点击事件禁用，
                        disableGrid();
                        //游戏介绍 回调给ui界面
                        if (listener != null) {
                            listener.onBoom();
                        }
                    } else if (grid.isZero()) {//如果点击的格子周围炸弹数量是0
                        //遍历此格子周边所有为0的格子和此格子周边的格子并打开
                        search(Collections.singletonList(grid));
                    }
                    //设置此格子显示
                    grid.getPointer().setShow(true);
                    //格子打开 回调给ui界面
                    if (listener != null) {
                        listener.onShow();
                    }
                    //格子刷新
                    grid.invalidate();
                    //判断是否游戏结束
                    judgeGameIsWin();
                }
            });
            final Pointer localPointer = new Pointer(pointer);
            //格子设置位置
            grid.setPointer(localPointer);
            //再此容器当中添加此视图
            addView(grid);
            //记录这个格子
            allGrid.add(grid);
            //地址移动至下一位
            pointer.moveToNextPosition();
        }

        //初始化炸弹位置
        createBoom();

        //计算非炸弹格子周围的炸弹数量
        initEmptyGrid();
    }

    //the zero grid
    private final Set<Grid> allSearchListTempGrid = new ArraySet<>();
    //the round zero grid's grid
    private final List<Grid> roundGrid = new ArrayList<>();
    private int roundCount = 0;
    private int loopCount = 0;

    /**
     * 当点击一个炸弹计数为0的方块将会调用此方法，此方法用于遍历此方法周边所有的有炸弹计数的方块并显示
     * 此方法为递归
     * 构造一个需要递归周边方块的列表传入，递归出周边所有方块，检查这些方块是否是已经递归过的。未递归的方块则加入已递归队列当中
     *
     * @param waitSearchGrid 递归方块
     */
    private void search(List<Grid> waitSearchGrid) {
        Log.e("第" + (++roundCount) + "循环", "::");
        //当前搜索后下一次应该遍历的格子
        final List<Grid> Mi = new ArrayList<>();
        if (!waitSearchGrid.isEmpty()) {
            for (Grid grid : waitSearchGrid) {
                //检查周边8个格子是否为0.是则加入到下一次的遍历集合当中，并标记此格子已经检查过，下次不再遍历
                judgeGridIsZero(Mi, getGridOfPointer(grid.getPointer().getX() - 1, grid.getPointer().getY() - 1));
                judgeGridIsZero(Mi, getGridOfPointer(grid.getPointer().getX(), grid.getPointer().getY() - 1));
                judgeGridIsZero(Mi, getGridOfPointer(grid.getPointer().getX() + 1, grid.getPointer().getY() - 1));
                judgeGridIsZero(Mi, getGridOfPointer(grid.getPointer().getX() - 1, grid.getPointer().getY()));
                judgeGridIsZero(Mi, getGridOfPointer(grid.getPointer().getX() + 1, grid.getPointer().getY()));
                judgeGridIsZero(Mi, getGridOfPointer(grid.getPointer().getX() - 1, grid.getPointer().getY() + 1));
                judgeGridIsZero(Mi, getGridOfPointer(grid.getPointer().getX(), grid.getPointer().getY() + 1));
                judgeGridIsZero(Mi, getGridOfPointer(grid.getPointer().getX() + 1, grid.getPointer().getY() + 1));
                Log.e("第" + (roundCount) + "循环", "循环次数：" + (++loopCount));
            }
            loopCount = 0;
            //开始下一次遍历
            search(Mi);
        }
        Log.e("Hello", "");
        //遍历完成，清空搜索列表
        allSearchListTempGrid.clear();
        //打开0周边的格子
        for (Grid grid : roundGrid) {
            grid.getPointer().setShow(true);
            post(grid::invalidate);
        }
        //打开完毕，清空周边列表
        roundGrid.clear();
    }

    /**
     * 检查这个格子周边炸弹数量是否为0，是则加入到下一次遍历集合当中，并标记，下一次不再遍历
     * 如果炸弹数量不为0，添加至周边待翻开列表当中
     *
     * @param Mi   下一次遍历列表
     * @param grid 当前检查的格子
     */
    private void judgeGridIsZero(List<Grid> Mi, Grid grid) {
        //此格子不在已检查列表当中、不在当前遍历列表当中、不是已经显示的格子、不是炸弹
        if (grid != null && !allSearchListTempGrid.contains(grid) &&
                !Mi.contains(grid) && !grid.getPointer().isShow() && !grid.isBoom()) {
            //如果此格子周围炸弹数量为0
            if (grid.getRoundBoomCount() == 0) {
                //显示此格子
                grid.getPointer().setShow(true);
                post(grid::invalidate);
                //添加到检查列表当中
                allSearchListTempGrid.add(grid);
                //添加到下一次的遍历列表当中
                Mi.add(grid);
            } else {//此格子周围炸弹数量不为0
                //周边列表不包含此格子时）添加到周边列表当中
                if (!roundGrid.contains(grid)) {
                    roundGrid.add(grid);
                }
            }

        }
    }

    /**
     * 检查游戏是否胜利
     * 检查当前未翻开数量是否和炸弹数列相同
     * 如果相同 大概可能差不多应该 就是赢了
     */
    private void judgeGameIsWin() {
        //获取当前未翻开格子数量
        final long count = allGrid.stream()
                .filter(grid -> !grid.getPointer().isShow())
                .count();
        //判断
        if (count == boomCount) {
            //回调给主页
            if (listener != null) {
                listener.onFinish();
            }
            disableGrid();
        }

    }

    /**
     * 禁用格子
     */
    private void disableGrid() {
        for (Grid grid : allGrid) {
            grid.setClickable(false);
            grid.setFocusable(false);
            grid.setFocusableInTouchMode(false);
        }
    }

    /**
     * 初始化非炸弹格的格子，设置格子周围炸弹数量
     */
    private void initEmptyGrid() {
        for (Grid grid : allGrid) {
            if (!grid.isBoom()) {
                grid.setRoundBoomCount(getRoundBoomCount(grid));
            }
        }
    }

    /**
     * 获取格子周围炸弹数量
     * idea教我写代码
     *
     * @param grid 需要检查的格子
     * @return 周围的炸弹数量
     */
    private int getRoundBoomCount(Grid grid) {
        return (int) Stream.of(getGridOfPointer(grid.getPointer().getX() - 1, grid.getPointer().getY() - 1),
                        getGridOfPointer(grid.getPointer().getX(), grid.getPointer().getY() - 1),
                        getGridOfPointer(grid.getPointer().getX() + 1, grid.getPointer().getY() - 1),
                        getGridOfPointer(grid.getPointer().getX() - 1, grid.getPointer().getY()),
                        getGridOfPointer(grid.getPointer().getX() + 1, grid.getPointer().getY()),
                        getGridOfPointer(grid.getPointer().getX() - 1, grid.getPointer().getY() + 1),
                        getGridOfPointer(grid.getPointer().getX(), grid.getPointer().getY() + 1),
                        getGridOfPointer(grid.getPointer().getX() + 1, grid.getPointer().getY() + 1))
                .filter(roundGrid -> roundGrid != null && roundGrid.isBoom())
                .count();
    }

    /**
     * 随机炸弹位置
     */
    private void createBoom() {
        //更改  boomCount 大小以获得更"优质"的游戏体验

        while (allBoom.size() < 15) {
            final int x = random.nextInt(rowSize);
            final int y = random.nextInt(columnSize);
            final Grid randomGridPosition = getGridOfPointer(x, y);
            if (randomGridPosition != null && !allBoom.contains(randomGridPosition)) {
                randomGridPosition.setBoom(true);
                allBoom.add(randomGridPosition);
            }
        }
    }

    /**
     * 获取指定位置的格子
     *
     * @param x x
     * @param y y
     * @return 格子（可能为空
     */
    private Grid getGridOfPointer(int x, int y) {
        for (Grid grid : allGrid) {
            if (grid.getPointer().getX() == x && grid.getPointer().getY() == y) {
                return grid;
            }
        }
        return null;
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
            final Grid child = (Grid) getChildAt(i);
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
            final Grid child = (Grid) getChildAt(i);
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
        for (int i = 0; i < columnSize; i++) {
            canvas.drawLine(0, i * childViewHeight, getWidth(), i * childViewHeight, paint);
        }
        for (int i = 0; i < rowSize; i++) {
            canvas.drawLine(i * childViewWidth, 0, i * childViewWidth, getHeight(), paint);
        }
    }

    @Override
    public void onClick(View v) {

    }

    public void setListener(OnSaoJiListener listener) {
        this.listener = listener;
    }

    public void setFlagModel(boolean flagModel) {
        this.flagModel = flagModel;
    }

    public int getBoomCount() {
        return boomCount;
    }

    public int getFlagBoomCount() {
        int count = 0;
        for (Grid grid : allGrid) {
            if (grid != null && grid.isShowFlag()) {
                count++;
            }
        }
        return count;
    }
}
