package com.wotingfm.ui.mine;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.woting.commonplat.utils.SequenceUUID;
import com.wotingfm.R;
import com.wotingfm.common.utils.L;
import com.wotingfm.ui.base.baseactivity.BaseFragmentActivity;
import com.wotingfm.ui.mine.fragment.MineFragment;

/**
 * 作者：xinLong on 2017/6/2 12:15
 * 邮箱：645700751@qq.com
 */
public class MineActivity extends BaseFragmentActivity {

    private static MineActivity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine);
        context = this;
        MineActivity.open(new MineFragment());
    }

    /**
     * 打开新的 Fragment
     */
    public static void open(Fragment frg) {
        context.getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_content, frg)
                .addToBackStack(SequenceUUID.getUUID())
                .commit();
    }

    /**
     * 关闭已经打开的 Fragment
     */
    public static void close() {
        context.getSupportFragmentManager().popBackStackImmediate();
    }

    @Override
    public void onBackPressed() {
        L.d("TAG", "onBackPressed");
    }
}
