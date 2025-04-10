package edu.uncc.evaluation05;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import edu.uncc.evaluation05.databinding.FragmentCreatePurchaseListBinding;
import edu.uncc.evaluation05.databinding.ListItemProductAddBinding;
import edu.uncc.evaluation05.models.AuthResponse;
import edu.uncc.evaluation05.models.Product;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CreatePurchaseListFragment extends Fragment {
    FragmentCreatePurchaseListBinding binding;
    ArrayList<Product> mProducts = new ArrayList<>();
    ProductsAdapter adapter;
    AuthResponse mAuthResponse;

    OkHttpClient client = new OkHttpClient();

    public CreatePurchaseListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCreatePurchaseListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Create Purchase List");

        mAuthResponse = mListener.getAuthResponse();

        binding.buttonCancel.setOnClickListener(v -> mListener.doneCreatingPurchaseList());



        binding.buttonSubmit.setOnClickListener(v -> {
            String name = binding.editTextPurchaseListName.getText().toString();
            if (name.isEmpty()) {
                Toast.makeText(getActivity(), "Enter purchase list name!", Toast.LENGTH_SHORT).show();
            } else {
                StringBuilder productIdsBuilder = new StringBuilder();
                for (Product product : mProducts) {
                    if (product.getQuantity() > 0) { // Include only products with non-zero quantity
                        if (productIdsBuilder.length() > 0) {
                            productIdsBuilder.append(","); // Separate product IDs with a comma
                        }
                        productIdsBuilder.append(product.getPid()).append(":").append(product.getQuantity());
                    }
                }

                String productIds = productIdsBuilder.toString();
                if (productIds.isEmpty()) {
                    Toast.makeText(getActivity(), "No products selected for the purchase list!", Toast.LENGTH_SHORT).show();
                } else {

                    createPurchaseList(name, productIds);
                }
            }
        });

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ProductsAdapter();
        binding.recyclerView.setAdapter(adapter);
        getAllProducts();
    }

    private void createPurchaseList(String name, String productIds) {
        FormBody formBody = new FormBody.Builder()
                .add("name", name)
                .add("productIds", productIds)
                .build();

        Request request = new Request.Builder()
                .url("https://www.theappsdr.com/api/purchases/new")
                .addHeader("Authorization", "Bearer " + mAuthResponse.getToken())
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "Network error. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mListener.doneCreatingPurchaseList();
                            Log.d("Demo", "run: Successfully created purchase list.");
                        }
                    });
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "Unable to create post. Response: " + response.code(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

    private void getAllProducts() {
        Request request = new Request.Builder()
                .url("https://www.theappsdr.com/api/purchases/products")
                .addHeader("Authorization", "Bearer " + mAuthResponse.getToken())
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                getActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "Failed to retrieve products: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String body = response.body().string();
                        JSONObject json = new JSONObject(body);
                        JSONArray productsArray = json.getJSONArray("products");

                        mProducts.clear();

                        for (int i = 0; i < productsArray.length(); i++) {
                            JSONObject productJson = productsArray.getJSONObject(i);
                            String pid = productJson.getString("pid");
                            String name = productJson.getString("name");
                            String imgUrl = productJson.getString("img_url");
                            double price = productJson.getDouble("price_per_item");

                            mProducts.add(new Product(pid, name, imgUrl, price));
                        }

                        getActivity().runOnUiThread(() -> adapter.notifyDataSetChanged());
                    } catch (JSONException e) {
                        e.printStackTrace();
                        getActivity().runOnUiThread(() ->
                                Toast.makeText(getContext(), "Error parsing product data", Toast.LENGTH_SHORT).show());
                    }
                } else {
                    getActivity().runOnUiThread(() ->
                            Toast.makeText(getContext(), "Failed to fetch products: " + response.message(), Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

    class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductViewHolder> {
        @NonNull
        @Override
        public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ListItemProductAddBinding itemBinding = ListItemProductAddBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new ProductViewHolder(itemBinding);
        }

        @Override
        public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
            holder.bind(mProducts.get(position));
        }

        @Override
        public int getItemCount() {
            return mProducts.size();
        }

        private void updateOverallCost() {
            double overallCost = 0.0;
            for (Product product : mProducts) {
                overallCost += product.getPrice_per_item() * product.getQuantity();
            }
            binding.textViewOverallCost.setText("Overall Cost: $" + String.format("%.2f", overallCost));
        }

        class ProductViewHolder extends RecyclerView.ViewHolder {
            ListItemProductAddBinding itemBinding;
            Product mProduct;

            public ProductViewHolder(ListItemProductAddBinding itemBinding) {
                super(itemBinding.getRoot());
                this.itemBinding = itemBinding;
            }

            public void bind(Product product) {
                mProduct = product;

                // Display product details
                itemBinding.textViewName.setText(mProduct.getName());
                itemBinding.textViewCostPerItem.setText("$" + mProduct.getPrice_per_item());
                itemBinding.textViewItemQuantity.setText(String.valueOf(mProduct.getQuantity()));

                // Load product image using Picasso
                Picasso.get()
                        .load(mProduct.getImg_url())
                        .into(itemBinding.imageViewIcon);

                // Handle Increment
                itemBinding.imageViewPlus.setOnClickListener(v -> {
                    mProduct.setQuantity(mProduct.getQuantity() + 1);
                    itemBinding.textViewItemQuantity.setText(String.valueOf(mProduct.getQuantity()));
                    updateOverallCost();
                });

                // Handle Decrement
                itemBinding.imageViewMinus.setOnClickListener(v -> {
                    if (mProduct.getQuantity() > 0) {
                        mProduct.setQuantity(mProduct.getQuantity() - 1);
                        itemBinding.textViewItemQuantity.setText(String.valueOf(mProduct.getQuantity()));
                        updateOverallCost();
                    }
                });
            }
        }
    }

    CreatePurchaseListListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mListener = (CreatePurchaseListListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement CreatePurchaseListListener");
        }
    }

    public interface CreatePurchaseListListener {
        void doneCreatingPurchaseList();

        AuthResponse getAuthResponse();
    }
}
