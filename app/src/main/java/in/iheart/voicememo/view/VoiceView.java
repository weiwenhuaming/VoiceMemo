package in.iheart.voicememo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import in.iheart.voicememo.util.DisplayUtil;

/**
 * 录音视图
 */
public class VoiceView extends View { //in.iheart.voicememo.view.VoiceView

    private Context mContext;

    /**
     * 蓝色时间线模式(初始化,移动,中间三种模式)
     **/
    private static final int INIT_MODE_TIME_LINE = 0;
    private static final int MOVE_MODE_TIME_LINE = 1;
    private static final int MIDDLE_MODE_TIME_LINE = 2;

    /**
     * 移动速度
     */
    private static final int MOVE_STEP_SPEED = 5;

    /**
     * 左声道高度
     **/
    private float LEFT_VOICE_CHANNEL_HEIGHT = 100F;
    /**
     * 右声道高度
     **/
    private float RIGHT_VOICE_CHANNEL_HEIGHT = 100F;
    /**
     * 时间刻度宽度
     **/
    private float TIME_STEP_WIDTH = 20F;
    /**
     * 时间刻度高度
     **/
    private float TIME_STEP_HEIGHT = 40F;

    private int mHeight;
    private int mWidth;
    /**
     * 声道刻度高度
     **/
    private int mDialHeight;
    /**
     * 声道刻度外部偏移量
     **/
    private int mDialOffset;
    /**
     * 时间刻度高度(时间指针高度)
     **/
    private int mTimeDialHeight;
    /**
     * 声道刻度字体大小
     **/
    private int mVoiceDialSize;
    /**
     * 时间刻度字体大小
     **/
    private int mTimeDialSize;

    /**
     * 时间线模式
     */
    private int mTimeLineMode = MOVE_MODE_TIME_LINE;


    private boolean isInitFinish;

    /**
     * 时间线步进
     */
    private int mTimeLineStepOffset;
    /**
     * 时间刻度步进
     */
    private int mTimeDialStepOffset;

    /**
     * 时间记录
     */
    private int mTimeHistory;


    private Paint mBackgroundPaint;
    private Paint mRedPaint;
    private Paint mGrayPaint;
    private Paint mBluePaint;
    private Paint mWhitePaint;
    private TextPaint mTextPaint;


    public VoiceView(Context context) {
        super(context);
        initView(context);
    }

    public VoiceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public VoiceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public VoiceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;

        mHeight = DisplayUtil.getHeight(context);
        mWidth = DisplayUtil.getWidth(context);

        LEFT_VOICE_CHANNEL_HEIGHT = DisplayUtil.dip2pxF(mContext, LEFT_VOICE_CHANNEL_HEIGHT);
        RIGHT_VOICE_CHANNEL_HEIGHT = DisplayUtil.dip2pxF(mContext, RIGHT_VOICE_CHANNEL_HEIGHT);
        TIME_STEP_WIDTH = DisplayUtil.dip2pxF(mContext, TIME_STEP_WIDTH);
        TIME_STEP_HEIGHT = DisplayUtil.dip2pxF(mContext, TIME_STEP_HEIGHT);

        mDialOffset = DisplayUtil.dip2px(mContext, 10);
        mDialHeight = (int) ((LEFT_VOICE_CHANNEL_HEIGHT - mDialOffset) / 7);

        mVoiceDialSize = DisplayUtil.dip2px(mContext, 8);
        mTimeDialSize = DisplayUtil.dip2px(mContext, 12);
        mTimeDialHeight = DisplayUtil.dip2px(mContext, 5);

        mBackgroundPaint = new Paint();
        mBackgroundPaint.setAntiAlias(true);
        mBackgroundPaint.setColor(Color.BLACK);

        mRedPaint = new Paint();
        mRedPaint.setAntiAlias(true);
        mRedPaint.setColor(Color.RED);

        mGrayPaint = new Paint();
        mGrayPaint.setAntiAlias(true);
        mGrayPaint.setColor(Color.GRAY);
        mGrayPaint.setStrokeWidth(DisplayUtil.dip2px(mContext, 1));

        mBluePaint = new Paint();
        mBluePaint.setAntiAlias(true);
        mBluePaint.setColor(Color.parseColor("#0074ED"));
        mBluePaint.setStrokeWidth(DisplayUtil.dip2px(mContext, 1));

        mWhitePaint = new Paint();
        mWhitePaint.setAntiAlias(true);
        mWhitePaint.setColor(Color.WHITE);

        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(mWidth, mHeight);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawGrayHoriLine(canvas);
        drawVoiceChannelDial(canvas);

        drawTimeDial(canvas);
        drawCurrentTimeLine(canvas);
        drawTimeByDial(canvas);

