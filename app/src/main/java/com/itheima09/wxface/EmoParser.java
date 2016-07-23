package com.itheima09.wxface;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sanpi on 2016/6/15.
 */

public class EmoParser {
    public static SpannableString parseEmoStr(Context context, String msg){
        int emoSize = (int) (25 * context.getResources().getDisplayMetrics().density);
        //[smiley_00]jsdljfls[smiley_23]fdf
        SpannableString spannableString = new SpannableString(msg);
        //用正则表达式找出代表表情的字符串
        Pattern pattern = Pattern.compile("\\[smiley_(.*?)\\]");
        Matcher matcher = pattern.matcher(msg);
        while(matcher != null && matcher.find()){
            //取出一个匹配结果
            String group = matcher.group();//[smiley_00]
            //获取资源的名字
            String resName = group.substring(1,group.length()-1);//smiley_00
            //通过资源的名字获取资源
            //参数1：资源的名字，参数2：资源的类型
            int id = context.getResources().getIdentifier(resName, "mipmap", context.getPackageName());
            if(id != 0){
                Drawable drawable = context.getResources().getDrawable(id);
                drawable.setBounds(0,0,emoSize,emoSize);
                ImageSpan span = new ImageSpan(drawable);
                spannableString.setSpan(span,matcher.start(),matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }


        return  spannableString;
    }
}
