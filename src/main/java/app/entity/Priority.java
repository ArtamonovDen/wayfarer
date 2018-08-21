package app.entity;
public enum Priority {
    minor, normal, major, critical, blocker;

    public static Priority getByOrder(int n){
        switch(n){
            case 1: return Priority.minor;
            case 2: return Priority.normal;
            case 3: return Priority.major;
            case 4: return Priority.critical;
            case 5: return Priority.blocker;
            default: return Priority.normal;
        }

    }

    public static Priority getByString(String str){
        switch (str){
            case "minor": return Priority.minor;
            case "normal": return Priority.normal;
            case "major": return Priority.major;
            case "critical": return Priority.critical;
            case "blocker": return Priority.blocker;
            default: return null;
        }
    }
}
