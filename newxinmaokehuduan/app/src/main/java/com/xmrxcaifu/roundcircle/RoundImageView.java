package com.xmrxcaifu.roundcircle;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.xmrxcaifu.R;


/**
 * 閸﹀棗鑸癐mageView閿涘苯褰茬拋鍓х枂閺堬拷顧嬫稉銈勯嚋鐎硅棄瀹虫稉宥呮倱娑撴棃顤侀懝韫瑝閸氬瞼娈戦崷鍡楄埌鏉堣顢嬮妴锟�
 * 鐠佸墽鐤嗘０婊嗗閸︹杹ml鐢啫鐪弬鍥︽娑擃厾鏁遍懛顏勭暰娑斿鐫橀幀褔鍘ょ純顔煎棘閺佺増瀵氱�锟�
 */ 
public class RoundImageView extends ImageView { 
    private int mBorderThickness = 0; 
    private Context mContext; 
    private int defaultColor = 0xFFFFFFFF; 
    // 婵″倹鐏夐崣顏呮箒閸忔湹鑵戞稉锟介嚋閺堝锟介敍灞藉灟閸欘亞鏁炬稉锟介嚋閸﹀棗鑸版潏瑙勵攱 
    private int mBorderOutsideColor = 0; 
    private int mBorderInsideColor = 0; 
    // 閹貉傛姒涙顓婚梹瑁わ拷鐎癸拷
    private int defaultWidth = 0; 
    private int defaultHeight = 0; 
   
    public RoundImageView(Context context) { 
        super(context); 
        mContext = context; 
    } 
   
    public RoundImageView(Context context, AttributeSet attrs) { 
        super(context, attrs); 
        mContext = context; 
        setCustomAttributes(attrs); 
    } 
   
    public RoundImageView(Context context, AttributeSet attrs, int defStyle) { 
        super(context, attrs, defStyle); 
        mContext = context; 
        setCustomAttributes(attrs); 
    } 
   
