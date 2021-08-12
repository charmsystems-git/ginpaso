
package jp.co.ginpaso;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jp.co.ginpaso.service.SiteServiceImpl;
import lombok.extern.slf4j.Slf4j;

/**
 * サイトチェックサービスを行うアプリケーション.
 */
@SpringBootApplication
@Slf4j
public class SiteCheckApplication implements ApplicationRunner {

    /**
     * メイン起動.
     * @param args 起動パラメータ(利用しない)
     */
    public static void main(String[] args) {
        SpringApplication.run(SiteCheckApplication.class, args);
    }

    /** サイトチェックサービス. */
    @Autowired
    private SiteServiceImpl siteService;

    /**
     * 処理開始.
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("[サイトチェック開始]");
        siteService.check();
        log.info("[サイトチェック終了]");
    }
}
