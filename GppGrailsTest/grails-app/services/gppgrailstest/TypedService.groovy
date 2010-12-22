package gppgrailstest

@Typed class TypedService {
    static transactional = true

    String serviceMethod() {
        this.class.name
    }
}
