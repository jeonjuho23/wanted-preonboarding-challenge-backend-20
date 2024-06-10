package wanted.jun.pre_subject.product;

public enum SortBy {
    REGIST_TIME("registTime"),
    ;


    private final String value;

    private SortBy(String value) {
        this.value = value;
    }

    public String value(){
        return this.value;
    }

}
