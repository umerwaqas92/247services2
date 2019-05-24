package com.dcservicez.a247services;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.dcservicez.a247services.Extras.View_Mnagae;
import com.dcservicez.a247services.Notifiers.Dilouges;
import com.dcservicez.a247services.debugs.Debug;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends Activity {

   private EditText fullName, mobileNo, email, pass, Cpassword;
     Spinner spinner_gndr;
    private  RadioGroup radioSp;
    private Context context;
    RadioButton rd_btn,rd_btn2;

    Debug debug;

    String srvc,srvc_exp,srvc_des;
    Dilouges dilouges;

  //  ArrayList<String> chategories = new ArrayList<>();
  //  ArrayList<String> nodes = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getvies();
        context=this;
        fill_genders();
        debug=new Debug(context);
        dilouges=new Dilouges(context);
        listiners();


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==92333){
            Intent i=data;
            srvc=i.getStringExtra("svrc");
            srvc_des=i.getStringExtra("svrc_exp");
            srvc_exp=i.getStringExtra("svrc_desc");

        }
    }

    void listiners() {
            radioSp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    if(i==R.id.radioBtn_yes){
                        startActivityForResult(new Intent(context,Signup_sp.class),92333);
                    }
//                    debug.print("chosed"+i);
                }
            });
    }

    void fill_genders(){
        String []genders_data={"Not to Say","Male","Female"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                context, R.layout.spinner_item, genders_data);
        spinner_gndr.setAdapter(adapter);

        spinner_gndr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(MainActivity.this,chategories.get(position),Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }



    void getvies() {
        fullName = (EditText) findViewById(R.id.edt_fullName);
        mobileNo = (EditText) findViewById(R.id.edt_mobile);
        email = (EditText) findViewById(R.id.edt_email);
        pass = (EditText) findViewById(R.id.edt_pass);
        Cpassword = (EditText) findViewById(R.id.confirm_password);
        spinner_gndr = (Spinner) findViewById(R.id.spn_gender);
        radioSp = (RadioGroup) findViewById(R.id.radio_sp);
         rd_btn=(RadioButton)findViewById(R.id.radioBtn_yes);
         rd_btn2=(RadioButton)findViewById(R.id.radioBtn_No);
    }


    public void register_srvc(boolean isService,final View view) {


        final AlertDialog builder=new ProgressDialog.Builder(context)
                .setCancelable(false)
                .setTitle("Registration")
                .setMessage("wait..").create();
        builder.show();
        view.setEnabled(false);



        DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference("Users");

        if(isService) {
            builder.setMessage("Registration as service provider");
            DatabaseReference ref=firebaseDatabase.getRef();
            String key =  email.getText().toString().replace(".", ",");

            ref.child(key).child("fullname").setValue(fullName.getText().toString());
            ref.child(key).child("mobile_no").setValue(mobileNo.getText().toString());
//            ref.child(key).child("Email").setValue(email.getText().toString());
            ref.child(key).child("password").setValue(pass.getText().toString());
            ref.child(key).child("profile_url").setValue("https://firebasestorage.googleapis.com/v0/b/students24by7.appspot.com/o/aizaz_pic.jpg?alt=media&token=8e1a7e81-0dd1-42d2-bfde-6aed919b9648");
            ref.child(key).child("service").child("title").setValue(srvc);
            ref.child(key).child("service").child("service_des").setValue(srvc_des);
            ref.child(key).child("service").child("active").setValue(true);

            ref.child(key).child("service").child("service_exp").setValue(srvc_exp).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    view.setEnabled(false);
                    builder.hide();


                    dilouges.complition_dilouge("Registration is success", "You have completed registration as servive provider", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(new Intent(context,SignIn.class));
                            ((Activity)context).finish();
                        }
                    }).show();
                }
            });
        }else {
            builder.setMessage("Registration as service user");

            String key =  email.getText().toString().replace(".", ","); // firebaseDatabase.push().getKey();
            firebaseDatabase.child(key).child("fullname").setValue(fullName.getText().toString());
            firebaseDatabase.child(key).child("mobile_no").setValue(mobileNo.getText().toString());
            firebaseDatabase.child(key).child("profile_url").setValue("https://firebasestorage.googleapis.com/v0/b/students24by7.appspot.com/o/aizaz_pic.jpg?alt=media&token=8e1a7e81-0dd1-42d2-bfde-6aed919b9648");

//            firebaseDatabase.child(key).child("Email").setValue(email.getText().toString());
            firebaseDatabase.child(key).child("password").setValue(pass.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    view.setEnabled(false);
                    builder.hide();

                    dilouges.complition_dilouge("Registration is success", "You have completed registration as service user", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(new Intent(context,SignIn.class));
                            ((Activity)context).finish();
                        }
                    }).show();

                }
            });
        }




    }



    boolean isValid(){

        View_Mnagae viewMnagae=new View_Mnagae(context);
        viewMnagae.remove_error(fullName);
        viewMnagae.remove_error(mobileNo);
        viewMnagae.remove_error(email);
        viewMnagae.remove_error(pass);
        viewMnagae.remove_error(Cpassword);


        if(fullName.getText().toString().isEmpty()){
            fullName.setError("Enter your name here ");
            return false;
        }

    if(mobileNo.getText().toString().trim().length()<11){
        mobileNo.setError("Mobile Number Must be 11 Digits");
        return false;

    }

        if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
        email.setError("Please enter a Valid E-Mail Address!");
        return false;
    }if(email.getText().toString().isEmpty()){
        email.setError("Enter your email here ");

        return false;
    }

        if(pass.getText().toString().isEmpty()) {
        pass.setError("Enter password here ");
        return false;
    } if(pass.getText().toString().length()<8){
        pass.setError("Password must be at least 8 character with one numeric and one Special Character ");
        return false;
    }  if (!isValidPassword(pass.getText().toString().trim())){
        pass.setError("Password must be at least 8 character with one numeric and one Special Character ");
        return false;
    }
 if(!pass.getText().toString().equals(Cpassword.getText().toString())){
            Cpassword.setError("Confirm pass is not matched");
            return false;
        }


        if((!rd_btn.isChecked() && !rd_btn2.isChecked())){
            Toast.makeText(context,"Chose a registration type",Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;


    }


    public void SignUp(View view) {
        //here check all the fileds are correctly filled


        try {


            if(isValid()){
                //now check if service provider or not

                if(rd_btn.isChecked()){
                    ///go for sp
//                startActivityForResult(new Intent(context,Signup_sp.class),9233);
                    register_srvc(true,view);
                }else{
                    register_srvc(false,view);

                }
            }
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();

        }

    }

    public void back_signUp(View view) {
//        Intent intent=new Intent(MainActivity.this,SignIn.class);
//        startActivity(intent);
        finish();
    }
    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }
}