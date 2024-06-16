package com.example.graduationproject.data.remote;

import java.util.List;

public class Transcript {
    private String _id;
    private String className;
    private List<Transcript.StudentGrade> studentGrades;
    private String JsonSignature;
    private String PdfSignature;
    private boolean isSignedJson = false;
    private boolean isSignedPdf = false;

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

    public String getJsonSignature() {
        return JsonSignature;
    }

    public void setJsonSignature(String jsonSignature) {
        JsonSignature = jsonSignature;
    }

    public String getPdfSignature() {
        return PdfSignature;
    }

    public void setPdfSignature(String pdfSignature) {
        PdfSignature = pdfSignature;
    }

    public boolean isSignedJson() {
        return isSignedJson;
    }

    public void setSignedJson(boolean signedJson) {
        isSignedJson = signedJson;
    }

    public boolean isSignedPdf() {
        return isSignedPdf;
    }

    public void setSignedPdf(boolean signedPdf) {
        isSignedPdf = signedPdf;
    }

    public static class StudentGrade{
        private String studentId;
        private String name;
        private float grade;

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

    }
}
