# 概要
`sitemap.xml`をインターネットよりダウンロードしサイト解析を行う.

# チェック仕様
1. `sitemap.xml`に登録されているURLが200/OKのHTTP応答かチェックを行う.
1. `sitemap.xml`に登録されているURLに重複登録がないかチェックを行う.

# 実行方法
プログラムを展開したフォルダで実行.
```bash
# 1. build
mvn clean package -Dmaven.test.skip=true

# 2. 実行
java -jar target/site-check-0.0.1-SNAPSHOT.jar
```
