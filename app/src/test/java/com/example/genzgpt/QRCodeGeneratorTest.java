package com.example.genzgpt;

import static android.graphics.Bitmap.createBitmap;
import static com.example.genzgpt.Model.QRCodeGenerator.createQRCodeBitmap;
import static com.example.genzgpt.Model.QRCodeGenerator.generateCheckInQRCode;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import android.graphics.Bitmap;

import com.example.genzgpt.Model.Event;
import java.util.Date;

import org.junit.Before;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.MockitoRule;

//FIXME: God this is a horrendous being, I apologize in advance for whoever has to work on this next.
@RunWith(MockitoJUnitRunner.class)

/**
 * Test cases for the QRCode Generator Class.
 */
public class QRCodeGeneratorTest {
    private Bitmap bitmap;
    @Mock
    private static Bitmap mocked;
    @Before
    public void setup(){
        bitmap = createBitmap(25,25, Bitmap.Config.RGB_565);
        mocked = mock(Bitmap.class);
    }
    @Test
    void testGenerateCheckInQRCode(){
        Event event =  new Event("123", "TestEvent", new Date(),"University Of Alberta", 50, "img");
        //when(createBitmap(25,25, Bitmap.Config.RGB_565)).thenReturn(mocked);

        assertEquals(mocked, bitmap);
    }
    @Test
    void testCreateQRCodeBitmap(){
        assertNotNull(bitmap);
    }
}
