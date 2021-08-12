
package jp.co.ginpaso.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jp.co.ginpaso.service.SiteServiceImpl;

/**
 * サイトチェック設定.
 */
@Configuration
public class SiteConfig {

    /**
     * サイトチェックサービスのBean登録を行う.
     * @return {@linkplain SiteServiceImpl}
     */
    @Bean
    public SiteServiceImpl siteServiceImpl() {
        return new SiteServiceImpl();
    }
}
