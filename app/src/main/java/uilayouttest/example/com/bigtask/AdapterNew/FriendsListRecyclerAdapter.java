package uilayouttest.example.com.bigtask.AdapterNew;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import uilayouttest.example.com.bigtask.DESUtil;
import uilayouttest.example.com.bigtask.Interface.ItemClickListener;
import uilayouttest.example.com.bigtask.R;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.model.UserInfo;
import uilayouttest.example.com.bigtask.Views.CircleImageView;

/**
 * Created by 朱慧 on 18-7-15.
 */

public class FriendsListRecyclerAdapter extends RecyclerView.Adapter {
    private List<UserInfo> datas;
    private ItemClickListener mItemClickListener;

    public void setOnItemClickListener(ItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public void setDatas(List<UserInfo> datas) {
        this.datas = datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FriendsViewHolder viewHolder = new FriendsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.friends_list_item, null));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final FriendsViewHolder friendsViewHolder = (FriendsViewHolder) holder;
        UserInfo userInfo = datas.get(position);
//        TODO:更改头像
        userInfo.getAvatarBitmap(new GetAvatarBitmapCallback() {
            @Override
            public void gotResult(int i, String s, Bitmap bitmap) {
                friendsViewHolder.iv_portrait.setImageBitmap(bitmap);
            }
        });
        friendsViewHolder.iv_portrait.setTag(position);
//        这里解密
        try {
            String b = DESUtil.decrypt(userInfo.getUserName(), DESUtil.key);
            System.out.println("解密后的数据:" + b);
            friendsViewHolder.tv_username.setText(b);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (datas == null) {
            datas = new ArrayList<>();
        }
        return datas.size();
    }


    private class FriendsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private CircleImageView iv_portrait;
        private TextView tv_username;

        public FriendsViewHolder(View itemView) {
            super(itemView);
            iv_portrait = (CircleImageView) itemView.findViewById(R.id.iv_friends_list_portrait);
            tv_username = (TextView) itemView.findViewById(R.id.tv_friends_list_username);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.OnItemClick(v, (Integer) iv_portrait.getTag());
            }
        }
    }

}
