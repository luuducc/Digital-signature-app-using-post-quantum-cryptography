package com.example.graduationproject.data.local;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.graduationproject.data.remote.Transcript;

import java.util.List;

public class MyViewModel extends ViewModel {
    private final MutableLiveData<List<Transcript>> transcripts =
            new MutableLiveData<>();
    public LiveData<List<Transcript>> getTranscripts() {
        return transcripts;
    }
    public void setTranscripts(List<Transcript> transcripts) {
        this.transcripts.setValue(transcripts);
    }
    public void updateTranscripts(Transcript updatedTranscript) {
        List<Transcript> currentTranscripts = transcripts.getValue();

        if (currentTranscripts != null) {
            for (int i = 0; i < currentTranscripts.size(); i++) {
                if (currentTranscripts.get(i).get_id().equals(updatedTranscript.get_id())) {
                    currentTranscripts.set(i, updatedTranscript);
                    break;
                }
            }
            transcripts.setValue(currentTranscripts);
        }
    }
}
