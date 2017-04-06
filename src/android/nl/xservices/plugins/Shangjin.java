package nl.xservices.plugins;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Environment;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AlignmentSpan;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.app.AlertDialog; 
import android.app.Dialog; 

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

public class Shangjin extends CordovaPlugin {

  private static final String ACTION_SHOW_EVENT = "show";
  private static final String ACTION_HIDE_EVENT = "hide";
  private static final String ACTION_SHARE_EVENT = "share";

  private static final int GRAVITY_TOP = Gravity.TOP|Gravity.CENTER_HORIZONTAL;
  private static final int GRAVITY_CENTER = Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL;
  private static final int GRAVITY_BOTTOM = Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL;

  private static final int BASE_TOP_BOTTOM_OFFSET = 20;

  private android.widget.Toast mostRecentToast;
  private ViewGroup viewGroup;

  private static final boolean IS_AT_LEAST_LOLLIPOP = Build.VERSION.SDK_INT >= 21;

  // note that webView.isPaused() is not Xwalk compatible, so tracking it poor-man style
  private boolean isPaused;

  private String currentMessage;
  private JSONObject currentData;
  private static CountDownTimer _timer;

  private File[] files;
  private List<String> paths = new ArrayList<String>();

  public static boolean isInstallWeChart(Context context){
    PackageInfo packageInfo = null;
    try {
      packageInfo = context.getPackageManager().getPackageInfo("com.tencent.mm", 0);
    } catch (Exception e) {
      packageInfo = null;
      return false;
//      e.printStackTrace();
    }
    if (packageInfo == null) {
      return false;
    } else {
      return true;
    }
  }
  public static void share9PicsToWXCircle(Context context, String Kdescription, List<String> paths) {
    if (!isInstallWeChart(context)) {
      android.widget.Toast.makeText(context, "您没有安装微信", android.widget.Toast.LENGTH_SHORT).show();
      return;
    }
    Intent intent = new Intent();
    intent.setComponent(new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI"));
    intent.setAction("android.intent.action.SEND_MULTIPLE");

    ArrayList<Uri> imageList = new ArrayList<Uri>();
    for (String picPath : paths) {
      File f = new File(picPath);
      if (f.exists()) {
        imageList.add(Uri.fromFile(f));
      }
    }
    if(imageList.size() == 0){
      android.widget.Toast.makeText(context, "图片不存在", android.widget.Toast.LENGTH_SHORT).show();
      return;
    }
    intent.setType("image/*");
    intent.putExtra(Intent.EXTRA_STREAM, imageList); //图片数据（支持本地图片的Uri形式）
    intent.putExtra("Kdescription", Kdescription); //微信分享页面，图片上边的描述
    context.startActivity(intent);
  }

  public boolean share(String share_text){
    Context context= IS_AT_LEAST_LOLLIPOP ? cordova.getActivity().getWindow().getContext() : cordova.getActivity().getApplicationContext();

    if(!isInstallWeChart(context)){
      android.widget.Toast.makeText(context, "您没有安装微信", android.widget.Toast.LENGTH_SHORT).show();
      return false;
    }

    File root = (context).getExternalFilesDir("share");

    files = root.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
              return true;
            }
        });

    for(File file :files){
        paths.add(file.getAbsolutePath());
    }

    share9PicsToWXCircle(context, share_text, paths);

    return true;
  }
  @Override
  public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
    if (ACTION_SHARE_EVENT.equals(action)) {
      share(share_text);
      callbackContext.success();
      return true;
    }
    return true;
  }

  private void hide() {
    if (mostRecentToast != null) {
      mostRecentToast.cancel();
      getViewGroup().setOnTouchListener(null);
    }
    if (_timer != null) {
      _timer.cancel();
    }
  }

  // lazy init and caching
  private ViewGroup getViewGroup() {
    if (viewGroup == null) {
      viewGroup = (ViewGroup) ((ViewGroup) cordova.getActivity().findViewById(android.R.id.content)).getChildAt(0);
    }
    return viewGroup;
  }

  @Override
  public void onPause(boolean multitasking) {
    hide();
    this.isPaused = true;
  }

  @Override
  public void onResume(boolean multitasking) {
    this.isPaused = false;
  }
}