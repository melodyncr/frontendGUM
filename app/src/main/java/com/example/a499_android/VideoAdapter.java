package com.example.a499_android;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.VideoView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {


    public String TAG = "VideoAdapter";

    ArrayList<String> videosList;

    public VideoAdapter(){

    }
    public VideoAdapter(ArrayList<String> videos){
        this.videosList=videos;
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_video,parent,false);


        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position){
        Log.d(TAG, videosList.get(position));
        holder.videoWeb.loadData(videosList.get(position),"text/html","utf-8");

    }

    @Override
    public int getItemCount(){return videosList.size();}

    public class VideoViewHolder extends RecyclerView.ViewHolder{
        WebView videoWeb;
        public VideoViewHolder(View itemView){
            super(itemView);
            videoWeb = (WebView) itemView.findViewById(R.id.videoView);
            videoWeb.getSettings().setJavaScriptEnabled(true);
            videoWeb.setWebChromeClient(new WebChromeClient(){});

        }
    }
}
