
package jp.co.ginpaso.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import jp.co.ginpaso.bean.UrlBean;
import jp.co.ginpaso.bean.UrlSetBean;
import lombok.extern.slf4j.Slf4j;

/**
 * サイトチェックサービス.
 */
@Slf4j
public class SiteServiceImpl implements InitializingBean {

    /** sitemap.xmlの保存ファイル名. */
    private static final String FILE_NAME = "sitemap.xml";

    /** HTTPスキーマ. */
    private static final String HTTP_SCHEMA = "http://";

    /** HTTPSスキーマ. */
    private static final String HTTPS_SCHEMA = "https://";

    /** HTTP GETメソッド. */
    private static final String GET_METHOD = "GET";

    /** sitemap.xmlパラメータ. */
    @Value("${ginpaso.sitemap}")
    private String siteUrl;

    /** sitemap.xmlの解析Bean. */
    private UrlSetBean urlSetBean;

    /** sitemap.xmlの登録URL. */
    private Map<String, Integer> uniqueMap = new HashMap<>();

    /**
     * 初期化.
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        download();
    }

    /**
     * sitemap.xmlの内容チェックを行う.
     */
    public void check() {
        // 1. URL情報
        log.info("◆ 1) URL情報");
        infoURL();

        // 2. GETチェック
        log.info("◆ 2) URLのGETチェック");
        if (checkGET()) {
            log.info("  全てのURLが200/OKでした。");
        }

        // 3. 重複チェック
        log.info("◆ 3) URLの重複登録チェック");
        if (checkUnique()) {
            log.info("  重複登録はありませんでした。");
        }

        // ダウンロードしたsitemap.xmlを削除
        new File(FILE_NAME).delete();
    }

    /**
     * sitemap.xmlをURLよりダウンロードする.
     * @throws IOException ダウンロード例外
     */
    private void download() throws IOException {
        URL url = new URL(siteUrl);
        ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
        try (FileOutputStream fos = new FileOutputStream(FILE_NAME)) {
            fos.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
        }

        String text = Files.readString(Paths.get(FILE_NAME));
        urlSetBean = new XmlMapper().readValue(text, UrlSetBean.class);
    }

    /**
     * 登録URL情報をログ出力する.
     */
    private void infoURL() {
        int countHTTP = 0;
        int countHTTPS = 0;
        for (UrlBean urlBean : urlSetBean.getUrlBeans()) {
            if (urlBean.getLoc().indexOf(HTTP_SCHEMA) >= 0) {
                countHTTP++;
            } else if (urlBean.getLoc().indexOf(HTTPS_SCHEMA) >= 0) {
                countHTTPS++;
            } else {
                log.info(" HTTP(s)スキーマエラー: {}", urlBean.getLoc());
            }
        }
        log.info("  URL登録件数     : {}", urlSetBean.getUrlBeans().size());
        log.info("  HTTP  schema件数: {}", countHTTP);
        log.info("  HTTPS schema件数: {}", countHTTPS);
    }

    /**
     * 登録URLの有効性チェックを行う.
     * @return true:全件正常終了, false:1件以上のエラー(200応答以外)がある
     */
    private boolean checkGET() {
        boolean result = true;
        for (UrlBean urlBean : urlSetBean.getUrlBeans()) {
            try {
                URL targetUrl = new URL(urlBean.getLoc());
                HttpURLConnection connection = (HttpURLConnection) targetUrl.openConnection();
                connection.setRequestMethod(GET_METHOD);
                connection.connect();
                int responseCode = connection.getResponseCode();
                connection.disconnect();

                if (responseCode != 200) {
                    result = false;
                    log.info("  200系以外の応答(ResponseCode={}): {}", responseCode, urlBean.getLoc());
                    continue;
                }
            } catch (IOException e) {
                result = false;
                log.info("  システムエラー: {}", urlBean.getLoc());
                continue;
            }
            if (uniqueMap.containsKey(urlBean.getLoc())) {
                uniqueMap.put(urlBean.getLoc(), uniqueMap.get(urlBean.getLoc()) + 1);
            } else {
                uniqueMap.put(urlBean.getLoc(), 1);
            }
        }
        return result;
    }

    /**
     * 登録URLのUniqueチェックを行う.
     * @return true:全件正常終了, false:1件以上のエラー(重複登録)がある
     */
    private boolean checkUnique() {
        boolean result = true;
        Iterator<String> iter = uniqueMap.keySet().iterator();
        while (iter.hasNext()) {
            String url = iter.next();
            if (uniqueMap.get(url) > 1) {
                log.info("  重複登録エラー: {}", url);
                result = false;
            }
        }
        return result;
    }
}
