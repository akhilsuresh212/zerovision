/* Copyright 2015 The TensorFlow Authors. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
==============================================================================*/

package org.tensorflow.demo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import org.tensorflow.demo.Classifier.Recognition;
import org.tensorflow.tensorflowdemo.MainActivity;

import java.util.List;
import java.util.Locale;

public class RecognitionScoreView extends View {
  private static final float TEXT_SIZE_DIP = 24;
  private List<Recognition> results;
  private final float textSizePx;
  private final Paint fgPaint;
  private final Paint bgPaint;

  private TextToSpeech myTTS;

  public RecognitionScoreView(final Context context, final AttributeSet set) {
    super(context, set);

    textSizePx =
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, TEXT_SIZE_DIP, getResources().getDisplayMetrics());
    fgPaint = new Paint();
    fgPaint.setTextSize(textSizePx);

    bgPaint = new Paint();
    bgPaint.setColor(0xcc4285f4);

      myTTS = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
          @Override
          public void onInit(int status) {
              myTTS.setLanguage(Locale.US);
              speak("Scanning");
          }
      });
  }

  private void speak(String message) {

    if(Build.VERSION.SDK_INT >= 21){
      myTTS.speak(message,TextToSpeech.QUEUE_FLUSH,null,null);
    }else {
      myTTS.speak(message,TextToSpeech.QUEUE_FLUSH,null);
    }
  }

  public void setResults(final List<Recognition> results) {
    this.results = results;
      if(results.size() > 0 )
      {
          speak(results.get(0).getTitle());
      }
    postInvalidate();
  }

  @Override
  public void onDraw(final Canvas canvas) {
    final int x = 10;
    int y = (int) (fgPaint.getTextSize() * 1.5f);

    canvas.drawPaint(bgPaint);

    if (results != null) {
      for (final Recognition recog : results) {
        canvas.drawText(recog.getTitle() + ": " + recog.getConfidence(), x, y, fgPaint);
        y += fgPaint.getTextSize() * 1.5f;
      }
    }
  }
}
