package com.example.graduationproject.data.remote;

import com.example.graduationproject.TranscriptData;

import java.util.List;

public class Transcript {
    private String _id;
    private String className;
    private List<Transcript.StudentGrade> studentGrades;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<Transcript.StudentGrade> getStudentGrades() {
        return studentGrades;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

//    public void setStudentGrades(List<TranscriptData.studentGrades> studentGrades) {
//        this.studentGrades = studentGrades;
//    }

    public static class StudentGrade{
        private String studentId;
        private String name;
        private float grade;
        private int __v;

        public String getStudentId() {
            return studentId;
        }

        public void setStudentId(String studentId) {
            this.studentId = studentId;
        }

        public StudentGrade(String studentId, String name, float grade) {
            this.studentId = studentId;
            this.name = name;
            this.grade = grade;
        }


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public float getGrade() {
            return grade;
        }

        public void setGrade(float grade) {
            this.grade = grade;
        }

        public int get__v() {
            return __v;
        }

        public void set__v(int __v) {
            this.__v = __v;
        }
    }
}
