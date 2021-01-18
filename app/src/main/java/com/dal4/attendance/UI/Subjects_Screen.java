package com.dal4.attendance.UI;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.dal4.attendance.Models.SubjectsModel;
import com.dal4.attendance.R;
import com.dal4.attendance.Repository.Repository;

import java.util.ArrayList;
import java.util.List;

public class Subjects_Screen extends AppCompatActivity {
    //Views
    RecyclerView Rv_Sub;
    Button Btn_Add_Subject_Main;
    Dialog dialog;
    RelativeLayout Subjects_list;
    LinearLayout linear_empty_class;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects_list);
        initViews();
    }

    private void initViews() {
        //initViews
        Btn_Add_Subject_Main = findViewById(R.id.Btn_Add_Subject_Main);
        Rv_Sub = findViewById(R.id.RV_Subjects);
        Subjects_list = findViewById(R.id.Subjects_list);
        linear_empty_class = findViewById(R.id.linear_empty_classes);

        //Other FUn
        Btn_Add_Subject_Main.setOnClickListener(v ->
                OpenCodeDialog()
        );


    }

    class GetAllSubjects extends AsyncTask<Void, Void, List<SubjectsModel>> {

        List<SubjectsModel> List = new ArrayList<>();

        @Override
        protected List<SubjectsModel> doInBackground(Void... voids) {

            List = Repository.InitSubjectsTable(getApplicationContext()).subDao().GetAll_Subjects();

            return List;
        }

        @Override
        protected void onPostExecute(List<SubjectsModel> models) {
            super.onPostExecute(models);
            if (List.size() == 0) {
                linear_empty_class.setVisibility(View.VISIBLE);
            } else {
                linear_empty_class.setVisibility(View.GONE);
            }
            Rv_Sub.setAdapter(new Sub_Adapter(List));

        }
    }

    class Insert extends AsyncTask<SubjectsModel, Void, Void> {

        @Override
        protected Void doInBackground(SubjectsModel... models) {

            Repository.InitSubjectsTable(getApplicationContext()).subDao().CreateNew_Subject(models[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            new GetAllSubjects().execute();

        }
    }

    class DeleteItem extends AsyncTask<SubjectsModel, Void, Void> {

        @Override
        protected Void doInBackground(SubjectsModel... subjectsModels) {
            Repository.InitSubjectsTable(getApplicationContext()).subDao().DeleteItem_Subject(subjectsModels[0]);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            new GetAllSubjects().execute();
        }
    }

    class UpdateItem extends AsyncTask<SubjectsModel, Void, Void> {

        @Override
        protected Void doInBackground(SubjectsModel... subjectsModels) {
            Repository.InitSubjectsTable(getApplicationContext()).subDao().Update_Subject(subjectsModels[0]);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            new GetAllSubjects().execute();

        }
    }

    public void OpenCodeDialog() {
        dialog = new Dialog(this); // Context, this, etc.
        dialog.setContentView(R.layout.dialog_new_subject);

        EditText Ed_Subject_Name, Ed_Group_Number, Ed_Student_Num;
        Button Btn_Add;

        Ed_Subject_Name = dialog.findViewById(R.id.Ed_Sub_Name);
        Ed_Group_Number = dialog.findViewById(R.id.Ed_Group_Number_ForSubject);
        Ed_Student_Num = dialog.findViewById(R.id.Ed_Student_Number);
        Btn_Add = dialog.findViewById(R.id.Btn_Add_New_Subject);

        Btn_Add.setOnClickListener(v -> {
            String Sub_Name = Ed_Subject_Name.getText().toString();
            String GroupNum = Ed_Group_Number.getText().toString();
            String StudentNum = Ed_Student_Num.getText().toString();

            if (Sub_Name.isEmpty() || GroupNum.isEmpty() || StudentNum.isEmpty()) {
                Toast.makeText(Subjects_Screen.this, "Valid Data", Toast.LENGTH_SHORT).show();
                return;
            }

            SubjectsModel subjectsModel = new SubjectsModel(Sub_Name, GroupNum, StudentNum, "0");
            new Insert().execute(subjectsModel);
            dialog.dismiss();
        });


        dialog.setTitle("Add New Subject");
        dialog.show();
        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

    }

    public void OpenUpdateDialog(SubjectsModel subjectsModel) {
        dialog = new Dialog(this); // Context, this, etc.
        dialog.setContentView(R.layout.dialog_update_subject);

        EditText Ed_New_Subject_Name, Ed_New_Group_Number, Ed_New_Student_Num;
        Button Btn_Update;

        Ed_New_Subject_Name = dialog.findViewById(R.id.Ed_New_Sub_Name);
        Ed_New_Group_Number = dialog.findViewById(R.id.Ed_New_Group_Number_ForSubject);
        Ed_New_Student_Num = dialog.findViewById(R.id.Ed_New_Student_Number);
        Btn_Update = dialog.findViewById(R.id.Btn_Update_Subject);

        Ed_New_Subject_Name.setText(subjectsModel.getSubName());
        Ed_New_Group_Number.setText(subjectsModel.getSubGroup());
        Ed_New_Student_Num.setText(subjectsModel.getSubStudentNum());

        Btn_Update.setOnClickListener(v -> {
            String Sub_Name = Ed_New_Subject_Name.getText().toString();
            String GroupNum = Ed_New_Group_Number.getText().toString();
            String StudentNum = Ed_New_Student_Num.getText().toString();

            if (Sub_Name.isEmpty() || GroupNum.isEmpty() || StudentNum.isEmpty()) {
                Toast.makeText(Subjects_Screen.this, "Valid Data", Toast.LENGTH_SHORT).show();

                return;
            }

            subjectsModel.setSubName(Sub_Name);
            subjectsModel.setSubGroup(GroupNum);
            subjectsModel.setSubStudentNum(StudentNum);

            new UpdateItem().execute(subjectsModel);
            dialog.dismiss();
        });


        dialog.setTitle("Add New Subject");
        dialog.show();
        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

    }

    public class Sub_Adapter extends RecyclerView.Adapter<VH_Sub> {

        List<SubjectsModel> subjectsLists;


        public Sub_Adapter(List<SubjectsModel> subjectsLists) {
            this.subjectsLists = subjectsLists;
        }

        @NonNull
        @Override
        public VH_Sub onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.sub_item, parent, false);

            return new VH_Sub(view);
        }

        @Override
        public void onBindViewHolder(@NonNull VH_Sub holder, int position) {
            SubjectsModel subjectsModel = subjectsLists.get(position);
            String sub_name = subjectsModel.getSubName();
            String sub_group = subjectsModel.getSubGroup();
            String sub_Attendance = subjectsModel.getSubStudentNum();
            String sub_Current_Attendance = subjectsModel.getCurrentStudent();


            holder.SubName.setText(sub_name);
            holder.Group.setText(sub_group);
            holder.Attendance.setText(sub_Attendance + "/" + sub_Current_Attendance);

            ColorView(holder.view, subjectsModel.getCurrentStudent(), subjectsModel.getSubStudentNum());
            holder.itemView.setOnLongClickListener(v -> {

                holder.DeleteItem.setVisibility(View.VISIBLE);
                holder.DaoLayout.setVisibility(View.VISIBLE);
                return true;
            });

            holder.CloseDeleteItem.setOnClickListener(v -> {
                holder.DeleteItem.setVisibility(View.GONE);
                holder.DaoLayout.setVisibility(View.GONE);

            });

            holder.DeleteItem.setOnClickListener(v -> {
                subjectsLists.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, subjectsLists.size());
                new DeleteItem().execute(subjectsModel);
            });

            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(Subjects_Screen.this, Students_Screen.class);
                intent.putExtra("SubjectModel", subjectsModel);
                startActivity(intent);
            });

            holder.UpdateItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OpenUpdateDialog(subjectsModel);

                }
            });
        }

        @Override
        public int getItemCount() {
            return subjectsLists.size();
        }
    }

    private class VH_Sub extends RecyclerView.ViewHolder {
        TextView SubName, Attendance, Group;
        ImageView DeleteItem, CloseDeleteItem, UpdateItem;
        RelativeLayout DaoLayout;
        View view;
        CardView cardView;

        public VH_Sub(@NonNull View itemView) {
            super(itemView);
            SubName = itemView.findViewById(R.id.SubName_item);
            Attendance = itemView.findViewById(R.id.Attendance_item);
            Group = itemView.findViewById(R.id.Group_item);
            DeleteItem = itemView.findViewById(R.id.Delete_Item);
            CloseDeleteItem = itemView.findViewById(R.id.Close_Deleted_Item);
            cardView = itemView.findViewById(R.id.CardView);
            DaoLayout = itemView.findViewById(R.id.Dao_Layout);
            UpdateItem = itemView.findViewById(R.id.Update_Item);
            view = itemView.findViewById(R.id.SubView);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        new GetAllSubjects().execute();
    }

    public void ColorView(View view, String CurrentStd, String AllStd) {

        int IntCurrentStd = Integer.parseInt(CurrentStd);
        int IntAllStd = Integer.parseInt(AllStd);
        int law = IntAllStd / 3;
        int medium = IntAllStd / 2;

        if (IntCurrentStd <= law) {
            view.setBackground(getResources().getDrawable(R.drawable.shape_radius_red));
        } else if (IntCurrentStd > law && IntCurrentStd <= medium) {
            view.setBackground(getResources().getDrawable(R.drawable.shape_radius_orange));
        } else {
            view.setBackground(getResources().getDrawable(R.drawable.shape_radius_green));
        }
    }

}