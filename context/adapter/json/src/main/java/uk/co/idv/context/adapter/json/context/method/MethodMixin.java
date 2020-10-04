package uk.co.idv.context.adapter.json.context.method;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import uk.co.idv.method.entities.eligibility.Eligibility;
import uk.co.idv.method.entities.result.Results;

import java.time.Duration;

public interface MethodMixin {

    @JsonIgnore
    Eligibility getEligibility();

    @JsonIgnore
    Duration getDuration();

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Results getResults();

}
