package com.hqbb.duanzi.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.hqbb.duanzi.R;
import com.hqbb.duanzi.bean.support.BookMark;
import com.hqbb.easyadapterlibrary.abslistview.EasyLVAdapter;
import com.hqbb.easyadapterlibrary.abslistview.EasyLVHolder;

import java.util.List;

/**
 * @author yuyh.
 * @date 2016/11/18.
 */
public class BookMarkAdapter extends EasyLVAdapter<BookMark> {

    public BookMarkAdapter(Context context, List<BookMark> list) {
        super(context, list, R.layout.item_read_mark);
    }

    @Override
    public void convert(EasyLVHolder holder, int position, BookMark item) {
        TextView tv = holder.getView(R.id.tvMarkItem);

        SpannableString spanText = new SpannableString((position + 1) + ". " + item.title + ": ");
        spanText.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.light_coffee)),
                0, spanText.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

        tv.setText(spanText);

        if (item.desc != null) {
            tv.append(item.desc
                    .replaceAll("　", "")
                    .replaceAll(" ", "")
                    .replaceAll("\n", ""));
        }

    }
}
