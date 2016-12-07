package cn.mstar.store.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.mstar.store.R;
import cn.mstar.store.functionutils.UpdateUtil;

public class AboutUsActivity extends AppCompatActivity {
    @Bind(R.id.id_tv_version)
    TextView idTvVersion;

    //ceshisvn...
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        ButterKnife.bind(this);

        idTvVersion.setText("当前版本："+String.valueOf(UpdateUtil.getVersionCode(AboutUsActivity.this)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_about_us, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        super.finish();
        //ea8a42ffa6d0c0c8
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
