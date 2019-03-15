package com.xmrxcaifu.gesture;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 手势password路径绘制
 */
public class GestureDrawline extends View {
    private int mov_x;// 声明起点坐标
    private int mov_y;
    private Paint paint;// 声明起点坐标
    private Canvas canvas;//  画布
    private Bitmap bitmap;// 位图
    private List<GesturePoint> list;//装有各个view坐标的集合
    private List<Pair<GesturePoint, GesturePoint>> lineList;// 记录画过的线�
    private Map<String, GesturePoint> autoCheckPointMap;// 自己主动选中的情况点�
    private boolean isDrawEnable = true; //  是否同意绘制�

    /**
     * 屏幕的宽度和高度�
     */
    private int[] screenDispaly;

    /**
     * 手指当前在哪个Point内
     */
    private GesturePoint currentPoint;
    /**
     * 手指当前在哪个Point内
     */
    private GestureCallBack callBack;

    /**
     * 用户当前绘制的图形password
     */
    private StringBuilder passWordSb;

    /**
     * 是否为校验
     */
    private boolean isVerify;

    /**
     * 用户传入的passWord
     */
    private String passWord;

    public GestureDrawline(Context context, List<GesturePoint> list, boolean isVerify,
                           String passWord, GestureCallBack callBack) {
        super(context);
        screenDispaly = AppUtil.getScreenDispaly(context);
        paint = new Paint();// 锟斤拷锟斤拷一锟斤拷锟斤拷锟斤拷
        try {

            bitmap = Bitmap.createBitmap(screenDispaly[0], screenDispaly[0], Bitmap.Config.ARGB_8888); // 锟设置位图的宽高
        } catch (OutOfMemoryError e) {
            // TODO: handle exception
        }
        canvas = new Canvas();
        canvas.setBitmap(bitmap);
        paint.setStyle(Style.STROKE);// 设置非填充�
//        paint.setStrokeWidth(5);// 笔宽5像素
        paint.setStrokeWidth(3);

        paint.setColor(Color.parseColor("#cca259"));//  设置默认连线颜色
        paint.setAntiAlias(true);// 锟斤拷锟斤拷示锟斤拷锟�

        this.list = list;
        this.lineList = new ArrayList<Pair<GesturePoint, GesturePoint>>();

        initAutoCheckPointMap();
        this.callBack = callBack;

        // 设置位图的宽高
        this.isVerify = isVerify;
        this.passWordSb = new StringBuilder();
        this.passWord = passWord;
    }

    private void initAutoCheckPointMap() {
        autoCheckPointMap = new HashMap<String, GesturePoint>();
        autoCheckPointMap.put("1,3", getGesturePointByNum(2));
        autoCheckPointMap.put("1,7", getGesturePointByNum(4));
        autoCheckPointMap.put("1,9", getGesturePointByNum(5));
        autoCheckPointMap.put("2,8", getGesturePointByNum(5));
        autoCheckPointMap.put("3,7", getGesturePointByNum(5));
        autoCheckPointMap.put("3,9", getGesturePointByNum(6));
        autoCheckPointMap.put("4,6", getGesturePointByNum(5));
        autoCheckPointMap.put("7,9", getGesturePointByNum(8));
    }

    private GesturePoint getGesturePointByNum(int num) {
        for (GesturePoint point : list) {
            if (point.getNum() == num) {
                return point;
            }
        }
        return null;
    }

    // 锟斤拷位图
    @Override
    protected void onDraw(Canvas canvas) {
        // super.onDraw(canvas);
        canvas.drawBitmap(bitmap, 0, 0, null);
    }

