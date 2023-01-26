package advisor;

import com.beust.jcommander.Parameter;

public class Args {
    @Parameter(names = {"-access"})
    String access;

    @Parameter(names = {"-resource"})
    String resource;

    @Parameter(names = {"-page"})
    String page;

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }
}


