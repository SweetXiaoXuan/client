package com.lv.activity;

import org.junit.Test;
import junit.framework.TestCase;
import org.springframework.web.util.HtmlUtils;

public class TestHtml extends TestCase{

    @Test
    public void testHtmlUnescape()
    {
        String s="&lt;html&gt;&lt;p&gt;&lt;img src=&quot;http://img.hodays.com:8088//images/0042/activity//20170831/1504182170242_355_300.img&quot;/&gt;&lt;/p&gt;&lt;/html&gt;";
        String r=HtmlUtils.htmlUnescape(s);
        System.out.println(r);
    }
    @Test
    public void testHtmlEscape()
    {
        String s="<html><p><img src=\"http://img.hodays.com:8088//images/0042/activity//20170831/1504182170242_355_300.img\"/></p></html>";
        String r=HtmlUtils.htmlEscape(s);
        System.out.println(r);
    }
}
