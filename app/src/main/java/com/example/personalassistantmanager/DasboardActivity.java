package com.example.personalassistantmanager;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DasboardActivity extends AppCompatActivity {
    DatabaseReference reference;
    RecyclerView recyclerView;
    List<Members> listdata;
    MyAdpter adpter;
    FirebaseDatabase fbd;
    Members members;

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dasboard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //recyclerView = findViewById(R.id.recyler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
        listdata = new ArrayList();
        adpter = new MyAdpter(listdata);
        fbd = FirebaseDatabase.getInstance();
        recyclerView.setHasFixedSize(true);
        getFbddata();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
                Snackbar.make(view, "Please Add a Account", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dasboard, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void showDialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this).setCancelable(false);
        LayoutInflater inflater = LayoutInflater.from(this);
        View reg_layout = inflater.inflate(R.layout.add_account, null);

        final EditText actname = reg_layout.findViewById(R.id.input_act);
        final Button savet = reg_layout.findViewById(R.id.btn_save);
        final Button cancelt = reg_layout.findViewById(R.id.btn_cancel);
        dialog.setView(reg_layout);
        final AlertDialog alertDialog = dialog.create();

        savet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

        cancelt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    void getFbddata() {
        reference = fbd.getReference().child("Members");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    members = new Members();
                    //members = ds.getValue(Members.class);
                    //listdata.add(members);


                    String name = ds.child("member_name").getValue(String.class);
                    String email = ds.child("email").getValue(String.class);
                    String mobile = ds.child("mobile").getValue(String.class);
                    String profile = ds.child("prolink").getValue(String.class);
                    //Log.d("TAG", name + " / "+email+" / "+mobile+" / "+profile);
                    //Members members = new Members(name, mobile, email, profile);
                    listdata.add(members);

                }

                recyclerView.setAdapter(adpter);
                adpter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public class MyAdpter extends RecyclerView.Adapter<MyAdpter.BlogViewHolder> {
        List<Members> listArry;

        public MyAdpter(List<Members> list) {
            this.listArry = list;

        }

        @NonNull
        @Override
        public BlogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_account, parent, false);
            return new BlogViewHolder(view);
        }

        @Override
        public void onBindViewHolder(BlogViewHolder holder, int position) {
            final Members data = listArry.get(position);

            //holder.name.setText(data.getMember_name());
            //holder.mobile.setText(data.getMobile());
            //holder.email.setText(data.getEmail());
            //Picasso.get().load(data.getProlink()).into(holder.pic);
            holder.mobile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent callintetnt = new Intent(Intent.ACTION_CALL);
                    //callintetnt.setData(Uri.parse("tel:" + data.getMobile()));
                    //Toast.makeText(MemberActivity.this, "Calling to " + data.getMember_name() + " " + data.getMobile(), Toast.LENGTH_LONG).show();
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    startActivity(callintetnt);

                }
            });


        }

        @Override
        public int getItemCount() {
            return listArry.size();
        }

        public class BlogViewHolder extends RecyclerView.ViewHolder {
            TextView name, email, mobile;
            ImageView pic;

            public BlogViewHolder(@NonNull View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.profilename);
                //email = (TextView) itemView.findViewById(R.id.profileemail);
               // mobile = (TextView) itemView.findViewById(R.id.profilemobile);
               // pic = (ImageView) itemView.findViewById(R.id.profilepic);

            }
        }
    }
}