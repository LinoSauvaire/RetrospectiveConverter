
package com.smarttrade.retro_converter;

import org.junit.Assert;
import org.junit.Test;

public class SimpleConfluenceWriterUTest {

    private SimpleConfluenceWriter tested;

    @Test
    public void login_is_blank() {
        // Given
        final ConfluenceParams params = new ConfluenceParams();
        params.setLogin("");
        params.setPassword("password");
        params.setUrl("url");
        params.setSpaceKey("spaceKey");
        params.setParentUrl("parentUrl");

        try {
            // When
            new SimpleConfluenceWriter(params);
            Assert.fail();
        } catch (final IllegalArgumentException e) {
            // Then an exception is thrown
        }

    }

    @Test
    public void password_is_blank() {
        final ConfluenceParams params = new ConfluenceParams();
        params.setLogin("login");
        params.setPassword("");
        params.setParentUrl("parentUrl");
        params.setSpaceKey("spaceKey");
        params.setUrl("url");

        try {
            new SimpleConfluenceWriter(params);
            Assert.fail();
        } catch (final IllegalArgumentException e) {
            // then an exception thrown
        }
    }

    @Test
    public void url_is_blank() {
        final ConfluenceParams params = new ConfluenceParams();
        params.setLogin("login");
        params.setPassword("password");
        params.setParentUrl("parentUrl");
        params.setSpaceKey("spaceKey");
        params.setUrl("");

        try {
            new SimpleConfluenceWriter(params);
            Assert.fail();
        } catch (final IllegalArgumentException e) {
            // then an exception thrown
        }
    }

    @Test
    public void space_key_is_blank() {
        final ConfluenceParams params = new ConfluenceParams();
        params.setLogin("login");
        params.setPassword("password");
        params.setParentUrl("parentUrl");
        params.setSpaceKey("");
        params.setUrl("url");

        try {
            new SimpleConfluenceWriter(params);
            Assert.fail();
        } catch (final IllegalArgumentException e) {
            // then an exception thrown
        }
    }

    @Test
    public void parent_url_is_blank() {
        final ConfluenceParams params = new ConfluenceParams();
        params.setLogin("login");
        params.setPassword("password");
        params.setParentUrl("");
        params.setSpaceKey("spaceKey");
        params.setUrl("url");

        try {
            new SimpleConfluenceWriter(params);
            Assert.fail();
        } catch (final IllegalArgumentException e) {
            // then an exception thrown
        }
    }
}
