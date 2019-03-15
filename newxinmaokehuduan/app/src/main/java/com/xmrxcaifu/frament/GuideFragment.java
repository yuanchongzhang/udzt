package com.xmrxcaifu.frament;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;


import com.xmrxcaifu.BitmapUtils;
import com.xmrxcaifu.Constant;
import com.xmrxcaifu.MainActivity;

import com.xmrxcaifu.R;
import com.xmrxcaifu.util.MySharePreferenceUtil;


public class GuideFragment extends Fragment {

    private String type;
    private ImageView iv_v4_guide;
    private View view;
    private ImageView iv_v4_guide1;
    private RelativeLayout rl_v4_guide_bg;
    private Bitmap bitmap;
    private boolean isShow;

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    iv_v4_guide.setVisibility(View.VISIBLE);
                    Animation anim = AnimationUtils.loadAnimation(getActivity(),
                            R.anim.slide_in_from_bottom);
                    LinearInterpolator lir = new LinearInterpolator();
                    anim.setInterpolator(lir);
                    iv_v4_guide.startAnimation(anim);
                    break;
                case 2:
                    iv_v4_guide.setVisibility(View.GONE);
                    break;
            }
        }

        ;
    };

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getArguments().getString("tpye");

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (type.equals("1")) {
            view = inflater.inflate(R.layout.v4_guide_fragment, container, false);
            iv_v4_guide1 = (ImageView) view.findViewById(R.id.iv_v4_guide1);
            iv_v4_guide = (ImageView) view.findViewById(R.id.iv_v4_guide);
            iv_v4_guide.setImageBitmap(BitmapUtils.readBitMap2(getActivity(), R.drawable.native_1));

        } else if (type.equals("2")) {
            view = inflater.inflate(R.layout.v4_guide_fragment_two, container, false);
            iv_v4_guide1 = (ImageView) view.findViewById(R.id.iv_v4_guide1);
            iv_v4_guide = (ImageView) view.findViewById(R.id.iv_v4_guide);
            iv_v4_guide.setImageBitmap(BitmapUtils.readBitMap2(getActivity(), R.drawable.native_2));

        } else if (type.equals("3")) {
            view = inflater.inflate(R.layout.v4_guide_fragment_three, container, false);
            iv_v4_guide1 = (ImageView) view.findViewById(R.id.iv_v4_guide1);
            iv_v4_guide = (ImageView) view.findViewById(R.id.iv_v4_guide);
            iv_v4_guide.setImageBitmap(BitmapUtils.readBitMap2(getActivity(), R.drawable.native_3));

        } else if (type.equals("4")) {
            view = inflater.inflate(R.layout.v4_guide_fragment_four, container, false);
//			BbtApplication.getInstance().displayid(iv_v4_guide, R.drawable.native_4);
            iv_v4_guide = (ImageView) view.findViewById(R.id.iv_v4_guide);
            iv_v4_guide.setImageBitmap(BitmapUtils.readBitMap2(getActivity(), R.drawable.native_4));
//			iv_v4_guide.setImageResource(R.drawable.native_4);
//			rl_v4_guide_bg = (RelativeLayout) view.findViewById(R.id.rl_v4_guide_bg_4);
//			if (bitmap != null && !bitmap.isRecycled()) {
//	            BitmapDrawable bd = new BitmapDrawable(getResources(),bitmap);
//	            rl_v4_guide_bg.setBackgroundDrawable(bd);
//	        }
            iv_v4_guide1 = (ImageView) view.findViewById(R.id.iv_v4_guide1);
            iv_v4_guide.setOnClickListener(new OnClickListener() {
                public void onClick(View arg0) {
//                    startActivity(new Intent(getActivity(), MainActivity.class));

                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.putExtra("url", Constant.URL);
                    intent.putExtra("flage", "2");
                    startActivity(intent);
                    getActivity().finish();
                    MySharePreferenceUtil.put(getActivity(), "flag_guide", "3");

                }

            });
        }

        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void onDestroy() {
        super.onDestroy();
//		bitmap.recycle();
        System.gc();
    }

//	public void setUserVisibleHint(boolean isVisibleToUser) {
//		super.setUserVisibleHint(isVisibleToUser);
//		if (isVisibleToUser) {
//			handler.sendEmptyMessage(1);
//		} else {
//			// 相当于Fragment的onPause
//			handler.sendEmptyMessage(2);
//		}
//	}

    public static Fragment newInstance(String string) {
        GuideFragment uf = new GuideFragment();
        Bundle b = new Bundle();
        b.putString("tpye", string);
        uf.setArguments(b);
        return uf;
    }
}
