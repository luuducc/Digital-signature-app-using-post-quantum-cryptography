package com.example.graduationproject.data.local;

import com.example.graduationproject.data.remote.Transcript;

import java.util.List;

// Because the transcript fetched from the server is different by the time (eg: isSignedPdf, isSignedJson field changes based on the user's operations
// So only need to hash the based or the important field of the transcript, which is immutable by the time
// However, the studentsGrades could be changed => require signed transcript must not be interfered, or if change studentGrades => must require resign
public class TranscriptToHash {
    private String _id;
    private String className;
    private List<Transcript.StudentGrade> studentGrades;

    public TranscriptToHash(String _id, String className, List<Transcript.StudentGrade> studentGrades) {
        this._id = _id;
        this.className = className;
        this.studentGrades = studentGrades;
    }

    public static TranscriptToHash transfer(Transcript transcript) {
        return new TranscriptToHash(
                transcript.get_id(),
                transcript.getClassName(),
                transcript.getStudentGrades()
        );
    }
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
