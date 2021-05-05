# 目標
## 以下構成のアプリケーションを構築すること
* 画面 (JavaScript)
  * Vue.js
* Web API
  * Spring Boot
* Database
  * MySQL

## アプリケーションのおおまかな動き
* Vue.js 上の画面に入力された値を元に、Spring Boot 上の Web API を実行する
  * Web API はデータベースからデータを取得して値を返す
    * Vue.js が Web API で取得したデータを表示する

# 環境
## IDE
* Intellij IDEA Ultimate (2020.3.3)
  
## Java 関連
* AdoptOpenJDK 16 HotSpot
  * https://adoptopenjdk.net/
    * set JAVA HOME variable を有効にしておく
* Spring Boot 2.4.4

## Database
* MySQL 8.0.23
  * https://dev.mysql.com/downloads/
    * MySQL Installer for Windows

## JavaScript
* Node.js 14.16.0
* Vue.js 

# 構築作業
## IntelliJ で Spring Boot プロジェクトを作成する
* New Project -> Spring initializr
* Project SDK
  * インストールした JDK を指定する
* Choose starter service URL
  * Default
* Spring initializr project Settings
  * Group: 適宜
  * Artifact: 適宜
  * Type: Gradle
  * Language: Java
  * Pacaging: Jar
  * Java version: 16
  * Version: 適宜
  * Name: 適宜
  * Description: 適宜
  * Package: 適宜
* Spring Boot: 2.4.4
  * Developer Tools
    * Spring Boot DevTools
  * Web
    * Spring Web
  * SQL
    * MyBatis Framework
    * MySQL Driver
* Project name: 適宜
* Project location: 適宜

# Gradle
* 2021.3.24 現在、Java16 を使う場合に radle が6系だとビルド時に例外が出るため以下設定を変更して、gradle を7にする
  * gradle/wrapper/gradle-wrapper.properties
    * distributionUrl=https\://services.gradle.org/distributions-snapshots/gradle-7.0-20210303140106+0000-bin.zip

# MySQL 関連の設定をする
* MySQL にタイムゾーンデータを投入する
  * https://dev.mysql.com/downloads/timezones.html
    * タイムゾーンデータをダウンロードする
      * 今回は timezone_2021a_posix_sql.zip を使用した
    * データを投入する
```
use mysql
source パス\timezone_posix.sql
または
mysql -uroot -p -Dmysql < パス\timezone_posix.sql
```

# データベースを作成する
* IntelliJ 上で MySQL に接続する
  * view -> Tool Windows -> Database
    * + -> Data Source -> MySQL
      * Name: 適宜
      * General
        * User: 適宜
        * Password: 適宜
        * ↓「Advanced」を設定した後、Test Connection で接続できることを確認する
      * Advanced
        * serverTimezonea: Asia/Tokyo
* console ウィンドウで以下 SQL を発行する
  * create database sample;
* 右上のプルダウンで sample が選択できれば OK

``` schema.sql
DROP TABLE IF EXISTS clerk;

CREATE TABLE IF NOT EXISTS clerk (
    id INT AUTO_INCREMENT,
    name TEXT NOT NULL,
    created_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);
```
``` data.sql
INSERT INTO clerk (name) values ('suzuki ichiro');
INSERT INTO clerk (name) values ('sato jiro');
```

## Spring Boot でデータベースに接続してデータ取得ができることを確認する
``` モデルクラス(Clerk.java) 
package dev.itatyo.demo;

import java.sql.Timestamp;

public class Clerk {
    private int id;
    private String name;
    private Timestamp created_timestamp;
    private Timestamp updated_timestamp;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Clerk{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", created_datetime=" + created_timestamp +
                ", updated_datetime=" + updated_timestamp +
                '}';
    }
}
```

``` DB からデータを取得する Mapper (ClerkMapper.java)
package dev.itatyo.demo;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ClerkMapper {
    @Select("select * from clerk where id = #{id}")
    Clerk findByID(int id);
}

```

``` Mapper を使ってデータを取得して標準出力に出す (DemoApplication.java)
package dev.itatyo.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    private ClerkMapper clerkmapper;
    public DemoApplication (ClerkMapper clerkMapper) {
        this.clerkmapper = clerkMapper;
    }
    @Bean
    CommandLineRunner sampleCommandLineRunner() {
        return args -> System.out.println(this.clerkmapper.findByID(1));
    }
}

```
* 上クラスを実行して以下メッセージが表示されていれば、データベースにアクセスしてデータ取得できている
```
2021-04-10 18:53:28.098  INFO 15344 --- [  restartedMain] dev.itatyo.demo.DemoApplication          : Started DemoApplication in 2.181 seconds (JVM running for 2.968)
Clerk{id=1, name='suzuki ichiro', created_datetime=2021-03-25 16:23:54.0, updated_datetime=2021-03-25 16:23:54.0}
```

## Spring Boot で API を作る (Vue.js から呼び出す API)
``` ClerkController.java
package dev.itatyo.demo.restservice;

import dev.itatyo.demo.Clerk;
import dev.itatyo.demo.ClerkMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class ClerkController {
    @Autowired
    private ClerkMapper clerkMapper;
    @GetMapping("/clerk")
    public Clerk clerk(@RequestParam(value = "id", defaultValue = "1") int id) {
        return clerkMapper.findByID(id);
    }
}
```
* Spring Boot アプリケーションを起動して以下 URL をたたいて以下のような結果が返ってくればよい
  * http://localhost:8080/clerk
```
{"id":1,"name":"suzuki ichiro"}
```

## Node.js 関連をセットアップする
* Node.js をインストールする
  * https://nodejs.org/ja/

## IntelliJ の Vue.js プラグインをインストールする
* IntelliJ の Vue.js プラグインをインストールする

## IntelliJ のプロジェクトに Vue.js をモジュールとして追加する
* File -> New -> Module -> JavaScript -> Vue.js
  * パラメータは適宜 (Vue CLI が入っていなければこのタイミングで入る)

## ローカル環境のサーバーで Vue.js を動かせるようにする (Port 8080 は SpringBoot が使うので変える)
* Run -> Run -> Edit Configuration -> + -> npm
  * Command: run
  * Scripts: serve
  * Arguments: -- --port 18080

## Vue.js から Spring Boot の API を実行して画面表示する
* package.json に axios を追加する (Vue.js から Spring Boot の API を実行する用)
``` pakage.json
  "dependencies": {
    "core-js": "^3.6.5",
    "vue": "^2.6.11",
    "axios": "^0.21.1"
  },
```
* IntelliJ が axios を npm install するか？と言ってくるので install する
* 「ローカル環境のサーバーで Vue.js を動かせるようにする」で設定したもので Vue.js アプリを実行する
* 下記 URL にアクセスする
  * http://localhost:18080/
* テキストボックスとメッセージが表示されていて、テキストボックスに2を入力すると表示内容が変わることが確認できればおｋ

## Lombok を使えるようにする
* build.gradle の dependencies に Lombok を追加する
  * https://projectlombok.org/setup/gradle
* IntelliJ で Lombok を使えるようにする
  * https://projectlombok.org/setup/intellij

## @RestControllerAdvice で例外処理をする
* https://github.com/itatyo/springboot-vuejs/commit/ab2aa5afad1ecdb7f24f9c6d039809d045b56f38
