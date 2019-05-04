# Clickjacking

> 点击劫持是一种视觉上的欺骗手段，攻击者利用一个透明不可见的`<iframe>`覆盖在一个网页上，然后诱导用户在网页上进行操作，通过调整`<iframe>`页面位置，可以诱导用户点击在`<iframe>`功能性按钮上

## 1 攻击方式

通过控制`<iframe>`的长和宽以及调整top,left的位置，将`<iframe>`页面的任意部分覆盖到任意位置。

同时设置`<iframe>`的`position`为absolute，并将z-index设置为最大值，让`iframe`处于最顶层，然后设置opacity来控制`<iframe>`页面的透明度，值为0表示完全不可见

```css
iframe{
    width:900px;
    height:250px;
    
    position:absolute;
    top:-195px;
    left:-740px;
    z-index:2;
    
    -moz-opacity:0.5;
    opacity:0.5;
    filter:alpha(opacity=0.5);
}
```

## 2 防御

### 2.1 frame bustiong

通过写一段JavaScript代码，禁止`iframe`嵌套

```javascript
if(top.location != location){
    top.location = self.location;
}
```

但是由于是由javascript写的，实际上是可以绕过的

### 2.2 X-Frame-Options

它是一个HTTP头，有三个可选值

- DENY:拒绝当前页面加载任何frame
- SAMEORIGIN:只允许加载同一个源的页面
- ALLOW-FROM origin：可以加载任意特面