    // 锟斤拷锟斤拷锟铰硷拷
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isDrawEnable == false) {
            // 锟斤拷锟节诧拷锟斤拷锟斤拷锟斤拷锟�
            return true;
        }
        paint.setColor(Color.parseColor("#cca259"));// 锟斤拷锟斤拷默锟斤拷锟斤拷锟斤拷锟斤拷色
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mov_x = (int) event.getX();
                mov_y = (int) event.getY();
                // 锟叫断碉拷前锟斤拷锟斤拷锟轿伙拷锟斤拷谴锟斤拷锟斤拷母锟斤拷锟街拷锟�
                currentPoint = getPointAt(mov_x, mov_y);
                if (currentPoint != null) {
                    currentPoint.setPointState(Constants.POINT_STATE_SELECTED);
                    passWordSb.append(currentPoint.getNum());
                }
                // canvas.drawPoint(mov_x, mov_y, paint);// 锟斤拷锟斤拷
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                clearScreenAndDrawList();

                // 锟矫碉拷锟斤拷前锟狡讹拷位锟斤拷锟角达拷锟斤拷锟侥革拷锟斤拷锟斤拷
                GesturePoint pointAt = getPointAt((int) event.getX(), (int) event.getY());
                // 锟斤拷?前锟矫伙拷锟斤拷指锟斤拷锟节碉拷锟斤拷锟街�
                if (currentPoint == null && pointAt == null) {
                    return true;
                } else {// 锟斤拷锟斤拷没锟斤拷锟斤拷锟街革拷贫锟斤拷锟斤拷说锟斤拷锟�
                    if (currentPoint == null) {// 锟斤拷锟叫断碉拷前锟斤拷point锟角诧拷锟斤拷为null
                        // 锟斤拷锟轿拷眨锟斤拷锟矫达拷锟斤拷锟街革拷贫锟斤拷锟斤拷牡愀持碉拷锟絚urrentPoint
                        currentPoint = pointAt;
                        // 锟斤拷currentPoint锟斤拷锟斤拷锟斤拷锟斤拷锟窖★拷锟轿猼rue;
                        currentPoint.setPointState(Constants.POINT_STATE_SELECTED);
                        passWordSb.append(currentPoint.getNum());
                    }
                }
                if (pointAt == null || currentPoint.equals(pointAt) || Constants.POINT_STATE_SELECTED == pointAt.getPointState()) {
                    // 锟斤拷锟斤拷贫锟斤拷锟斤拷锟斤拷锟皆诧拷锟斤拷锟斤拷颍锟斤拷叩锟角帮拷锟斤拷锟侥碉拷锟诫当前锟狡讹拷锟斤拷锟侥碉拷锟轿伙拷锟斤拷锟酵拷锟斤拷锟斤拷叩锟角帮拷锟斤拷锟侥点处锟斤拷选锟斤拷状态
                    // 锟斤拷么锟皆碉拷前锟侥碉拷锟斤拷锟斤拷为锟斤拷悖拷锟斤拷锟街革拷贫锟轿伙拷锟轿拷盏慊拷锟�
                    canvas.drawLine(currentPoint.getCenterX(), currentPoint.getCenterY(), event.getX(), event.getY(), paint);// 锟斤拷锟斤拷
                } else {
                    // 锟斤拷锟角帮拷锟斤拷锟侥碉拷锟诫当前锟狡讹拷锟斤拷锟侥碉拷锟轿伙拷貌锟酵�
                    // 锟斤拷么锟斤拷前前锟斤拷锟斤拷锟斤拷锟轿拷锟姐，锟斤拷锟斤拷锟狡讹拷锟斤拷锟侥碉拷锟轿伙拷没锟斤拷锟�
                    canvas.drawLine(currentPoint.getCenterX(), currentPoint.getCenterY(), pointAt.getCenterX(), pointAt.getCenterY(), paint);// 锟斤拷锟斤拷
                    pointAt.setPointState(Constants.POINT_STATE_SELECTED);

                    // 锟叫讹拷锟角凤拷锟叫硷拷锟斤拷锟揭★拷锟�
                    GesturePoint betweenPoint = getBetweenCheckPoint(currentPoint, pointAt);
                    if (betweenPoint != null && Constants.POINT_STATE_SELECTED != betweenPoint.getPointState()) {
                        // 锟斤拷锟斤拷锟叫硷拷悴拷锟矫伙拷斜锟窖★拷锟�
                        Pair<GesturePoint, GesturePoint> pair1 = new Pair<GesturePoint, GesturePoint>(currentPoint, betweenPoint);
                        lineList.add(pair1);
                        passWordSb.append(betweenPoint.getNum());
                        Pair<GesturePoint, GesturePoint> pair2 = new Pair<GesturePoint, GesturePoint>(betweenPoint, pointAt);
                        lineList.add(pair2);
                        passWordSb.append(pointAt.getNum());
                        // 锟斤拷锟斤拷锟叫硷拷锟窖★拷锟�
                        betweenPoint.setPointState(Constants.POINT_STATE_SELECTED);
                        // 锟斤拷值锟斤拷前锟斤拷point;
                        currentPoint = pointAt;
                    } else {
                        Pair<GesturePoint, GesturePoint> pair = new Pair<GesturePoint, GesturePoint>(currentPoint, pointAt);
                        lineList.add(pair);
                        passWordSb.append(pointAt.getNum());
                        // 锟斤拷值锟斤拷前锟斤拷point;
                        currentPoint = pointAt;
                    }
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:// 锟斤拷锟斤拷指抬锟斤拷锟绞憋拷锟�
                if (isVerify) {
                    // 锟斤拷锟斤拷锟斤拷锟斤拷校锟斤拷
                    // 锟斤拷锟斤拷锟侥伙拷锟斤拷锟斤拷械锟斤拷撸锟街伙拷锟斤拷霞锟斤拷锟斤拷锟斤拷姹ｏ拷锟斤拷锟斤拷
                    if (passWord.equals(passWordSb.toString())) {
                        // 锟斤拷锟斤拷没锟斤拷锟斤拷频锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷氪拷锟斤拷锟斤拷锟斤拷锟斤拷同
                        callBack.checkedSuccess();

                    } else {
                        // 锟矫伙拷锟斤拷锟狡碉拷锟斤拷锟斤拷锟诫传锟斤拷锟斤拷锟斤拷氩煌拷锟�
                        callBack.checkedFail();
                        callBack.onGestureCodeInput(passWordSb.toString());
                    }
                } else {
                    callBack.onGestureCodeInput(passWordSb.toString());
                }
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onDetachedFromWindow() {
        // TODO Auto-generated method stub
        super.onDetachedFromWindow();
        bitmap.recycle();
    }

    /**
     * 指锟斤拷时锟斤拷去锟斤拷锟斤拷锟狡碉拷状态
     *
     * @param delayTime 锟接筹拷执锟斤拷时锟斤拷
     */
    public void clearDrawlineState(long delayTime) {
        if (delayTime > 0) {
            // 锟斤拷锟狡猴拷色锟斤拷示路锟斤拷
            isDrawEnable = false;
            drawErrorPathTip();
        }
        new Handler().postDelayed(new clearStateRunnable(), delayTime);
    }

    /**
     * 锟斤拷锟斤拷锟斤拷状态锟斤拷锟竭筹拷
     */
    final class clearStateRunnable implements Runnable {
        public void run() {
            // 锟斤拷锟斤拷passWordSb
            passWordSb = new StringBuilder();
            // 锟斤拷毡锟斤拷锟斤拷募锟斤拷锟�
            lineList.clear();
            // 锟斤拷锟铰伙拷锟狡斤拷锟斤拷
            clearScreenAndDrawList();
            for (GesturePoint p : list) {
                p.setPointState(Constants.POINT_STATE_NORMAL);
            }
            invalidate();
            isDrawEnable = true;
        }
    }

    /**
     * 通过点的位置去集合里面查找这个点是包括在哪个Point里面的
     *
     * @param x
     * @param y
     * @return 假设没有找到，则返回null。代表用户当前移动的地方属于点与点之间
     */
    private GesturePoint getPointAt(int x, int y) {

        for (GesturePoint point : list) {
            // 锟斤拷锟叫讹拷x
            int leftX = point.getLeftX();
            int rightX = point.getRightX();
            if (!(x >= leftX && x < rightX)) {
                // 锟斤拷锟轿拷伲锟斤拷锟斤拷锟斤拷锟揭伙拷锟斤拷员锟�
                continue;
            }

            int topY = point.getTopY();
            int bottomY = point.getBottomY();
            if (!(y >= topY && y < bottomY)) {
                // 锟斤拷锟轿拷伲锟斤拷锟斤拷锟斤拷锟揭伙拷锟斤拷员锟�
                continue;
            }

            // 锟斤拷锟街达拷械锟斤拷猓拷锟矫此碉拷锟斤拷锟角帮拷锟斤拷锟侥碉拷锟轿伙拷锟斤拷诒锟斤拷锟斤拷锟轿伙拷锟斤拷锟斤拷锟截凤拷
            return point;
        }

        return null;
    }

    private GesturePoint getBetweenCheckPoint(GesturePoint pointStart, GesturePoint pointEnd) {
        int startNum = pointStart.getNum();
        int endNum = pointEnd.getNum();
        String key = null;
        if (startNum < endNum) {
            key = startNum + "," + endNum;
        } else {
            key = endNum + "," + startNum;
        }
        return autoCheckPointMap.get(key);
    }

    /**
     * 锟斤拷锟斤拷锟侥伙拷锟斤拷锟斤拷械锟斤拷撸锟饺伙拷蠡锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
     */
    private void clearScreenAndDrawList() {
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        for (Pair<GesturePoint, GesturePoint> pair : lineList) {
            canvas.drawLine(pair.first.getCenterX(), pair.first.getCenterY(),
                    pair.second.getCenterX(), pair.second.getCenterY(), paint);// 锟斤拷锟斤拷
        }
    }

    /**
     * 校锟斤拷锟斤拷锟�锟斤拷锟轿伙拷锟狡诧拷一锟斤拷锟斤拷示
     */
    private void drawErrorPathTip() {
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        paint.setColor(Color.parseColor("#ff0505"));// 锟斤拷锟斤拷默锟斤拷锟斤拷路锟斤拷色
        for (Pair<GesturePoint, GesturePoint> pair : lineList) {
            pair.first.setPointState(Constants.POINT_STATE_WRONG);
            pair.second.setPointState(Constants.POINT_STATE_WRONG);
            canvas.drawLine(pair.first.getCenterX(), pair.first.getCenterY(),
                    pair.second.getCenterX(), pair.second.getCenterY(), paint);// 锟斤拷锟斤拷
        }
        invalidate();
    }


    public interface GestureCallBack {

        /**
         * 锟矫伙拷锟斤拷锟斤拷/锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
         */
        public abstract void onGestureCodeInput(String inputCode);

        /**
         * 锟斤拷锟斤拷没锟斤拷锟斤拷频锟斤拷锟斤拷锟斤拷氪拷锟斤拷锟斤拷锟斤拷锟斤拷同
         */
        public abstract void checkedSuccess();

        /**
         * 锟斤拷锟斤拷没锟斤拷锟斤拷频锟斤拷锟斤拷锟斤拷氪拷锟斤拷锟斤拷锟诫不锟斤拷同
         */
        public abstract void checkedFail();
    }

}
