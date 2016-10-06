package de.mlessmann.allocation;

import de.mlessmann.authentication.IAuthMethod;
import de.mlessmann.config.ConfigNode;
import de.mlessmann.homework.HWMgrSvc;
import de.mlessmann.homework.HomeWork;
import de.mlessmann.hwserver.HWServer;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;

/**
 * Created by Life4YourGames on 08.06.16.
 */
public class HWUser {

    public static final String DEFNAME = "default";
    public static final String DEFPASS = "default";

    //Authentication
    private String userName = DEFNAME;
    private String authData = DEFPASS;
    private IAuthMethod authMethod;

    private Map<String, HWPermission> permissions;

    private ConfigNode node;

    private GroupSvc group;
    private HWServer server;

    public HWUser(GroupSvc group, HWServer server) {
        this.permissions = new HashMap<String, HWPermission>();
        this.group = group;
        this.server = server;
    }

    public boolean init(ConfigNode node) {

        boolean valid = !node.getKey().isEmpty()
                && node.hasNode("auth")
                && node.getNode("auth").isHub()
                && node.getNode("auth").hasNode("method")
                && node.getNode("auth", "method").isType(String.class)
                && node.hasNode("permissions")
                && node.getNode("permissions").isHub();
        if (!valid) {
            return false;
        }
        Optional<IAuthMethod> m = server.getAuthProvider().getMethod(node.getNode("auth", "method").getString());
        if (!m.isPresent()) {
            server.onMessage(this, Level.SEVERE, "Unable to initialize user \"" + node.getKey()
                    + "\": Auth method not present!");
            return false;
        }
        authMethod = m.get();
        this.node = node;
        return true;
    }

    public String getUserName() {
        return node.getKey();
    }

    public String getAuthData() {
        return node.getNode("auth", node.getNode("auth", "method").getString()).getString();
    }

    public boolean setAuthInfo(String method, String plaintextPW) {
        ConfigNode n = node.getNode("auth");

        Optional<IAuthMethod> optM = server.getAuthProvider().getMethod(method);
        if (!optM.isPresent()) {
            return false;
        }
        IAuthMethod m = optM.get();

        n.getNode("method").setString(method);
        n.getNode(method).setString(m.masqueradePass(plaintextPW));
        return true;
    }

    public boolean authorize(String auth) {
        return authMethod.authorize(getAuthData(), auth);
    }

    public Optional<HWPermission> getPermission(String permissionName) {
        ConfigNode perms = node.getNode("permissions");
        HWPermission perm = null;
        if (perms.hasNode(permissionName)) {
            HWPermission p = new HWPermission(this, server);
            if (p.readFrom(perms.getNode(permissionName)))
                perm = p;
        }
        return Optional.ofNullable(perm);
    }

    public void addPermission(HWPermission perm) {
        node.addNode(perm.getNode());
    }

    public int addHW(JSONObject obj) {
        Optional<HWMgrSvc> svc = group.getHWMgr();
        if (svc.isPresent()) {
            return svc.get().addHW(obj, this);
        } else {
            server.onMessage(this, Level.SEVERE, "Unable to add HW: No HWMgr found!");
            return -1;
        }
    }

    public ArrayList<HomeWork> getHWOn(LocalDate date, ArrayList<String> subjectFilter) {
        Optional<HWMgrSvc> svc = group.getHWMgr();
        if (svc.isPresent()) {
            return svc.get().getHWOn(date, subjectFilter);
        } else {
            server.onMessage(this, Level.SEVERE, "Unable to search for HW: No HWMgr found!");
            return new ArrayList<>();
        }
    }

    public ArrayList<HomeWork> getHWBetween(LocalDate from, LocalDate to, ArrayList<String> subjectFilter, boolean overrideLimit) {
        Optional<HWMgrSvc> svc = group.getHWMgr();
        if (svc.isPresent()) {
            return svc.get().getHWBetween(from, to, subjectFilter, overrideLimit);
        } else {
            server.onMessage(this, Level.SEVERE, "Unable to search for HW: No HWMgr found!");
            return new ArrayList<>();
        }
    }

    public int delHW(LocalDate date, String id) {
        Optional<HWMgrSvc> svc = group.getHWMgr();
        if (svc.isPresent()) {
            return svc.get().delHW(date, id, this);
        } else {
            server.onMessage(this, Level.SEVERE, "Unable to delete HW: No HWMgr found!");
            return -1;
        }
    }

    public boolean isValid() {
        //TODO: NEW USER CLASS
        return false;
    }

    public IAuthMethod getAuth() { return authMethod; }

    public String getAuthIdent() { return authMethod.getIdentifier(); }

}