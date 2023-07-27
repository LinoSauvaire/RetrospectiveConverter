
package com.smarttrade.retro_converter;

/**
 * Project Name : RetroSpective Converter for Confluence<br>
 * Author : @lsauvaire<br>
 * Author Name : Lino Sauvaire( recovery of Maxime Bernard )
 */
public class ConfluenceParams {

    /**
     * The Confluence URL
     */
    private String url;

    /**
     * The user login to use for Confluence
     */
    private String login;

    /**
     * The user password to use for Confluence
     */
    private String password;

    /**
     * The space key that have to be used in Confluence
     */
    private String spaceKey;

    /**
     * The page parent URL of the parent page that have to be used in Confluence
     */
    private String parentUrl;

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" + "url = " + (getUrl() == null ? "null" : getUrl()) + ", " + "login = "
                + (getLogin() == null ? "null" : getLogin()) + ", " + "password = " + (getPassword() == null ? "null" : getPassword()) + ", " + "spaceKey = "
                + (getSpaceKey() == null ? "null" : getSpaceKey()) + ", " + "parentUrl = " + (getParentUrl() == null ? "null" : getParentUrl()) + "}";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (getLogin() == null ? 0 : getLogin().hashCode());
        result = prime * result + (getParentUrl() == null ? 0 : getParentUrl().hashCode());
        result = prime * result + (getPassword() == null ? 0 : getPassword().hashCode());
        result = prime * result + (getSpaceKey() == null ? 0 : getSpaceKey().hashCode());
        result = prime * result + (getUrl() == null ? 0 : getUrl().hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final ConfluenceParams other = (ConfluenceParams) obj;
        if (getLogin() == null) {
            if (other.getLogin() != null) return false;
        } else if (!getLogin().equals(other.getLogin())) return false;
        if (getParentUrl() == null) {
            if (other.getParentUrl() != null) return false;
        } else if (!getParentUrl().equals(other.getParentUrl())) return false;
        if (getPassword() == null) {
            if (other.getPassword() != null) return false;
        } else if (!getPassword().equals(other.getPassword())) return false;
        if (getSpaceKey() == null) {
            if (other.getSpaceKey() != null) return false;
        } else if (!getSpaceKey().equals(other.getSpaceKey())) return false;
        if (getUrl() == null) {
            if (other.getUrl() != null) return false;
        } else if (!getUrl().equals(other.getUrl())) return false;
        return true;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSpaceKey() {
        return spaceKey;
    }

    public void setSpaceKey(String spaceKey) {
        this.spaceKey = spaceKey;
    }

    public String getParentUrl() {
        return parentUrl;
    }

    public void setParentUrl(String parentUrl) {
        this.parentUrl = parentUrl;
    }
}
