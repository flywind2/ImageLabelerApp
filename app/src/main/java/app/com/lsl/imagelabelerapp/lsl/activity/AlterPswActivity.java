package app.com.lsl.imagelabelerapp.lsl.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import app.com.lsl.imagelabelerapp.R;
import app.com.lsl.imagelabelerapp.lsl.activity.menu.TopMenuHeader;
import app.com.lsl.imagelabelerapp.lsl.activity.view.UserView;
import app.com.lsl.imagelabelerapp.lsl.presenter.UserPresenter;
import app.com.lsl.imagelabelerapp.lsl.utils.DialogUtil;

import static app.com.lsl.imagelabelerapp.R.id.iv_icon;
import static app.com.lsl.imagelabelerapp.R.id.top_menu_left;
import static app.com.lsl.imagelabelerapp.R.id.top_menu_right;
import static app.com.lsl.imagelabelerapp.lsl.activity.LoginActivity.IS_REMEMBER;
import static app.com.lsl.imagelabelerapp.lsl.activity.LoginActivity.PASSWORD;
import static app.com.lsl.imagelabelerapp.lsl.activity.LoginActivity.SPF_USERINFO;
import static app.com.lsl.imagelabelerapp.lsl.activity.LoginActivity.USER_NAME;

/** 修改用户密码页面
 * Created by M1308_000 on 2017/5/22.
 */

public class AlterPswActivity extends AppCompatActivity implements View.OnClickListener, UserView {

    private EditText et_new_rpsw;
    private EditText et_new_psw;
    private EditText et_origin_psw;
    private String TYPE = "alter_psw";
    private SharedPreferences spf;
    private String in_origin_psw;
    private String in_new_psw;
    private String in_new_rpsw;
    private TopMenuHeader topMenuHeader;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alter_psw);
        initLayout();
        initMenu();
    }

    private void initLayout() {
        AppActivities.addActivity(this);
        et_origin_psw = (EditText) findViewById(R.id.et_origin_psw);
        et_new_psw = (EditText) findViewById(R.id.et_new_psw);
        et_new_rpsw = (EditText) findViewById(R.id.et_new_rpsw);

    }

    private void initMenu() {
        topMenuHeader = new TopMenuHeader(getWindow().getDecorView());
        topMenuHeader.topMenuTitle.setText("修改密码");
        topMenuHeader.topMenuTitle.setTextSize(20);
        topMenuHeader.topMenuTitle.setTextColor(Color.parseColor("#33CCB6"));
        topMenuHeader.topMenuLeft.setText(" 设置");
        topMenuHeader.topMenuLeft.setTextColor(Color.parseColor("#33CCB6"));
        topMenuHeader.topMenuRight.setOnClickListener(this);
        topMenuHeader.topMenuLeft.setOnClickListener(this);
        topMenuHeader.ivTopMenuLeft.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case top_menu_right:
                in_origin_psw = et_origin_psw.getText().toString().trim();
                in_new_psw = et_new_psw.getText().toString().trim();
                in_new_rpsw = et_new_rpsw.getText().toString().trim();

                spf = getSharedPreferences(SPF_USERINFO, Context.MODE_WORLD_READABLE);
                if (TextUtils.isEmpty(in_origin_psw) || TextUtils.isEmpty(in_new_psw) || TextUtils.isEmpty(in_new_rpsw)) {
                    Toast.makeText(AlterPswActivity.this, "选项不能留空", Toast.LENGTH_SHORT).show();
                } else {
                    if (spf.getBoolean(IS_REMEMBER,false)) {
                        String origin_psw = spf.getString(PASSWORD,"");
                        if (in_origin_psw.equals(origin_psw)) {
                            if (in_new_psw.equals(in_new_rpsw)) {
                                // 执行密码修改业务
                                AlterPsw(in_origin_psw, in_new_psw);
                            } else {
                                Toast.makeText(AlterPswActivity.this,"两次密码输入不同",Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(AlterPswActivity.this,"原密码错误！",Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (in_new_psw.equals(in_new_rpsw)) {
                            // 执行密码修改业务
                            AlterPsw(in_origin_psw, in_new_psw);
                        } else {
                            Toast.makeText(AlterPswActivity.this,"两次密码输入不同",Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                break;
            case top_menu_left:
                finish();
                break;
            case iv_icon:
                finish();
                break;
        }
    }

    /**
     * 修改用户密码
     * @param in_origin_psw
     * @param in_new_psw
     */
    private void AlterPsw(String in_origin_psw, String in_new_psw) {
        Map<String,String> map = new HashMap<>();
        // 得到用户名
        map.put("user_name",spf.getString(USER_NAME,""));
        map.put("origin_psw",in_origin_psw);
        map.put("new_psw",in_new_psw);
        map.put("type",TYPE);
        // 从服务器检索密码是否一样
        new UserPresenter(AlterPswActivity.this, map, "userinfo").fetch();
    }

    @Override
    public void ShowLoading() {
        DialogUtil.showLoadingDialog(AlterPswActivity.this, "保存中...", true);
    }

    @Override
    public void ShowBackMsg(Object obj) {
//        DialogUtil.closeLoadingDialog();
        if (obj.toString().equals("12")) {
            DialogUtil.closeLoadingDialog(AlterPswActivity.this);
            Toast.makeText(AlterPswActivity.this,"密码修改成功",Toast.LENGTH_SHORT).show();
            SharedPreferences.Editor editor = spf.edit();
            editor.putString(PASSWORD, in_new_psw);
            editor.commit();
            finish();
        } else if (obj.toString().equals("0")) {
            Toast.makeText(AlterPswActivity.this,"原密码错误！",Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(AlterPswActivity.this,"密码修改失败",Toast.LENGTH_SHORT).show();
        }
    }


}
