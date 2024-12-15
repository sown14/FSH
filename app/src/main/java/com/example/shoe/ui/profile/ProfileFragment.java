package com.example.shoe.ui.profile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoe.MyApplication;
import com.example.shoe.R;
import com.example.shoe.models.ItemActionProfile;
import com.example.shoe.models.LoginMethod;
import com.example.shoe.models.User;
import com.example.shoe.ui.ChangePasswordActivity;
import com.example.shoe.ui.EditProfileActivity;
import com.example.shoe.ui.LoginActivity;
import com.example.shoe.ui.order.OrderActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public ProfileFragment() {
        // Required empty public constructor
    }

    ItemActionProfileAdapter itemActionAdapter;

    RecyclerView rvActions;

    User user;

    TextView tvName, tvChangeInfo;

    AppCompatButton btnLogout;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        rvActions = rootView.findViewById(R.id.rv_actions);
        tvName = rootView.findViewById(R.id.tv_name);
        tvChangeInfo = rootView.findViewById(R.id.tv_change_info);
        btnLogout = rootView.findViewById(R.id.btn_logout);
        _initActionsUI();

        user = MyApplication.getUser();

        if (user != null) {
            tvName.setText(user.getFullname());
        }

        _tvChangeInfoOnClick();
        _btnLogoutOnclick();

        return rootView;

    }

    private void _logout() {
        Toast.makeText(getContext(), "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
        MyApplication.removeUser();
        Intent intent = new Intent(getContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


    private void _showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Xác nhận đăng xuất");
        builder.setMessage("Bạn có chắc chắn muốn đăng xuất không?");
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                User user = MyApplication.getUser();
                if(user.getMethod().equals(LoginMethod.google)){
                    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestIdToken(getString(R.string.default_web_client_id))
                            .requestEmail()
                            .build();
                    GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);
                    mGoogleSignInClient.signOut();
                }
                _logout();
            }
        });

        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void _btnLogoutOnclick() {
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _showLogoutConfirmationDialog();
            }
        });
    }

    private void _tvChangeInfoOnClick() {
        tvChangeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _navigateToEditProfile();
            }
        });
    }

    private void _navigateToEditProfile() {
        Intent intent = new Intent(getContext(), EditProfileActivity.class);
        startActivity(intent);
    }


    private void _initActionsUI() {
        List<ItemActionProfile> actions = new ArrayList<>();

        actions.add(new ItemActionProfile(
                R.drawable.ic_all_orders,
                "Đơn hàng",
                view -> {
                    Intent intent = new Intent(getContext(), OrderActivity.class);
                    startActivity(intent);
                }));

        User user = MyApplication.getUser();
        if (user.getMethod().equals(LoginMethod.username)) {
            actions.add(new ItemActionProfile(
                    R.drawable.ic_app_security,
                    "Đổi mật khẩu",
                    view -> {
                        Intent intent = new Intent(getContext(), ChangePasswordActivity.class);
                        startActivity(intent);
                    }));
        }


        itemActionAdapter = new ItemActionProfileAdapter(actions);
        rvActions.setLayoutManager(new LinearLayoutManager(getContext()));
        rvActions.setAdapter(itemActionAdapter);
    }

}