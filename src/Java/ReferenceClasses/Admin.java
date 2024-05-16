package Java.ReferenceClasses;

public class Admin {
    private String adminId;
    private String username;
    private String sessionToken;

    public Admin(String adminId, String username, String sessionToken) {
        this.adminId = getAdminId();
        this.username = username;
        this.sessionToken = sessionToken;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }
}
