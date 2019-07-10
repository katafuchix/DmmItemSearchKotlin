# DmmItemSearchKotlin


## Getting Started
create app/src/main/java/com/example/dmmitemsearchsample/common/Constants.kt like this.
```
package com.example.dmmitemsearchsample.common

object Constants {

    const val BASE_URL          = "https://api.dmm.com/"
    const val TIME_INTERVAL     = 30L

    // API ID
    const val api_id            = " YOUR DMM API ID "

    // affiliate ID
    const val affiliate_id      = " YOUR DMM AFFILIATE ID "

    // 1ページに表示する件数
    const val hits              = 20
    // 出力形式
    const val output            = "json"
}
```
