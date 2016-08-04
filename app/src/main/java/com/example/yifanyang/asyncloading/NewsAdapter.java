package com.example.yifanyang.asyncloading;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.net.URL;
import java.util.List;

/**
 * Created by yifanyang on 16/8/4.
 */

public class NewsAdapter extends BaseAdapter implements AbsListView.OnScrollListener{

    private List<NewsBean> mList;
    private LayoutInflater minflater;
    private ImageLoader mimageLoader;
    private int mStart,mEnd;
    public   static String[]  URLS;
    private boolean mFirstIn;

    public NewsAdapter(Context context, List<NewsBean> data, ListView listView){
        mList=data;
        minflater =LayoutInflater.from(context);
        mimageLoader = new ImageLoader(listView);
        URLS = new  String[data.size()];
        for (int i=0; i<data.size(); i++){
            URLS[i]=data.get(i).newsIconUrl;
        }
        mFirstIn = true;
        //一定要注册对应事件
        listView.setOnScrollListener(this);
    }
    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder = null;
        if (view == null){
            viewHolder = new ViewHolder();
            view=minflater.inflate(R.layout.item_layout,null);
            viewHolder.ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
            viewHolder.tvTitle= (TextView) view.findViewById(R.id.tv_title);
            viewHolder.tvContent = (TextView) view.findViewById(R.id.tv_content);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.ivIcon.setImageResource(R.mipmap.ic_launcher);
        String url=mList.get(i).newsIconUrl;
        viewHolder.ivIcon.setTag(url);
//        new ImageLoader().showImageByThread(viewHolder.ivIcon,url);
        mimageLoader.showImageByAsyncTask(viewHolder.ivIcon,url);
        viewHolder.tvTitle.setText(mList.get(i).newsTitle);
        viewHolder.tvContent.setText(mList.get(i).newsContent);
        return view;
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {

        if (scrollState == SCROLL_STATE_IDLE){
            //加载可见项
            mimageLoader.loadImages(mStart,mEnd);
        }else {
            //停止任务
            mimageLoader.cancelAllTask();
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemConut, int totalItem) {

        mStart=firstVisibleItem;
        mEnd=firstVisibleItem+visibleItemConut;
        //第一次显示的时候调用
        if (mFirstIn && visibleItemConut > 0){
            mimageLoader.loadImages(mStart,mEnd);
            mFirstIn = false;
        }
    }

    class ViewHolder{
        public TextView tvTitle,tvContent;
        public ImageView ivIcon;
    }
}
