

一句代码适配4.4以上沉浸状态栏和沉浸导航栏, 状态栏颜色和样式自动根据状态栏下面的背景颜色自动调整, 适配魅族，小米等国产手机.

[![](https://jitpack.io/v/sovegetables/systembarhelper.svg)](https://jitpack.io/#sovegetables/systembarhelper)
#### Usage
```groovy
    maven { url "https://jitpack.io" }
    
    implementation 'com.github.sovegetables:systembarhelper:0.1.2'
```

```java
    SystemBarHelper.Builder().into(activity)
```


```java
    SystemBarHelper.Builder()
                    .statusBarColor()    // 设置状态栏颜色
                    .statusBarFontStyle()  // 设置状态栏时间,电量的风格, 6.0以上, 部分国产机可以不用6.0以上.
                    .navigationBarColor()  // 设置导航栏颜色
                    .enableImmersedStatusBar()  // 布局嵌入状态栏，例如图片嵌入状态栏
                    .enableImmersedNavigationBar()  // 布局嵌入导航栏，例如图片嵌入导航栏
                    .into(this)
```


```java
   SystemBarHelper helper = SystemBarHelper.Builder().into(activity);    // SystemBarHelper也Builder有相应的方法,方便动态设置
   helper.setNavigationBarColor()
   helper.setStatusBarColor()
   helper.statusBarFontStyle()
   helper.enableImmersedStatusBar()
   helper.enableImmersedNavigationBar()
```


#### Feature
1. 根据状态栏下面的背景颜色自动调整状态栏的颜色
2. 根据状态栏下面的背景颜色自动调整状态栏时间,电量等风格
3. 设置图片嵌入状态栏,图片嵌入导航栏
4. 修个状态栏的颜色和导航栏颜色


#### License

```
Copyright 2016 Albert Liu

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
