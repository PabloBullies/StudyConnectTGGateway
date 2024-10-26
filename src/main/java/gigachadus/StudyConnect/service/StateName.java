package gigachadus.StudyConnect.service;

public enum StateName {
    START ("START"),
    REGISTRATION("REGISTRATION");

    private final String name;

    StateName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
