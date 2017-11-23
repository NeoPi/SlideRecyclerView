package com.neopi.slideitem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    private SlideRecyclerView recyclerView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view) ;
        recyclerView.setLayoutManager(new SlideLinearLayoutManager(this));
        recyclerView.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_main,parent,false) ;
                return new ViewHoder(view);
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                ((ViewHoder)holder).bindData(position);
            }

            @Override
            public int getItemCount() {
                return 10;
            }
        });
    }

    public static class ViewHoder extends RecyclerView.ViewHolder {
        TextView content ;
        public ViewHoder(View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.content) ;

        }

        public void bindData(int position){
            content.setText("position:"+position);
        }
    }
}
