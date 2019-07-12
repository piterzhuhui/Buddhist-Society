package uilayouttest.example.com.bigtask.AdapterNew;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;


import java.util.ArrayList;

import uilayouttest.example.com.bigtask.Activity.HomeTeaCeremonyDetailActivity;
import uilayouttest.example.com.bigtask.Entity.TeaCeremony;
import uilayouttest.example.com.bigtask.R;

/**
 * Created by 朱慧 on 2019/2/26.
 */

public class TeaCeremonyAdapter extends RecyclerView.Adapter<TeaCeremonyAdapter.myViewHodler>{

    Bundle bundle ;
    private Context context;
    private ArrayList<TeaCeremony> teaCeremoniesEntityList;


    //创建构造函数
    public TeaCeremonyAdapter(Context context, ArrayList<TeaCeremony> teaCeremoniesEntityList) {
        //将传递过来的数据，赋值给本地变量
        this.context = context;//上下文
        this.teaCeremoniesEntityList = teaCeremoniesEntityList;//实体类数据ArrayList
    }

    /**
     * 创建viewhodler，相当于listview中getview中的创建view和viewhodler
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public myViewHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        //创建自定义布局
        View itemView = View.inflate(context, R.layout.home_item_tea_ceremony, null);


        return new myViewHodler(itemView);
    }

    /**
     * 绑定数据，数据与view绑定
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(myViewHodler holder, int position) {
        //根据点击位置绑定数据
        bundle= new Bundle();
        TeaCeremony data = teaCeremoniesEntityList.get(position);
//        holder.mItemGoodsImg.setImageResource(R.mipmap.cat);
//        Bitmap bitmap = getHttpBitmap("http://images.missyuan.com/attachments/day_071123/20071123_b2bfefe1ec56e2df6582vCGIZG81gbyM.png");
//        Bitmap bitmap=getHttpBitmap(data.getImgPath());
//        holder.mItemGoodsImg.setImageBitmap(bitmap);
//        holder.mItemGoodsImg.setImageURI(Uri.parse(data.getImgPath()));

//        Glide.with(context).load(data.getImgPath()).into(holder.mItemGoodsImg);
        bundle.putInt("item_photo",data.getImgPath());
        bundle.putString("item_name",data.getTeaCeremonyName());
        bundle.putString("item_content",data.getContent());



        //当加载网络图片时，由于加载过程中图片未能及时显示，此时可能需要设置等待时的图片，通过placeHolder()方法
        Glide.with(context).load(data.getImgPath()).placeholder(R.mipmap.loading).into(holder.mItemGoodsImg);
        holder.mItemGoodsName.setText(data.getTeaCeremonyName());//获取实体类中的name字段并设置
        holder.mItemGoodsContent.setText(data.getContent());//获取实体类中的content字段并设置
//        holder.mItemGoodsPrice.setText(data.goodsPrice);//获取实体类中的price字段并设置


    }



    /**
     * 得到总条数
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return teaCeremoniesEntityList.size();
    }

    //自定义viewhodler
    class myViewHodler extends RecyclerView.ViewHolder {
        private ImageView mItemGoodsImg;
        private TextView mItemGoodsName;
        private TextView mItemGoodsContent;

        public myViewHodler(View itemView) {
            super(itemView);
            mItemGoodsImg = (ImageView) itemView.findViewById(R.id.item_tea_image);
            mItemGoodsName = (TextView) itemView.findViewById(R.id.item_tea_name);
            mItemGoodsContent= itemView.findViewById(R.id.home_tea_content);
//            mItemGoodsPrice = (TextView) itemView.findViewById(R.id.item_goods_price);
            //点击事件放在adapter中使用，也可以写个接口在activity中调用
            //方法一：在adapter中设置点击事件
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //可以选择直接在本位置直接写业务处理
//                    Toast.makeText(context,"点击了xxx", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(Tab1Pager.class, HomeTeaCeremonyDetailActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putInt("photo",data.getImgPath());
//                    bundle.putString("message", mItemGoodsName);
                    Intent intent = new Intent();
                    intent.putExtras(bundle);
                    intent.setClass(context, HomeTeaCeremonyDetailActivity.class);
                    context.startActivity(intent);


                    //此处回传点击监听事件
                    if(onItemClickListener!=null){
                        onItemClickListener.OnItemClick(v, teaCeremoniesEntityList.get(getLayoutPosition()));
                    }
                }
            });

        }
    }

    /**
     * 设置item的监听事件的接口
     */
    public interface OnItemClickListener {
        /**
         * 接口中的点击每一项的实现方法，参数自己定义
         *
         * @param view 点击的item的视图
         * @param data 点击的item的数据
         */
        public void OnItemClick(View view, TeaCeremony data);
    }

    //需要外部访问，所以需要设置set方法，方便调用
    public OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }





}
