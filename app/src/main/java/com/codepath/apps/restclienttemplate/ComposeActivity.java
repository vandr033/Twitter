package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.parceler.Parcel;
import org.parceler.Parcels;

import okhttp3.Headers;

public class ComposeActivity extends AppCompatActivity {
    TwitterClient client;

    public static final String TAG = "ComposeActivity";

    public static final int MAX_TWEET_LENGTH = 280;
    EditText etCompose;
    Button btnTweet;
    TextView tvCount;

    int counter =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        client = TwitterApplication.getRestClient(this);

        tvCount = findViewById(R.id.tvCharCount);
        etCompose = findViewById(R.id.etCompose);
        btnTweet = findViewById(R.id.btnTweet);


        tvCount.setText(counter+"/"+MAX_TWEET_LENGTH);

        etCompose.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                if (count == 0){
                    counter++;
                }
                if (count == 1){
                    counter--;
                }
                    if(counter <= MAX_TWEET_LENGTH){
                        tvCount.setTextColor(Color.rgb(255,255,255));
                        tvCount.setText(counter + "/" + MAX_TWEET_LENGTH);
                    }else{
                        tvCount.setTextColor(Color.rgb(255,0,0));
                        tvCount.setText(MAX_TWEET_LENGTH - counter + "/" + MAX_TWEET_LENGTH);
                    }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tweetContent = etCompose.getText().toString();
                if(tweetContent.isEmpty()) {
                    Toast.makeText(ComposeActivity.this, "Sorry your tweet cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                }else if(tweetContent.length()>MAX_TWEET_LENGTH){
                    Toast.makeText(ComposeActivity.this, "Sorry your tweet cannot exceed 140 characters", Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(ComposeActivity.this, tweetContent, Toast.LENGTH_SHORT).show();
                client.publishTweet(tweetContent, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Log.i(TAG, "onSuccess publish tweet");
                        try {
                            Tweet tweet = Tweet.fromJson(json.jsonObject);
                            Log.i(TAG, "Published tweet says" + tweet);
                            Intent intent = new Intent();
                            intent.putExtra("tweet", Parcels.wrap(tweet));
                            setResult(RESULT_OK, intent);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.e(TAG, "onFailure pbluish tweet",throwable);
                    }
                });
            }
        });
    }
}