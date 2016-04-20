校园导航
====
##By Abitalo & Zela
##(2016-3-12)
----
<i>
地理位置信息驱动的校园信息发布平台，通过地理围栏技术实现当进入某区域时自动推送对应的信息。  
目前进度：已完成基本框架，并集成了地图、定位和地理围栏功能。  
尚待完成：信息来源（学校提供接口或者自己爬）、教务系统接入、重写地理围栏。  
</i>

#Sample  
##首页
![image](http://7xkp3n.com1.z0.glb.clouddn.com/Screenshot_20160421-002634.png-gitstyle)  

上方为预留的信息窗口，下方为校园地图。地图上的地理标记可以点击。

##定位模式开启后
![image](http://7xkp3n.com1.z0.glb.clouddn.com/Screenshot_20160421-002640.png-gitstyle)  

开启定位模式后会定期同步当前地理位置并更新地图。定位模式可以随时关闭。

##地点信息卡片
![image](http://7xkp3n.com1.z0.glb.clouddn.com/Screenshot_20160421-002658.png-gitstyle)  

点击地图上的地理标记，会弹出与该地点对应的信息卡片，卡片上方是地点缩略图，下方预留位置用于显示与该地点相关的即时信息。 
点击卡片中的缩略图可进入详细信息界面。

##菜单界面

![image](http://7xkp3n.com1.z0.glb.clouddn.com/Screenshot_20160421-002737.png-gitstyle)  

#Dependencies

* [高德地图][AMap_Android_2D_API](http://lbs.amap.com/api/android-sdk/down/)
* [Fresco][Fresco](https://github.com/facebook/fresco)
* [MaterialDrawer][MaterialDrawer](hhttps://github.com/mikepenz/MaterialDrawer)

#Credits

Author: Aitalo [http://www.abitalo.com/](http://www.abitalo.com/)

<a href="https://cn.linkedin.com/in/abitalo">
  <img alt="Follow me on LinkedIn"
       src="https://raw.githubusercontent.com/florent37/DaVinci/master/mobile/src/main/res/drawable-hdpi/linkedin.png" />
</a>
