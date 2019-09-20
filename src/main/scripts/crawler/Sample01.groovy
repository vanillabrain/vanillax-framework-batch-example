/*
 * Copyright (C) 2016 Vanilla Brain, Team - All Rights Reserved
 *
 * This file is part of 'VanillaTopic'
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Vanilla Brain Team and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Vanilla Brain Incorporated
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Vanilla Brain Team.
 */

package crawler

import groovy.util.logging.Log4j2
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import vanillax.framework.core.object.Autowired
import vanillax.framework.batch.action.ActionBase

/**
 *W
 */
@Log4j2
class Sample01 extends ActionBase {
     def process(obj) {
         def url = 'http://demo.3hand.io/tas-web/test/aa.jsp'
        log.info "request url : $url"

         def WEBVIEW_MOBILE_AGENT = "Mozilla/5.0 (iPhone; CPU iPhone OS 9_3 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13E188a Safari/601.1"
         def WEBVIEW_WEB_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.157 Safari/537.36"

        //목록 조회는 web에서 가져온다 (WEBVIEW_WEB_AGENT)
        def conn = Jsoup.connect(url)
                .ignoreContentType(true)
                .userAgent(WEBVIEW_WEB_AGENT)
                .referrer("http://www.google.com")
                .timeout(5000)
                .followRedirects(true)

        if (url.contains("ddanzi.com") || url.contains("hungryboarder.com"))
            conn.header("Accept-Encoding", "")

        conn.request().method(Connection.Method.GET)
        Document doc = conn.get()

        if(doc.charset().name() == "EUC-KR"){
            def response = conn.execute()
            response.charset("ms949")
            doc = response.parse()
        }

         def html = doc.outerHtml()
         log.info(html)


    }
}
