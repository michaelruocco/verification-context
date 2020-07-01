package uk.co.idv.context.adapter.json.error.identitynotfound;

import com.fasterxml.jackson.annotation.JsonIgnore;
import uk.co.idv.context.entities.alias.Aliases;


public interface IdentityNotFoundErrorMixin {

    @JsonIgnore
    Aliases getAliases();

}