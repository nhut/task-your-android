package fi.donhut.taskyourandroid;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.Menu;
import android.view.MenuItem;

public class UserGuideActivity extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_user_guide);
        
//        WebView webView = (WebView) findViewById(R.id.webView1);
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.loadUrl("https://dl.dropbox.com/u/60333129/Test/Blob_Tracking_Modules.htm");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	return super.onOptionsItemSelected(item);
    }
    
    public void doTest() {
    	// We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences settings = getSharedPreferences("", 0);
        SharedPreferences.Editor editor = settings.edit();
//        editor.putBoolean("silentMode", mSilentMode);

        // Commit the edits!
        editor.commit();
    }
}
