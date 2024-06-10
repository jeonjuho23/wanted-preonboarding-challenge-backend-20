package wanted.jun.pre_subject.product;

public enum SortBy {
    REGIST_TIME("registTime"),
    STATE_UPDATE_TIME("stateUpdateTime"),
    PRODUCT_STATE_UPDATE_TIME("product.stateUpdateTime");


    private final String value;

    private SortBy(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }

    }
