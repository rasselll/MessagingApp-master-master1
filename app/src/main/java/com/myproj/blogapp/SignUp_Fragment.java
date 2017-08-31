package com.myproj.blogapp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.myproj.blogapp.R;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class SignUp_Fragment extends Fragment implements OnClickListener {
	private static View view;

	private EditText mNameField;
	private EditText mEmailField;
	private EditText mPasswordField;
	private EditText mConfirmPasword;

	private static EditText
					fullName,
					emailId,
					mobileNumber,
					location,
					password,
					confirmPassword;


	private static TextView login;
	private static Button signUpButton;
	private static CheckBox terms_conditions;


	private Button mRegisterBtn;

	private FirebaseAuth mAuth;
	private DatabaseReference mDatabase;
	private ProgressDialog mProgress;





	public SignUp_Fragment() {


	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.signup_layout, container, false);

		mAuth = FirebaseAuth.getInstance();

		mDatabase = FirebaseDatabase.getInstance().getReference().child("users");


		mProgress = new ProgressDialog(getContext());

		mNameField = (EditText) view.findViewById(R.id.fullName);
		mEmailField = (EditText) view.findViewById(R.id.userEmailId);
		mPasswordField = (EditText) view.findViewById(R.id.password);
		mConfirmPasword = (EditText) view.findViewById(R.id.confirmPassword);
		mRegisterBtn = (Button) view.findViewById(R.id.signUpBtn);





		initViews();
		setListeners();
		return view;
	}

	// Initialize all views
	private void initViews() {

		fullName = (EditText) view.findViewById(R.id.fullName);
		emailId = (EditText) view.findViewById(R.id.userEmailId);
		mobileNumber = (EditText) view.findViewById(R.id.mobileNumber);
		location = (EditText) view.findViewById(R.id.location);
		password = (EditText) view.findViewById(R.id.password);
		confirmPassword = (EditText) view.findViewById(R.id.confirmPassword);
		signUpButton = (Button) view.findViewById(R.id.signUpBtn);
		login = (TextView) view.findViewById(R.id.already_user);
		terms_conditions = (CheckBox) view.findViewById(R.id.terms_conditions);

		// Setting text selector over textviews
		XmlResourceParser xrp = getResources().getXml(R.xml.text_selector);
		try {
			ColorStateList csl = ColorStateList.createFromXml(getResources(),
					xrp);

			login.setTextColor(csl);
			terms_conditions.setTextColor(csl);
		} catch (Exception e) {
		}
	}

	// Set Listeners
	private void setListeners() {

		signUpButton.setOnClickListener(this);
		login.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.signUpBtn:

			// Call checkValidation method
			checkValidation();
			break;

		case R.id.already_user:

			// Replace login fragment
			new mainact().replaceLoginFragment();
			break;
		}

	}

	// Check Validation Method
	private void checkValidation() {

		// Get all edittext texts
		String getFullName = fullName.getText().toString();
		String getEmailId = emailId.getText().toString();
		String getMobileNumber = mobileNumber.getText().toString();
		String getLocation = location.getText().toString();
		String getPassword = password.getText().toString();
		String getConfirmPassword = confirmPassword.getText().toString();

		// Pattern match for email id
		Pattern p = Pattern.compile(Utils.regEx);
		Matcher m = p.matcher(getEmailId);

		// Check if all strings are null or not
		if (getFullName.equals("") || getFullName.length() == 0
				|| getEmailId.equals("") || getEmailId.length() == 0
/*				|| getMobileNumber.equals("") || getMobileNumber.length() == 0
				|| getLocation.equals("") || getLocation.length() == 0*/
				|| getPassword.equals("") || getPassword.length() == 0
				|| getConfirmPassword.equals("")
				|| getConfirmPassword.length() == 0)

			new CustomToast().Show_Toast(getActivity(), view,
					"All fields are required.");

		// Check if email id valid or not
		else if (!m.find())
			new CustomToast().Show_Toast(getActivity(), view,
					"Your Email Id is Invalid.");

		// Check if both password should be equal
		else if (!getConfirmPassword.equals(getPassword))
			new CustomToast().Show_Toast(getActivity(), view,
					"Both password doesn't match.");

		// Make sure user should check Terms and Conditions checkbox
		else if (!terms_conditions.isChecked())
			new CustomToast().Show_Toast(getActivity(), view,
					"Please select Terms and Conditions.");

		// Else do signup or do your stuff
		else

			Toast.makeText(getActivity(), "Do SignUp.", Toast.LENGTH_SHORT)
					.show();
				startRegister();

	}


	private void startRegister() {

		final String name = mNameField.getText().toString().trim();
		String email = mEmailField.getText().toString().trim();
		String password = mPasswordField.getText().toString().trim();

		if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){

			mProgress.setMessage(("Signing up..."));
			mProgress.show();

			mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
				@Override
				public void onComplete(@NonNull Task<AuthResult> task) {
					if(task.isSuccessful()){

						String user_id = mAuth.getCurrentUser().getUid();

						DatabaseReference current_user_db =  mDatabase.child(user_id);

						current_user_db.child("name").setValue(name);
						current_user_db.child("image").setValue("default");


						mProgress.dismiss();

						Intent setupIntent = new Intent(getActivity(), SetupActivity.class);
						setupIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(setupIntent);

					}
				}
			});

		}
	}
}
