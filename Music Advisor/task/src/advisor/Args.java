package advisor;

import com.beust.jcommander.Parameter;

public class Args {
    @Parameter(names = {"-access"})
    String access;

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }
}


