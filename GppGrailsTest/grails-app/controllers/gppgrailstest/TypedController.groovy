package gppgrailstest

@Typed(TypePolicy.MIXED) class TypedController {

    def typedService

    def index = {
         render "${TypedController.name} Hi from typed code"
    }

    @Typed void getStringFromService () {
        typedService.serviceMethod()
    }
}
