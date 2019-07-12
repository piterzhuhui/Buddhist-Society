package uilayouttest.example.com.bigtask.AdapterNew;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import uilayouttest.example.com.bigtask.EasyLifeConstant;
import uilayouttest.example.com.bigtask.Entry.BaseMessageEntry;
import uilayouttest.example.com.bigtask.Entry.TextMessageEntry;
import uilayouttest.example.com.bigtask.Entry.VoiceMessageEntry;
import uilayouttest.example.com.bigtask.Interface.ItemClickListener;
import uilayouttest.example.com.bigtask.R;
import uilayouttest.example.com.bigtask.Views.BubbleLinearLayout;
import uilayouttest.example.com.bigtask.Views.BubbleTextView;

import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;


/**
 * Created by 朱慧 on 18-5-3.
 */

public class ChatRecyclerViewAdapter extends RecyclerView.Adapter {
    private List<BaseMessageEntry> datas;
    private ItemClickListener mItemClickListener;
    private ChatMessageRightViewHolder tempRightHolder;
    private ChatMessageLeftViewHolder tempLeftHolder;
    private Drawable myPortrait, friendsPortrait;
    private Context context;

    public ChatRecyclerViewAdapter(Context context, String username, String friendsusername) {

        JMessageClient.getUserInfo(username, new GetUserInfoCallback() {
            @Override
            public void gotResult(int i, String s, UserInfo userInfo) {
                userInfo.getAvatarBitmap(new GetAvatarBitmapCallback() {
                    @Override
                    public void gotResult(int i, String s, Bitmap bitmap) {
                        myPortrait = new BitmapDrawable(bitmap);
                        ChatRecyclerViewAdapter.this.notifyDataSetChanged();
                    }
                });
            }
        });

        if (friendsusername.equals(EasyLifeConstant.APPNAME)) {
            friendsPortrait = context.getResources().getDrawable(R.mipmap.ic_launcher);
            ChatRecyclerViewAdapter.this.notifyDataSetChanged();
        } else {
            JMessageClient.getUserInfo(friendsusername, new GetUserInfoCallback() {
                @Override
                public void gotResult(int i, String s, UserInfo userInfo) {
                    userInfo.getAvatarBitmap(new GetAvatarBitmapCallback() {
                        @Override
                        public void gotResult(int i, String s, Bitmap bitmap) {
                            friendsPortrait = new BitmapDrawable(bitmap);
                            ChatRecyclerViewAdapter.this.notifyDataSetChanged();

                        }
                    });
                }
            });
        }

    }

    public void setDatas(List<BaseMessageEntry> datas){
        this.datas=datas;
    }


    public void setOnItemClickListener(ItemClickListener itemClickListener){
        mItemClickListener=itemClickListener;
    }

    /**
     *
     * @param parent
     * @param viewType 这个viewType由getItemViewType获得，所以得重写这个函数
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case TextMessageEntry.RECEIVEMESSAGE:
                ChatMessageLeftViewHolder viewHolder=new ChatMessageLeftViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_left_item,null));
                return viewHolder;
            case TextMessageEntry.SENDMESSAGE:
                ChatMessageRightViewHolder viewHolder1=new ChatMessageRightViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_right_item,null));
                return viewHolder1;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position)==TextMessageEntry.SENDMESSAGE) {
            tempRightHolder = (ChatMessageRightViewHolder) holder;
                if (datas.get(position) instanceof TextMessageEntry){
                    TextMessageEntry tempMessage = (TextMessageEntry) datas.get(position);
                    tempRightHolder.bubble_textview.setVisibility(View.VISIBLE);
                    tempRightHolder.chat_right_bubble.setVisibility(View.GONE);
                    tempRightHolder.id_recorder_time_right.setVisibility(View.GONE);
                    //为Item设置监听所用
                    tempRightHolder.bubble_textview.setText(tempMessage.getContent());
                }else if(datas.get(position) instanceof VoiceMessageEntry){
                    VoiceMessageEntry tempMessage=(VoiceMessageEntry)datas.get(position);
                    tempRightHolder.bubble_textview.setVisibility(View.GONE);
                    tempRightHolder.id_recorder_time_right.setVisibility(View.VISIBLE);
                    tempRightHolder.chat_right_bubble.setVisibility(View.VISIBLE);
                    tempRightHolder.id_recorder_time_right.setText((int) tempMessage.getTime() + "");
                }

            tempRightHolder.iv_portrait.setImageDrawable(myPortrait);
            tempRightHolder.iv_portrait.setTag(position);



        }else if (getItemViewType(position)==TextMessageEntry.RECEIVEMESSAGE){
            tempLeftHolder = (ChatMessageLeftViewHolder) holder;
                if (datas.get(position) instanceof TextMessageEntry){
                    TextMessageEntry tempMessage = (TextMessageEntry) datas.get(position);
                    tempLeftHolder.bubble_textview.setVisibility(View.VISIBLE);
                    tempLeftHolder.chat_left_bubble.setVisibility(View.GONE);
                    tempLeftHolder.tv_recorder_time_left.setVisibility(View.GONE);
                    //为Item设置监听所用
                    tempLeftHolder.bubble_textview.setText(tempMessage.getContent());
                }else if (datas.get(position) instanceof VoiceMessageEntry){
                    VoiceMessageEntry tempMessage=(VoiceMessageEntry)datas.get(position);
                    tempLeftHolder.bubble_textview.setVisibility(View.GONE);
                    tempLeftHolder.chat_left_bubble.setVisibility(View.VISIBLE);
                    tempLeftHolder.tv_recorder_time_left.setText((int) tempMessage.getTime() + "");
                }
            tempLeftHolder.iv_portrait.setImageDrawable(friendsPortrait);
            tempLeftHolder.iv_portrait.setTag(position);



        }
    }

    @Override
    public int getItemViewType(int position) {
        return datas.get(position).getViewType();
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }


    private class ChatMessageLeftViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView iv_portrait;
        private BubbleTextView bubble_textview;
        private BubbleLinearLayout chat_left_bubble;
        private TextView tv_recorder_time_left;

        public ChatMessageLeftViewHolder(View itemView) {
            super(itemView);
            iv_portrait=(ImageView)itemView.findViewById(R.id.iv_chat_left_portrait);
            bubble_textview=(BubbleTextView)itemView.findViewById(R.id.bubble_chat_tv_left);
            chat_left_bubble=(BubbleLinearLayout)itemView.findViewById(R.id.chat_left_bubble);
            tv_recorder_time_left = (TextView) itemView.findViewById(R.id.id_recorder_time_left);
            //获取事件发生的会话对象

            //一定要记得加入这一句，才能成功实现监听
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener!=null){
                mItemClickListener.OnItemClick(v,(Integer) iv_portrait.getTag());
            }
        }
    }

    private class ChatMessageRightViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView iv_portrait;
        private BubbleTextView bubble_textview;
        private BubbleLinearLayout chat_right_bubble;
        private TextView id_recorder_time_right;

        public ChatMessageRightViewHolder(View itemView) {
            super(itemView);
            iv_portrait=(ImageView)itemView.findViewById(R.id.iv_chat_right_portrait);
            bubble_textview=(BubbleTextView)itemView.findViewById(R.id.bubble_tv_chat_right);
            chat_right_bubble=(BubbleLinearLayout)itemView.findViewById(R.id.chat_right_bubble);
            id_recorder_time_right=(TextView)itemView.findViewById(R.id.id_recorder_time_right);
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
