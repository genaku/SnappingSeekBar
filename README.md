SnappingSeekBar
===============
 
This is a sample app which contains my implementation of a snapping seek bar. If you build the project you can see some different seek bar examples which are all attached to the same OnItemSelectionListener.
 
##Video
![](https://github.com/jozsefmezei/SnappingSeekBar/master/sample.gif)


##Features
 - updated project
 - reached indicators have other colors
 - indicators can represent themselves by different drawables
 - added SeekbarElement model
 - simplify code
 
##Fixed issues
 - indicators have bad positions
 - thumb has bad positions
 - SnappingSeekBar has to build itself again
 - other bugs
 
##Usage
 
####In xml use the following attributes:

```xml
<hu.mesys.snappingseekbar.library.views.SnappingSeekBar
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:progressDrawable="@drawable/progress"
    app:thumbDrawable="@drawable/apptheme_scrubber_control_selector_holo_light"
    app:progressColor="@color/blue"
    app:thumbnailColor="@color/blue_light"
    app:indicatorColor="@color/white"
    app:indicatorTextColor="@color/white"
    app:indicatorTextSize="12dp"
    app:indicatorSize="12dp"
    app:itemsArrayId="@array/seek_bar_with_big_indicators_items"/> 
```

</br>
</br>
Most of the attributes have a default value, so the minimum setup looks like the following:

```xml
<hu.mesys.snappingseekbar.library.views.SnappingSeekBar
    android:id="@+id/activity_main_seek_bar_without_texts"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:indicatorAmount="5"/>
```

</br>
####From code it could look like this:
``` java
//Find view
 snappingSeekBar = (SnappingSeekBar) findViewById(R.id.snapp);
 ...
 
 private void snappingSeekBarFromList() {
        // Create list with: indicator drawable, indicator color, reached indicator color, indicator text, indicator text color 
         elements = new ArrayList<>();
         elements.add(new SeekbarElement(R.drawable.circle_background, ContextCompat.getColor(this, R.color.blue), ContextCompat.getColor(this, R.color.red), "1", ContextCompat.getColor(this, R.color.blue)));
         elements.add(new SeekbarElement(R.drawable.circle_background_small, ContextCompat.getColor(this, R.color.blue), ContextCompat.getColor(this, R.color.red), "2", ContextCompat.getColor(this, R.color.blue)));
         elements.add(new SeekbarElement(R.drawable.circle_background, ContextCompat.getColor(this, R.color.blue), ContextCompat.getColor(this, R.color.red), "3", ContextCompat.getColor(this, R.color.blue)));
         elements.add(new SeekbarElement(R.drawable.circle_background_small, ContextCompat.getColor(this, R.color.blue), ContextCompat.getColor(this, R.color.red), "4", ContextCompat.getColor(this, R.color.blue)));
         elements.add(new SeekbarElement(R.drawable.circle_background, ContextCompat.getColor(this, R.color.blue), ContextCompat.getColor(this, R.color.red), "5", ContextCompat.getColor(this, R.color.blue)));
         elements.add(new SeekbarElement(R.drawable.circle_background_small, ContextCompat.getColor(this, R.color.blue), ContextCompat.getColor(this, R.color.red), "6", ContextCompat.getColor(this, R.color.blue)));
         elements.add(new SeekbarElement(R.drawable.circle_background, ContextCompat.getColor(this, R.color.blue), ContextCompat.getColor(this, R.color.red), "7", ContextCompat.getColor(this, R.color.blue)));
 
         snappingSeekBar.setItems(elements)
                 .setProgressBaseDrawable(R.drawable.progress)
                 .setProgressColor(colorRed)
                 .setThumbnailColor(colorRed)
                 .setOnItemSelectionListener(this);
     }
```
Set start position: 
```java
UiUtils.waitForLayoutPrepared(snappingSeekBar, new UiUtils.LayoutPreparedListener() {
             @Override
             public void onLayoutPrepared(final View preparedView) {
                 snappingSeekBar.setProgressToIndex(2);
             }
         });
```

 </br>

##Developed by
* Tobias Buchholz - <tobias.buchholz89@gmail.com>
* upgraded by József Mezei

##License

    Copyright 2014 Tobias Buchholz
   
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
