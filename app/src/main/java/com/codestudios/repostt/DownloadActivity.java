package com.codestudios.repostt;

import android.Manifest;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.vistrav.ask.Ask;
import com.vistrav.ask.annotations.AskDenied;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;


public class DownloadActivity extends AppCompatActivity {
    String URL="";
//    String NAME_FOLDER = "Repostt/";
    ImageView image;
    VideoView videoView;
    ImageView download;
    TextView textView;
//    ProgressDialog progressDialog;
    String downloadURL,downloadFilename;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_download);
        image = (ImageView) findViewById(R.id.image1);
        download = (ImageView) findViewById(R.id.download1);
        videoView = (VideoView) findViewById(R.id.videoView);


        ClipboardManager clipBoard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
        clipBoard.addPrimaryClipChangedListener( new ClipboardListener(this) );


        try{
            ClipData abc = clipBoard.getPrimaryClip();
            ClipData.Item item = abc.getItemAt(0);
            URL = item.getText().toString();
            Log.e("url",URL);
            Pattern pattern = Pattern.compile("https://www.instagram.com/p/([^/]+)");
            Matcher matcher = pattern.matcher(URL);
            if(matcher.find()){
                URL = matcher.group(0);

                AsyncHttpClient client = new AsyncHttpClient();
                client.get(URL, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String HTML = new String(responseBody);
                        doDaThang(HTML);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Toast.makeText(DownloadActivity.this, "FAILED", Toast.LENGTH_SHORT).show();
                    }

                });

            }
            else {
                textView.setVisibility(View.VISIBLE);
                textView.setText("URL is Invalid");
            }

        }
        catch (Exception e){
            Log.e("Error",e.toString());
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                DownloadManager mManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                DownloadManager.Request mRqRequest = new DownloadManager.Request(Uri.parse(downloadURL));
                mRqRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                mRqRequest.allowScanningByMediaScanner();
                mRqRequest.setTitle(downloadFilename);
                mManager.enqueue(mRqRequest);
            }
        });
    }

    private void doDaThang(String html) {

        Pattern pattern = Pattern.compile("<script type=\\\"text/javascript\\\">window._sharedData = (.+)?;</script>");
        Matcher matcher = pattern.matcher(html);
        if(matcher.find()) try {
            JSONObject root = new JSONObject(matcher.group(1));
            JSONObject media = root.getJSONObject("entry_data")
                    .getJSONArray("PostPage")
                    .getJSONObject(0)
                    .getJSONObject("media");
            if (media.getBoolean("is_video")) {
                downloadURL = media.getString("video_url").split("\\?ig_cache_key")[0];
                videoView.setVisibility(View.VISIBLE);
                image.setVisibility(View.GONE);
                videoView.setVideoURI(Uri.parse(media.getString("video_url")));
                //progressDialog = ProgressDialog.show(DownloadActivity.this, "Loading...", "Fetching your Video. Wait a moment");
                MediaController mediaController = new MediaController(DownloadActivity.this);
                mediaController.show();
                videoView.setMediaController(mediaController);


                videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        //progressDialog.dismiss();
                        videoView.start();
                        download.setVisibility(View.VISIBLE);
                    }
                });
            } else {
                downloadURL = media.getString("display_src").split("\\?ig_cache_key")[0];
                videoView.setVisibility(View.GONE);
                image.setVisibility(View.VISIBLE);
                //progressDialog = ProgressDialog.show(DownloadActivity.this, "Loading...", "Fetching your Image. Wait a moment");
                Picasso.with(DownloadActivity.this).load(media.getString("display_src")).into(image, new Callback() {
                    @Override
                    public void onSuccess() {
                        download.setVisibility(View.VISIBLE);
                        //progressDialog.dismiss();
                    }

                    @Override
                    public void onError() {

                    }
                });
            }
            downloadFilename = media.getString("code")+downloadURL.substring( downloadURL.lastIndexOf(".") );

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
