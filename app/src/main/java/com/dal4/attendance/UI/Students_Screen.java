package com.dal4.attendance.UI;

import android.app.Dialog;
import android.content.DialogInterface;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.dal4.attendance.Models.StudentModel;
import com.dal4.attendance.Models.SubjectsModel;
import com.dal4.attendance.R;
import com.dal4.attendance.Repository.Repository;

import java.util.ArrayList;
import java.util.List;

public class
Students_Screen extends AppCompatActivity {
    //Views
    private RecyclerView Rv_Student;
    private Button Add_Student_Main;
    private Dialog Dialog_Std_Info, DialogAddNew_Std;
    private TextView SubjectName, StudentNumber;
    private LinearLayout linear_empty_class;

    //Var
    SubjectsModel SubModel;
    List<StudentModel> List = new ArrayList<>();
    List<StudentModel> ListAfterFilter = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_list);
        intiViews();
    }

    private void intiViews() {
        Rv_Student = findViewById(R.id.RV_Student);
        Add_Student_Main = findViewById(R.id.Add_Student_Main);
        SubjectName = findViewById(R.id.Subject_Name);
        StudentNumber = findViewById(R.id.Students_Number);
        linear_empty_class = findViewById(R.id.linear_empty_class);


        SubModel = (SubjectsModel) getIntent().getSerializableExtra("SubjectModel");
        SubjectName.setText(SubModel.getSubName());
        StudentNumber.setText(SubModel.getSubStudentNum() + "/" + SubModel.getCurrentStudent());

        new GetAllStudents().execute();

        //Other Fun
        Add_Student_Main.setOnClickListener(v -> {
            Open_New_Std_Dialog();
        });


    }

    private class GetAllStudents extends AsyncTask<Void, Void, List<StudentModel>> {

        @Override
        protected List<StudentModel> doInBackground(Void... voids) {
            List = Repository.InitStudentsTable(getApplicationContext()).studentsDao().GetAllStudents();

            ListAfterFilter.clear();
            for (int i = 0; i < List.size(); i++) {
                if (List.get(i).getSubUid().equals(SubModel.getUid() + "")) {
                    ListAfterFilter.add(List.get(i));
                    SubModel.setCurrentStudent(ListAfterFilter.size() + "");

                    new Update_SubjectModel().execute(SubModel);
                }
            }
            return List;
        }

        @Override
        protected void onPostExecute(List<StudentModel> studentModels) {
            super.onPostExecute(studentModels);
            Rv_Student.setAdapter(new Std_Adapter(ListAfterFilter));
            StudentNumber.setText(SubModel.getSubStudentNum() + "/" + SubModel.getCurrentStudent());
            if (ListAfterFilter.size() == 0) {
                linear_empty_class.setVisibility(View.VISIBLE);
            } else {
                linear_empty_class.setVisibility(View.GONE);
            }

        }
    }

    private class CreateNewItem extends AsyncTask<StudentModel, Void, Void> {

        @Override
        protected Void doInBackground(StudentModel... studentModels) {
            Repository.InitStudentsTable(getApplicationContext()).studentsDao().CreateNew_Student(studentModels[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            new GetAllStudents().execute();

        }
    }

    public class DeleteItem_Student extends AsyncTask<StudentModel, Void, Void> {

        @Override
        protected Void doInBackground(StudentModel... studentModels) {
            Repository.InitStudentsTable(getApplicationContext()).studentsDao().DeleteItem_Student(studentModels[0]);
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            new GetAllStudents().execute();
        }
    }

    public class UpdateItem_Student extends AsyncTask<StudentModel, Void, Void> {

        @Override
        protected Void doInBackground(StudentModel... studentModels) {
            Repository.InitStudentsTable(getApplicationContext()).studentsDao().Update_Student(studentModels[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            new GetAllStudents().execute();

        }
    }

    class Update_SubjectModel extends AsyncTask<SubjectsModel, Void, Void> {

        @Override
        protected Void doInBackground(SubjectsModel... subjectsModels) {
            Repository.InitSubjectsTable(getApplicationContext()).subDao().Update_Subject(subjectsModels[0]);

            return null;
        }

    }


    private class Std_Adapter extends RecyclerView.Adapter<VH_Sub> {

        List<StudentModel> studentModels = new ArrayList<>();

        public Std_Adapter(List<StudentModel> studentModels) {
            this.studentModels = studentModels;
        }

        @NonNull
        @Override
        public VH_Sub onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.std_item, parent, false);

            return new VH_Sub(view);
        }

        @Override
        public void onBindViewHolder(@NonNull VH_Sub holder, int position) {
            StudentModel studentModel = studentModels.get(position);
            String firstName;
            String middle;

            String lastName;
            String StdName = studentModel.getStdName();
            int numberOfSpace = CountSpace(StdName);

            if (numberOfSpace == 0) {
                firstName = StdName;
                middle = "";
                lastName = "";
            } else if (numberOfSpace == 1) {
                int firstSpace = StdName.indexOf(" ");

                firstName = StdName.substring(0, firstSpace);
                middle = StdName.substring(firstSpace).trim();
                lastName = "";
            } else {
                int length = StdName.length();
                int firstSpace = StdName.indexOf(" ");
                int lastSpace = StdName.lastIndexOf(" ");
                firstName = StdName.substring(0, firstSpace);
                middle = StdName.substring(firstSpace, lastSpace);
                lastName = StdName.substring(firstSpace, length);
            }

            holder.Text_StdName.setText(firstName + " " + middle);
            holder.Text_StdID.setText(studentModel.getStdID());
            holder.Text_StdGroup.setText(studentModel.getStdGroup());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OpenDialogDetail(studentModel.getStdName(), studentModel.getStdID(), studentModel.getStdGroup(), SubModel.getSubName(), studentModel);
                    Dialog_Std_Info.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            notifyDataSetChanged();
                            new GetAllStudents().execute();
                            studentModels.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, studentModels.size());
                        }
                    });
                }

            });


        }

        @Override
        public int getItemCount() {
            return studentModels.size();
        }
    }

    private class VH_Sub extends RecyclerView.ViewHolder {
        TextView Text_StdName, Text_StdGroup, Text_StdID;

        public VH_Sub(@NonNull View itemView) {
            super(itemView);
            Text_StdName = itemView.findViewById(R.id.StdName_item);
            Text_StdGroup = itemView.findViewById(R.id.StdGroup_item);
            Text_StdID = itemView.findViewById(R.id.StdID_item);

        }
    }

    public void Open_New_Std_Dialog() {
        DialogAddNew_Std = new Dialog(this); // Context, this, etc.
        DialogAddNew_Std.setContentView(R.layout.dialog_new_student);

        EditText Ed_Student_Name, Ed_Student_ID, Ed_Student_Group;
        Button Btn_Add;

        Ed_Student_Name = DialogAddNew_Std.findViewById(R.id.Ed_Std_Name);
        Ed_Student_ID = DialogAddNew_Std.findViewById(R.id.Ed_ID);
        Ed_Student_Group = DialogAddNew_Std.findViewById(R.id.Ed_Group);
        Btn_Add = DialogAddNew_Std.findViewById(R.id.Btn_Add_New_Student);

        Btn_Add.setOnClickListener(v -> {
            String Std_Name = Ed_Student_Name.getText().toString();
            String Std_ID = Ed_Student_ID.getText().toString();
            String Std_Group = Ed_Student_Group.getText().toString();

            if (Std_Name.isEmpty() || Std_ID.isEmpty() || Std_Group.isEmpty()) {
                Toast.makeText(Students_Screen.this, "Valid Data", Toast.LENGTH_SHORT).show();
                return;
            }

            StudentModel studentModel = new StudentModel(Std_Name, Std_ID, Std_Group, SubModel.getUid() + "");
            new CreateNewItem().execute(studentModel);

            DialogAddNew_Std.dismiss();
        });

        DialogAddNew_Std.setTitle("Add New Student");
        DialogAddNew_Std.show();
        DialogAddNew_Std.setCanceledOnTouchOutside(true);
        Window window = DialogAddNew_Std.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        DialogAddNew_Std.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    public void OpenDialogDetail(String Name, String Id, String Group, String Subject, StudentModel studentModel) {
        Dialog_Std_Info = new Dialog(this); // Context, this, etc.
        Dialog_Std_Info.setContentView(R.layout.dialog_more_detail_std);

        TextView TextName, TextId, TextGroup, TextSubject, TextSubject_linear_Edit;
        Button Btn_Update;
        ImageView Update, Delete , Close;
        LinearLayout linear_TextView, linear_EditText, linear_Dao_btn;
        EditText Ed_New_Name, Ed_New_ID, Ed_New_Group;

        Ed_New_Name = Dialog_Std_Info.findViewById(R.id.Ed_New_Std_Name);
        Ed_New_ID = Dialog_Std_Info.findViewById(R.id.Ed_New_Id_Detail);
        Ed_New_Group = Dialog_Std_Info.findViewById(R.id.Ed_New_Group_Number_ForStudent);
        TextSubject_linear_Edit = Dialog_Std_Info.findViewById(R.id.TextView_Subject_detail_Update);

        Btn_Update = Dialog_Std_Info.findViewById(R.id.Btn_Update_Student);

        Update = Dialog_Std_Info.findViewById(R.id.Image_Update_Student);
        Delete = Dialog_Std_Info.findViewById(R.id.Image_Delete_Student);
        Close = Dialog_Std_Info.findViewById(R.id.Close_Detail_dialog);

        linear_TextView = Dialog_Std_Info.findViewById(R.id.linear_detail_TextView);
        linear_EditText = Dialog_Std_Info.findViewById(R.id.linear_detail_editTExt);
        linear_Dao_btn = Dialog_Std_Info.findViewById(R.id.linear_Dao_Button);

        TextName = Dialog_Std_Info.findViewById(R.id.StdName_Detail);
        TextId = Dialog_Std_Info.findViewById(R.id.ID_Detail);
        TextGroup = Dialog_Std_Info.findViewById(R.id.Group_Detail);
        TextSubject = Dialog_Std_Info.findViewById(R.id.Subject_Detail);

        Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog_Std_Info.dismiss();
            }
        });
        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DeleteItem_Student().execute(studentModel);
                Dialog_Std_Info.dismiss();
            }
        });

        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linear_TextView.setVisibility(View.GONE);
                linear_EditText.setVisibility(View.VISIBLE);
                linear_Dao_btn.setVisibility(View.GONE);

                Ed_New_Name.setText(Name);
                Ed_New_ID.setText(Id);
                Ed_New_Group.setText(Group);
                TextSubject_linear_Edit.setText(Subject);

            }
        });

        Btn_Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linear_TextView.setVisibility(View.VISIBLE);
                linear_EditText.setVisibility(View.GONE);
                linear_Dao_btn.setVisibility(View.VISIBLE);


                String NewName = Ed_New_Name.getText().toString();
                String NewID = Ed_New_ID.getText().toString();
                String NewGroup = Ed_New_Group.getText().toString();

                studentModel.setStdName(NewName);
                studentModel.setStdID(NewID);
                studentModel.setStdGroup(NewGroup);

                new UpdateItem_Student().execute(studentModel);

                TextName.setText("Name : " + studentModel.getStdName());
                TextId.setText("ID : " + studentModel.getStdID());
                TextGroup.setText("Group : " + studentModel.getStdGroup());
                TextSubject.setText("Subject : " + Subject);
            }
        });



        TextName.setText("Name : " + Name);
        TextId.setText("ID : " + Id);
        TextGroup.setText("Group : " + Group);
        TextSubject.setText("Subject : " + Subject);

        Dialog_Std_Info.setTitle("Student Detail");
        Dialog_Std_Info.show();
        Dialog_Std_Info.setCanceledOnTouchOutside(true);
        Window window = Dialog_Std_Info.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Dialog_Std_Info.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

    }


    @Override
    protected void onStart() {
        super.onStart();
        new GetAllStudents().execute();
    }

    public int CountSpace(String word) {
        int spaceCount = 0;
        for (char c : word.toCharArray()) {
            if (c == ' ') {
                spaceCount++;
            }
        }
        return spaceCount;
    }
}