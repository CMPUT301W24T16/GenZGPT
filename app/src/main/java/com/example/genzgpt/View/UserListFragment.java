package com.example.genzgpt.View;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.genzgpt.Controller.Firebase;
import com.example.genzgpt.Model.User;
import com.example.genzgpt.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

/**
 * FIXME CAN WE GET ADMINPROFILESFRAGMENT DELETED AND REFACTOR THIS TO BE ADMINPROFILESFRAGMENT?
 * A simple {@link Fragment} subclass.
 * Serves as a Display for the User's List
 * Use the {@link UserListFragment} factory method to
 * create an instance of this fragment.
 */
public class UserListFragment extends Fragment {
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> userList;
    private Firebase firebase;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment UserListFragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_list_fragment, container, false);

        int spacingInPixels = 16; // Adjust the spacing as needed
        recyclerView = view.findViewById(R.id.userRecyclerView);
        recyclerView.addItemDecoration(new SpacingItemDecoration(spacingInPixels));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        userList = new ArrayList<>();
        userAdapter = new UserAdapter(userList);
        recyclerView.setAdapter(userAdapter);

        firebase = new Firebase();

        // Fetch the list of users from Firestore and update the RecyclerView
        fetchUsers();

        return view;
    }

    /**
     * Fetch the list of users from Firestore and update the RecyclerView
     */
    private void fetchUsers() {
        userList.clear();
        firebase.fetchUsers(new Firebase.OnUsersLoadedListener() {
            @Override
            public void onUsersLoaded(List<User> loadedUsers) {
                userList.clear();
                userList.addAll(loadedUsers);
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onUsersLoadFailed(Exception e) {
                // Handle the error case
                Log.e("UserListFragment", "Failed to load users: " + e.getMessage());
            }
        });
    }

    /**
     * Adapter for the RecyclerView to display the list of users
     */
    private class UserAdapter extends RecyclerView.Adapter<UserViewHolder> {
        private List<User> users;

        public UserAdapter(List<User> users) {
            this.users = users;
        }

        /**
         * Create a new ViewHolder for the RecyclerView
         */
        @Override
        public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
            return new UserViewHolder(itemView);
        }

        /**
         * Bind the user to the ViewHolder
         */
        @Override
        public void onBindViewHolder(UserViewHolder holder, int position) {
            User user = users.get(position);
            holder.bind(user);
        }

        /**
         * Return the number of users in the list
         */
        @Override
        public int getItemCount() {
            return users.size();
        }

        /**
         * Set the list of users to display in the RecyclerView
         */
        public void setUsers(List<User> newUsers) {
            this.users.clear();
            this.users.addAll(newUsers);
            notifyDataSetChanged();
        }

        /**
         * Return the list of users
         */
        public List<User> getUsers() {
            return users;
        }
    }

    /**
     * ViewHolder for the RecyclerView to display the user
     */
    private class UserViewHolder extends RecyclerView.ViewHolder {
        private TextView firstName;
        private TextView lastName;
        private TextView email;
        protected ImageView userImage;


        /**
         * Constructor for the ViewHolder
         */
        public UserViewHolder(View itemView) {
            super(itemView);
            firstName = itemView.findViewById(R.id.first_name);
            lastName = itemView.findViewById(R.id.last_name);
            email = itemView.findViewById(R.id.user_email);
            userImage = itemView.findViewById(R.id.imageView);

        }

        /**
         * Bind the user to the ViewHolder
         */
        public void bind(User user) {
            firstName.setText(user.getFirstName());
            lastName.setText(user.getLastName());
            email.setText(user.getEmail());

            // Load the image using Picasso
            if (user.getImageURL() != null && !user.getImageURL().isEmpty()) {
                Picasso.get()
                        .load(user.getImageURL())
//                        .resize(200, 200) // Specify the desired width and height
                        .into(userImage);
            }

            itemView.setOnClickListener(v -> {
                // Context for creating AlertDialog
                final Context context = v.getContext();

                // Options for the user to select
                final CharSequence[] deleteOptions = {"Delete User", "Delete User Image", "Cancel"};

                // Creating AlertDialog.Builder
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Choose an option");

                // Setting the options
                builder.setItems(deleteOptions, (dialog, item) -> {
                    if (deleteOptions[item].equals("Delete User")) {
                        showDeleteUserDialog(user);
                    } else if (deleteOptions[item].equals("Delete User Image")) {
                        showDeleteUserImageDialog(user);
                    } else if (deleteOptions[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
                });

                builder.show();
            });
        }
    }

    /**
     * Show a dialog to confirm the deletion of the user
     */
    private void showDeleteUserDialog(User user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete User")
                .setMessage("Are you sure you want to delete this user?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    deleteUser(user);
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }

    private void showDeleteUserImageDialog(User user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete User Image")
                .setMessage("Are you sure you want to delete this user Image?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    deleteUserImage(user);
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }

    /**
     * Delete the user from Firestore and update the RecyclerView
     */
    private void deleteUser(User user) {
        firebase.deleteUser(user.getEmail());
        int position = userAdapter.getUsers().indexOf(user);
        if (position != -1) {
            userAdapter.getUsers().remove(position);
            userAdapter.notifyItemRemoved(position);
        }
    }

    private void deleteUserImage(User user) {
        // Call deleteImage to delete the associated image
        if (user.getImageURL() != null) {
            firebase.deleteUserImage(user.getEmail());
        } else {
            Toast.makeText(getContext(), "No user image to delete", Toast.LENGTH_SHORT).show();
        }
    }
}
