package lk.ijse.aad.backend.Entity;

public enum Category {
    HOME_AND_TECHNICAL_SERVICE("Home and Technical Service"),
    VEHICLE_SERVICE("Vehicle Service"),
    IT_AND_DIGITAL_SERVICE("IT and Digital Service"),
    BEAUTY_AND_PERSONAL_CARE("Beauty and Personal Care"),
    HEALTH_AND_WELLNESS("Health and Wellness"),
    FOOD_SERVICE("Food Service"),
    EVENT_AND_MEDIA("Event and Media"),
    PROFESSIONAL_SERVICE("Professional Service");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}