package otus.atm.entity;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Cassette.class, name = "cassette")
})
public interface CassetteInterface {
    int getCount();
    int getDenomination();
    void setCount(int count);
}
