package com.github.dschreid.groups.sign;

import com.github.dschreid.groups.configuration.ConfigBean;
import org.bukkit.configuration.ConfigurationSection;

public class SignConfigBean implements ConfigBean {
    private boolean enabled;
    private int updateRadius;
    private int updateTickSpeed;

    @Override
    public String getSection() {
        return "group-signs";
    }

    @Override
    public void loadConfiguration(ConfigurationSection cs) {
        if (cs == null) {
            return;
        }

        this.enabled = cs.getBoolean("enabled");
        this.updateRadius = cs.getInt("update-radius", 25);
        this.updateTickSpeed = cs.getInt("update-tick-speed", 60);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public int getUpdateRadius() {
        return updateRadius;
    }

    public int getUpdateTickSpeed() {
        return updateTickSpeed;
    }
}
