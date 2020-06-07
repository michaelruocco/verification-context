package uk.co.idv.context.entities.alias;

public class DefaultAliasFactory implements AliasFactory {

    @Override
    public Alias build(String type, String value) {
        switch(type) {
            case IdvId.TYPE: return new IdvId(value);
            case CreditCardNumber.TYPE: return new CreditCardNumber(value);
            case DebitCardNumber.TYPE: return new DebitCardNumber(value);
            default: throw new AliasTypeNotSupportedException(type);
        }
    }

}
