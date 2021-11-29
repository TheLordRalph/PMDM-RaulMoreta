package com.juanagui.room;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.juanagui.room.db.AppDatabase;
import com.juanagui.room.db.Student;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class EditStudentActivity extends AppCompatActivity {

    public static final String TAG = EditStudentActivity.class.getName();
    public static final String STUDENT_ID_KEY = "STUDENT_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_student);

        AppDatabase appDatabase = ((RoomApplication) getApplication()).appDatabase;

        Consumer<Student> studentConsumer = new Consumer<Student>() {
            @Override
            public void accept(Student student) {
                EditText editStudentNameText = findViewById(R.id.edit_student_name);
                editStudentNameText.setText(student.name);
                EditText editStudentFirstSurnameText = findViewById(R.id.edit_student_first_surname);
                editStudentFirstSurnameText.setText(student.firstSurname);
                EditText editStudentSecondSurnameText = findViewById(R.id.edit_student_second_surname);
                editStudentSecondSurnameText.setText(student.secondSurname);

                findViewById(R.id.save_student_button).setOnClickListener(view -> {
                    student.name = editStudentNameText.getText().toString();
                    student.firstSurname = editStudentFirstSurnameText.getText().toString();
                    student.secondSurname = editStudentSecondSurnameText.getText().toString();

                    Action navigateToMainActivityAction = new Action() {
                        @Override
                        public void run() throws Throwable {
                            Intent intent = new Intent();
                            intent.setClass(EditStudentActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    };

                    if (student.sid > 0) {
                        appDatabase.studentDao().updateStudent(student).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(navigateToMainActivityAction);
                    } else {
                        appDatabase.studentDao().insertStudent(student).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(navigateToMainActivityAction);
                    }
                });
            }
        };

        int studentId = getIntent().getIntExtra(STUDENT_ID_KEY, 0);
        if (studentId > 0) {
            appDatabase.studentDao().find(studentId).subscribeOn(Schedulers.io()).subscribe(studentConsumer);
        } else {
            try {
                studentConsumer.accept(new Student());
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }
}