package uk.co.idv.context.adapter.json.policy.sequence;

import static uk.co.mruoc.file.content.ContentLoader.loadContentFromClasspath;

public interface SequencePoliciesJsonMother {

    static String build() {
        return loadContentFromClasspath("policy/sequence/sequence-policies.json");
    }

}
