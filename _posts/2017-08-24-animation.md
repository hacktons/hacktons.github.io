---
layout: post
title: "懒加载帧动画"
summary: "使用懒加载帧动画可以避免在执行帧动画时的内存问题，特别是OutOfMemoryError。他的实现原理在于异步加载图片， 并且支持全局图片缓存。我们都知道原生的Android帧动画在加载序列帧时，是一次性将所有序列帧的图片编码到内存当中的，所以执行帧数较多的动画时很容易 发生内存不足，抛出OutOfMemoryError。"
target: _blank
link: "http://hacktons.cn/animation/"
---

