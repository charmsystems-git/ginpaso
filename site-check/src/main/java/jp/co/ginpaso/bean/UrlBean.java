
package jp.co.ginpaso.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * sitemap.xmlのurlノード情報.
 */
@Data
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UrlBean implements Serializable {

    /** serialVersionUID. */
    private static final long serialVersionUID = 9171880164760255754L;

    /** (必須)locノード. */
    @JacksonXmlProperty(localName = "loc")
    private String loc;

    /** (必須)priorityノード. */
    @JacksonXmlProperty(localName = "priority")
    private String priority;

    /** (任意)lastmodノード. */
    @JacksonXmlProperty(localName = "lastmod")
    @JsonIgnore
    private String lastmod;
}
