package com.example.qhsj.okhttpdownloadapk.pojo;

import java.io.Serializable;

/**
 * Created by Chris on 2017/6/7.
 * 版本信息
 */
public class AppVersion implements Serializable {


    public String channel = "official";

    public String name = "name";

    public String changeLog = "changeLog";

     // 应用宝找的qq 的地址
    public String downloadUrl= "http://119.147.33.13/imtt.dd.qq.com/16891/8C3E058EAFBFD4F1EFE0AAA815250716.apk?mkey=59379d5b5bdd1acf&f=e673&c=0&fsname=com.tencent.mobileqq_7.1.0_692.apk&csr=1bbd&p=.apk";

    public String updateType= "updateType";

    public String status= "status";
}
