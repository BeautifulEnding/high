package com.example.asus.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asus.R;

/**
 *
 * 用户资料查看.
 *
 * @author shimiso
 */
public class FriendInfoActivity extends Activity {
	private TextView personal_company_detail;
	private TextView personal_username_detail;
	private TextView personal_nickname_detail;
	private TextView personal_job_detail;
	private TextView personal_signature_detail;
	private ImageView personal_head_imageview;
	private String imagePath;
	private Button mBackBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_friend_layout);
	}

}
