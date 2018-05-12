package com.lichangxin.xiuchat.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lichangxin.xiuchat.R;

/**
 * 自定义Dialog弹窗
 */
public class CustomDialog extends Dialog {
    /**
     * 提示
     */
    protected TextView hintTv;

    /**
     * 左边按钮
     */
    protected Button doubleLeftBtn;

    /**
     * 右边按钮
     */
    protected Button doubleRightBtn;

    /**
     * 输入框
     */
    protected EditText editText;

    public CustomDialog(Context context) {
        super(context, R.style.CustomDialogStyle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setCancelable(false);  // 是否可以撤销

        setContentView(R.layout.dialog_custom);

        hintTv = findViewById(R.id.tv_common_dialog_hint);
        doubleLeftBtn = findViewById(R.id.btn_common_dialog_double_left);
        doubleRightBtn = findViewById(R.id.btn_common_dialog_double_right);
        editText = findViewById(R.id.dialog_edit_text);
    }

    @Override
    public void show() {
        super.show();

        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity = Gravity.BOTTOM;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        getWindow().getDecorView().setPadding(10, 0, 10, 0);
        getWindow().setAttributes(layoutParams);
    }

    /**
     * 设置右键文字和点击事件
     *
     * @param rightStr 文字
     * @param clickListener 点击事件
     */
    public void setRightButton(String rightStr, View.OnClickListener clickListener) {
        doubleRightBtn.setOnClickListener(clickListener);
        doubleRightBtn.setText(rightStr);
    }

    /**
     * 设置左键文字和点击事件
     *
     * @param leftStr 文字
     * @param clickListener 点击事件
     */
    public void setLeftButton(String leftStr, View.OnClickListener clickListener) {
        doubleLeftBtn.setOnClickListener(clickListener);
        doubleLeftBtn.setText(leftStr);
    }

    /**
     * 设置提示内容
     *
     * @param str 内容
     */
    public void setHintText(String str) {
        hintTv.setText(str);
        hintTv.setVisibility(View.VISIBLE);
    }

    /**
     * 设置 Edit 是否可见
     *
     */
    public void setEditVisibility(Boolean visibility) {
        if (!visibility) {
            editText.setVisibility(View.GONE);
        }
    }

    /**
     * 获取 Edit 内容
     *
     */
    public String getEdit() {
        return editText.getText().toString();
    }
}
