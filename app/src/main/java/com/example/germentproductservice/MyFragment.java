package com.example.germentproductservice;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyFragment extends Fragment {

    ArrayList<ProductModel> productsData = new ArrayList<>();
    RecyclerView recyclerView;
    String viewProfile = MainActivity.serverURL+"/showProduct";
    private Handler handler = new Handler();
    private Runnable runnable;

    public MyFragment() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.product_service, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager( new LinearLayoutManager(container.getContext()));



        SendRequest task = new SendRequest(viewProfile,container.getContext());

        task.execute("{ \"data\" : \"get request\" }");
        runnable = new Runnable() {
            @Override
            public void run() {
                if (task.result !=""){

                    try {
                        JSONObject mr = new JSONObject(task.result);
                        if (mr.getBoolean("isOK")) {

                            JSONArray productsArray = new JSONArray(String.valueOf(mr.get("result")));
                            for ( int i = 0;i< productsArray.length();i++){
                                JSONObject productJson ;
                                try {
                                    productJson = new JSONObject(String.valueOf(productsArray.get(i)));
                                    int productId = productJson.getInt("product_id");
                                    String productName = productJson.getString("product_name");
                                    int price = productJson.getInt("price");
                                    String size = productJson.getString("size");
                                    String color = productJson.getString("color");
                                    String detail = productJson.getString("detail");

                                    productsData.add( new ProductModel(productId,productName,price,size,color,detail));
                                }catch (Exception e){}
                            }

                            MyAdapter myAdapter = new MyAdapter(productsData, container.getContext());
                            recyclerView.setAdapter(myAdapter);
                        }else {
                            Toast.makeText(container.getContext(), "An Error !. Please log in again", Toast.LENGTH_SHORT).show();

                        }

                    }catch (Exception e){}

                    handler.removeCallbacks(this);
                }else {
                    handler.postDelayed(this, 1000);
                }
            }
        };
        handler.postDelayed(runnable,1000);






        return view;
    }
}

class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{
    ArrayList<ProductModel> productsData  = new ArrayList<>();
    Context context;
  MyAdapter(ArrayList<ProductModel> pm, Context ctx){
      productsData = pm;
      context = ctx;
  }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
      holder.productName.setText(productsData.get(position).product_name);
      holder.price.setText("Price "+String.valueOf(productsData.get(position).price)+" tk");
      holder.buyTextView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              String []productDetails = {String.valueOf(productsData.get(position).getProduct_id()), productsData.get(position).getProduct_name(), String.valueOf(productsData.get(position).getPrice()),productsData.get(position).getSize(),productsData.get(position).getColor()};

              Intent intent = new Intent(context,ProductDetails.class);
              intent.putExtra("productDetails",productDetails);
              context.startActivity(intent);
          }
      });
    }

    @Override
    public int getItemCount() {
        return productsData.size();
    }
}
class MyViewHolder extends RecyclerView.ViewHolder{
    TextView productName ,price, buyTextView;
    public MyViewHolder(@NonNull View itemView) {

        super(itemView);
        productName = itemView.findViewById(R.id.product_name);
        price = itemView.findViewById(R.id.product_price);
        buyTextView = itemView.findViewById(R.id.buyTextView);
    }
}



class ProductModel {
    String product_name, color, size,detail;
    int product_id,price;
    ProductModel(int id, String pn, int price , String size, String color,String dtl){
        this.product_id = id;
        this.product_name = pn;
        this.size = size;
        this.color = color;
        this.price = price;
        this.detail = dtl;
    }
    public int getProduct_id(){
        return product_id;
    }

    public String getColor() {
        return color;
    }

    public int getPrice() {
        return price;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getSize() {
        return size;
    }

    public String getDetail() {
        return detail;
    }
}