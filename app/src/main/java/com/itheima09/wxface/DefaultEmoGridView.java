package com.itheima09.wxface;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by sanpi on 2016/6/15.
 */

public class DefaultEmoGridView extends GridView implements AdapterView.OnItemClickListener {

    private GridViewAdapter adapter;

    public DefaultEmoGridView(Context context) {
        this(context, null);
    }

    public DefaultEmoGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DefaultEmoGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //初始化
        setNumColumns(7);
        //设置数据源
        adapter = new GridViewAdapter();
        setAdapter(adapter);
        setOnItemClickListener(this);

    }

    private int mTotalPage;
    private int mCurrentPage;
    private int mStartResId;
    //每一页展示20个表情
    private int mPageSize = 20;

    public void initData(int totalPage, int currentPage) {
        mTotalPage = totalPage;
        mCurrentPage = currentPage;
        //计算第一个表情的资源id
        mStartResId = R.mipmap.smiley_00 + currentPage * mPageSize;
        adapter.notifyDataSetChanged();
    }

    public interface OnDefaultEmoItemClickListener {
        void onEmoItemClick(int emoResId, String resName);

        void onDelete();
    }

    public void setOnDefaultEmoItemClickListener(OnDefaultEmoItemClickListener onDefaultEmoItemClickListener) {
        this.onDefaultEmoItemClickListener = onDefaultEmoItemClickListener;
    }

    private OnDefaultEmoItemClickListener onDefaultEmoItemClickListener;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (onDefaultEmoItemClickListener != null) {
            int emoResId = mStartResId + position;
            String resName = "";
            //R.mipmap.smiley_01   resName = smiley_01
            int resPosition = mPageSize * mCurrentPage + position;//smiley_下划线后的数字
            if (resPosition < 10) {
                resName = "smiley_0" + position;
            } else {
                resName = "smiley_" + position;
            }
            if(position == 20){
                onDefaultEmoItemClickListener.onDelete();
            }else {
                onDefaultEmoItemClickListener.onEmoItemClick(emoResId, resName);
            }
        }

    }


    class GridViewAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            //20 + 1
            return 21;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView = new ImageView(parent.getContext());

            if (position == 20) {
                imageView.setImageResource(R.mipmap.del_btn_nor);
            } else {
                int resId = mStartResId + position;
                imageView.setImageResource(resId);
            }
            return imageView;
        }
    }
}
