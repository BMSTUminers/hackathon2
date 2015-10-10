package org.bmstuminers.hackathon2.model;

/**
 * Description of the parameter
 * @author Konstantin Grechishchev
 */
public class ParameterDescription {

    /**
     * Name of the parameter
     */
    private String name;
    /**
     * Plain text parameter description
     */
    private String description;
    /**
     * if true, the parameter is optional. defaultValue should be specified in this case
     */
    private boolean optional;
    /**
     * Default value for optional parameter
     */
    private String defaultValue;

    public ParameterDescription(String name, String description, boolean optional, String defaultValue) {
        this.name = name;
        this.optional = optional;
        this.description = description;
        this.defaultValue = defaultValue;
    }

    public ParameterDescription(String name, String description) {
        this.description = description;
        this.name = name;
        this.optional = false;
        this.defaultValue = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOptional() {
        return optional;
    }

    public void setOptional(boolean optional) {
        this.optional = optional;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
}
