package edu.uncc.evaluation05;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import edu.uncc.evaluation05.databinding.FragmentPurchaseListDetailsBinding;
import edu.uncc.evaluation05.databinding.ListItemProductInfoBinding;
import edu.uncc.evaluation05.models.Product;
import edu.uncc.evaluation05.models.PurchaseList;

public class PurchaseListDetailsFragment extends Fragment {
    private static final String ARG_PARAM_PURCHASE_LIST = "ARG_PARAM_PURCHASE_LIST";
    private PurchaseList mPurchaseList;
    private ArrayList<Product> mProducts = new ArrayList<>();
    PurchaseListProductsAdapter adapter;
    public PurchaseListDetailsFragment() {
        // Required empty public constructor
    }

    public static PurchaseListDetailsFragment newInstance(PurchaseList purchaseList) {
        PurchaseListDetailsFragment fragment = new PurchaseListDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM_PURCHASE_LIST, purchaseList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPurchaseList = (PurchaseList) getArguments().getSerializable(ARG_PARAM_PURCHASE_LIST);
        }
    }

    FragmentPurchaseListDetailsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPurchaseListDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Purchase List Details");
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PurchaseListProductsAdapter();
        binding.recyclerView.setAdapter(adapter);

        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.doneViewingPurchaseList();
            }
        });
    }

    class PurchaseListProductsAdapter extends RecyclerView.Adapter<PurchaseListProductsAdapter.PurchaseListViewHolder> {

        @NonNull
        @Override
        public PurchaseListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ListItemProductInfoBinding itemBinding = ListItemProductInfoBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new PurchaseListViewHolder(itemBinding);
        }

        @Override
        public void onBindViewHolder(@NonNull PurchaseListViewHolder holder, int position) {
            holder.bind(mProducts.get(position));
        }

        @Override
        public int getItemCount() {
            return mProducts.size();
        }

        class PurchaseListViewHolder extends RecyclerView.ViewHolder {
            ListItemProductInfoBinding itemBinding;
            public PurchaseListViewHolder(ListItemProductInfoBinding itemBinding) {
                super(itemBinding.getRoot());
                this.itemBinding = itemBinding;
            }

            public void bind(Product product) {

            }
        }
    }


    PurchaseListDetailsListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mListener = (PurchaseListDetailsListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement PurchaseListDetailsListener");
        }
    }

    public interface PurchaseListDetailsListener {
        void doneViewingPurchaseList();
    }
}