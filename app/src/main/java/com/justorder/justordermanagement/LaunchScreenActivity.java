package com.justorder.justordermanagement;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.SpriteFactory;
import com.github.ybq.android.spinkit.Style;
import com.github.ybq.android.spinkit.sprite.Sprite;

/**
 * Created by Numan on 11/06/2017.
 */

public class LaunchScreenActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launchscreen);

        LinearLayout bg = (LinearLayout) findViewById(R.id.bg);
        bg.setBackgroundColor(Color.BLUE);

        TextView title = (TextView) findViewById(R.id.title);
        title.setText("JustOrder");

        SpinKitView spinKit = (SpinKitView) findViewById(R.id.spin_kit);
        Style style = Style.values()[2];
        Sprite drawable = SpriteFactory.create(style);
        spinKit.setIndeterminateDrawable(drawable);
    }
}
