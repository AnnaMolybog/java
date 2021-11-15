package otus.atm.entity;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = Banknote5.class, name = "banknote5"),
    @JsonSubTypes.Type(value = Banknote10.class, name = "banknote10"),
    @JsonSubTypes.Type(value = Banknote20.class, name = "banknote20"),
    @JsonSubTypes.Type(value = Banknote50.class, name = "banknote50"),
    @JsonSubTypes.Type(value = Banknote100.class, name = "banknote100"),
    @JsonSubTypes.Type(value = Banknote200.class, name = "banknote200"),
    @JsonSubTypes.Type(value = Banknote500.class, name = "banknote500")
})
public interface BanknoteInterface {
    int getCount();
    int getDenomination();
    void setCount(int count);
}