        invalidate();
    }

    /**
     * 绘制时间刻度值
     *
     * @param canvas
     */
    private void drawTimeByDial(Canvas canvas) {
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(mTimeDialSize);
        float height = DisplayUtil.getTextHeight(mTextPaint);
        //文字偏移量:起始偏移1.5的TIME_STEP_WIDTH,文字偏移量是低时间指针
        float startOffset = TIME_STEP_WIDTH * 1.5F + mTimeDialHeight - mTimeDialStepOffset;
        //每次绘制屏幕宽度容纳的数量加上5
        int times = (int) (mWidth / TIME_STEP_WIDTH + 5);
        canvas.save();
        for (int i = 0; i < times; i++) {
            if (i % 4 == 0) {
                //在时间历史基础上,向后延续时间
                int temp = mTimeHistory + i / 4;
                String text = DisplayUtil.getTimeByPosition(temp);
                canvas.drawText(text, TIME_STEP_WIDTH * i + startOffset, mTimeDialHeight * 4 + height, mTextPaint);
            }
        }
        canvas.restore();
    }

    /**
     * 绘制当前时间点上的标记线
     *
     * @param canvas
     */
    private void drawCurrentTimeLine(Canvas canvas) {
        //起始偏移1.5的TIME_STEP_WIDTH
        float startOffset = TIME_STEP_WIDTH * 1.5F;
        if (mTimeLineMode == MIDDLE_MODE_TIME_LINE) {
            startOffset = mWidth / 2;
            mTimeDialStepOffset += MOVE_STEP_SPEED;
            //每四个时间刻度就重新绘制
            if (mTimeDialStepOffset >= TIME_STEP_WIDTH * 4) {
                mTimeDialStepOffset = 0;
                //记录时间,因为时间是每四个刻度加一秒,所以每次重新绘制时自增
                mTimeHistory++;
            }
        }
        if (mTimeLineMode == MOVE_MODE_TIME_LINE) {
            startOffset += (mTimeLineStepOffset += MOVE_STEP_SPEED);
            if ((int) (startOffset + 0.5F) >= mWidth / 2) {
                //改变时间线模式,并同步使时间刻度动起来
                mTimeLineMode = MIDDLE_MODE_TIME_LINE;
            }
        }
        canvas.save();
        //绘制中间线
        canvas.drawLine(startOffset, TIME_STEP_HEIGHT, startOffset, TIME_STEP_HEIGHT + LEFT_VOICE_CHANNEL_HEIGHT + RIGHT_VOICE_CHANNEL_HEIGHT, mBluePaint);
        //绘制上下两端圆形
        float radius = mTimeDialHeight * 1.5F / 2;
        canvas.drawCircle(startOffset, TIME_STEP_HEIGHT - radius, radius, mBluePaint);
        canvas.drawCircle(startOffset, TIME_STEP_HEIGHT + LEFT_VOICE_CHANNEL_HEIGHT + RIGHT_VOICE_CHANNEL_HEIGHT + radius, radius, mBluePaint);
        canvas.restore();
    }

    /**
     * 绘制时间刻度
     *
     * @param canvas
     */
    private void drawTimeDial(Canvas canvas) {
        mGrayPaint.setAlpha(200);
        //起始偏移2.5的TIME_STEP_WIDTH
        float offsetX = -(TIME_STEP_WIDTH * 2.5F + mTimeDialStepOffset);
        int times = (int) (mWidth / TIME_STEP_WIDTH + 10);
        canvas.save();
        for (int i = 0; i < times; i++) {
            float startX = TIME_STEP_WIDTH * i + offsetX;
            float startY = TIME_STEP_HEIGHT - mTimeDialHeight;
            float stopX = startX;
            float stopY = TIME_STEP_HEIGHT;
            if (i % 4 == 0)
                startY = TIME_STEP_HEIGHT - mTimeDialHeight * 4;
            canvas.drawLine(startX, startY, stopX, stopY, mGrayPaint);
        }
        canvas.restore();
    }

    /**
     * 绘制声道音量刻度
     *
     * @param canvas
     */
    private void drawVoiceChannelDial(Canvas canvas) {
        mTextPaint.setColor(Color.GRAY);
        mTextPaint.setTextSize(mVoiceDialSize);

        int height = mDialHeight - DisplayUtil.getTextHeight(mTextPaint);

        canvas.save();
        for (int i = 0; i < 7; i++) {
            int temp = i;
            if (temp == 6)
                temp = 10;
            if (temp == 5)
                temp = 7;
            if (temp == 4)
                temp = 5;
            String text = temp - temp * 2 + "";
            //以声道中间横线为界,向上下两边绘制刻度
            float leftY = TIME_STEP_HEIGHT + LEFT_VOICE_CHANNEL_HEIGHT - mDialHeight * (7 - i) + height;
            float rightY = TIME_STEP_HEIGHT + LEFT_VOICE_CHANNEL_HEIGHT + mDialHeight * (7 - i);

            float x = mWidth - mTextPaint.measureText(text) - TIME_STEP_WIDTH / 3;

            canvas.drawText(text, x, leftY, mTextPaint);
            canvas.drawText(text, x, rightY, mTextPaint);
        }
        canvas.restore();
    }

    /**
     * 绘制声道分割线
     *
     * @param canvas
     */
    private void drawGrayHoriLine(Canvas canvas) {
        mGrayPaint.setAlpha(255);
        canvas.save();
        //绘制第一条横线
        canvas.drawLine(0, TIME_STEP_HEIGHT, mWidth, TIME_STEP_HEIGHT, mGrayPaint);
        //绘制第二条横线
        canvas.drawLine(0, TIME_STEP_HEIGHT + LEFT_VOICE_CHANNEL_HEIGHT, mWidth, TIME_STEP_HEIGHT + LEFT_VOICE_CHANNEL_HEIGHT, mGrayPaint);
        //绘制第三条横线
        canvas.drawLine(0, TIME_STEP_HEIGHT + LEFT_VOICE_CHANNEL_HEIGHT + RIGHT_VOICE_CHANNEL_HEIGHT, mWidth, TIME_STEP_HEIGHT + LEFT_VOICE_CHANNEL_HEIGHT + RIGHT_VOICE_CHANNEL_HEIGHT, mGrayPaint);
        canvas.restore();
    }
}
