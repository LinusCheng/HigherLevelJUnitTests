package practice.constant;

public enum OperationRole {

    OP_ADMIN(3),
    OP_EXECUTE(2),
    OP_VIEWER(1),
    OP_DENIED(0);

    private int accessLevel;

    OperationRole(int accessLevel) {
        this.accessLevel = accessLevel;
    }

    public static OperationRole parseString2Enum(String role) {
        try {
            return OperationRole.valueOf(role.toUpperCase());
        } catch (Exception e) {
            return OP_DENIED;
        }
    }

}
