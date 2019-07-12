package uilayouttest.example.com.bigtask.AdapterNew;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import uilayouttest.example.com.bigtask.Entry.MessageEntry;
import uilayouttest.example.com.bigtask.Interface.ItemClickListener;
import uilayouttest.example.com.bigtask.R;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;

/**
 * Created by 朱慧 on 18-10-1.
 */

public class MessageRecyclerViewAdapter extends RecyclerView.Adapter {
    private List<MessageEntry> datas = new ArrayList<>();
    private ItemClickListener mItemClickListener;

    public void setDatas(List<MessageEntry> datas){
        this.datas=datas;
    }

    public void setOnItemClickListener(ItemClickListener itemClickListener){
        mItemClickListener=itemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MessageViewHolder viewHolder=new MessageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.message_list_item,null));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final MessageViewHolder tempHolder = (MessageViewHolder) holder;
        final MessageEntry tempMessage = datas.get(position);

        JMessageClient.getUserInfo(tempMessage.getUsername(), new GetUserInfoCallback() {
            @Override
            public void gotResult(int i, String s, UserInfo userInfo) {
                userInfo.getAvatarBitmap(new GetAvatarBitmapCallback() {
                    @Override
                    public void gotResult(int i, String s, Bitmap bitmap) {
                        tempHolder.iv_portrait.setImageBitmap(bitmap);
                    }
                });
                tempHolder.tv_title.setText(userInfo.getNickname());
            }
        });
        //为Item设置监听所用
        tempHolder.iv_portrait.setTag(position);
        tempHolder.tv_content.setText(tempMessage.getContent());
        tempHolder.tv_time.setText(tempMessage.getTime());
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }
    private class MessageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView iv_portrait;
        private TextView tv_title,tv_content,tv_time;

        public MessageViewHolder(View itemView) {
            super(itemView);
            iv_portrait=(ImageView)itemView.findViewById(R.id.iv_message_list_portrait);
            tv_title=(TextView)itemView.findViewById(R.id.tv_message_list_title);
            tv_content=(TextView)itemView.findViewById(R.id.tv_message_list_content);
            tv_time=(TextView)itemView.findViewById(R.id.tv_message_list_time);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener!=null){
                mItemClickListener.OnItemClick(v,(Integer) iv_portrait.getTag());
            }
        }
    }
}
