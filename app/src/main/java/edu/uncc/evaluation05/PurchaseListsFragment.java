package edu.uncc.evaluation05;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import edu.uncc.evaluation05.databinding.FragmentPurchaseListsBinding;
import edu.uncc.evaluation05.databinding.ListItemProductlistBinding;
import edu.uncc.evaluation05.models.AuthResponse;
import edu.uncc.evaluation05.models.Product;
import edu.uncc.evaluation05.models.PurchaseList;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PurchaseListsFragment extends Fragment {


    public PurchaseListsFragment() {
        // Required empty public constructor
    }

    FragmentPurchaseListsBinding binding;
    AuthResponse mAuthResponse;
    ArrayList<PurchaseList> mPurchaseLists = new ArrayList<>();
    PurchaseListsAdapter adapter;
    OkHttpClient client = new OkHttpClient();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPurchaseListsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Purchase Lists");
        mAuthResponse = mListener.getAuthResponse();
        MenuHost menuHost = (MenuHost) getActivity();
        menuHost.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.main_menu, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if(menuItem.getItemId() == R.id.action_logout){
                    mListener.logout();
                    return true;
                } else if(menuItem.getItemId() == R.id.action_add){
                    mListener.gotoCreatePurchaseList();
                    return true;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PurchaseListsAdapter();
        binding.recyclerView.setAdapter(adapter);
        getMyPurchaseLists();

    }

/*
    private void getMyPurchaseLists() {
        // Build the request to fetch all purchase lists
        Request request = new Request.Builder()
                .url("https://www.theappsdr.com/api/purchases/lists")
                .addHeader("Authorization", "Bearer " + mAuthResponse.getToken())
                .build();

        // Use OkHttp client to send the request
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                // Handle failure to connect or fetch data
                getActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "Failed to retrieve purchase lists: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        // Parse the response body
                        String body = response.body().string();
                        JSONObject json = new JSONObject(body);
                        JSONArray purchaseListsArray = json.getJSONArray("purchase_lists");

                        // Clear the current list to avoid duplicates
                        mPurchaseLists.clear();

                        // Iterate through the JSON array of purchase lists
                        for (int i = 0; i < purchaseListsArray.length(); i++) {
                            JSONObject purchaseListJson = purchaseListsArray.getJSONObject(i);

                            // Extract purchase list details
                            String plid = purchaseListJson.getString("plid");
                            String name = purchaseListJson.getString("name");
                            JSONArray itemsArray = purchaseListJson.getJSONArray("items");

                            // Prepare a list of products
                            ArrayList<Product> products = new ArrayList<>();
                            for (int j = 0; j < itemsArray.length(); j++) {
                                JSONObject itemJson = itemsArray.getJSONObject(j);

                                // Extract product details
                                String pid = itemJson.getString("pid");
                                int quantity = itemJson.getInt("quantity");
                                String productName = itemJson.getString("name");
                                double price = itemJson.getDouble("price_per_item");
                                String img_url = itemJson.getString("img_url");

                                // Create and add a Product object
                                products.add(new Product(pid, productName, img_url, price, quantity));
                            }

                            // Create and add a PurchaseList object
                            mPurchaseLists.add(new PurchaseList(plid, name, products));
                        }

                        // Update the RecyclerView on the main thread
                        getActivity().runOnUiThread(() -> adapter.notifyDataSetChanged());
                    } catch (JSONException e) {
                        // Handle JSON parsing errors
                        getActivity().runOnUiThread(() ->
                                Toast.makeText(getContext(), "Error parsing purchase list data", Toast.LENGTH_SHORT).show());
                    }
                } else {
                    // Handle unsuccessful response
                    getActivity().runOnUiThread(() ->
                            Toast.makeText(getContext(), "Failed to fetch purchase lists: " + response.message(), Toast.LENGTH_SHORT).show());
                }
            }
        });
    }*/

    private void getMyPurchaseLists() {
        // Build the request to fetch all purchase lists
        Request request = new Request.Builder()
                .url("https://www.theappsdr.com/api/purchases/lists")
                .addHeader("Authorization", "Bearer " + mAuthResponse.getToken())
                .build();

        // Use OkHttp client to send the request
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                // Handle failure to connect or fetch data
                getActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "Failed to retrieve purchase lists: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        // Parse the response body
                        String body = response.body().string();
                        JSONObject json = new JSONObject(body);
                        JSONArray purchaseListsArray = json.getJSONArray("purchase_lists");

                        // Clear the current list to avoid duplicates
                        mPurchaseLists.clear();

                        // Iterate through the JSON array of purchase lists
                        for (int i = 0; i < purchaseListsArray.length(); i++) {
                            JSONObject purchaseListJson = purchaseListsArray.getJSONObject(i);

                            // Extract purchase list details
                            String plid = purchaseListJson.getString("plid");
                            String name = purchaseListJson.getString("name");
                            JSONArray itemsArray = purchaseListJson.getJSONArray("items");

                            // Prepare a list of products
                            ArrayList<Product> products = new ArrayList<>();
                            int totalItems = 0;
                            double totalCost = 0.0;

                            for (int j = 0; j < itemsArray.length(); j++) {
                                JSONObject itemJson = itemsArray.getJSONObject(j);

                                // Extract product details
                                String pid = itemJson.getString("pid");
                                String productName = itemJson.getString("name");
                                int quantity = itemJson.getInt("quantity");
                                double price = itemJson.getDouble("price_per_item");
                                String imageUrl = itemJson.getString("img_url");

                                // Update total items and total cost
                                totalItems += quantity;
                                totalCost += quantity * price;

                                // Create and add a Product object
                                products.add(new Product(pid, productName,imageUrl,price, quantity ));
                            }

                            // Create and add a PurchaseList object with total cost and total items calculated
                            PurchaseList purchaseList = new PurchaseList(plid, name, products);
                            purchaseList.setTotalItems(totalItems);
                            purchaseList.setTotalCost(totalCost);
                            mPurchaseLists.add(purchaseList);
                        }

                        // Update the RecyclerView on the main thread
                        getActivity().runOnUiThread(() -> adapter.notifyDataSetChanged());
                    } catch (JSONException e) {
                        // Handle JSON parsing errors
                        getActivity().runOnUiThread(() ->
                                Toast.makeText(getContext(), "Error parsing purchase list data", Toast.LENGTH_SHORT).show());
                    }
                } else {
                    // Handle unsuccessful response
                    getActivity().runOnUiThread(() ->
                            Toast.makeText(getContext(), "Failed to fetch purchase lists: " + response.message(), Toast.LENGTH_SHORT).show());
                }
            }
        });
    }


    private void deletePurchaseList(PurchaseList purchaseList) {

    }



    class PurchaseListsAdapter extends RecyclerView.Adapter<PurchaseListsAdapter.PurchaseListViewHolder>{

        @NonNull
        @Override
        public PurchaseListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ListItemProductlistBinding rowItemBinding = ListItemProductlistBinding.inflate(getLayoutInflater(), parent, false);
            return new PurchaseListViewHolder(rowItemBinding);
        }

        @Override
        public void onBindViewHolder(@NonNull PurchaseListViewHolder holder, int position) {
            holder.bind(mPurchaseLists.get(position));
        }

        @Override
        public int getItemCount() {
            return mPurchaseLists.size();
        }

        class PurchaseListViewHolder extends RecyclerView.ViewHolder{
            ListItemProductlistBinding rowItemBinding;
            public PurchaseListViewHolder(ListItemProductlistBinding rowItemBinding) {
                super(rowItemBinding.getRoot());
                this.rowItemBinding = rowItemBinding;
            }

            public void bind(PurchaseList purchaseList) {
                // Set the list name
                rowItemBinding.textViewName.setText(purchaseList.getName());


                // Use the pre-calculated total items and total cost
                rowItemBinding.textViewTotalItems.setText("Total Items: " + purchaseList.getTotalItems() + " Items");
                rowItemBinding.textViewTotalCost.setText("Total Cost: $" + String.format("%.2f", purchaseList.getTotalCost()));

                // Set delete button functionality
                rowItemBinding.imageViewDelete.setOnClickListener(v -> deletePurchaseList(purchaseList));
            }

        }
    }





    PurchaseListsListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof PurchaseListsListener) {
            mListener = (PurchaseListsListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement PurchaseListsListener");
        }
    }

    public interface PurchaseListsListener {
        void logout();
        AuthResponse getAuthResponse();
        void gotoCreatePurchaseList();
        void gotoPurchaseListDetails(PurchaseList purchaseList);
    }
}