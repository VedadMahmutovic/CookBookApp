package com.example.cookbook.ui.theme;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.cookbook.LoginActivity;
import com.example.cookbook.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class ProfileFragment extends Fragment {

    private TextView emailTextView, userIdTextView, passwordTextView;
    private Button logoutButton, changeUsernameButton, changePasswordButton;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    public ProfileFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        emailTextView = view.findViewById(R.id.emailTextView);
        userIdTextView = view.findViewById(R.id.userIdTextView);
        passwordTextView = view.findViewById(R.id.passwordTextView);
        logoutButton = view.findViewById(R.id.logoutButton);
        changeUsernameButton = view.findViewById(R.id.changeUsernameButton);
        changePasswordButton = view.findViewById(R.id.changePasswordButton);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null) {
            emailTextView.setText(firebaseUser.getEmail());
            userIdTextView.setText(firebaseUser.getDisplayName() != null ? firebaseUser.getDisplayName() : getString(R.string.no_username));
            passwordTextView.setText("********");
        }

        logoutButton.setOnClickListener(v -> showLogoutConfirmation());
        changeUsernameButton.setOnClickListener(v -> showChangeUsernameDialog());
        changePasswordButton.setOnClickListener(v -> showChangePasswordDialog());
        FrameLayout decorOverlayContainer = view.findViewById(R.id.decorOverlayContainer);
        View decor = LayoutInflater.from(requireContext()).inflate(R.layout.layout_decor_sipaj, decorOverlayContainer, false);
        decorOverlayContainer.setElevation(100f);
        decor.setElevation(100f);
        decorOverlayContainer.addView(decor);

        return view;
    }

    private void showLogoutConfirmation() {
        Dialog dialog = new Dialog(requireContext());

        FrameLayout root = new FrameLayout(requireContext());
        root.setLayoutParams(new FrameLayout.LayoutParams(dpToPx(200), ViewGroup.LayoutParams.WRAP_CONTENT));
        root.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.rounded_clip_background));
        root.setClipToOutline(true);

        ImageView bg = new ImageView(requireContext());
        bg.setImageResource(R.drawable.ic_dialog_background);
        bg.setScaleType(ImageView.ScaleType.FIT_XY);
        bg.setAdjustViewBounds(true);
        bg.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        TextView message = new TextView(requireContext());
        message.setText(getString(R.string.text_logout));
        message.setTextSize(16);
        message.setTextColor(Color.parseColor("#4d412e"));
        message.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        message.setPadding(dpToPx(24), dpToPx(24), dpToPx(24), dpToPx(12));

        Button yesButton = new Button(requireContext());
        yesButton.setText(getString(R.string.text_yes));
        yesButton.setTextSize(12);
        yesButton.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.bg_input_rounded));
        yesButton.setPadding(dpToPx(8), dpToPx(4), dpToPx(8), dpToPx(4));

        Button noButton = new Button(requireContext());
        noButton.setText(getString(R.string.text_no));
        noButton.setTextSize(12);
        noButton.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.bg_input_rounded));
        noButton.setPadding(dpToPx(8), dpToPx(4), dpToPx(8), dpToPx(4));

        FrameLayout.LayoutParams yesParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        yesParams.gravity = Gravity.END | Gravity.BOTTOM;
        yesParams.setMargins(0, 0, dpToPx(16), dpToPx(16));

        FrameLayout.LayoutParams noParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        noParams.gravity = Gravity.START | Gravity.BOTTOM;
        noParams.setMargins(dpToPx(16), 0, 0, dpToPx(16));

        yesButton.setLayoutParams(yesParams);
        noButton.setLayoutParams(noParams);

        yesButton.setOnClickListener(v -> {
            firebaseAuth.signOut();
            startActivity(new Intent(getActivity(), LoginActivity.class));
            requireActivity().finish();
        });

        noButton.setOnClickListener(v -> dialog.dismiss());

        root.addView(bg);
        root.addView(message);
        root.addView(yesButton);
        root.addView(noButton);

        dialog.setContentView(root);
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setLayout(dpToPx(240), dpToPx(260));
        }

        dialog.show();
    }


    private void showChangePasswordDialog() {
        Dialog dialog = new Dialog(requireContext());

        FrameLayout root = new FrameLayout(requireContext());
        root.setLayoutParams(new FrameLayout.LayoutParams(dpToPx(300), ViewGroup.LayoutParams.WRAP_CONTENT));
        root.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.rounded_clip_background));
        root.setClipToOutline(true);

        ImageView bg = new ImageView(requireContext());
        bg.setImageResource(R.drawable.ic_dialog_background);
        bg.setScaleType(ImageView.ScaleType.FIT_XY);
        bg.setAdjustViewBounds(true);
        bg.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        View contentView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_change_password, null);
        EditText oldPasswordInput = contentView.findViewById(R.id.editTextOldPassword);
        EditText newPasswordInput = contentView.findViewById(R.id.editTextNewPassword);

        ScrollView scroll = new ScrollView(requireContext());
        scroll.setPadding(dpToPx(16), dpToPx(16), dpToPx(16), dpToPx(16));
        scroll.addView(contentView);

        Button confirmButton = new Button(requireContext());
        confirmButton.setText(getString(R.string.button_confirm));
        confirmButton.setTextSize(12);
        confirmButton.setPadding(dpToPx(16), dpToPx(8), dpToPx(16), dpToPx(8));
        confirmButton.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.bg_input_rounded));

        FrameLayout.LayoutParams btnParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        btnParams.gravity = Gravity.END | Gravity.BOTTOM;
        btnParams.setMargins(0, 0, dpToPx(16), dpToPx(16));
        confirmButton.setLayoutParams(btnParams);

        confirmButton.setOnClickListener(v -> {
            String oldPass = oldPasswordInput.getText().toString().trim();
            String newPass = newPasswordInput.getText().toString().trim();

            if (oldPass.isEmpty() || newPass.isEmpty()) {
                Toast.makeText(requireContext(), getString(R.string.fill_password_field), Toast.LENGTH_SHORT).show();
                return;
            }

            if (newPass.length() < 6) {
                Toast.makeText(requireContext(), getString(R.string.length_password), Toast.LENGTH_SHORT).show();
                return;
            }

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null && user.getEmail() != null) {
                user.reauthenticate(com.google.firebase.auth.EmailAuthProvider.getCredential(user.getEmail(), oldPass))
                        .addOnCompleteListener(authTask -> {
                            if (authTask.isSuccessful()) {
                                user.updatePassword(newPass).addOnCompleteListener(updateTask -> {
                                    if (updateTask.isSuccessful()) {
                                        Toast.makeText(requireContext(), getString(R.string.update_password), Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    } else {
                                        Toast.makeText(requireContext(), getString(R.string.error_password), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                Toast.makeText(requireContext(), getString(R.string.incorrect_password), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        root.addView(bg);
        root.addView(scroll);
        root.addView(confirmButton);

        dialog.setContentView(root);
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setLayout(dpToPx(250), ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        scroll.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            int totalHeight = scroll.getHeight();
            int maxHeight = dpToPx(200);
            FrameLayout.LayoutParams scrollParams = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    Math.min(totalHeight + dpToPx(48), maxHeight)
            );
            scroll.setLayoutParams(scrollParams);
        });

        dialog.show();
    }

    private int dpToPx(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }
    private void showChangeUsernameDialog() {
        Dialog dialog = new Dialog(requireContext());

        FrameLayout root = new FrameLayout(requireContext());
        root.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        root.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.rounded_clip_background));
        root.setClipToOutline(true);

        ImageView bg = new ImageView(requireContext());
        bg.setImageResource(R.drawable.ic_dialog_background);
        bg.setScaleType(ImageView.ScaleType.FIT_XY);
        bg.setAdjustViewBounds(true);
        bg.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        // Wrapper
        LinearLayout contentLayout = new LinearLayout(requireContext());
        contentLayout.setOrientation(LinearLayout.VERTICAL);
        contentLayout.setPadding(dpToPx(24), dpToPx(24), dpToPx(24), dpToPx(24));
        contentLayout.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        contentLayout.setGravity(Gravity.CENTER_HORIZONTAL);

        TextView title = new TextView(requireContext());
        title.setText(getString(R.string.change_username));
        title.setTextSize(18);
        title.setTextColor(Color.parseColor("#4d412e"));
        title.setGravity(Gravity.CENTER);
        title.setPadding(0, 0, 0, dpToPx(12));
        contentLayout.addView(title);

        EditText input = new EditText(requireContext());
        input.setHint(getString(R.string.new_username));
        input.setTextColor(Color.BLACK);
        input.setPadding(dpToPx(16), dpToPx(10), dpToPx(16), dpToPx(10));
        input.setMinWidth(dpToPx(200));
        input.setGravity(Gravity.CENTER);
        input.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.bg_input_rounded));
        contentLayout.addView(input);

        Button confirmButton = new Button(requireContext());
        confirmButton.setText(getString(R.string.text_confirm));
        confirmButton.setTextSize(14);
        confirmButton.setPadding(dpToPx(24), dpToPx(12), dpToPx(24), dpToPx(12));
        confirmButton.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.bg_input_rounded));
        LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        btnParams.gravity = Gravity.CENTER_HORIZONTAL;
        btnParams.setMargins(0, dpToPx(16), 0, 0);
        confirmButton.setLayoutParams(btnParams);

        contentLayout.addView(confirmButton);

        confirmButton.setOnClickListener(v -> {
            String newUsername = input.getText().toString().trim();
            if (newUsername.isEmpty()) {
                Toast.makeText(requireContext(), getString(R.string.invalid_username), Toast.LENGTH_SHORT).show();
                return;
            }

            firebaseUser.updateProfile(new UserProfileChangeRequest.Builder()
                            .setDisplayName(newUsername)
                            .build())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            userIdTextView.setText(newUsername);
                            Toast.makeText(requireContext(), getString(R.string.successful_username_update), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        } else {
                            Toast.makeText(requireContext(), getString(R.string.error_username), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        root.addView(bg);
        root.addView(contentLayout);

        dialog.setContentView(root);
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setLayout(dpToPx(240), ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        dialog.show();
    }

}
