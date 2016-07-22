package in.iheart.voicememo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import in.iheart.voicememo.util.DisplayUtil;

/**
 * 录音视图
 */
public class VoiceView extends View { //in.iheart.voicememo.view.VoiceView

    private Context mContext;

    private float LEFT_VOICE_CHANNEL_HEIGHT = 100F;
    private float RIGHT_VOICE_CHANNEL_HEIGHT = 100F;
    private float TIME_STEP_WIDTH = 20F;
    private float TIME_STEP_HEIGHT = 40F;

    private int mHeight;
    private int mWidth;

    private Paint mBackgroundPaint;
    private Paint mRedPaint;
    private Paint mGrayPaint;
    private Paint mBluePaint;
    private Paint mWhitePaint;


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

        mBackgroundPaint = new Paint();
        mBackgroundPaint.setAntiAlias(true);
        mBackgroundPaint.setColor(Color.BLACK);

        mRedPaint = new Paint();
        mRedPaint.setAntiAlias(true);
        mRedPaint.setColor(Color.RED);

        mGrayPaint = new Paint();
        mGrayPaint.setAntiAlias(true);
        mGrayPaint.setColor(Color.GRAY);

        mBluePaint = new Paint();
        mBluePaint.setAntiAlias(true);
        mBluePaint.setColor(Color.BLUE);

        mWhitePaint = new Paint();
        mWhitePaint.setAntiAlias(true);
        mWhitePaint.setColor(Color.WHITE);


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

    }

    private void drawGrayHoriLine(Canvas canvas) {
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
