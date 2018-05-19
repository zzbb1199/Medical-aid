# Medical-aid
辅助抓药系统，Android客户端。

Table of Contents
=================

* [Medical\-aid](#medical-aid)
  * [1\.概要](#1%E6%A6%82%E8%A6%81)
    * [1\.1 项目介绍](#11-%E9%A1%B9%E7%9B%AE%E4%BB%8B%E7%BB%8D)
    * [1\.2 客户端功能介绍](#12-%E5%AE%A2%E6%88%B7%E7%AB%AF%E5%8A%9F%E8%83%BD%E4%BB%8B%E7%BB%8D)
      * [1\.2\.1 医师功能](#121-%E5%8C%BB%E5%B8%88%E5%8A%9F%E8%83%BD)
      * [1\.2\.2 病人功能](#122-%E7%97%85%E4%BA%BA%E5%8A%9F%E8%83%BD)
      * [1\.2\.3 公共功能](#123-%E5%85%AC%E5%85%B1%E5%8A%9F%E8%83%BD)
    * [1\.3 系统流程图](#13-%E7%B3%BB%E7%BB%9F%E6%B5%81%E7%A8%8B%E5%9B%BE)
  * [2\. 功能展示](#2-%E5%8A%9F%E8%83%BD%E5%B1%95%E7%A4%BA)
    * [主界面&amp;病人队列&amp;开药中心](#%E4%B8%BB%E7%95%8C%E9%9D%A2%E7%97%85%E4%BA%BA%E9%98%9F%E5%88%97%E5%BC%80%E8%8D%AF%E4%B8%AD%E5%BF%83)
    * [个人中心&amp;药材百科&amp;药材保质](#%E4%B8%AA%E4%BA%BA%E4%B8%AD%E5%BF%83%E8%8D%AF%E6%9D%90%E7%99%BE%E7%A7%91%E8%8D%AF%E6%9D%90%E4%BF%9D%E8%B4%A8)
  * [3 开发环境及技术支持](#3-%E5%BC%80%E5%8F%91%E7%8E%AF%E5%A2%83%E5%8F%8A%E6%8A%80%E6%9C%AF%E6%94%AF%E6%8C%81)
    * [3\.1  开发环境及运行平台](#31--%E5%BC%80%E5%8F%91%E7%8E%AF%E5%A2%83%E5%8F%8A%E8%BF%90%E8%A1%8C%E5%B9%B3%E5%8F%B0)
      * [3\.1\.1 开发环境](#311-%E5%BC%80%E5%8F%91%E7%8E%AF%E5%A2%83)
      * [3\.1\.2 运行环境](#312-%E8%BF%90%E8%A1%8C%E7%8E%AF%E5%A2%83)
    * [3\.2 技术支持](#32-%E6%8A%80%E6%9C%AF%E6%94%AF%E6%8C%81)

Created by [gh-md-toc](https://github.com/ekalinin/github-markdown-toc.go)

## 1.概要

### 1.1 项目介绍

本项目以国内中药房智能化程度不足为背景，拟实现对于中小型中药药店的辅助系统，主要帮助零散在各个城市街道的中小型中药药店实现快速抓药，并且形成病人的信息整合，方便将全国中小型药店实现系统化、科技化的管理与运作。

- 项目背景：中药房智能化不足
- 针对对象：中小型中药房
- 项目目的：实现药房快速抓药，药方、病历等信息电子化
- 项目组成：硬件端+服务器+软件客户端（Android + [iOS](!https://github.com/Mclarenyang/medicine_aid))
- 职责担任：我在本项目中担任Android开方成员

### 1.2 客户端功能介绍

软件客户端针对两个对象：**医师**和**病人**，所以在功能上分为两类。

#### 1.2.1 医师功能

1. 生成二维码：用于患者扫描挂号
2. 当前病人队列：查看，选择，踢出当前挂号病人。
3. **开药**
   1. 硬件系统使用授权认证
   2. 药方病历填写
   3. 药方数据上传服务器
   4. 药材保质期查看，设置。药材过期警告
   5. 开方历史备份：方便医师查看自己的开药历史。

#### 1.2.2 病人功能

1. 扫描医师二维码挂号
2. 查看当前排队队列：合理安排看病时间
3. 历史病历查询
4. 看病药方查询
5. 服务提醒设置

#### 1.2.3 公共功能

1. 登陆&注册
2. 药材百科查询
3. 病人队列看

### 1.3 系统流程图

![](https://github.com/zzbb1199/Medical-aid/blob/master/pic/%E7%B3%BB%E7%BB%9F%E6%B5%81%E5%9B%BEpng.png)

## 2. 功能展示

### 主界面&病人队列&开药中心

​    <img src="https://github.com/zzbb1199/Medical-aid/blob/master/pic/%E4%B8%BB%E7%95%8C%E9%9D%A2.jpg" title="主界面" width="250px" alt="主界面">  <img src="https://github.com/zzbb1199/Medical-aid/blob/master/pic/%E7%97%85%E4%BA%BA%E9%98%9F%E5%88%97.jpg" title="病人队列"  width="250px" alt="病人队列"><img src="https://github.com/zzbb1199/Medical-aid/blob/master/pic/%E5%BC%80%E8%8D%AF%E4%B8%AD%E5%BF%83.jpg" title="开药中心"  width="250px" alt="开药中心">

### 个人中心&药材百科&药材保质

​    <img src="https://github.com/zzbb1199/Medical-aid/blob/master/pic/%E4%B8%AA%E4%BA%BA%E4%B8%AD%E5%BF%83.jpg" title="个人中心"  width="250px" alt="个人中心">  <img src="https://github.com/zzbb1199/Medical-aid/blob/master/pic/%E8%8D%AF%E6%9D%90%E7%99%BE%E7%A7%91.jpg" title="药材百科"  width="250px" alt="药材百科">  <img src="https://github.com/zzbb1199/Medical-aid/blob/master/pic/%E8%8D%AF%E6%9D%90%E4%BF%9D%E8%B4%A8%E6%9F%A5%E8%AF%A2.jpg" title="药材保质"  width="250px" alt="药材保质">




## 3 开发环境及技术支持

### 3.1  开发环境及运行平台

#### 3.1.1 开发环境

- Android Studio 2.3.3 
- JDK 1.8,java语言开发

#### 3.1.2 运行环境

- Android平台系列手机
- minSDK>=18

### 3.2 技术支持

1. 界面设计
   - 遵从Google Material Design设计。
2. 网络数据交互
   - OkHttp3：网络请求的优秀开源框架
   - Retrofit2+RxJava：简化网络请求API与主子线程调度
3. 本地数据存储
   - SharedPreference：Android 自带简单本地存储API。
   - GreenDao：轻量高效数据库。
4. 代码解耦
   - MVP设计模式：业务分为3个层次，M-Model，V-View，P-presnter，通过p进行中转达到解耦。
   - Dagger2：依赖注入框架，减少模块之间的依赖
