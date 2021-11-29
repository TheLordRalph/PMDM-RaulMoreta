package com.juanagui.room;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.juanagui.room.db.Student;

public class StudentViewHolder extends RecyclerView.ViewHolder {

    private final TextView _nameTextView;
    private final TextView _firstSurnameTextView;
    private final TextView _secondSurnameTextView;

    public StudentViewHolder(@NonNull View itemView, StudentAdapter.StudentClickListener studentClickListener) {
        super(itemView);
        _nameTextView = itemView.findViewById(R.id.name);
        _firstSurnameTextView = itemView.findViewById(R.id.first_surname);
        _secondSurnameTextView = itemView.findViewById(R.id.second_surname);
        ImageButton buttonDelete = itemView.findViewById(R.id.button_delete);
        buttonDelete.setOnClickListener(view -> studentClickListener.onStudentDelete(getAdapterPosition()));
        ImageButton buttonEdit = itemView.findViewById(R.id.button_details);
        buttonEdit.setOnClickListener(view -> studentClickListener.onStudentEdit(getAdapterPosition()));
    }

    public void bind(Student student) {
        _nameTextView.setText(student.name);
        _firstSurnameTextView.setText(student.firstSurname);
        _secondSurnameTextView.setText(student.secondSurname);
    }
}