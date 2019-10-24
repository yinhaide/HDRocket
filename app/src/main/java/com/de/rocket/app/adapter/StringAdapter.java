package com.de.rocket.app.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.de.rocket.Rocket;
import com.de.rocket.app.R;
import com.de.rocket.ue.injector.BindView;
import com.de.rocket.ue.injector.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * 文档的容器
 * Created by haide.yin(haide.yin@tcl.com) on 2019/10/16 10:19.
 */
public class StringAdapter extends RecyclerView.Adapter<StringAdapter.StringViewHolder> {

    private List<String> mList = new ArrayList<>();
    private Context context;

    public StringAdapter(Context context, List<String> mList) {
        this.context = context;
        if(mList != null){
            this.mList = mList;
        }
    }

    @NonNull
    @Override
    public StringViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_docs, viewGroup, false);
        return new StringViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull StringViewHolder viewHolder, int i) {
        viewHolder.tvTitle.setText(mList.get(i));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    /**
     * View容器
     */
    class StringViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_title)
        TextView tvTitle;

        StringViewHolder(View itemView) {
            super(itemView);
            //View注解
            Rocket.bindViewHolder(this,itemView);
        }

        /**
         * 1. 方法必须私有限定,
         * 2. 方法参数形式必须和type对应的Listener接口一致.
         * 3. 注解参数value支持数组: value={id1, id2, id3}
         **/
        @Event(R.id.rl_container)
        private void itermClick(View view) {//子项的点击
            onOnItemClickNext(tvTitle.getText().toString());
        }
    }

    /* ***************************** OnItemClick ***************************** */

    private OnOnItemClickListener onOnItemClickListener;

    // 接口类 -> OnOnItemClickListener
    public interface OnOnItemClickListener {
        void onOnItemClick(String content);
    }

    // 对外暴露接口 -> setOnOnItemClickListener
    public void setOnOnItemClickListener(OnOnItemClickListener onOnItemClickListener) {
        this.onOnItemClickListener = onOnItemClickListener;
    }

    // 内部使用方法 -> OnItemClickNext
    private void onOnItemClickNext(String content) {
        if (onOnItemClickListener != null) {
            onOnItemClickListener.onOnItemClick(content);
        }
    }
}
