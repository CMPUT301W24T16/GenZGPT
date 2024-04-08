package com.example.genzgpt;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import com.example.genzgpt.Controller.Firebase;
import com.example.genzgpt.Model.Event;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.jupiter.api.BeforeEach; // Use JUnit 5 BeforeEach
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import com.google.firebase.firestore.CollectionReference;

/**
 * Test cases for the Firebase Class.
 */
public class FirebaseTest {


    @Mock
    private FirebaseFirestore mockedFirestore;

    // Mock CollectionReference
    @Mock
    private CollectionReference mockedCollection;

    private Firebase firebaseInstance;

//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this); // Updated for JUnit 5
//        firebaseInstance = new Firebase(mockedFirestore); // Assuming a constructor that accepts FirebaseFirestore instance
//        when(mockedFirestore.collection("events")).thenReturn(mockedCollection);
//
//    }

    @Test
    public void testAddEventSuccess() {
//
//        Task<DocumentReference> mockedTask = mock(Task.class);
////        System.out.println("Mocked Task created: " + mockedTask);
//        DocumentReference mockedDocumentReference = mock(DocumentReference.class);
////        System.out.println("Mocked DocumentReference created: " + mockedDocumentReference);
//
//
//        doAnswer(invocation -> {
//            OnSuccessListener<DocumentReference> listener = invocation.getArgument(0);
//            listener.onSuccess(mockedDocumentReference);
//            return null;
//        }).when(mockedTask).addOnSuccessListener(any(OnSuccessListener.class));
////        System.out.println("Mocked Task behavior set");
//
//        when(mockedFirestore.collection("events").add(any())).thenReturn(mockedTask);
//        Event mockEvent = new Event("0", "Sample Event", new Date(), "Edmonton", 100, "event_poster.jpeg");
//
//
//        firebaseInstance.addEvent(mockEvent);
//
//
//        verify(mockedFirestore.collection("events")).add(mockEvent.toMap());
    }
}
