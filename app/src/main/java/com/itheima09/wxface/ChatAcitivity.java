package com.itheima09.wxface;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sanpi on 2016/6/15.
 */

public class ChatAcitivity extends Activity implements DefaultEmoGridView.OnDefaultEmoItemClickListener, View.OnClickListener, ViewPager.OnPageChangeListener {

    private ViewPager emoPager;
    private EditText chatEdit;
    private ListView chatListView;
    private List<Message> messageList = new ArrayList<>();
    private ChatAdapter chatAdapter;
    private PagerIndicatorView pagerIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_acitivity);
        //获取显示表情的viewpager
        emoPager = (ViewPager) findViewById(R.id.emo_vp);
        emoPager.setAdapter(new EmoPagerAdapter());
        emoPager.addOnPageChangeListener(this);
        chatEdit = (EditText) findViewById(R.id.chat_edit);
        findViewById(R.id.send_btn).setOnClickListener(this);
        chatListView = (ListView) findViewById(R.id.chat_lv);
        //对话的数据源
        chatAdapter = new ChatAdapter();
        chatListView.setAdapter(chatAdapter);
        pagerIndicator = (PagerIndicatorView) findViewById(R.id.piv);
        pagerIndicator.initData(5, 0);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        pagerIndicator.notifiyDataSetChanged(5, position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    class ChatAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return messageList.size();
        }

        @Override
        public Object getItem(int position) {
            return messageList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //展示对话内容
            Message message = messageList.get(position);

            //将消息中代表表情的字符串替换成表情
            SpannableString spannableString = EmoParser.parseEmoStr(parent.getContext(), message.msg);

            View view = null;
            if (message.isFromMe) {
                view = View.inflate(parent.getContext(), R.layout.chat_me_item, null);
            } else {
                view = View.inflate(parent.getContext(), R.layout.chat_friend_item, null);

            }
            TextView msgTv = (TextView) view.findViewById(R.id.msg_tv);
            msgTv.setText(spannableString);
            return view;
        }
    }

    @Override
    public void onEmoItemClick(int emoResId, String resName) {
        int emoSize = (int) (25 * getResources().getDisplayMetrics().density);
        //点击表情item的回调
        String emoName = "[" + resName + "]";//加上中括号是为了，在消息展示时方便获取代表表情的字符串
        //可以实现图文混排的string
        SpannableString spannableString = new SpannableString(emoName);
        //加载对应的表情图片
        Drawable drawable = getResources().getDrawable(emoResId);
        //设置图片大小,不设置无法显示
        drawable.setBounds(0, 0, emoSize, emoSize);
        ImageSpan span = new ImageSpan(drawable);
        //将文字替换成图片
        spannableString.setSpan(span, 0, emoName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        //获取当前EditText中的内容
        Editable originalText = chatEdit.getText();
        //获取光标所在位置
        int selectionEnd = chatEdit.getSelectionEnd();
        if (selectionEnd < originalText.length()) {
            //光标在文字的中间
            originalText.insert(selectionEnd, spannableString);
        } else {
            originalText.append(spannableString);
        }

    }

    @Override
    public void onDelete() {
        //模拟系统的删除
        chatEdit.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
        chatEdit.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL));
    }

    @Override
    public void onClick(View v) {
        //从edittext上获取消息让listview展示
        String msgStr = chatEdit.getText().toString();
        if (!TextUtils.isEmpty(msgStr)) {
            Message msg = new Message(msgStr, true, Message.MessageType.Text);
            messageList.add(msg);
            chatAdapter.notifyDataSetChanged();
        }


    }

    class EmoPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            DefaultEmoGridView defaultEmoGridView = new DefaultEmoGridView(container.getContext());
            defaultEmoGridView.initData(5, position);
            defaultEmoGridView.setOnDefaultEmoItemClickListener(ChatAcitivity.this);
            //添加到容器
            container.addView(defaultEmoGridView);
            return defaultEmoGridView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

}