    private void setCustomAttributes(AttributeSet attrs) { 
        TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.roundedimageview);
        mBorderThickness = a.getDimensionPixelSize(R.styleable.roundedimageview_border_thickness, 0);
        mBorderOutsideColor = a.getColor(R.styleable.roundedimageview_border_outside_color,defaultColor); 
        mBorderInsideColor = a.getColor(R.styleable.roundedimageview_border_inside_color, defaultColor); 
    } 
   
    @Override 
    protected void onDraw(Canvas canvas) { 
        Drawable drawable = getDrawable() ;  
        if (drawable == null) { 
            return; 
        } 
        if (getWidth() == 0 || getHeight() == 0) { 
            return; 
        } 
        this.measure(0, 0);
        if (drawable.getClass() == NinePatchDrawable.class) 
            return; 
        Bitmap b = ((BitmapDrawable) drawable).getBitmap(); 
        Bitmap bitmap = b.copy(Config.ARGB_8888, true);
        if (defaultWidth == 0) { 
            defaultWidth = getWidth(); 
        } 
        if (defaultHeight == 0) { 
            defaultHeight = getHeight(); 
        } 
        int radius = 0; 
        if (mBorderInsideColor != defaultColor && mBorderOutsideColor != defaultColor) {// 鐎规矮绠熼悽璁宠⒈娑擃亣绔熷鍡礉閸掑棗鍩嗘稉鍝勵檱閸﹀棜绔熷鍡楁嫲閸愬懎娓炬潏瑙勵攱 
            radius = (defaultWidth < defaultHeight ? defaultWidth : defaultHeight) / 2 - 2 * mBorderThickness; 
            // 閻㈣鍞撮崷锟�
            drawCircleBorder(canvas, radius + mBorderThickness / 2,mBorderInsideColor); 
            // 閻㈣顧囬崷锟�
            drawCircleBorder(canvas, radius + mBorderThickness + mBorderThickness / 2, mBorderOutsideColor); 
        } else if (mBorderInsideColor != defaultColor && mBorderOutsideColor == defaultColor) {// 鐎规矮绠熼悽璁崇娑擃亣绔熷锟�
            radius = (defaultWidth < defaultHeight ? defaultWidth : defaultHeight) / 2 - mBorderThickness; 
            drawCircleBorder(canvas, radius + mBorderThickness / 2, mBorderInsideColor); 
        } else if (mBorderInsideColor == defaultColor && mBorderOutsideColor != defaultColor) {// 鐎规矮绠熼悽璁崇娑擃亣绔熷锟�
            radius = (defaultWidth < defaultHeight ? defaultWidth : defaultHeight) / 2 - mBorderThickness; 
            drawCircleBorder(canvas, radius + mBorderThickness / 2, mBorderOutsideColor); 
        } else {// 濞屸剝婀佹潏瑙勵攱 
            radius = (defaultWidth < defaultHeight ? defaultWidth : defaultHeight) / 2; 
        } 
        Bitmap roundBitmap = getCroppedRoundBitmap(bitmap, radius); 
        canvas.drawBitmap(roundBitmap, defaultWidth / 2 - radius, defaultHeight / 2 - radius, null); 
    } 
   
    /** 
     * 閼惧嘲褰囩憗浣稿閸氬海娈戦崷鍡楄埌閸ュ墽澧�
     * @param radius閸楀﹤绶�
     */ 
    public Bitmap getCroppedRoundBitmap(Bitmap bmp, int radius) { 
        Bitmap scaledSrcBmp; 
        int diameter = radius * 2; 
        // 娑撹桨绨￠梼鍙夘剾鐎逛粙鐝稉宥囨祲缁涘绱濋柅鐘冲灇閸﹀棗鑸伴崶鍓у閸欐ê鑸伴敍灞芥礈濮濄倖鍩呴崣鏍毐閺傜懓鑸版稉顓烆樀娴滃簼鑵戦梻缈犵秴缂冾喗娓舵径褏娈戝锝嗘煙瑜般垹娴橀悧锟�
        int bmpWidth = bmp.getWidth(); 
        int bmpHeight = bmp.getHeight(); 
        int squareWidth = 0, squareHeight = 0; 
        int x = 0, y = 0; 
        Bitmap squareBitmap; 
        if (bmpHeight > bmpWidth) {// 妤傛ê銇囨禍搴☆啍 
            squareWidth = squareHeight = bmpWidth; 
            x = 0; 
            y = (bmpHeight - bmpWidth) / 2; 
            // 閹搭亜褰囧锝嗘煙瑜般垹娴橀悧锟�
            squareBitmap = Bitmap.createBitmap(bmp, x, y, squareWidth, squareHeight); 
        } else if (bmpHeight < bmpWidth) {// 鐎硅棄銇囨禍搴ㄧ彯 
            squareWidth = squareHeight = bmpHeight; 
            x = (bmpWidth - bmpHeight) / 2; 
            y = 0; 
            squareBitmap = Bitmap.createBitmap(bmp, x, y, squareWidth,squareHeight); 
        } else { 
            squareBitmap = bmp; 
        } 
        if (squareBitmap.getWidth() != diameter || squareBitmap.getHeight() != diameter) { 
            scaledSrcBmp = Bitmap.createScaledBitmap(squareBitmap, diameter,diameter, true); 
        } else { 
            scaledSrcBmp = squareBitmap; 
        } 
        Bitmap output = Bitmap.createBitmap(scaledSrcBmp.getWidth(), 
                scaledSrcBmp.getHeight(),  
                Config.ARGB_8888); 
        Canvas canvas = new Canvas(output); 
   
        Paint paint = new Paint(); 
        Rect rect = new Rect(0, 0, scaledSrcBmp.getWidth(),scaledSrcBmp.getHeight()); 
   
        paint.setAntiAlias(true); 
        paint.setFilterBitmap(true); 
        paint.setDither(true); 
        canvas.drawARGB(0, 0, 0, 0); 
        canvas.drawCircle(scaledSrcBmp.getWidth() / 2, 
                scaledSrcBmp.getHeight() / 2,  
                scaledSrcBmp.getWidth() / 2, 
                paint); 
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN)); 
        canvas.drawBitmap(scaledSrcBmp, rect, rect, paint); 
        bmp = null; 
        squareBitmap = null; 
        scaledSrcBmp = null; 
        return output; 
    } 
   
    /** 
     * 鏉堝湱绱悽璇叉妇 
     */ 
    private void drawCircleBorder(Canvas canvas, int radius, int color) { 
        Paint paint = new Paint(); 
        /* 閸樺鏁锟�/ 
        paint.setAntiAlias(true); 
        paint.setFilterBitmap(true); 
        paint.setDither(true); 
        paint.setColor(color); 
        /* 鐠佸墽鐤唒aint閻ㄥ嫨锟絪tyle閵嗭拷璐烻TROKE閿涙氨鈹栬箛锟�/ 
        paint.setStyle(Paint.Style.STROKE); 
        /* 鐠佸墽鐤唒aint閻ㄥ嫬顧囧鍡楊啍鎼达拷*/ 
        paint.setStrokeWidth(mBorderThickness); 
        canvas.drawCircle(defaultWidth / 2, defaultHeight / 2, radius, paint); 
    } 
}