[ ![Download](https://api.bintray.com/packages/huiger/maven/labelView/images/download.svg) ](https://bintray.com/huiger/maven/labelView/_latestVersion)

## 这是一款可滑动的标签View, 可直接设置显示位置

## 效果图
![](http://ouvaxa1n0.bkt.clouddn.com/labelView.gif)
## 你可以这样使用
1. 引入
> compile 'com.huiger.labelView:1.0.0'


2. xml中
```
<huiger.lib.LabelView
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:labelViewLineColor=""
        app:labelViewLineSize=""
        app:labelViewTextColor=""
        app:labelViewTextSize=""
        app:labelViewScrollEnabled=""/>
```

4. setmCircleX(float mCircleX)
 设置原点的x轴(针对该VIew所占的面积的x轴)

5. setmCircleY(float mCircleY)
设置原点的Y轴(针对该VIew所占的面积的Y轴)


6. setmScrollEnabled(boolean mScrollEnabled)
设置该view是否可移动(当然为true后, 长按后才可移动)


## 参数介绍

属性名称 | 用途 | 类型 | 默认值
---|---|---|---
labelViewLineColor | 细线的颜色 | color | Color.WHITE
labelViewLineSize | 线条的粗细 | demension | 2dp
labelViewTextColor | 文字的颜色 | color | Color.WHITE
labelViewTextSize | 文字大小 | dimension | 15sp
labelViewScrollEnabled | 是否可以滑动 | boolean | false

