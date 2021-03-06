package uk.co.idv.context.adapter.json.context.sequence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import uk.co.idv.method.entities.method.Method;

import java.util.Optional;

public interface SequenceMixin {

    @JsonIgnore
    boolean isEligible();

    @JsonIgnore
    Optional<Method> getNext();

    @JsonIgnore
    long getCompletedCount();

}
