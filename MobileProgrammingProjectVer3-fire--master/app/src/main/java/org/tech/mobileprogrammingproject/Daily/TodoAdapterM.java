package org.tech.mobileprogrammingproject.Daily;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.tech.mobileprogrammingproject.R;

import java.util.ArrayList;
import java.util.List;

public class TodoAdapterM extends RecyclerView.Adapter<TodoAdapterM.Holder> {

    private Context context;
    private List<TodoM> morningTodo = new ArrayList<>();
    private EditText addedText;

    public TodoAdapterM(Context context, List<TodoM> morningTodo) {
        this.context = context;
        this.morningTodo = morningTodo;
    }

    // ViewHolder 생성
    // row layout을 화면에 뿌려주고 holder에 연결
    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todolist_layout, parent, false);
        Holder holder = new Holder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(Holder holder, int position) {
        // 각 위치에 문자열 세팅
        final TodoM todoM = morningTodo.get(position);
        if(addedText == null){
            holder.todo_item.setText("");
        } else {
            holder.todo_item.setText(addedText.getText().toString());
        }
    }


    @Override
    public int getItemCount() {
        return (morningTodo != null ? morningTodo.size() : 0); // RecyclerView의 size return
    }

    // ViewHolder는 하나의 View를 보존하는 역할을 한다
    public class Holder extends RecyclerView.ViewHolder{
        public TextView todo_item;
        public EditText addedText;

        public Holder(View view){
            super(view);
            todo_item = (TextView) view.findViewById(R.id.todo_list_item);
            addedText = (EditText) view.findViewById(R.id.add_todo);
        }
    }

}
