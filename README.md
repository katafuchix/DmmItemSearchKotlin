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

### Examples in this Android Studio Project
- getting items from DMM API with Retrofit, Rx.
- creating grid item list with RecyclerView, LiveData .
- while getting items from DMM API, show loading KProgressHUD.
- example of Repository pattern.

### Screen capture
<div>
<img src="https://user-images.githubusercontent.com/6063541/61166249-d9734180-a565-11e9-809c-d15276e9d59d.jpg" width="250">
　
<img src="https://user-images.githubusercontent.com/6063541/61166329-7c2bc000-a566-11e9-92b1-b48428701bca.jpg" width="250">
　
<img src="https://user-images.githubusercontent.com/6063541/61166331-8c439f80-a566-11e9-820e-0b75548ff388.jpg" width="250">
</div>
<div>
<img src="https://user-images.githubusercontent.com/6063541/61166339-9d8cac00-a566-11e9-9b6b-5e1511366b64.jpg" width="250">
　
<img src="https://user-images.githubusercontent.com/6063541/61166345-abdac800-a566-11e9-8cc1-70f945c1d8b1.jpg" width="250">
　
</div>
