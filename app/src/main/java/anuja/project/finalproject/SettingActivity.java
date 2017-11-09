package anuja.project.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import anuja.project.finalproject.fragment.SettingsFragment;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by USER on 08-11-2017.
 */

public class SettingActivity extends AppCompatActivity {

    SettingsFragment mSettingFragment = new SettingsFragment();

    @BindView(R.id.frameLayout)
    FrameLayout mFrameLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        ButterKnife.bind(this);

        mSettingFragment = new SettingsFragment();
        getFragmentManager().beginTransaction().replace(R.id.frameLayout,mSettingFragment).commit();
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == SettingsFragment.REQUEST_SETTINGS){
            mSettingFragment.onActivityResult(requestCode,resultCode,data);
        }
        else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
