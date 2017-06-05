package com.wotingfm.ui.user.preference;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wotingfm.R;
import com.wotingfm.ui.base.baseactivity.BaseActivity;
import com.wotingfm.ui.user.logo.LogoActivity;

/**
 * 作者：xinLong on 2017/6/4 22:16
 * 邮箱：645700751@qq.com
 */
public class PreferenceActivity extends BaseActivity implements View.OnClickListener{

    private TextView tv_pass,tv_enter,tv_RAWY,tv_FYKSJ,tv_ZZS,tv_XSSH,tv_TGSTXS,tv_YQQ,tv_XJ;
    private ImageView left;

    private boolean type1=false;
    private boolean type2=false;
    private boolean type3=false;
    private boolean type4=false;
    private boolean type5=false;
    private boolean type6=false;
    private boolean type7=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);
        inItView();
        inItListener();
    }

    private void inItView(){
        left = (ImageView) findViewById(R.id.head_left_btn);                // 返回
        tv_pass= (TextView) findViewById(R.id.tv_pass);                     // 跳过
        tv_enter= (TextView) findViewById(R.id.tv_enter);                   // 进入应用

        tv_RAWY= (TextView) findViewById(R.id.tv_RAWY);                     // 热爱文艺
        tv_FYKSJ= (TextView) findViewById(R.id.tv_FYKSJ);                   // 放眼看世界
        tv_ZZS= (TextView) findViewById(R.id.tv_ZZS);                       // 涨知识
        tv_XSSH= (TextView) findViewById(R.id.tv_XSSH);                     // 享受生活
        tv_TGSTXS= (TextView) findViewById(R.id.tv_TGSTXS);                 // 听故事听小说
        tv_YQQ= (TextView) findViewById(R.id.tv_YQQ);                       // 有情趣
        tv_XJ= (TextView) findViewById(R.id.tv_XJ);                         // 喜剧

    }

    private void inItListener(){
        left.setOnClickListener(this);
        tv_pass.setOnClickListener(this);
        tv_enter.setOnClickListener(this);

        tv_RAWY.setOnClickListener(this);
        tv_FYKSJ.setOnClickListener(this);
        tv_ZZS.setOnClickListener(this);
        tv_XSSH.setOnClickListener(this);
        tv_TGSTXS.setOnClickListener(this);
        tv_YQQ.setOnClickListener(this);
        tv_XJ.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.head_left_btn:
                finish();
                break;
            case R.id.tv_pass:
                finish();
                break;
            case R.id.tv_enter:
//                startActivity(new Intent(this, MainActivity.class));
                startActivity(new Intent(this, LogoActivity.class));
                break;
            case R.id.tv_RAWY:
                setBackground(1,type1);
                break;
            case R.id.tv_FYKSJ:
                setBackground(2,type2);
                break;
            case R.id.tv_ZZS:
                setBackground(3,type3);
                break;
            case R.id.tv_XSSH:
                setBackground(4,type4);
                break;
            case R.id.tv_TGSTXS:
                setBackground(5,type5);
                break;
            case R.id.tv_YQQ:
                setBackground(6,type6);
                break;
            case R.id.tv_XJ:
                setBackground(7,type7);
                break;

        }
    }

    private void setBackground(int type,boolean b){
        if(type==1){
          if(b){
              tv_RAWY.setBackgroundResource(R.drawable.background_preference_white);
              type1=false;
          }else{
              tv_RAWY.setBackgroundResource(R.drawable.background_preference_green);
              type1=true;
          }
        }else if(type==2){
            if(b){
                tv_FYKSJ.setBackgroundResource(R.drawable.background_preference_white);
                type2=false;
            }else{
                tv_FYKSJ.setBackgroundResource(R.drawable.background_preference_light_blue);
                type2=true;
            }
        }else if(type==3){
            if(b){
                tv_ZZS.setBackgroundResource(R.drawable.background_preference_white);
                type3=false;
            }else{
                tv_ZZS.setBackgroundResource(R.drawable.background_preference_blue);
                type3=true;
            }
        }else if(type==4){
            if(b){
                tv_XSSH.setBackgroundResource(R.drawable.background_preference_white);
                type4=false;
            }else{
                tv_XSSH.setBackgroundResource(R.drawable.background_preference_orange);
                type4=true;
            }
        }else if(type==5){
            if(b){
                tv_TGSTXS.setBackgroundResource(R.drawable.background_preference_white);
                type5=false;
            }else{
                tv_TGSTXS.setBackgroundResource(R.drawable.background_preference_purple);
                type5=true;
            }
        }else if(type==6){
            if(b){
                tv_YQQ.setBackgroundResource(R.drawable.background_preference_white);
                type6=false;
            }else{
                tv_YQQ.setBackgroundResource(R.drawable.background_preference_pink);
                type6=true;
            }
        }else if(type==7){
            if(b){
                tv_XJ.setBackgroundResource(R.drawable.background_preference_white);
                type7=false;
            }else{
                tv_XJ.setBackgroundResource(R.drawable.background_preference_red);
                type7=true;
            }
        }
    }
}
