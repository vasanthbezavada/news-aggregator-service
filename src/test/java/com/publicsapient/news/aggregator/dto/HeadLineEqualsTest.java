package com.publicsapient.news.aggregator.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HeadLineEqualsTest {

    @Test
    void testEquals_SameObject() {
        HeadLine headline = new HeadLine();
        headline.setTitle("Breaking News");
        assertEquals(headline, headline, "An object should be equal to itself");
    }

    @Test
    void testEquals_NullObject() {
        HeadLine headline = new HeadLine();
        headline.setTitle("Breaking News");
        assertNotEquals(headline, null, "An object should not be equal to null");
    }

    @Test
    void testEquals_DifferentClassObject() {
        HeadLine headline = new HeadLine();
        headline.setTitle("Breaking News");
        String otherObject = "Not a HeadLine";
        assertNotEquals(headline, otherObject, "An object should not be equal to an instance of a different class");
    }

    @Test
    void testEquals_SameTitle() {
        HeadLine headline1 = new HeadLine();
        headline1.setTitle("Breaking News");

        HeadLine headline2 = new HeadLine();
        headline2.setTitle("Breaking News");

        assertEquals(headline1, headline2, "Headlines with the same title should be equal");
    }

    @Test
    void testEquals_DifferentTitle() {
        HeadLine headline1 = new HeadLine();
        headline1.setTitle("Breaking News");

        HeadLine headline2 = new HeadLine();
        headline2.setTitle("Other News");

        assertNotEquals(headline1, headline2, "Headlines with different titles should not be equal");
    }

    @Test
    void testEquals_BothTitlesNull() {
        HeadLine headline1 = new HeadLine();
        HeadLine headline2 = new HeadLine();
        assertEquals(headline1, headline2, "Headlines with both titles null should be equal");
    }

    @Test
    void testEquals_OneTitleNull() {
        HeadLine headline1 = new HeadLine();
        headline1.setTitle("Breaking News");

        HeadLine headline2 = new HeadLine();

        assertNotEquals(headline1, headline2, "A headline with a title should not be equal to a headline with a null title");
    }
}
