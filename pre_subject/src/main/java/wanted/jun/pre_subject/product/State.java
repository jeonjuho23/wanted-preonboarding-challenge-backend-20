package wanted.jun.pre_subject.product;

public enum State {
    SALE("sale"),
    RESERVED("reserved"),
    COMPLETE("complete"),
    ;

    private final String value;

    private State(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }
    }
